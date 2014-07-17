package com.github.virgo47.respsec.main;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.github.virgo47.respsec.AuthenticationService;
import com.github.virgo47.respsec.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	public static final String FIXED_TOKEN = "47";

	@Autowired
	private UserDao userDao;

	@Autowired
	private ApplicationContext applicationContext;

	private Map<String, UserContext> validUsers = new HashMap<>();

	@PostConstruct
	public void init() {
		System.out.println(" *** AuthenticationServiceImpl.init with: " + applicationContext);
	}

	@Override
	public UserContext authenticate(String login, String password) {
		System.out.println(" *** AuthenticationServiceImpl.authenticate");
		User user = userDao.getByLogin(login);
		if (user == null) {
			return null;
		}
		if (login.equals(user.getLogin()) && password.equals(user.getPassword())) {
			UserContext userContext = new UserContext(user);
			validUsers.put(FIXED_TOKEN, userContext);
			return userContext;
		}
		return null;
	}

	@Override
	public UserContext checkToken(String token) {
		System.out.println(" *** AuthenticationServiceImpl.checkToken");
		return validUsers.get(token);
	}

	public Map<String, UserContext> getValidUsers() {
		return validUsers;
	}
}
