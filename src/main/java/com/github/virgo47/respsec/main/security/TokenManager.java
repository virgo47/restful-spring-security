package com.github.virgo47.respsec.main.security;

import java.util.Collection;
import java.util.Map;

/**
 * Manages tokens - separated from {@link com.github.virgo47.respsec.AuthenticationService},
 * so we can implement and plug various policies.
 */
public interface TokenManager {

	/**
	 * Stores new token for the user. It may add it to token list or replace the old one.
	 * Must return {@code null} if there is a token collision (however unlikely).
	 */
	TokenInfo createNewToken(UserContext userContext, String token);

	/** Removes all tokens for user. */
	void removeUserContext(UserContext userContext);

	/** Removes a single token. */
	UserContext removeToken(String token);

	/** Returns user context for a token. */
	UserContext getUserContext(String token);

	/** Returns a collection with token information for a particular user. */
	Collection<TokenInfo> getUserTokens(UserContext userContext);

	// TODO remove eventually?
	Map<String, UserContext> getValidUsers();
}
