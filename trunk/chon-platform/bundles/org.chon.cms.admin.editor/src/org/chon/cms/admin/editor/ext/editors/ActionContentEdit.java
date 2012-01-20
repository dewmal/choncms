package org.chon.cms.admin.editor.ext.editors;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.chon.cms.admin.editor.model.NodeEditor;
import org.chon.cms.admin.utils.RepoJSONService;
import org.chon.cms.core.Repo;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.jcr.client.service.RepoService;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class ActionContentEdit implements Action {
	private static final NodeEditor DEFAULT_NODE_EDITOR = new DefaultNodeEditor();
	private BundleContext bundleContext;
	private NodeEditor nodeEditor;
	private JSONObject pluginConfig;

	public ActionContentEdit(BundleContext bundleContext, JSONObject config) {
		this.pluginConfig = config;
		this.bundleContext = bundleContext;
	}
	public ActionContentEdit(NodeEditor nodeEditor) {
		this.nodeEditor = nodeEditor;
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		IContentNode editedNode = null;
		String path = req.get("path");
		if (path != null) {
			editedNode = cm.getContentNode(path);
		}
		if (editedNode == null) {
			String id = req.get("id");
			Node node;
			try {
				node = cm.getSession().getNodeByIdentifier(id);
				editedNode = cm.getContentNode(node);
			} catch (ItemNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// we have editedNode

		// FIND EDITOR,
		// PUT NODE IN CONTEXT
		Map<String, Object> params = new HashMap<String, Object>();
		boolean isInPublicFolder = editedNode.getAbsPath().startsWith(
				cm.getPublicNode().getAbsPath());
		params.put("this", editedNode);
		params.put("isInPublicFolder", isInPublicFolder);
		NodeEditor nodeEditor = getNodeEditor(editedNode.getType());
		RepoService service = Repo.getRepoService();
		RepoJSONService jsonService = new RepoJSONService(service,
				cm.getSession());
		Map<String, Object> config = new HashMap<String, Object>();
		try {
			config.put("showTemplateHelpers", pluginConfig.getBoolean("showTemplateHelpers"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.put("config", config);
		String tplOut = resp.formatTemplate(nodeEditor.getTemplate(editedNode), params);

		try {
			JSONObject svcReq = new JSONObject("{sessionId: 'wtf', id: '"
					+ editedNode.getNode().getIdentifier()
					+ "', depth: 1, includeAttributes: true }");
			// TODO: check error
			JSONObject svcResp = jsonService.getNode(svcReq);
			params.put("nodeJSON", svcResp.getJSONObject("node").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.put("BODY", tplOut);
		return resp.formatTemplate("admin/editor/base.html", params);
	}

	protected NodeEditor getNodeEditor(String type) {
		if(this.nodeEditor != null) {
			return nodeEditor;
		}
		
		try {
			ServiceReference[] refs = bundleContext.getServiceReferences(
					NodeEditor.class.getName(), "(type=" + type + ")");
			if (refs != null && refs.length > 0) {
				return (NodeEditor) bundleContext.getService(refs[0]);
			}
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DEFAULT_NODE_EDITOR;
	}
}
