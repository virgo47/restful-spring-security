package com.github.virgo47.respsec.main.security;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages tokens - separated from {@link com.github.virgo47.respsec.AuthenticationService},
 * so we can implement and plug various policies.
 */
public class TokenManager {

	private Map<String, UserContext> validUsers = new HashMap<>();

	/**
	 * This maps system users to tokens because equals/hashCode is delegated to User entity.
	 * This can store either one token or list of them for each user, depending on what you want to do.
	 * Here we store single token, which means, that any older tokens are invalidated.
	 */
	private Map<UserContext, String> tokens = new HashMap<>();

	public void storeNewToken(UserContext userContext, String token) {
		removeUserContext(userContext);

		validUsers.put(token, userContext);
		tokens.put(userContext, token);
	}

	public String removeUserContext(UserContext userContext) {
		String token = tokens.remove(userContext);
		validUsers.remove(token);
		return token;
	}

	public UserContext removeToken(String token) {
		UserContext userContext = validUsers.remove(token);
		if (userContext != null) {
			tokens.remove(userContext);
		}
		return userContext;
	}

	public UserContext getUserContext(String token) {
		return validUsers.get(token);
	}

	public Map<String, UserContext> getValidUsers() {
		return Collections.unmodifiableMap(validUsers);
	}
}
