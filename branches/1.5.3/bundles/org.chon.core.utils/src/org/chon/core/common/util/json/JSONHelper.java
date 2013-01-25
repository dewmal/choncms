package org.chon.core.common.util.json;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONHelper {
	JSONObject json;
	public JSONHelper(JSONObject json) {
		this.json = json;
	}
	
	public String get(String key) {
		try {
			return json.getString(key);
		} catch(Exception e) {}
		return null;
	}
	public static JSONArray ensureArray(Object opt) {
		if(opt instanceof JSONArray) 
			return (JSONArray) opt;
		JSONArray ja = new JSONArray();
		ja.put(opt);
		return ja;
	}
}
