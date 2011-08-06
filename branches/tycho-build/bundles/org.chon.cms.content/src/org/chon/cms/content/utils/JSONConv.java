package org.chon.cms.content.utils;

import java.util.List;

import org.chon.cms.core.model.types.ContentNode;
import org.chon.cms.core.model.types.FileContentNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONConv {
	public static JSONArray toJSONArr(List<FileContentNode> images) throws JSONException {
		JSONArray arr = new JSONArray();
		for(int i=0; i<images.size(); i++) {
			FileContentNode fcn = images.get(i);
			JSONObject img = new JSONObject();
			img.put("name", fcn.getName());
			img.put("contentType", fcn.getMimeType());
			arr.put(img);
		}
		return arr;
	}
	
	public static String toJSONStr(ContentNode node) {
		JSONObject n = new JSONObject();
		try {
			n.put("id", node.getId());
			n.put("path", node.getPath());
			n.put("htmlText", node.prop("htmlText"));
			JSONArray files = toJSONArr(node.getFiles());
			n.put("files", files);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return n.toString();
	}
}
