package org.chon.cms.ui.lightbox.ext;

import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public class LightboxExtension implements Extension {
	private JCRApplication app;

	public LightboxExtension(JCRApplication app) {
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
		try {
			return new LightBox(req, resp, node, app);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
