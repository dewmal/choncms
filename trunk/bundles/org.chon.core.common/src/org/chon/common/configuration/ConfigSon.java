package org.chon.common.configuration;

import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

public interface ConfigSon {
	public static final String FILE_EXTENSION = ".json";
	
	/**
	 * Load json from file, <br/>
	 *  $dir = System.getProperty("commons.configson.configuration.dir") <br/>
	 *  
	 * 	config_file = $dir/@param{name}.json
	 * 
	 * Configurations are cached, use getConfig(name, true) to reload file
	 * 
	 * @param name
	 * @return
	 */
	public JSONObject getConfig(String name) throws FileNotFoundException, JSONException;
	
	/**
	 * 
	 * 
	 * @param name
	 * @param forceReload
	 * @return
	 */
	public JSONObject getConfig(String name, boolean forceReload) throws FileNotFoundException, JSONException;
	
	/**
	 * get path from where configurations are loaded
	 *  
	 * @return
	 */
	public String getConfigurationPath();
	
}
