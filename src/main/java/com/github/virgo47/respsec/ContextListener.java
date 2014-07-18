package com.github.virgo47.respsec;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/** Added just for monitoring and debugging. */
@WebListener
public class ContextListener implements ServletContextListener, HttpSessionListener {

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println(" *** ContextListener.contextInitialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println(" *** ContextListener.contextDestroyed");
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println(" *** ContextListener.sessionCreated - NOT GOOD!");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println(" *** ContextListener.sessionDestroyed - NOT GOOD!");
	}
}
