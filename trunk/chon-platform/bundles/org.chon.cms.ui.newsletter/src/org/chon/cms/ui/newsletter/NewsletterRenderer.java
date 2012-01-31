package org.chon.cms.ui.newsletter;

import java.io.PrintWriter;

import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.INodeRenderer;
import org.chon.cms.services.newsletter.Newsletter;
import org.chon.cms.services.newsletter.NewsletterException;
import org.chon.cms.services.newsletter.NewsletterSystem;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;
import org.osgi.framework.BundleContext;

public class NewsletterRenderer implements INodeRenderer {
	private BundleContext bundleContext;
	
	public NewsletterRenderer(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public void render(IContentNode contentNode, Request req, Response resp,
			Application app, ServerInfo serverInfo) {
		PrintWriter out = resp.getOut();
		String action = req.get("action");
		if("unsubscribe".equals(action)) {
			String email = req.get("subscriber");
			String code = req.get("code");
			String msg = unsubscribe(contentNode, email, code);
			if("Success".equals(msg)) {
				if("POST".equals(req.getServletRequset().getMethod())) {
					out.print("OK");
				} else {
					//TODO: from config...
					out.println("Email '" + email + "' successfully removed from mailing list.");
				}
			} else {
				out.println(msg);
			}
		} else if("subscribe".equals(action)) {
			String email = req.get("subscriber");
			String msg = subscribe(contentNode, email);
			if("Success".equals(msg)) {
				if("POST".equals(req.getServletRequset().getMethod())) {
					out.print("OK");
				} else {
					//TODO: from config...
					out.println("Email '" + email + "' successfully added to subscribtion list.");
				}
			} else {
				out.println(msg);
			}
		} else if("preview".equals(action)) {
			boolean templateOnly = false;
			if("true".equals(req.get("templateOnly"))) {
				templateOnly = true;
			}
			resp.getServletResponse().setContentType("text/html");
			NewsletterHelper newsletterHelper = new NewsletterHelper(null, contentNode, req, resp, app);
			String r = newsletterHelper.getPreview(templateOnly);
			out.println(r);
		} else {
			out.println("Invalid action!");
		}
	}

	private String subscribe(IContentNode contentNode, String email) {
		try {
			String newsletterName = contentNode.getName();
			NewsletterSystem newsletterSystem = NewsletterHelper.getNewsletterSystem(bundleContext);
			Newsletter newsletter = newsletterSystem.getNewsletter(newsletterName, false);
			newsletter.subscribe(email, null);
			return "Success";
		} catch (NewsletterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Oops, an error occured, " + e.getMessage();
		}
	}

	private String unsubscribe(IContentNode contentNode, String email, String code) {
		try {
			String newsletterName = contentNode.getName();
			NewsletterSystem newsletterSystem = NewsletterHelper.getNewsletterSystem(bundleContext);
			Newsletter newsletter = newsletterSystem.getNewsletter(newsletterName, false);
			newsletter.unsubscribe(email, code);
			return "Success";
		} catch (NewsletterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Oops, an error occured, " + e.getMessage();
		}
	}

}
