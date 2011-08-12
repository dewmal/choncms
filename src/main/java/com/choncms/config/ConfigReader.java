package com.choncms.config;


import java.io.File;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigReader {

	private static final String SYS_PROPS_DIR = System
			.getProperty("system-properties-dir");
	private static final String SYS_PROPS_FILE_NAME = System.getProperty(
			"system-properties", "system.properties");

	public static void readSystemProperties(ServletContext servletContext)
			throws ConfigurationException {


		File sysPropsDir = null;
		if (SYS_PROPS_DIR != null) {
			sysPropsDir = new File(SYS_PROPS_DIR);
		} else {
			sysPropsDir = new File(servletContext.getRealPath("/WEB-INF/"));
		}

		File propertiesFile = new File(sysPropsDir, SYS_PROPS_FILE_NAME);

		if (propertiesFile.exists()) {
			servletContext.log(":::> Reading " + SYS_PROPS_FILE_NAME);
			PropertiesConfiguration properties = new PropertiesConfiguration(propertiesFile);
			
			// TODO: put other useful properties
			properties.setProperty("user.home", System.getProperty("user.home"));
			
			initDefaultSystemProperties(properties);
			
			copyToSystemProperties(properties);
		}
	}
	
	private static void initDefaultSystemProperties(PropertiesConfiguration properties) {
		System.setProperty("felix.fileinstall.dir", properties.getString("chon.dropins.dir"));
	}
	
	private static void copyToSystemProperties(PropertiesConfiguration properties) {
		Iterator<?> it = properties.getKeys();
		while (it.hasNext()) {
			String k = (String) it.next();
			String value = properties.getString(k);
			System.getProperties().put(k, value);
		}
	}

}