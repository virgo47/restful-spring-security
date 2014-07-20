package com.github.virgo47.respsec.main.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements simple token manager, that keeps a single token for each user. If user logs in again,
 * older token will be invalidated.
 */
public class TokenManagerSingle implements TokenManager {

	private Map<String, UserContext> validUsers = new HashMap<>();

	/**
	 * This maps system users to tokens because equals/hashCode is delegated to User entity.
	 * This can store either one token or list of them for each user, depending on what you want to do.
	 * Here we store single token, which means, that any older tokens are invalidated.
	 */
	private Map<UserContext, TokenInfo> tokens = new HashMap<>();

	@Override
	public TokenInfo createNewToken(UserContext userContext, String token) {
		TokenInfo tokenInfo = new TokenInfo(token);

		removeUserContext(userContext);
		UserContext previous = validUsers.put(token, userContext);
		if (previous != null) {
			System.out.println(" *** SERIOUS PROBLEM HERE - we generated the same token (randomly?)!");
			return null;
		}
		tokens.put(userContext, tokenInfo);

		return tokenInfo;
	}

	@Override
	public void removeUserContext(UserContext userContext) {
		TokenInfo token = tokens.remove(userContext);
		if (token != null) {
			validUsers.remove(token.getToken());
		}
	}

	@Override
	public UserContext removeToken(String token) {
		UserContext userContext = validUsers.remove(token);
		if (userContext != null) {
			tokens.remove(userContext);
		}
		return userContext;
	}

	@Override
	public UserContext getUserContext(String token) {
		return validUsers.get(token);
	}

	@Override
	public Collection<TokenInfo> getUserTokens(UserContext userContext) {
		return Arrays.asList(tokens.get(userContext));
	}

	@Override
	public Map<String, UserContext> getValidUsers() {
		return Collections.unmodifiableMap(validUsers);
	}
}
