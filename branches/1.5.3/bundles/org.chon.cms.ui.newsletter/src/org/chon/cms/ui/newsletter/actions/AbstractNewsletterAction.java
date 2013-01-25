package org.chon.cms.ui.newsletter.actions;

import java.util.ArrayList;
import java.util.List;

import org.chon.cms.services.newsletter.Newsletter;
import org.chon.cms.services.newsletter.NewsletterException;
import org.chon.cms.services.newsletter.NewsletterSystem;
import org.chon.cms.ui.newsletter.NewsletterExtension;
import org.chon.cms.ui.newsletter.NewsletterHelper;
import org.chon.web.mpac.Action;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractNewsletterAction implements Action {
	private NewsletterExtension newsletterExtension;

	public AbstractNewsletterAction(NewsletterExtension newsletterExtension) {
		this.newsletterExtension = newsletterExtension;
	}
	
	protected Newsletter getNewsletter(String name, boolean create) throws NewsletterException {
		NewsletterSystem newsletterSystem = NewsletterHelper.getNewsletterSystem(newsletterExtension.getBundleContext());
		return newsletterSystem.getNewsletter(name, create);
	}
	
	protected List<Newsletter> getConfiguredNewsletterList() throws NewsletterException, JSONException {
		List<Newsletter> rv = new ArrayList<Newsletter>();
		JSONArray dashboardNewsletters = newsletterExtension.getConfig().optJSONArray("dashboardNewsletters");
		if(dashboardNewsletters != null && dashboardNewsletters.length() > 0) {
			NewsletterSystem newsletterSystem = NewsletterHelper.getNewsletterSystem(newsletterExtension.getBundleContext());
			for(int i=0; i<dashboardNewsletters.length(); i++) {
				String newsletterName = dashboardNewsletters.getString(i);
				Newsletter newsletter = newsletterSystem.getNewsletter(newsletterName, true);
				rv.add(newsletter);
			}
		}
		return rv;
	}
	
	protected String getPrefix() {
		return newsletterExtension.getPrefix();
	}
	
	protected JSONObject getConfig() {
		return newsletterExtension.getConfig();
	}
}
