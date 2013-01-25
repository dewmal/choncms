package org.chon.cms.admin.mpac;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringEscapeUtils;
import org.chon.cms.admin.mpac.actions.Action_createNode;
import org.chon.cms.admin.utils.JSONUtils;
import org.chon.cms.admin.utils.NodeView;
import org.chon.cms.admin.utils.RepoJSONService;
import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.Repo;
import org.chon.cms.model.ContentModel;
import org.chon.jcr.client.service.RepoService;
import org.chon.jcr.client.service.model.NodeAttribute;
import org.chon.jcr.client.service.model.NodeInfo;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.chon.web.mpac.ActionHandler;
import org.chon.web.util.upload.UploadInfo;
import org.chon.web.util.upload.UploadListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AdminAjax implements ActionHandler {

	private final class Action_getNodes implements Action {
		@Override
		public String run(Application app, Request req, Response resp) {
			ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
			RepoService repoSvc = Repo.getRepoService();
			String id = req.get("node");
			if("root".equals(id)) {
				id = null;
			}
			
			try {
				NodeInfo node = repoSvc.getNode(cm.getSession(), id, 2, true);
				JSONArray arr = new JSONArray();
				if(id!=null) {
					JSONObject idJson = new JSONObject();
					idJson.put("text", "id=" + "<span style='color: gray;'>"+node.getId()+"</span>");
					idJson.put("leaf", true);
					idJson.put("id", node.getPath() + "/id");
					idJson.put("draggable", false);
					idJson.put("cls", "property");
					arr.put(idJson);
					
					JSONObject typeJson = new JSONObject();
					typeJson.put("text", "type=" + "<span style='color: gray;'>"+node.getType()+"</span>");
					typeJson.put("leaf", true);
					typeJson.put("id", node.getPath() + "/type");
					typeJson.put("draggable", false);
					typeJson.put("cls", "property");
					arr.put(typeJson);
				}
				NodeAttribute[] properties = node.getAttributes();
				if(properties != null) {
					for(NodeAttribute a : properties) {
						if("jcr:primaryType".equals(a.getName())) {
							continue;
						}
						if("type".equals(a.getName())) {
							continue;
						}
						
						JSONObject j = new JSONObject();
						String value = a.getData();
						if(value==null) value="";
						value = StringEscapeUtils.escapeHtml(value);
						if(value.length()>100) {
							value = "<span title='"+value+"' style='color: blue;'>[DATA]</span>";
						} else {
							value = "<span style='color: green;'>"+value+"</span>";
						}
						j.put("text", a.getName() + "=" + value);
						j.put("leaf", true);
						j.put("id", node.getPath() + "/" + a.getName());
						j.put("draggable", false);
						j.put("cls", "property");
						arr.put(j);
					}
				}
				List<NodeInfo> childs = node.getChilds();
				for(NodeInfo ni : childs) {
						
					boolean leaf = ni.getChilds()==null;
					JSONObject j = new JSONObject();
					j.put("text", ni.getName());
					j.put("leaf", leaf);
					j.put("id", ni.getId());
					j.put("cls", "type-" + ni.getType().replace(':', '-'));
					if("root".equals(ni.getType())) {
						j.put("expanded", true);
					}
					arr.put(j);
				}
				return arr.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	

	private final class Action_jrs implements Action {
		@Override
		public String run(Application app, Request req, Response resp) {
			ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
			Session session = cm.getSession();
			try {
				String svc_name = req.get("name");
				JSONObject svc_req = new JSONObject(req.get("req"));
				
				RepoService repoSvc = Repo.getRepoService();
				RepoJSONService jsonSvc = new RepoJSONService(repoSvc, session);
				Method m = jsonSvc.getClass().getMethod(svc_name, JSONObject.class);
				Object responseObj = m.invoke(jsonSvc, svc_req);
				//session.logout();
				if(responseObj instanceof JSONArray) {
					return ((JSONArray)responseObj).toString(2);
				}
				if(responseObj instanceof JSONObject) {
					return ((JSONObject)responseObj).toString(2);
				}
				return responseObj.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
		}
	}

	private final class Action_getUploadProgess implements Action {
		@Override
		public String run(Application app, Request req, Response resp) {
			UploadInfo info = (UploadInfo) req.attr(UploadListener.UPLOAD_INFO_SESSSION_ATTR);
			if(info == null) {
				return "null";
			} 
			if("done".equals(info.getStatus())) {
				req.setSessAttr(UploadListener.UPLOAD_INFO_SESSSION_ATTR, null);
			}
			
			JSONObject fi = new JSONObject();
			try {
				fi.put("bytesRead", info.getBytesRead());
				fi.put("elapsedTime", info.getElapsedTime());
				fi.put("fileIndex", info.getFileIndex());
				fi.put("status", info.getStatus());
				fi.put("totalSize", info.getTotalSize());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return fi.toString();
		}
	}



	private final class Action_default implements Action {
		@Override
		public String run(Application app, Request req, Response resp) {
			return "Ajax default Action";
		}
	}

	private final class Action_deleteNode implements Action {
		@Override
		public String run(Application app, Request req, Response resp) {
			String nodeId = req.get("id");
			try {
				ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
				Node node = cm.getSession().getNodeByIdentifier(nodeId);
				node.remove();
				cm.getSession().save();
			} catch (ItemNotFoundException e) {
				return JSONUtils.errorMsg(e, "ERROR").toString();
			} catch (RepositoryException e) {
				return JSONUtils.errorMsg(e, "FATAL ERROR").toString();
			}
			return JSONUtils.msg("OK").toString();
		}
	}

	private final class Action_getNode implements Action {
		@Override
		public String run(Application app, Request req, Response resp) {
			ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
			NodeView nView = new NodeView(cm.getSession());
			String nodeId = req.get("id");
			if(nodeId != null) {
				return nView.renderNodeByIdToJSON(nodeId).toString();
			}
			
			Node node = nView.getNode(req.get("node"));
			return nView.renderNodeToJson(node).toString();
		}
	}

	private JCRApplication app;
	private HashMap<String, Action> actions;

	public AdminAjax(JCRApplication app) {
		this.app = app;
		actions = new HashMap<String, Action>();
		actions.put("deleteNode", new Action_deleteNode());
		actions.put("getNode", new Action_getNode());
		actions.put("createNode", new Action_createNode());
		actions.put("getUploadProgress", new Action_getUploadProgess());
		actions.put("jrs", new Action_jrs());
		actions.put("getNodes", new Action_getNodes());
	}
	@Override
	public Map<String, Action> getActions() {
		Map<String, Extension> exts = app.getExts();
		Iterator<String> keys = exts.keySet().iterator();
		while(keys.hasNext()) {
			String k = keys.next();
			Extension ext = exts.get(k);
			Map<String, Action> ajaxActons = ext.getAjaxActons();
			if(ajaxActons != null) {
				actions.putAll(ajaxActons);
			}
		}
		return actions;
	}

	@Override
	public Action getDefaulAction() {
		return new Action_default();
	}

}
