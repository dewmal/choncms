package org.chon.cms.ui.newsletter.actions.ajax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chon.cms.services.newsletter.Newsletter;
import org.chon.cms.services.newsletter.NewsletterException;
import org.chon.cms.ui.newsletter.NewsletterExtension;
import org.chon.cms.ui.newsletter.Paginator;
import org.chon.cms.ui.newsletter.actions.AbstractNewsletterAction;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class SubscriberListAjaxAction extends AbstractNewsletterAction {
	public SubscriberListAjaxAction(NewsletterExtension newsletterExtension) {
		super(newsletterExtension);
	}

	private JSONObject getSubscriberListGridConfig() throws JSONException {
		return getConfig().getJSONObject("subscriberListGrid");
	}

	private Integer getPageSize() throws JSONException {
		return getSubscriberListGridConfig().getInt("pageSize");
	}

	private Integer getPaginatorSize() throws JSONException {
		return getSubscriberListGridConfig().getInt("paginatorSize");
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prefix", getPrefix());

		try {
			Newsletter newsletter = getNewsletter(req.get("name"), false);

			params.put("this", newsletter);
			int page = req.getInt("page", 1);
			
			long totalSubscribers = newsletter.getTotalSubscribers();
			Paginator paginator = new Paginator(page, totalSubscribers, getPageSize());
			
			List<String> subscribers = newsletter.getSubscribers(
					paginator.getStart(), paginator.getLimit());
			
			params.put("subscribers", subscribers);
			params.put("paginator", paginator);
			
			params.put("cfgPaginatorSize", getPaginatorSize());

			return resp.formatTemplate(getPrefix()
					+ "/admin/subscriberList.html", params);
		} catch (NewsletterException e) {
			e.printStackTrace();
			return "Oops, an error occured. " + e.getMessage();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Oops, Configuration exception. " + e.getMessage();
		}
	}
}