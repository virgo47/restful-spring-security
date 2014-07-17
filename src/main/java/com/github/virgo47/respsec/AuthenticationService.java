package com.github.virgo47.respsec;

import java.util.Map;

/**
 * Authenticates the user or checks valid token.
 *
 * TODO: How to encode token?
 */
public interface AuthenticationService {

	UserContext authenticate(String login, String password);

	UserContext checkToken(String token);

	/** This is only for debug purposes obviously. */
	Map<String, UserContext> getValidUsers();
}
