package com.github.virgo47.respsec.main.security;

import java.util.Date;

/** Contains information about a token. */
public final class TokenInfo {

	private final long created = System.currentTimeMillis();
	private final String token;
	// TODO expiration etc

	public TokenInfo(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		return "TokenInfo{" +
			"created=" + new Date(created) +
			", token='" + token + '\'' +
			'}';
	}
}
