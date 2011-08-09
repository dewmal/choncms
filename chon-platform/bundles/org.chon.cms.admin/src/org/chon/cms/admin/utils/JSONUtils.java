package org.chon.cms.admin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

	public static JSONObject errorMsg(Exception e, String msg) {
		JSONObject j = new JSONObject();
		try {
			j.put("msg", msg);
			j.put("error", e.getMessage());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return j;
	}

	public static JSONObject msg(String msg) {
		JSONObject j = new JSONObject();
		try {
			j.put("msg", msg);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return j;
	}
	
	@SuppressWarnings("deprecation")
	public static Node createNode(Session session, JSONObject nc) throws Exception {
		String name = nc.getString("name");
		String pid = nc.getString("pid");
		String type = nc.getString("type");
		Node parent = session.getNodeByIdentifier(pid);
		//Node node = parent.addNode(name, type);
		Node node = parent.addNode(name);
		setProperties(node, nc.optJSONArray("props"));
		node.setProperty("type", type);
		if("nt:file".equals(type)) {
			String fileName = nc.getString("fileName");
			String uploadDir = System.getProperty("uploadDir");
			File file = new File(uploadDir, fileName);
			Node resNode = node.addNode ("jcr:content", "nt:resource");
	        resNode.setProperty ("jcr:data", new FileInputStream (file));
	        Calendar lastModified = Calendar.getInstance ();
	        lastModified.setTimeInMillis (file.lastModified ());
	        resNode.setProperty ("jcr:lastModified", lastModified);
	        resNode.setProperty ("jcr:mimeType", nc.getString("mime"));
		}
		//else if("nt:unstructured".equals(type)) {
			node.setProperty("jcr:created", Calendar.getInstance());
		//}
		session.save();
		return node;
	}

	public static void setProperties(Node node, JSONArray props) throws Exception {
		if(props != null) {
			boolean modified = false;
			for(int i=0; i<props.length(); i++) {
				JSONObject o = props.getJSONObject(i);
				String name = o.getString("name");
				String value = o.getString("value");
				node.setProperty(name, value);
				modified = true;
			}
			if(modified) {
				node.setProperty ("jcr:lastModified", Calendar.getInstance());
			}
		}
	}
	
}
