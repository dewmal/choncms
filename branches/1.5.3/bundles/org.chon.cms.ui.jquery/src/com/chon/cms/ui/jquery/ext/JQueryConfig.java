package com.chon.cms.ui.jquery.ext;

import org.json.JSONException;
import org.json.JSONObject;


public class JQueryConfig {
	private boolean autoInclude;
	
	public JQueryConfig(JSONObject cfg) {
		try {
			this.autoInclude = cfg.getBoolean("autoInclude");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isAutoInclude() {
		return autoInclude;
	}

	public void setAutoInclude(boolean autoInclude) {
		this.autoInclude = autoInclude;
	}
}
