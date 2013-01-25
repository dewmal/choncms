package org.chon.cms.ui.newsletter.actions.ajax;

import java.util.Map;

import org.chon.cms.services.newsletter.Newsletter;
import org.chon.cms.services.newsletter.NewsletterException;
import org.chon.cms.ui.newsletter.NewsletterExtension;
import org.chon.cms.ui.newsletter.actions.AbstractNewsletterAction;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class GetNewsletterPercentCompleteAjaxAction extends
		AbstractNewsletterAction {
	public GetNewsletterPercentCompleteAjaxAction(
			NewsletterExtension newsletterExtension) {
		super(newsletterExtension);
	}

	@Override
	public String run(Application app, Request req, Response resp) {

		String newsletterName = req.get("name");
		try {
			Newsletter newsletter = getNewsletter(newsletterName, false);
			Map<String, Object> ni = newsletter.getInfo();
			return "" + ni.get("percentComplete");
		} catch (NewsletterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Oops, an error occured. " + e.getMessage();
		}
	}
}