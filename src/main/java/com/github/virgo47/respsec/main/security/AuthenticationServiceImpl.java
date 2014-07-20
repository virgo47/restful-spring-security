package com.github.virgo47.respsec.main.security;

import com.github.virgo47.respsec.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.SecureRandom;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

/**
 * Service responsible for all around authentication, token checks, etc.
 * This class does not care about HTTP protocol at all.
 */
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private ApplicationContext applicationContext;

	private final AuthenticationManager authenticationManager;
	private final TokenManager tokenManager = new TokenManager();

	public AuthenticationServiceImpl(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@PostConstruct
	public void init() {
		System.out.println(" *** AuthenticationServiceImpl.init with: " + applicationContext);
	}

	@Override
	public String authenticate(String login, String password) {
		System.out.println(" *** AuthenticationServiceImpl.authenticate");

		// Here principal=username, credentials=password
		Authentication authentication = new UsernamePasswordAuthenticationToken(login, password);
		try {
			authentication = authenticationManager.authenticate(authentication);
			// Here principal=UserDetails (UserContext in our case), credentials=null (security reasons)
			SecurityContextHolder.getContext().setAuthentication(authentication);

			if (authentication.getPrincipal() != null) {
				UserContext userContext = (UserContext) authentication.getPrincipal();

				return generateToken(userContext);
			}
		} catch (AuthenticationException e) {
			System.out.println(" *** AuthenticationServiceImpl.authenticate - FAILED: " + e.toString());
		}
		return null;
	}

	private String generateToken(UserContext userContext) {
		byte[] tokenBytes = new byte[32];
		new SecureRandom().nextBytes(tokenBytes);
		String token = DatatypeConverter.printBase64Binary(tokenBytes);
		tokenManager.storeNewToken(userContext, token);
		return token;
	}

	@Override
	public boolean checkToken(String token) {
		System.out.println(" *** AuthenticationServiceImpl.checkToken");

		UserContext userContext = tokenManager.getUserContext(token);
		if (userContext == null) {
			return false;
		}

		UsernamePasswordAuthenticationToken securityToken = new UsernamePasswordAuthenticationToken(
			userContext.getUsername(), null, userContext.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(securityToken);

		return true;
	}

	public Map<String, UserContext> getValidUsers() {
		return tokenManager.getValidUsers();
	}

	@Override
	public void logout(String token) {
		UserContext logoutUser = tokenManager.removeToken(token);
		System.out.println(" *** AuthenticationServiceImpl.logout: " + logoutUser);
		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
