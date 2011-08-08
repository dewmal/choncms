package org.chon.core.runtime.container;

import java.io.File;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigReader {

	private static final String SYS_PROPS_DIR = System.getProperty("system-properties-dir");
	private static final String SYS_PROPS_FILE_NAME = System.getProperty("system-properties", "system.properties");
	
	public static void readSystemProperties(ServletContext servletContext) throws ConfigurationException {		 
	//	try {
			
			File sysPropsDir = null;
			if(SYS_PROPS_DIR != null) {
				sysPropsDir = new File(SYS_PROPS_DIR);
			} else {
				sysPropsDir = new File(servletContext.getRealPath("/WEB-INF/"));
			}
			
			File propertiesFile = new File(sysPropsDir, SYS_PROPS_FILE_NAME);
			
			
			
			if (propertiesFile.exists()) {
				servletContext.log(":::> Reading " + SYS_PROPS_FILE_NAME);
				PropertiesConfiguration properties = new PropertiesConfiguration(propertiesFile);
				//TODO: put other useful properties 
				properties.setProperty("user.home", System.getProperty("user.home"));
				
				//Properties appProperties = new Properties();
				//appProperties.load(new FileInputStream(propertiesFile));
				Iterator<?> it = properties.getKeys();
				
				//Enumeration<Object> itKeys = appProperties.keys();
				StringBuffer logBuf = new StringBuffer();
				while (it.hasNext()) {
					String k = (String) it.next();
					String value = properties.getString(k);
					/*
					if (value.startsWith("./")) {
						value = sysPropsDir.getAbsolutePath() + "/"
								+ value.substring(2);
						value = new File(value).getCanonicalPath().replaceAll("\\\\", "/");
					}
					*/
					logBuf.append(k + "=" + value + ";");
					System.getProperties().put(k, value);
				}
				servletContext.log(":::> SYSTEM PROPERTIES ["+logBuf.toString()+"]");
			}
		//} catch (FileNotFoundException e) {
		//	servletContext.log("WARN: "+SYS_PROPS_FILE_NAME+" not found", e);
		//} catch (IOException e) {
		//	servletContext.log("ERROR: Can not read "+SYS_PROPS_FILE_NAME+" file", e);
		//}
	}
}