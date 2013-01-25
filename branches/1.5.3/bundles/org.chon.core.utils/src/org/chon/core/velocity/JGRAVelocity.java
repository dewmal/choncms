package org.chon.core.velocity;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;

public class JGRAVelocity {
	public class VUtils {
		public int atoi(String s) {
			return Integer.parseInt(s);
		}
	}
	private static Map<String, JGRAVelocity> cache = new HashMap<String, JGRAVelocity>();
	public static JGRAVelocity getInstance(String path) {
		if(cache.get(path)!=null) {
			return cache.get(path);
		}
		JGRAVelocity jVelocity = new JGRAVelocity(path);
		cache.put(path, jVelocity);
		return jVelocity;
	}
	
	static JSONHelper jheHelper = new JSONHelper();
	private VelocityEngine velocityEngine;
	
	private JGRAVelocity(String path) {
		
		velocityEngine = new VelocityEngine();
		try {
			Properties p = new Properties();
			if(path!=null) p.setProperty("file.resource.loader.path", path);
			p.setProperty("file.resource.loader.cache", "false");
			p.setProperty("file.resource.loader.modificationCheckInterval", "120");
			p.setProperty("input.encoding", "UTF-8");
			p.setProperty("output.encoding", "UTF-8");
			p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.Log4JLogSystem");
			p.setProperty("runtime.log", "");
			velocityEngine.init(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public String jsonExec(String tpl, String jsonStr) throws Exception {
		Map<String, Object> scopeMap = (Map<String, Object>) prepareJSONStrForVelocity(jsonStr);	
		return this.exec(tpl, scopeMap);
	}
	
	@SuppressWarnings("unchecked")
	public String jsonExec(String tpl, String tplStr, String jsonStr) throws Exception {
		Map<String, Object> scopeMap = (Map<String, Object>) prepareJSONStrForVelocity(jsonStr);	
		return this.exec(tpl, tplStr, scopeMap);
	}

	public static Object prepareJSONStrForVelocity(String str) {
		try {
			JSONObject o = new JSONObject(str);
			return prepareJSONForVelocity(o);
		} catch (Exception e) { }
		try {
			JSONArray o = new JSONArray(str);
			return prepareJSONForVelocity(o);
		} catch (Exception e) { }
		return null;
	}
	public static Object prepareJSONForVelocity(Object v) {
		if(v instanceof JSONObject) {
			v = jheHelper.toMap((JSONObject)v);
		} else if(v instanceof JSONArray){
			v = jheHelper.toArray((JSONArray) v);
		}
		return v;
	}
	
	public String exec(String tpl, Map<String, Object> scopeVars) throws ResourceNotFoundException, ParseErrorException, Exception {
		VelocityContext context = createVelocityContext(scopeVars);
		String out = execTemplateFile(tpl, context);
		return out;
	}
	
	public String exec(String tplName, String templateText, Map<String, Object> scopeVars) throws ResourceNotFoundException, ParseErrorException, Exception {
		VelocityContext context = createVelocityContext(scopeVars);
		StringWriter sw = new StringWriter();
		StringReader sr = new StringReader(templateText);
		velocityEngine.evaluate(context, sw, tplName, sr);
		String out = sw.toString();
		return out;
	}

	private String execTemplateFile(String tpl, VelocityContext context)
			throws Exception, IOException {
		Template template = velocityEngine.getTemplate(tpl);

		StringWriter sw = new StringWriter();
		template.setEncoding("UTF-8");
		template.merge(context, sw);
		String out = sw.toString();
		return out;
	}

	private VelocityContext createVelocityContext(Map<String, Object> scopeVars) {
		VelocityContext context = new VelocityContext();
		
		context.put("U", new VUtils());
		for(String key : scopeVars.keySet()) {
			Object v = scopeVars.get(key);
			if(v instanceof JSONObject) {
				v = jheHelper.toMap((JSONObject)v);
			} else if(v instanceof JSONArray){
				v = jheHelper.toArray((JSONArray) v);
			}
			context.put(key, v);
		}
		return context;
	}
	
	
	private static class JSONHelper {
		@SuppressWarnings("unchecked")
		private Map<String, Object> toMap(JSONObject json) {
			Iterator<String> it = json.keys();
			Map<String, Object> rvMap = new HashMap<String, Object>();
			while(it.hasNext()) {
				String key = it.next();
				Object obj = json.opt(key);
				if(obj instanceof JSONObject) {
					rvMap.put(key, toMap((JSONObject)obj ));
				} else if(obj instanceof JSONArray) {
					rvMap.put(key, toArray((JSONArray)obj));
				} else {
					rvMap.put(key, obj);
				}
			}
			return rvMap;
		}

		private Object[] toArray(JSONArray arr) {
			Object [] obj_arr = new Object[arr.length()];
			for(int i=0; i<arr.length(); i++) {
				Object obj = arr.opt(i);
				if(obj instanceof JSONObject) {
					obj_arr[i] = toMap((JSONObject)obj);
				} else if(obj instanceof JSONArray) {
					obj_arr[i] = toArray((JSONArray)obj);
				} else {
					obj_arr[i] = obj;
				}
			}
			return obj_arr;
		}
	}
}
