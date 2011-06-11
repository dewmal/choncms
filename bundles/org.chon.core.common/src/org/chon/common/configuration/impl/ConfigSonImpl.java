package org.chon.common.configuration.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.common.configuration.ConfigSon;
import org.chon.core.common.util.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class ConfigSonImpl implements ConfigSon {
	private static final String CONFIG_PREFIX = "commons.configson.";
	
	private static final Log log = LogFactory.getLog(ConfigSonImpl.class);
	
	private HashMap<String, JSONObject> _cache = new HashMap<String, JSONObject>();
	private File dir;

	public ConfigSonImpl() throws Exception {
		this.dir = new File(System.getProperty(CONFIG_PREFIX + "configuration.dir"));
		if(!this.dir.exists()) {
			throw new Exception(
					"Configuration dir commons.configson.configuration.dir is not available, it is "
							+ System.getProperty("commons.configson.configuration.dir"));
		}
		log.info("Created ConfigSon instance on " + this.dir.getAbsolutePath());
	}
	
	@Override
	public JSONObject getConfig(String name) throws FileNotFoundException, JSONException {
		return getConfig(name, false);
	}

	@Override
	public JSONObject getConfig(String name, boolean forceReload) throws FileNotFoundException, JSONException {
		if(forceReload) {
			_cache.remove(name);
		}
		if(_cache.containsKey(name)) {
			return _cache.get(name);			
		}
		File file = new File(dir, name + FILE_EXTENSION);
		if(!file.exists()) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
		try {
			String str = FileUtils.readFileToString(file);
			Map<String, String> cfgVars = getConfigVars();
			for(String k : cfgVars.keySet()) {
				str = str.replaceAll("\\$" + k, cfgVars.get(k));
			}
			System.out.println("Reading Config: " + file);
			System.out.println(str);
			JSONObject obj = new JSONObject(str);
			_cache.put(name, obj);
		} catch (IOException e) {
			String msg = "IO Exception while reading file " + file.getAbsolutePath();
			log.error(msg, e);
			throw new FileNotFoundException(msg + "; Cause: " + e.getMessage());
		}
		return _cache.get(name);
	}

	private Map<String, String> getConfigVars() {
		Map<String, String> rv = new HashMap<String, String>();
		Properties props = System.getProperties();
		Set<Object> keys = props.keySet();
		for(Object k : keys) {
			String key = (String)k;
			if(key.startsWith(CONFIG_PREFIX)) {
				rv.put(key.substring(CONFIG_PREFIX.length()), System.getProperty(key));
			}
		}
		return rv;
	}

	@Override
	public String getConfigurationPath() {
		return dir.getAbsolutePath();
	}

}
