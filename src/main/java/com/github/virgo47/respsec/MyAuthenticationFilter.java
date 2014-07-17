package com.github.virgo47.respsec;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.filter.GenericFilterBean;

/**
 * MyAuthenticationFilter.
 */
public class MyAuthenticationFilter extends GenericFilterBean {

	public static final String TOKEN_HEADER = "X-Auth-Token";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("HAHA :-)");
		chain.doFilter(request, response);
	}
}
