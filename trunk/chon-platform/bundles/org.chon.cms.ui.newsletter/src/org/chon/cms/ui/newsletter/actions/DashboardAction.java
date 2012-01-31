package org.chon.cms.ui.newsletter.actions;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.services.newsletter.NewsletterException;
import org.chon.cms.ui.newsletter.NewsletterExtension;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.json.JSONException;

public class DashboardAction extends AbstractNewsletterAction {

	public DashboardAction(NewsletterExtension newsletterExtension) {
		super(newsletterExtension);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prefix", getPrefix());
		try {
			params.put("newsletters", getConfiguredNewsletterList());
		} catch (NewsletterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp.formatTemplate(getPrefix() + "/admin/dashboard.html", params);
	}
}