package org.chon.common.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.common.configuration.impl.ConfigSonImpl;


public class ConfigurationFactory {
	private static final Log log = LogFactory.getLog(ConfigurationFactory.class);
	private ConfigurationFactory() {
		
	}
	private static ConfigSon configSonInstance = null;
	static {
		try {
			configSonInstance = new ConfigSonImpl();
		} catch (Exception e) {
			log.error("Error creating configson service", e);
		}
	}
	
	public static ConfigSon getConfigSonInstance() {
		return configSonInstance;
	}
}
