package com.github.virgo47.respsec;

import java.util.Map;

import com.github.virgo47.respsec.main.security.UserContext;

/** Authenticates the user or checks valid token. */
public interface AuthenticationService {

	/** Authenticates the user and returns valid token. If anything fails, {@code null} is returned instead. */
	String authenticate(String login, String password);

	boolean checkToken(String token);

	/** This is only for debug purposes obviously. */
	Map<String, UserContext> getValidUsers();

	/** Logouts the user - token is invalidated/forgotten. */
	void logout(String token);
}
