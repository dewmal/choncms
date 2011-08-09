package org.chon.cms.admin.utils;

import java.util.List;

import javax.jcr.Session;

import org.chon.jcr.client.service.RepoService;
import org.chon.jcr.client.service.model.AttributeType;
import org.chon.jcr.client.service.model.NodeAttribute;
import org.chon.jcr.client.service.model.NodeInfo;
import org.chon.jcr.client.service.model.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RepoJSONService {
	private RepoService service;
	private Session session;
	public RepoJSONService(RepoService service, Session session) {
		this.service = service;
		this.session = session;
	}

	private Session getSession(String sessionId) {
		return session;
	}

	private RepoService getRepoService() {
		return service;
	}

	
	
	private JSONObject toJSONObject(Status s) throws JSONException {
		JSONObject j = new JSONObject();
		j.put("code", s.getCode());
		j.put("description", s.getDescription());
		j.put("data", s.getData());
		return j;
	}
	
	private JSONObject toJSONObject(NodeInfo nodeInfo, Status status, boolean topLevel) throws JSONException {
		JSONObject resp = new JSONObject();
		if(nodeInfo != null) {
		if(topLevel) {
			nodeInfoJSON(nodeInfo, resp);
		} else {
			JSONObject nodeInfoJSONObj = new JSONObject();
			nodeInfoJSON(nodeInfo, nodeInfoJSONObj);
			resp.put("node", nodeInfoJSONObj);
		}
		}
		resp.put("status", toJSONObject(status));
		return resp;
	}

	private void nodeInfoJSON(NodeInfo nodeInfo, JSONObject obj) throws JSONException {
		if(nodeInfo==null) {
			return;
		}
		
		obj.put("id", nodeInfo.getId());
		obj.put("name", nodeInfo.getName());
		obj.put("path", nodeInfo.getPath());
		NodeAttribute[] attrs = nodeInfo.getAttributes();
		if(attrs != null) {
			JSONArray arr = toJSONArray(attrs);
			obj.put("attrs", arr);
		}
		List<NodeInfo> childs = nodeInfo.getChilds();
		if(childs != null) {
			JSONArray arr = toJSONArray(childs);
			obj.put("childs", arr);
		}
	}

	private JSONArray toJSONArray(List<NodeInfo> nodes) throws JSONException {
		JSONArray arr = new JSONArray();
		for(NodeInfo n : nodes) {
			JSONObject obj = new JSONObject();
			nodeInfoJSON(n, obj);
			arr.put(obj);
		}
		return arr;
	}

	private JSONArray toJSONArray(NodeAttribute[] attrs) throws JSONException {
		JSONArray arr = new JSONArray();
		for(NodeAttribute a : attrs) {
			JSONObject obj = new JSONObject();
			String t = a.getType()==null ? "U" : a.getType().toString().substring(0, 1);
			obj.put("type", t);
			obj.put("name", a.getName());
			obj.put("value", a.getData());
			arr.put(obj);
		}
		return arr;
	}

	/**
	 * Check if node exists at passed absolute path
	 * 
	 * REQUEST: 
	 * 	{ sessionId: "the-session-id", path: "path/to/node" }
	 * 
	 * RESPONSE:
	 * 	{code: 0, description: "string", data: "true" }
	 * 
	 */
	public JSONObject nodeExists(JSONObject req) throws Exception {
		RepoService svc = getRepoService();
		Session session = getSession(req.getString("sessionId"));
		Status s = svc.nodeExists(session, req.getString("path"));
		return toJSONObject(s);
	}
	

	/**
	 * Check if node has property
	 * 
 	 * REQUEST: 
	 * 	{ sessionId: "the-session-id", id: "node-id", attr: "nodePropertyName" }
	 * 
	 * RESPONSE:
	 * 	{code: 0, description: "string", data: "true" }
	 */
	public JSONObject attrExists(JSONObject req) throws Exception {
		RepoService svc = getRepoService();
		Session session = getSession(req.getString("sessionId"));
		Status s = svc.attrExists(session, req.getString("id"), req.getString("attr"));
		return toJSONObject(s);
	}
	
	/**
	 * Creates New Node under Node with id - parentId
	 *  
	 * REQUEST: 
	 * 	{ 
	 * 		sessionId: "the-session-id", 
	 * 		parentId: "parent-node-id", 		// not required, if null parent will be root node
	 * 		name: "node-name", 
	 * 		attrs: [{							// node attributes
	 * 			name: 'attrName',
	 * 			type: 'type', 					// can be: B for BOOLEAN, T for TEXT, N for NUMBER, D for DATE; not required, default T
	 * 			value: 'attrValue'				// based on type this will be parsed
	 * 		}, { ... }]
	 * 	}
	 * 
	 * RESPONSE:
	 * 	{code: 0, description: "string", data: "true" }
	 */
	public JSONObject createNode(JSONObject req) throws Exception {
		RepoService svc = getRepoService();
		Session session = getSession(req.getString("sessionId"));
		NodeAttribute[] attributes = nodeAttrArrFromJsonArr(req.optJSONArray("attrs")); 
		Status s = svc.createNode(session, req.optString("parentId"), req.getString("name"), attributes);
		return toJSONObject(s);
	}
	
	/**
	 * Adds file node to node with passed id
	 *  (File must be on file system, TODO: maybe get from url)
	 *  
	 * @param session
	 * @param nodeId
	 * @param file
	 * @param mimeType - mime type of file, can be null
	 * 
	 * @return Status.OK if successfully created, with id from newly created node, or Status.ERROR if there is an error
	 */
	//TODO: file from URL, or base64 data?!
	//public Status addFile(Session session, String nodeId, File file, String mimeType);
	
	/**
	 * Edit Node attributes
	 * 
	 * REQUEST: 
	 * 	{ 
	 * 		sessionId: "the-session-id", 
	 * 		id: "node-id", 
	 * 		attrs: [{							// node attributes
	 * 			name: 'attrName',
	 * 			type: 'type', 					// can be: B for BOOLEAN, T for TEXT, N for NUMBER, D for DATE; not required, default T
	 * 			value: 'attrValue'				// based on type this will be parsed
	 * 		}, { ... }]
	 * 	}
	 * 
	 * RESPONSE:
	 * 	{code: 0, description: "string", data: "true" }
	 */
	public JSONObject editNode(JSONObject req) throws Exception {
		RepoService svc = getRepoService();
		Session session = getSession(req.getString("sessionId"));
		NodeAttribute[] attributes = nodeAttrArrFromJsonArr(req.optJSONArray("attrs"));
		Status s = svc.editNode(session, req.getString("id"), attributes);
		return toJSONObject(s);
	}
	
	private NodeAttribute[] nodeAttrArrFromJsonArr(JSONArray arr) throws JSONException {
		NodeAttribute [] attributes = null;
		if(arr != null) {
			attributes = new NodeAttribute[arr.length()];
			for(int i=0; i<arr.length(); i++) {
				JSONObject a = arr.getJSONObject(i);
				String t = a.optString("type");
				AttributeType type;
				type = AttributeType.fromString(t);
				if(type == null) {
					type = AttributeType.TEXT;
				}
				attributes[i] = new NodeAttribute(a.getString("name"), type, a.getString("value"));
			}
		}
		return attributes;
	}

	/**
	 * Rename Node
	 * 
	 * REQUEST: 
	 * 	{ sessionId: "the-session-id", id: "node-id", name: "newName" }
	 * 
	 * RESPONSE:
	 * 	{code: 0, description: "string", data: "true" }
	 */
	public JSONObject renameNode(JSONObject req) throws Exception {
		RepoService svc = getRepoService();
		Session session = getSession(req.getString("sessionId"));
		Status s = svc.renameNode(session, req.getString("id"), req.getString("name"));
		return toJSONObject(s);
	}
	
	/**
	 * Move node (change its parent)
	 * 
	 * REQUEST: 
	 * 	{ sessionId: "the-session-id", id: "node-id", parentId: "newParentId" }
	 * 
	 * RESPONSE:
	 * 	{code: 0, description: "string", data: "true" }
	 */
	public JSONObject moveNode(JSONObject req) throws Exception {
		RepoService svc = getRepoService();
		Session session = getSession(req.getString("sessionId"));
		Status s = svc.moveNode(session, req.getString("id"), req.getString("parentId"));
		return toJSONObject(s);
	}
	
	/**
	 * Remove property from node
	 * 
	 * REQUEST: 
	 * 	{ sessionId: "the-session-id", id: "node-id", attr: "nodePropertyName" }
	 * 
	 * RESPONSE:
	 * 	{code: 0, description: "string", data: "true" }
	 */
	public JSONObject deleteAttr(JSONObject req) throws Exception {
		RepoService svc = getRepoService();
		Session session = getSession(req.getString("sessionId"));
		Status s = svc.deleteAttr(session, req.getString("id"), req.getString("attr"));
		return toJSONObject(s);
	}
	
	/**
	 * Remove Node
	 * 
	 * REQUEST: 
	 * 	{ sessionId: "the-session-id", id: "node-id" }
	 * 
	 * RESPONSE:
	 * 	{code: 0, description: "string", data: "true" }
	 */
	public JSONObject deleteNode(JSONObject req) throws Exception {
		RepoService svc = getRepoService();
		Session session = getSession(req.getString("sessionId"));
		Status s = svc.deleteNode(session, req.getString("id"));
		return toJSONObject(s);
	}
	
	/**
	 * Get Node Properties
	 * 
	 * REQUEST: 
	 * 	{ sessionId: "the-session-id", id: "node-id" }
	 * 
	 * RESPONSE:
	 * 	{
	 * 		id: 'req-node-id',
	 * 		status: {code: 0, description: "string", data: "true" } 
	 * 		attr: [{								// node attributes
	 * 			name: 'attrName',
	 * 			type: 'type', 					// can be: B for BOOLEAN, T for TEXT, N for NUMBER, D for DATE; not required, default T
	 * 			value: 'attrValue'				// based on type this will be parsed
	 * 		}, { ... }]
	 *  }
	 */
	public JSONObject listAttr(JSONObject req) throws Exception {
		RepoService svc = getRepoService();
		Session session = getSession(req.getString("sessionId"));
		String nodeId = req.getString("id");
		Status status = Status.DEFAULT;
		NodeAttribute[] attrs = null;
		try {
			attrs = svc.listAttr(session, nodeId);
			status = Status.OK;
		} catch (Exception e) {
			status = new Status(-1, e.getMessage());
		}
		NodeInfo nodeInfo = new NodeInfo(nodeId, null, null, attrs, null, null, null);
		return toJSONObject(nodeInfo, status, true);
	}
	

	/**
	 * Get Node with childs to given depth,
	 * if depth is 0 then no childs are returned
	 * if depth is 1 only first childs are returned
	 * 
	 * REQUEST: 
	 * 	{ sessionId: "the-session-id", id: "node-id", depth: 1, includeAttributes: true }
	 * 
	 * RESPONSE:
	 * 	{
	 * 		status: {code: 0, description: "string", data: "true" }
	 * 		node: { 
	 * 			id: 'req-node-id',
	 * 			path: '',
	 * 			name: '',
	 *	 		attr: [{								// node attributes
	 * 				name: 'attrName',
	 * 				type: 'type', 					// can be: B for BOOLEAN, T for TEXT, N for NUMBER, D for DATE; not required, default T
	 * 				value: 'attrValue'				// based on type this will be parsed
	 * 			}, { ... }],
	 * 			childs: [{}, see-node-structure]
	 * 		}
	 *  }
	 */
	public JSONObject getNode(JSONObject req) throws Exception {
		RepoService svc = getRepoService();
		Session session = getSession(req.getString("sessionId"));
		String nodeId = null;
		try {
			nodeId = req.getString("id");
		} catch (Exception e) { }
		
		Status status = Status.DEFAULT;
		int depth = req.optInt("depth");
		boolean includeAttributes = req.optBoolean("includeAttributes");
		NodeInfo nodeInfo = null;
		try {
			nodeInfo = svc.getNode(session, nodeId, depth, includeAttributes);
			status = Status.OK;
		} catch (Exception e) {
			status = new Status(-1, e.getMessage());
		}
		return toJSONObject(nodeInfo, status, false);
	}
}
