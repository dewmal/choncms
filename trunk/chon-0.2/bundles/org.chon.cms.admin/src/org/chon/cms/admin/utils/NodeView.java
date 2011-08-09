package org.chon.cms.admin.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NodeView {
	private Session session;

	public NodeView(Session session) {
		this.session = session;
	} 
	public Object renderNodeToJson(Node node) {
		try {
			if(node.getPrimaryNodeType().getName().equals("rep:root")) {
				return rootNode(node);
			} else {
				return childsToJSONArray(node);
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private JSONObject rootNode(Node root) throws RepositoryException {
		try {
			JSONObject o = new JSONObject();
			JSONObject attr = new JSONObject();
			attr.put("rel", "system");
			o.put("attr", attr);
			o.put("data", "ROOT");
			o.put("state", "open");
			o.put("children", childsToJSONArray(root));
			return o;
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	private JSONArray childsToJSONArray(Node node) throws RepositoryException {
		JSONArray arr = new JSONArray();
		boolean isJCRContentNode = "jcr:content".equals(node.getName());
		try {
			PropertyIterator pi = node.getProperties();
			while(pi.hasNext()) {
				Property p = pi.nextProperty();
				//if(!p.getName().startsWith("jcr:")) {
					JSONObject o = toJSONObject(p, isJCRContentNode);
					arr.put(o);
				//}
			}
			NodeIterator childs = node.getNodes();
			while (childs.hasNext()) {
				Node n = childs.nextNode();
				JSONObject o = toJSONObject(n);
				arr.put(o);
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sort(arr);
	}

	private JSONArray sort(JSONArray arr) {
		try {
			LinkedList<JSONObject> ls = new LinkedList<JSONObject>();
			for(int a=0; a<arr.length(); a++) {
				ls.add(arr.getJSONObject(a));
			}
			Collections.sort(ls, new Comparator<JSONObject>() {
				@Override
				public int compare(JSONObject o1, JSONObject o2) {
					return o1.optString("sid").compareTo(o2.optString("sid"));
				}
			});
			JSONArray sorted = new JSONArray();
			for(JSONObject o : ls) {
				sorted.put(o);
			}
			return sorted;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	private JSONObject toJSONObject(Property p, boolean contentNode) {
		JSONObject obj = new JSONObject();
		try {
			
			JSONObject attr = new JSONObject();
			obj.put("attr", attr);
			attr.put("rel", "property");
			String value;
			if (contentNode && "jcr:data".equals(p.getName())) {
				value = "TODO: SHOW Binary data somehow... barem slikcinjata :)";
			} else {
				if (p.isMultiple()) {
					Value[] values = p.getValues();
					JSONArray arr = new JSONArray();
					for(Value v : values) {						
						arr.put(v.getString());
					}
					value = arr.toString();
				} else {
					value = p.getValue().getString();
				}
			}
			if(value==null) value="";
			value = StringEscapeUtils.escapeHtml(value);
			String style;
			if(value.length()>200) {
				value = "<span title='"+value+"'>[DATA]</span>";
				style = "color:gray; font-size: small; font-weight: bold";
			} else {
				style = "color:green; font-size: small";
			}
			String name = p.getName();
			obj.put("sid", "0:"+name);
			obj.put("data", 
					"<span style='color:red; font-size: small'>" + name + "</span>" +
					"<span style='font-size: small'> = </span>" + 
					"<span style='"+style+"'>" + value + "</span>"
				);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	public static JSONObject toJSONObject(Node n) {
		JSONObject obj = new JSONObject();
		try {
			JSONObject attr = new JSONObject();
			obj.put("attr", attr);
			attr.put("id", n.getIdentifier());
			String type = getRelFromNodeType(n.getPrimaryNodeType().getName(), n);
			attr.put("rel", type);
			attr.put("path", n.getPath());
			obj.put("state", "closed");
			String name = n.getName();
			obj.put("data", name);
			obj.put("sid", type+":"+name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	private static String getRelFromNodeType(String name, Node n) {
		if(name.startsWith("rep:")) {
			return "system";
		} else if(name.startsWith("nt:")){
			if("nt:unstructured".equals(name)) {
				try {
					return n.getProperty("type").getString();
				} catch (Exception e) {	}
			}
			return name.substring(3);
		}
		return name;
	}

	public Node getNode(String nodeId) {
		Node node = null;
		try {
			if (nodeId == null || nodeId.length()==0) {
				node = session.getRootNode();
			} else {
				node = session.getNodeByIdentifier(nodeId);
			}
		} catch (ItemNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return node;
	}
	public JSONObject renderNodeByIdToJSON(String nodeId) {
		return toJSONObject(getNode(nodeId));
	}
}
