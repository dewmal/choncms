package org.chon.cms.ui.newsletter.actions.ajax;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.chon.cms.model.ContentModel;
import org.chon.cms.ui.newsletter.NewsletterExtension;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public class SaveNewsletterTextAjaxAction implements Action {
	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel contentModel = (ContentModel) req.attr(ContentModel.KEY);
		String newsletterName = req.get("name");
		try {
			Node node = contentModel
					.getPublicNode()
					.getChild(
							NewsletterExtension.NEWESLETTER_PUBLIC_CONTAINER_NAME)
					.getChild(newsletterName).getNode();
			node.setProperty("newsletterText", req.get("newsletterText"));
			node.setProperty("newsletterSubject", req.get("newsletterSubject"));
			node.getSession().save();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Oops, an error occured. " + e.getMessage();
		}
		return "OK";
	}
}