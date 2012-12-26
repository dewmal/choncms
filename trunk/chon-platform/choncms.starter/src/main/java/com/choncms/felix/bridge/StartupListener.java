package com.choncms.felix.bridge;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*
 * Part of the Felix ServletBridge Strategy: https://felix.apache.org/site/apache-felix-http-service.html#ApacheFelixHTTPService-UsingtheServletBridge
 * 
 * Code taken from https://svn.apache.org/repos/asf/felix/trunk/http/samples/bridge/src/main/java/org/apache/felix/http/samples/bridge/StartupListener.java
 */

public final class StartupListener implements ServletContextListener {
	private FrameworkService service;

	public void contextInitialized(ServletContextEvent event) {
		this.service = new FrameworkService(event.getServletContext());
		this.service.start();
		
	}

	public void contextDestroyed(ServletContextEvent event) {
		this.service.stop();
	}
}
