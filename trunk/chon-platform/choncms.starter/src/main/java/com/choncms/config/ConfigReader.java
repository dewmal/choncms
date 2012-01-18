package com.choncms.config;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigReader {
	private static final Log log = LogFactory.getLog(ConfigReader.class);
	
	private static final String SYS_PROPS_DIR = System
			.getProperty("system-properties-dir");
	private static final String SYS_PROPS_FILE_NAME = System.getProperty(
			"system-properties", "system.properties");
	
	public static File getSystemPropertiesDir() {
		if(SYS_PROPS_DIR == null) {
			return null;
		}
		File sysPropsDir = new File(SYS_PROPS_DIR);
		if(!(sysPropsDir.exists() && sysPropsDir.isDirectory())) {
			log.warn(" system-properties-dir " + sysPropsDir.getAbsolutePath() + " is not valid directory");
			return null;
		}
		return sysPropsDir;
	}
	
	public static InputStream getSystemPropertiesInputStream(ServletContext servletContext) throws FileNotFoundException {
		File sysPropsDir = getSystemPropertiesDir();
		if(sysPropsDir == null) {
			//from web-inf
			log.info("Loading system properties ('"+SYS_PROPS_FILE_NAME+"') from WEB-INF");
			return servletContext.getResourceAsStream("/WEB-INF/" + SYS_PROPS_FILE_NAME);
		} else {
			log.info("Loading system properties ('"+SYS_PROPS_FILE_NAME+"') from " + sysPropsDir.getAbsolutePath());
			return new FileInputStream(new File(sysPropsDir, SYS_PROPS_FILE_NAME));
		}
	}

	public static void readSystemProperties(ServletContext servletContext)
			throws ConfigurationException, IOException {
		
		InputStream propertiesInputStream = getSystemPropertiesInputStream(servletContext);

		if (propertiesInputStream != null) {
			PropertiesConfiguration properties = new PropertiesConfiguration();
			properties.load(propertiesInputStream);
			
			// TODO: put other useful properties
			properties.setProperty("user.home", System.getProperty("user.home"));
			
			initDefaultSystemProperties(properties);
			
			copyToSystemProperties(properties);
			if(log.isDebugEnabled()) {
				log.debug(" ---------- Properties loaded ---------- ");
				log.debug(properties);
				log.debug(" ---------- ----------------- ---------- ");
			}
			propertiesInputStream.close();
		} else {
			throw new ConfigurationException(
					" system properties file not found SYS_PROPS_DIR="
							+ SYS_PROPS_DIR + "; SYS_PROPS_FILE_NAME="
							+ SYS_PROPS_FILE_NAME);
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