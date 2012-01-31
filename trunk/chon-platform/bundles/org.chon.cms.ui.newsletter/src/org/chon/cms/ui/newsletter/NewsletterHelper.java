package org.chon.cms.ui.newsletter;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.core.model.renderers.VTplNodeRenderer;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.services.newsletter.Newsletter;
import org.chon.cms.services.newsletter.NewsletterException;
import org.chon.cms.services.newsletter.NewsletterSystem;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class NewsletterHelper {
	private Newsletter newsletter;
	private IContentNode newsletterPubNode;
	private Request req;
	private Response resp;
	private Application app;
	
	public NewsletterHelper(Newsletter newsletter,
			IContentNode newsletterPubNode, Request req, Response resp,
			Application app) {
		super();
		this.newsletter = newsletter;
		this.newsletterPubNode = newsletterPubNode;
		this.req = req;
		this.resp = resp;
		this.app = app;
	}
	
	public String getPreview(boolean templateOnly) {
		return getPreparedTemplate(!templateOnly, true);
	}
	
	private String getPreparedTemplate(boolean mergeData, boolean replaceNewLineWithBr) {
		Map<String, Object> params = new HashMap<String, Object>();
		VTplNodeRenderer.prepareParams(newsletterPubNode, req, resp, params, app);
		String newsletterHtmlTemplateText = newsletterPubNode.prop("newsletterHtmlTemplateText");
		if(newsletterHtmlTemplateText == null) {
			newsletterHtmlTemplateText = "Newsletter HTML Tempalte ...";
		}
		params.put("ctx", resp.getTemplateContext());
		if(mergeData) {
			String newsletterText = newsletterPubNode.prop("newsletterText");
			if(newsletterText == null) {
				newsletterText = "";
			}
			if(replaceNewLineWithBr) {
				newsletterText = newsletterText.replace("\n", "<br />");
			}
			params.put("newsletterText", newsletterText);
		}
		String r = app.getTemplate().formatStr(newsletterHtmlTemplateText, params, "#template: " + newsletterPubNode.getPath());
		return r;
	}


	
	public static NewsletterSystem getNewsletterSystem(BundleContext bundleContext) throws NewsletterException {
		ServiceReference ref = bundleContext.getServiceReference(NewsletterSystem.class.getName());
		if(ref == null) {
			throw new NewsletterException("NewsletterSystem service not available");
		}
		return (NewsletterSystem) bundleContext.getService(ref);	
	}
	
	private String getNewsletterSubject() {
		String newsletterSubject = newsletterPubNode.prop("newsletterSubject");
		if(newsletterSubject == null) {
			newsletterSubject = "Newsletter Subject";
		}
		return newsletterSubject;
	}

	public void send(String email) throws NewsletterException {
		String tpl = getPreparedTemplate(true, true);
		newsletter.send(email, getNewsletterSubject(), tpl, null);
	}
	
	public void sendAll() throws NewsletterException {
		String tpl = getPreparedTemplate(true, true);
		newsletter.send(getNewsletterSubject(), tpl, null);
	}
}
