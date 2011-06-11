package org.chon.cms.content.ext.news;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;



public class NewsExtension implements Extension {
	
	public NewsExtension(JCRApplication app) {
	
	}

	@Override
	public Map<String, Action> getAdminActons() {
		Map<String, Action> actions = new HashMap<String, Action>();
		
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		Map<String, Action> actions = new HashMap<String, Action>();
		
		return actions;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		return new NewsView(cm);
	}

	
	
}
