package com.chon.cms.ui.jquery.ext;

import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONObject;

public class JQueryExtenstion implements Extension {
	private JCRApplication app;
	private JQueryConfig config;

	public JQueryExtenstion(JCRApplication app, JSONObject cfg) {
		this.app = app;
		this.config = new JQueryConfig(cfg);
	}

	@Override
	public Map<String, Action> getAdminActons() {
		return null;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		return null;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		return new jQuery(req, resp, node, app, config);
	}

}
