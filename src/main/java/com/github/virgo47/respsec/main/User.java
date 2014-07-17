package com.github.virgo47.respsec.main;

public class User {

	private String login; // username
	private String name; // full name
	private String password; // should be hashed, but doesn't matter in our example
	private String[] roles;

	public User(String login, String name, String password, String... roles) {
		this.login = login;
		this.name = name;
		this.password = password;
		this.roles = roles;
	}

	public String getLogin() {
		return login;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String[] getRoles() {
		return roles;
	}
}
