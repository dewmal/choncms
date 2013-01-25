package org.choncms.console;

import org.json.JSONException;
import org.json.JSONObject;

public class ConsoleSession {
	public static final String KEY = ConsoleSession.class.getName();
	
	private String path = "/";
	public ConsoleSession() {
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public String toJSONStr() {
		JSONObject o = new JSONObject();
		try {
			o.put("path", path);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o.toString();
	}
}
