package com.github.virgo47.respsec.mvc;

import java.util.Map;

import javax.annotation.PostConstruct;

import com.github.virgo47.respsec.AuthenticationService;
import com.github.virgo47.respsec.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * One of two controllers, this one has login (with permitAll) and some protected method.
 */
@RestController
public class MainRestController {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private AuthenticationService authenticationService;

	@PostConstruct
	public void init() {
		System.out.println("MainRestController.init with: " + applicationContext);
	}

	@RequestMapping("/login")
	public void login() {
		System.out.println("MainRestController.login");
	}

	@RequestMapping("/test")
	public void test() {
		System.out.println("MainRestController.test");
		System.out.println("Security context = " + SecurityContextHolder.getContext()); // TODO Spring Security dependency unwanted here
	}

	// TODO security
	@RequestMapping("/secure/service1")
	public void service1() {
		System.out.println("MainRestController.service1");
	}

	// TODO security
	@RequestMapping("/secure/allusers")
	public Map<String, UserContext> allUsers() {
		System.out.println("MainRestController.allUsers");
		return authenticationService.getValidUsers(); // DEBUG ONLY!
	}
}
