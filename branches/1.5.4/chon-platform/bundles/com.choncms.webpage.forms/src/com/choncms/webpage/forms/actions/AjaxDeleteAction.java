package com.choncms.webpage.forms.actions;

import javax.jcr.RepositoryException;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class AjaxDeleteAction  extends AbstractFormsAction {
	public AjaxDeleteAction(String prefix, IContentNode appFormDataNode) {
		super(prefix, appFormDataNode);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		try {
			IContentNode form = appFormDataNode.getChild(req.get("name"));
			form.getNode().remove();
			appFormDataNode.getContentModel().getSession().save();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
		return "OK";
	}

}
