package com.github.virgo47.respsec.main.restsec;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Manages tokens - separated from {@link AuthenticationService},
 * so we can implement and plug various policies.
 */
public interface TokenManager {

	/**
	 * Creates a new token for the user and returns its {@link TokenInfo}.
	 * It may add it to the token list or replace the previous one for the user. Never returns {@code null}.
	 */
	TokenInfo createNewToken(UserDetails userDetails);

	/** Removes all tokens for user. */
	void removeUserDetails(UserDetails userDetails);

	/** Removes a single token. */
	UserDetails removeToken(String token);

	/** Returns user details for a token. */
	UserDetails getUserDetails(String token);

	/** Returns a collection with token information for a particular user. */
	Collection<TokenInfo> getUserTokens(UserDetails userDetails);

	/** Returns a map from valid tokens to users. */
	Map<String, UserDetails> getValidUsers();
}
