package com.github.virgo47.respsec.main.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link AuthenticationEntryPoint} that rejects all requests. Login-like function is featured
 * in {@link MyAuthenticationFilter}.
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		System.out.println(" *** UnauthorizedEntryPoint.commence: " + request.getRequestURI());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}