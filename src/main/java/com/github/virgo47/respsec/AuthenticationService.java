package com.github.virgo47.respsec;

import com.github.virgo47.respsec.main.security.TokenInfo;
import com.github.virgo47.respsec.main.security.UserContext;

import java.util.Collection;
import java.util.Map;

/** Authenticates the user or checks valid token. */
public interface AuthenticationService {

	/**
	 * Authenticates the user and returns valid token. If anything fails, {@code null} is returned instead.
	 * Prepares {@link org.springframework.security.core.context.SecurityContext} if authentication succeeded.
	 */
	TokenInfo authenticate(String login, String password);

	/**
	 * Checks the authentication token and if it is valid prepares
	 * {@link org.springframework.security.core.context.SecurityContext} and returns true.
	 */
	boolean checkToken(String token);

	/** This is only for debug purposes obviously. */
	Map<String, UserContext> getValidUsers();

	/** Returns tokens valid for current user. */
	Collection<TokenInfo> getMyTokens();

	/** Logouts the user - token is invalidated/forgotten. */
	void logout(String token);
}
