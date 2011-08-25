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

	public FormsExtension(JCRApplication app) {
		//this.app = app;
	}

	@Override
	public Map<String, Action> getAdminActons() {
		Map<String, Action> actions = new HashMap<String, Action>();
		actions.put("com.choncms.webpage.forms", new Action() {
			
			@Override
			public String run(Application app, Request req, Response resp) {
				return "com.choncms.webpage.forms";
			}
		});
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
