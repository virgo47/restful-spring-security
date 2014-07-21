/**
 * This package contains drop-in solution for token-based authentication. It is self-contained with a couple of
 * extension points to implement - one needs to implement {@link com.github.virgo47.respsec.main.restsec.AuthenticationService}
 * and {@link com.github.virgo47.respsec.main.restsec.TokenManager}.
 * <p>
 * None of these classes rely on annotation based configuration.
 */
package com.github.virgo47.respsec.main.restsec;