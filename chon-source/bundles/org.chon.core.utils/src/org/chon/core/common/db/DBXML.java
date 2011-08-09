package org.chon.core.common.db;

import java.util.Map;

import org.json.JSONObject;

public interface DBXML {
	public String qXML(String qName, String ... arguments);
	public JSONObject qJSON(String qName, String ... arguments);
	public String qXML(String qName, Map<String, Object> arguments);
	public JSONObject qJSON(String qName, Map<String, Object> arguments);
	public void query(String qName, Map<String, Object> arguments, ResultCallback callback);
	public void queryRS(String qName, Map<String, Object> arguments, ResultSetCallback rscallback);
	
	public void basicQuery(String query, ResultCallback callback);
	public void basicQueryRS(String query, ResultSetCallback rscallback);
	
}
