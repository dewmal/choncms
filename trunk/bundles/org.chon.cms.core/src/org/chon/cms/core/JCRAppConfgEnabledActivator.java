package org.chon.cms.core;

import java.io.FileNotFoundException;

import org.chon.common.configuration.ConfigSon;
import org.chon.common.configuration.ConfigurationFactory;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JCRAppConfgEnabledActivator extends JCRApplicationActivator {

	protected abstract String getName();

	public ConfigSon getConfigSonService() {
		return ConfigurationFactory.getConfigSonInstance();
	}
	
	public JSONObject getConfig() {
		try {
			return getConfigSonService().getConfig(getName());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	
}
