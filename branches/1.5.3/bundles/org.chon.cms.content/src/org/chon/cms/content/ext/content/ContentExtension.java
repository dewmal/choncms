package org.chon.cms.content.ext.content;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.chon.cms.content.ext.content.actions.AjaxAction_removeFile;
import org.chon.cms.content.ext.content.actions.GenericCreateAction;
import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;



public class ContentExtension implements Extension {
	
	private static Map<String, Action> actions = new HashMap<String, Action>();
	private static Map<String, Action> ajaxActions = new HashMap<String, Action>();
	
	public ContentExtension(JCRApplication app) {		
		actions.put("content.create.html", new GenericCreateAction(
				"Create HTML Content", "", "html", "html", null, null));
		actions.put("content.create.category", new GenericCreateAction(
				"Create Category",
				"Create container node for childs html and category nodes",
				"category", "", null, null));
		
		actions.put("index", new Action() {	
			@Override
			public String run(Application app, Request req, Response resp) {
				try {
					resp.getServletResponse().sendRedirect("explorer.main.do");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		});
		ajaxActions.put("removeFile", new AjaxAction_removeFile());
	}
	
	@Override
	public Map<String, Action> getAdminActons() {
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		return ajaxActions;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		return null;
	}
}
