package com.chon.cms.ui.jquery.ext;

import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public class JQueryExtenstion implements Extension {
	private JCRApplication app;

	public JQueryExtenstion(JCRApplication app) {
		this.app = app;
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
		return new jQuery(req, resp, node, app);
	}

}
