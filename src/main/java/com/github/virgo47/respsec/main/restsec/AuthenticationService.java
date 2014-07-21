package com.github.virgo47.respsec.main.restsec;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Authenticates the user or checks valid token. This should put together Spring's
 * {@link AuthenticationManager} and our {@link TokenManager}. It also provides {@link UserDetails}
 * for current user and thus hides interaction with {@link SecurityContextHolder}. However
 * this returns only Spring Security interface, so cast may be need to any application specific subclass.
 * Alternatively separate service for obtaining current user can be developed.
 * <p>
 * This should not interact with HTTP as that is job of {@link TokenAuthenticationFilter}.
 */
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

	/** Logouts the user - token is invalidated/forgotten. */
	void logout(String token);

	/** Returns current user or {@code null} if there is no authentication or user is anonymous. */
	UserDetails currentUser();
}
