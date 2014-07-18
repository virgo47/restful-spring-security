package com.github.virgo47.respsec.main.security;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import com.github.virgo47.respsec.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Takes care of request/response pre-processing for login/logout and token check.
 * Login can be performed on any URL, logout only on specified {@link #logoutLink}.
 * {@link SecurityContextHolder} is used only for debug outputs, otherwise this class
 * is configured to be used by Spring Security, but it doesn't import it at all.
 */
public class MyAuthenticationFilter extends GenericFilterBean {

	private static final String HEADER_TOKEN = "X-Auth-Token";
	private static final String HEADER_USERNAME = "X-Username";
	private static final String HEADER_PASSWORD = "X-Password";

	/** Request attribute that indicates that this filter will not continue with the chain. Handy after login/logout, etc. */
	private static final String REQUEST_ATTR_DONT_CONTINUE = "MyAuthenticationFilter-dont-continue";

	private final String logoutLink;

	@Autowired
	private AuthenticationService authenticationService;

	public MyAuthenticationFilter(String logoutLink) {
		this.logoutLink = logoutLink;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println(" *** MyAuthenticationFilter.doFilter");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		boolean authenticated = checkToken(httpRequest, httpResponse);

		if (httpRequest.getMethod().equals("POST")) {
			checkAuthorization(httpRequest, httpResponse);

			// If we're not authenticated, we don't bother with logout at all.
			// Logout does not work in the same request with login - this does not make sense, because logout works with token and login returns it.
			if (authenticated) {
				checkLogout(httpRequest);
			}
		}

		if (httpRequest.getAttribute(REQUEST_ATTR_DONT_CONTINUE) == null) {
			chain.doFilter(request, response);
		}
		System.out.println(" === AUTHENTICATION: " + SecurityContextHolder.getContext().getAuthentication());
	}

	private void checkAuthorization(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		String authorization = httpRequest.getHeader("Authorization");
		if (authorization != null) {
			checkBasicAuthorization(authorization, httpResponse);
		} else {
			checkCustomLoginHeaders(httpRequest, httpResponse);
		}
	}

	private void checkBasicAuthorization(String authorization, HttpServletResponse httpResponse) throws IOException {
		StringTokenizer tokenizer = new StringTokenizer(authorization);
		if (tokenizer.countTokens() < 2) {
			return;
		}
		if (!tokenizer.nextToken().equalsIgnoreCase("Basic")) {
			return;
		}

		String base64 = tokenizer.nextToken();
		String loginPassword = new String(DatatypeConverter.parseBase64Binary(base64));

		System.out.println("loginPassword = " + loginPassword);
		tokenizer = new StringTokenizer(loginPassword, ":");
		System.out.println("tokenizer = " + tokenizer);
		checkUsernameAndPassword(httpResponse, tokenizer.nextToken(), tokenizer.nextToken());
	}

	private void checkCustomLoginHeaders(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		String username = httpRequest.getHeader(HEADER_USERNAME);
		String password = httpRequest.getHeader(HEADER_PASSWORD);
		if (username != null && password != null) {
			checkUsernameAndPassword(httpResponse, username, password);
			dontContinueAfterThisFilter(httpRequest);
		}
	}

	private void checkUsernameAndPassword(HttpServletResponse httpResponse, String username, String password) throws IOException {
		String token = authenticationService.authenticate(username, password);
		if (token != null) {
			httpResponse.setHeader(HEADER_TOKEN, token);
		} else {
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	/** Returns true, if request contains valid authentication token. */
	private boolean checkToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		String token = httpRequest.getHeader(HEADER_TOKEN);
		if (token == null) {
			return false;
		}

		if (authenticationService.checkToken(token)) {
			System.out.println(" *** " + HEADER_TOKEN + " valid for " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			return true;
		} else {
			System.out.println(" *** Invalid " + HEADER_TOKEN + ' ' + token);
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return false;
	}

	private void checkLogout(HttpServletRequest httpRequest) {
		if (httpRequest.getServletPath().equals(logoutLink)) {
			String token = httpRequest.getHeader(HEADER_TOKEN);
			if (token != null) {
				authenticationService.logout(token);
				dontContinueAfterThisFilter(httpRequest);
			}
		}
	}

	private void dontContinueAfterThisFilter(HttpServletRequest httpRequest) {
		httpRequest.setAttribute(REQUEST_ATTR_DONT_CONTINUE, "");
	}
}
