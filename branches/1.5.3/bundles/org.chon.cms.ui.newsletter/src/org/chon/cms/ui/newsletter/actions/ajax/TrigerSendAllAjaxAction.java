package org.chon.cms.ui.newsletter.actions.ajax;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.services.newsletter.Newsletter;
import org.chon.cms.services.newsletter.NewsletterException;
import org.chon.cms.ui.newsletter.NewsletterExtension;
import org.chon.cms.ui.newsletter.NewsletterHelper;
import org.chon.cms.ui.newsletter.actions.AbstractNewsletterAction;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class TrigerSendAllAjaxAction extends AbstractNewsletterAction {
	public TrigerSendAllAjaxAction(NewsletterExtension newsletterExtension) {
		super(newsletterExtension);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel contentModel = (ContentModel) req.attr(ContentModel.KEY);
		String newsletterName = req.get("name");
		try {
			Newsletter newsletter = getNewsletter(newsletterName, false);
			IContentNode node = contentModel.getPublicNode()
					.getChild(NewsletterExtension.NEWESLETTER_PUBLIC_CONTAINER_NAME)
					.getChild(newsletterName);
			NewsletterHelper newsletterHelper = new NewsletterHelper(
					newsletter, node, req, resp, app);
			newsletterHelper.sendAll();
		} catch (NewsletterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Oops, an error occured. " + e.getMessage();
		}
		return "OK";
	}
}