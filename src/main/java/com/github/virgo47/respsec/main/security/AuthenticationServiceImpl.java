package com.github.virgo47.respsec.main.security;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

import com.github.virgo47.respsec.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for all around authentication, token checks, etc.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	@Qualifier("restAuthenticationManager")
	private AuthenticationManager authenticationManager;

	private Map<String, UserContext> validUsers = new HashMap<>();

	/**
	 * This maps system users to tokens because equals/hashCode is delegated to User entity.
	 * This can store either one token or list of them for each user, depending on what you want to do.
	 * Here we store single token, which means, that any older tokens are invalidated.
	 */
	private Map<UserContext, String> tokens = new HashMap<>();

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
		storeNewToken(userContext, token);
		return token;
	}

	private void storeNewToken(UserContext userContext, String token) {
		removeOldToken(userContext);

		validUsers.put(token, userContext);
		tokens.put(userContext, token);
	}

	private void removeOldToken(UserContext userContext) {
		String oldToken = tokens.remove(userContext);
		validUsers.remove(oldToken);
	}

	@Override
	public boolean checkToken(String token) {
		System.out.println(" *** AuthenticationServiceImpl.checkToken");

		UserContext userContext = validUsers.get(token);
		if (userContext == null) {
			return false;
		}

		UsernamePasswordAuthenticationToken securityToken = new UsernamePasswordAuthenticationToken(
			userContext.getUsername(), null, userContext.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(securityToken);

		return true;
	}

	public Map<String, UserContext> getValidUsers() {
		return validUsers;
	}

	@Override
	public void logout(String token) {
		UserContext logoutUser = validUsers.remove(token);
		if (logoutUser != null) {
			tokens.remove(logoutUser);
		}
		System.out.println(" *** AuthenticationServiceImpl.logout: " + logoutUser);
		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
