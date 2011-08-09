package org.chon.core.runtime.container;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContext;

public class ConfigReader {

	private static final String SYS_PROPS_DIR = System.getProperty("system-properties-dir");
	private static final String SYS_PROPS_FILE_NAME = System.getProperty("system-properties", "system.properties");
	
	public static void readSystemProperties(ServletContext servletContext) {		 
		try {
			
			File sysPropsDir = null;
			if(SYS_PROPS_DIR != null) {
				sysPropsDir = new File(SYS_PROPS_DIR);
			}
			File propertiesFile = null;
			if(sysPropsDir!= null && sysPropsDir.exists()) {
				propertiesFile = new File(sysPropsDir, SYS_PROPS_FILE_NAME);
			} else {
				File web_inf_dif = new File(servletContext.getRealPath("/WEB-INF/"));
				propertiesFile = new File(web_inf_dif, SYS_PROPS_FILE_NAME);
			}
			
			
			
			if (propertiesFile.exists()) {
				servletContext.log(":::> Reading " + SYS_PROPS_FILE_NAME);
				Properties appProperties = new Properties();
				appProperties.load(new FileInputStream(propertiesFile));
				Enumeration<Object> itKeys = appProperties.keys();
				StringBuffer logBuf = new StringBuffer();
				while (itKeys.hasMoreElements()) {
					String k = (String) itKeys.nextElement();
					String value = appProperties.getProperty(k);
					if (value.startsWith("./")) {
						value = sysPropsDir.getAbsolutePath() + "/"
								+ value.substring(2);
						value = new File(value).getCanonicalPath().replaceAll("\\\\", "/");
					}
					logBuf.append(k + "=" + value + ";");
					System.getProperties().put(k, value);
				}
				servletContext.log(":::> SYSTEM PROPERTIES ["+logBuf.toString()+"]");
			}
		} catch (FileNotFoundException e) {
			servletContext.log("WARN: "+SYS_PROPS_FILE_NAME+" not found", e);
		} catch (IOException e) {
			servletContext.log("ERROR: Can not read "+SYS_PROPS_FILE_NAME+" file", e);
		}
	}
}