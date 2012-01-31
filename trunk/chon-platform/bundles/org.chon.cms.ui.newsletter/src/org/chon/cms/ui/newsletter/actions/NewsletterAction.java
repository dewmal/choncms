package org.chon.cms.ui.newsletter.actions;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.chon.cms.admin.utils.RepoJSONService;
import org.chon.cms.core.Repo;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.services.newsletter.Newsletter;
import org.chon.cms.ui.newsletter.NewsletterExtension;
import org.chon.jcr.client.service.RepoService;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class NewsletterAction extends AbstractNewsletterAction {

	public NewsletterAction(NewsletterExtension newsletterExtension) {
		super(newsletterExtension);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel contentModel = (ContentModel) req.attr(ContentModel.KEY);

		// newsletter
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prefix", getPrefix());
		Newsletter newsletter;
		try {
			String newsletterName = req.get("name");
			Node newsletterPublicNode = contentModel
					.getPublicNode()
					.getNode()
					.getNode(
							NewsletterExtension.NEWESLETTER_PUBLIC_CONTAINER_NAME);
			Node pubNewsletter = getNodeCreateIfNotExists(newsletterPublicNode,
					newsletterName);
			IContentNode pubNewsletterContetNode = contentModel
					.getContentNode(pubNewsletter);
			params.put("this", pubNewsletterContetNode);
			String newsletterHtmlTemplateText = pubNewsletterContetNode
					.prop("newsletterHtmlTemplateText");
			if (newsletterHtmlTemplateText == null) {
				newsletterHtmlTemplateText = "Newsletter HTML Tempalte ...";
			}
			params.put("htmlEscaped_newsletterHtmlTemplateText",
					XML.escape(newsletterHtmlTemplateText));

			String newsletterText = pubNewsletterContetNode
					.prop("newsletterText");
			if (newsletterText == null) {
				newsletterText = "";
			}
			params.put("htmlEscaped_newsletterText", XML.escape(newsletterText));
			newsletter = getNewsletter(newsletterName, true);
			params.put("newsletter", newsletter);

			putNodeJson(params, pubNewsletter);

			params.put(
					"status",
					newsletter.getStatus() == Newsletter.STATUS_SENDING ? "SENDING"
							: "");
			return resp.formatTemplate(getPrefix()
					+ "/admin/newsletter.html", params);
		} catch (Exception e) {
			e.printStackTrace();
			return "Oops, an error occured. " + e.getMessage();
		}
	}

	private Node getNodeCreateIfNotExists(Node newsletterPublicNode,
			String newsletterName) throws RepositoryException {
		if (newsletterPublicNode.hasNode(newsletterName)) {
			return newsletterPublicNode.getNode(newsletterName);
		} else {
			Node node = newsletterPublicNode.addNode(newsletterName);
			node.setProperty("type", "newsletter.public");
			node.getSession().save();
			return node;
		}
	}

	private void putNodeJson(Map<String, Object> params, Node node) {
		RepoService service = Repo.getRepoService();
		try {
			RepoJSONService jsonService = new RepoJSONService(service,
					node.getSession());
			JSONObject svcReq = new JSONObject("{sessionId: 'wtf', id: '"
					+ node.getIdentifier()
					+ "', depth: 1, includeAttributes: true }");
			// TODO: check error
			JSONObject svcResp = jsonService.getNode(svcReq);
			params.put("nodeJSON", svcResp.getJSONObject("node").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}