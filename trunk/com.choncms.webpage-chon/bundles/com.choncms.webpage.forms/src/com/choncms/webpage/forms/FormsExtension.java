package com.choncms.webpage.forms;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public class FormsExtension implements Extension {
	private Map<String, Action> actions = new HashMap<String, Action>();

	public FormsExtension(JCRApplication app, final String prefix) {
		//this.app = app;
		actions.put(prefix + ".create", new Action() {
			@Override
			public String run(Application app, Request req, Response resp) {
				Map<String, Object> params = new HashMap<String, Object>();
				return resp.formatTemplate(prefix + "/editForm.html", params);
			}
		});
	}

	@Override
	public Map<String, Action> getAdminActons() {
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
