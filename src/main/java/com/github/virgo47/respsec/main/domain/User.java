package com.github.virgo47.respsec.main.domain;

import java.util.Arrays;
import java.util.Objects;

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

	// login works as ID here, if there is normal ID column, use that of course
	// equals/hashCode is very important for AuthenticationServiceImpl#tokens to work properly
	@Override
	public boolean equals(Object o) {
		return this == o
			|| o != null && o instanceof User
			&& Objects.equals(login, ((User) o).login);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(login);
	}

	@Override
	public String toString() {
		return "User{" +
			"login='" + login + '\'' +
			", name='" + name + '\'' +
			", password='" + password + '\'' +
			", roles=" + Arrays.toString(roles) +
			'}';
	}
}
