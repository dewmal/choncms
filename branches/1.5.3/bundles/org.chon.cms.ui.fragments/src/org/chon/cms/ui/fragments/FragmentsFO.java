package org.chon.cms.ui.fragments;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.chon.cms.core.model.renderers.VTplNodeRenderer;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.json.JSONObject;

public class FragmentsFO {
	private Application app;
	private Request req; 
	private Response resp;
	private IContentNode contentNode;
	private IContentNode fragmentsNode;
	private JSONObject config;
	
	public FragmentsFO(Application app, Request req, Response resp, IContentNode contentNode,
			IContentNode fragmentsNode, JSONObject config) {
		this.app = app;
		this.req = req;
		this.resp = resp;
		this.contentNode = contentNode;
		this.fragmentsNode = fragmentsNode;
		this.config = config;
	}

	public String get(String name) {
		try {
			Property p = fragmentsNode.getNode().getProperty(FragmentsExtenstion.FRAGMENT_PREFIX + name);
			String v = p.getString();
			Map<String, Object> params = new HashMap<String, Object>();
			VTplNodeRenderer.prepareParams(contentNode, req, resp, params, app);
			params.put("ctx", resp.getTemplateContext());
			return app.getTemplate().formatStr(v, params, "fragment#template[" + name +"]");
		} catch (PathNotFoundException e) {
			return config.optString("fragmentNotExistsErrorMsg", "Fragment " + name + " does not exists.");
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Oops, an error occured. " + e.getMessage();
		}
	}
}
