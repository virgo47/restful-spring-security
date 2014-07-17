package com.github.virgo47.respsec.mvc;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * MainController.
 */
@Controller
public class MainController {

	@Autowired
	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
		System.out.println("MainController.init with: " + applicationContext);
	}

	@RequestMapping("/")
	public String index() {
		return "some";
	}
}
