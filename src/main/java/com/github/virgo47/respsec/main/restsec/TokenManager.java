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
	 * Stores new token for the user. It may add it to token list or replace the old one.
	 * Must return {@code null} if there is a token collision (however unlikely).
	 */
	TokenInfo createNewToken(UserDetails userDetails, String token);

	/** Removes all tokens for user. */
	void removeUserDetails(UserDetails userDetails);

	/** Removes a single token. */
	UserDetails removeToken(String token);

	/** Returns user details for a token. */
	UserDetails getUserDetails(String token);

	/** Returns a collection with token information for a particular user. */
	Collection<TokenInfo> getUserTokens(UserDetails userDetails);

	// TODO remove eventually?
	Map<String, UserDetails> getValidUsers();
}
