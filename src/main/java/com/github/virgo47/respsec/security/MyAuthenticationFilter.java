package com.github.virgo47.respsec.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.github.virgo47.respsec.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

/**
 * MyAuthenticationFilter.
 */
public class MyAuthenticationFilter extends GenericFilterBean {

	public static final String TOKEN_HEADER = "X-Auth-Token";

	@Autowired
	private AuthenticationService authenticationService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println(" *** MyAuthenticationFilter.doFilter");
		chain.doFilter(request, response);
	}
}
