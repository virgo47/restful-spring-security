package com.github.virgo47.respsec.main;

import org.springframework.stereotype.Component;

/** This is domain DAO to access users. Kinda fake here. */
@Component
public class UserDao {

	public User getByLogin(String login) {
		switch (login) {
			case "admin":
				return new User("admin", "Administrator", "admin", "ADMIN");
			case "special":
				return new User("special", "Special Expert", "special", "ROLE_SPECIAL");
			case "user1":
				return new User("user1", "User Uno", "user1");
			case "Aladdin":
				return new User("Aladdin", "Aladdin", "open sesame");
			default:
				return null;
		}
	}
}
