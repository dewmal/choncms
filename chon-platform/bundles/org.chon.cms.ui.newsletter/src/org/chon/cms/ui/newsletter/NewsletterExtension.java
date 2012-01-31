package org.chon.cms.ui.newsletter;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.ui.newsletter.actions.DashboardAction;
import org.chon.cms.ui.newsletter.actions.NewsletterAction;
import org.chon.cms.ui.newsletter.actions.ajax.GetNewsletterPercentCompleteAjaxAction;
import org.chon.cms.ui.newsletter.actions.ajax.NewsletterSendExampleAjaxAction;
import org.chon.cms.ui.newsletter.actions.ajax.SaveNewsletterTemplateAjaxAction;
import org.chon.cms.ui.newsletter.actions.ajax.SaveNewsletterTextAjaxAction;
import org.chon.cms.ui.newsletter.actions.ajax.SubscriberListAjaxAction;
import org.chon.cms.ui.newsletter.actions.ajax.TrigerSendAllAjaxAction;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;

public class NewsletterExtension implements Extension {
	public static final String NEWESLETTER_PUBLIC_CONTAINER_NAME = "newsletter";
	
	private String prefix;
	private JSONObject config;
	private BundleContext bundleContext;

	public NewsletterExtension(String prefix, JSONObject config, BundleContext bundleContext) {
		this.prefix = prefix;
		this.config = config;
		this.bundleContext = bundleContext;
	}
	
	
	public String getPrefix() {
		return prefix;
	}


	public JSONObject getConfig() {
		return config;
	}


	public BundleContext getBundleContext() {
		return bundleContext;
	}
	
	@Override
	public Map<String, Action> getAdminActons() {
		Map<String, Action> actions = new HashMap<String, Action>();
		actions.put(prefix + ".dashboard", new DashboardAction(this));
		actions.put(prefix + ".newsletter", new NewsletterAction(this));
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		Map<String, Action> ajaxActions = new HashMap<String, Action>();
		ajaxActions.put(prefix + ".subscriberList", new SubscriberListAjaxAction(this));
		ajaxActions.put(prefix + ".saveNewsletterTemplate", new SaveNewsletterTemplateAjaxAction());
		ajaxActions.put(prefix + ".saveNewsletterText", new SaveNewsletterTextAjaxAction());
		ajaxActions.put(prefix + ".newsletterSendExample", new NewsletterSendExampleAjaxAction(this));
		ajaxActions.put(prefix + ".getNewsletterPercentComplete", new GetNewsletterPercentCompleteAjaxAction(this));
		ajaxActions.put(prefix + ".trigerSendAll", new TrigerSendAllAjaxAction(this));
		return ajaxActions;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
