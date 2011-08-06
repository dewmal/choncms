package org.chon.core.common.db.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.chon.core.common.db.DBXML;
import org.chon.core.common.db.Database;
import org.chon.core.common.db.ResultCallback;
import org.chon.core.common.db.ResultSetCallback;
import org.chon.core.velocity.JGRAVelocity;
import org.json.JSONObject;


public class DBXMLImpl implements DBXML {

	private Map<String, String> queries;
	private Database database;
	
	public DBXMLImpl(Map<String, String> queries, Database database) {
		this.queries = queries;
		this.database = database;
	}

	public JSONObject qJSON(String name, String... arguments) {
		String s = formatQuery(queries, name, arguments);
		return database.queryJSON(s).optJSONObject("resultset");
	}
	
	public String qXML(String name, String... arguments) {
		String s = formatQuery(queries, name, arguments);
		return database.queryXML(s);
	}

	private String formatQuery(final Map<String, String> queries,
			String name, String... arguments) {
		String s = queries.get(name);
		String f = s;
		if (arguments != null) {
			for (int i = 0; i < arguments.length; i++) {
				arguments[i] = escapeQuote(arguments[i]);
			}
			s = String.format(f, (Object [])arguments);
		}
		//log.debug(s);
		return s;
	}

	public JSONObject qJSON(String name, Map<String, Object> arguments) {
		String s = formatQuery(queries, name, arguments);
		return database.queryJSON(s).optJSONObject("resultset");
	}
	
	private Map<String, Object> dummyMap = new HashMap<String, Object>();
	private String formatQuery(Map<String, String> queries,
			String name, Map<String, Object> arguments) {
		String s = queries.get(name);
		
		Map<String, Object> map = arguments==null?dummyMap:arguments;
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			Object val = map.get(key);
			if(val instanceof String) {
				map.put(key, escapeQuote((String)val));
			}
		}
		JGRAVelocity v = JGRAVelocity.getInstance("");
		try {
			return v.exec(name, s, map);
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String qXML(String name, Map<String, Object> arguments) {
		String s = formatQuery(queries, name, arguments);
		return database.queryXML(s);
	}

	public void query(String name, Map<String, Object> arguments,
			ResultCallback cb) {
		String s = formatQuery(queries, name, arguments);
		database.query(s, cb);
	}

	public void queryRS(String name, Map<String, Object> arguments,
			ResultSetCallback rscallback) {
		String s = formatQuery(queries, name, arguments);
		basicQueryRS(s, rscallback);
	}

	public void basicQueryRS(String query, ResultSetCallback rscallback) {
		database.queryRS(query, rscallback);
	}

	public void basicQuery(String query, ResultCallback callback) {
		database.query(query, callback);
	}
	
	private static String escapeQuote(String s) {
		return s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\'", "\\\\'");
	}
}
