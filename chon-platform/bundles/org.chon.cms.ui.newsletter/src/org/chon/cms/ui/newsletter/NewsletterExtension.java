package org.chon.cms.ui.newsletter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.chon.cms.admin.utils.RepoJSONService;
import org.chon.cms.core.Extension;
import org.chon.cms.core.Repo;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.services.newsletter.Newsletter;
import org.chon.cms.services.newsletter.NewsletterException;
import org.chon.cms.services.newsletter.NewsletterSystem;
import org.chon.jcr.client.service.RepoService;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
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
	
	private Newsletter getNewsletter(String name) throws NewsletterException {
		NewsletterSystem newsletterSystem = NewsletterHelper.getNewsletterSystem(bundleContext);
		return newsletterSystem.getNewsletter(name);
	}
	
	private List<Newsletter> getConfiguredNewsletterList() throws NewsletterException, JSONException {
		List<Newsletter> rv = new ArrayList<Newsletter>();
		JSONArray dashboardNewsletters = config.optJSONArray("dashboardNewsletters");
		if(dashboardNewsletters != null && dashboardNewsletters.length() > 0) {
			NewsletterSystem newsletterSystem = NewsletterHelper.getNewsletterSystem(bundleContext);
			for(int i=0; i<dashboardNewsletters.length(); i++) {
				String newsletterName = dashboardNewsletters.getString(i);
				Newsletter newsletter = newsletterSystem.getNewsletter(newsletterName);
				rv.add(newsletter);
			}
		}
		return rv;
	}
	
	@Override
	public Map<String, Action> getAdminActons() {
		Map<String, Action> actions = new HashMap<String, Action>();
		actions.put(prefix + ".dashboard", new Action() {
			@Override
			public String run(Application app, Request req, Response resp) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("prefix", prefix);
				try {
					params.put("newsletters", getConfiguredNewsletterList());
				} catch (NewsletterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return resp.formatTemplate(prefix + "/admin/dashboard.html", params);
			}
		});
		actions.put(prefix + ".newsletter", new Action() {
			@Override
			public String run(Application app, Request req, Response resp) {
				ContentModel contentModel = (ContentModel) req.attr(ContentModel.KEY);
				
				//newsletter
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("prefix", prefix);
				Newsletter newsletter;
				try {
					String newsletterName = req.get("name");
					Node newsletterPublicNode = contentModel.getPublicNode().getNode().getNode(NEWESLETTER_PUBLIC_CONTAINER_NAME);
					Node pubNewsletter = getNodeCreateIfNotExists(newsletterPublicNode, newsletterName);
					IContentNode pubNewsletterContetNode = contentModel.getContentNode(pubNewsletter);
					params.put("this", pubNewsletterContetNode);
					String newsletterHtmlTemplateText = pubNewsletterContetNode.prop("newsletterHtmlTemplateText");
					if(newsletterHtmlTemplateText == null) {
						newsletterHtmlTemplateText = "Newsletter HTML Tempalte ...";
					}
					params.put("htmlEscaped_newsletterHtmlTemplateText", XML.escape(newsletterHtmlTemplateText));
					
					String newsletterText = pubNewsletterContetNode.prop("newsletterText");
					if(newsletterText == null) {
						newsletterText = "";
					}
					params.put("htmlEscaped_newsletterText", XML.escape(newsletterText));
					newsletter = getNewsletter(newsletterName);
					params.put("newsletter", newsletter);
					
					putNodeJson(params, pubNewsletter);
					return resp.formatTemplate(prefix + "/admin/newsletter.html", params);
				} catch (Exception e) {
					e.printStackTrace();
					return "Oops, an error occured. " + e.getMessage();
				}
			}
		});
		return actions;
	}

	protected void putNodeJson(Map<String, Object> params, Node node) {
		RepoService service = Repo.getRepoService();
		try {
			RepoJSONService jsonService = new RepoJSONService(service, node.getSession());
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

	protected Node getNodeCreateIfNotExists(Node newsletterPublicNode,
			String newsletterName) throws RepositoryException {
		if(newsletterPublicNode.hasNode(newsletterName)) {
			return newsletterPublicNode.getNode(newsletterName);
		} else {
			Node node = newsletterPublicNode.addNode(newsletterName);
			node.setProperty("type", "newsletter.public");
			node.getSession().save();
			return node;
		}
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		Map<String, Action> ajaxActions = new HashMap<String, Action>();
		ajaxActions.put(prefix + ".subscriberList", new Action() {
			
			@Override
			public String run(Application app, Request req, Response resp) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("prefix", prefix);
				Newsletter newsletter;
				try {
					newsletter = getNewsletter(req.get("name"));
					params.put("this", newsletter);
					return resp.formatTemplate(prefix + "/admin/subscriberList.html", params);
				} catch (NewsletterException e) {
					e.printStackTrace();
					return "Oops, an error occured. " + e.getMessage();
				}
			}
		});
		ajaxActions.put(prefix + ".saveNewsletterTemplate", new Action() {
			
			@Override
			public String run(Application app, Request req, Response resp) {
				ContentModel contentModel = (ContentModel) req.attr(ContentModel.KEY);
				String newsletterName = req.get("name");
				try {
					Node node = contentModel.getPublicNode().getChild(NEWESLETTER_PUBLIC_CONTAINER_NAME).getChild(newsletterName).getNode();
					node.setProperty("newsletterHtmlTemplateText", req.get("newsletterHtmlTemplateText"));
					node.getSession().save();
				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Oops, an error occured. " + e.getMessage();
				}
				return "OK";
			}
		});
		ajaxActions.put(prefix + ".saveNewsletterText", new Action() {
			
			@Override
			public String run(Application app, Request req, Response resp) {
				ContentModel contentModel = (ContentModel) req.attr(ContentModel.KEY);
				String newsletterName = req.get("name");
				try {
					Node node = contentModel.getPublicNode().getChild(NEWESLETTER_PUBLIC_CONTAINER_NAME).getChild(newsletterName).getNode();
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
		});
		ajaxActions.put(prefix + ".newsletterSendExample", new Action() {

			@Override
			public String run(Application app, Request req, Response resp) {
				ContentModel contentModel = (ContentModel) req
						.attr(ContentModel.KEY);
				String newsletterName = req.get("name");
				String email = req.get("email");
				try {
					Newsletter newsletter = getNewsletter(newsletterName);
					IContentNode node = contentModel.getPublicNode()
							.getChild(NEWESLETTER_PUBLIC_CONTAINER_NAME)
							.getChild(newsletterName);
					NewsletterHelper newsletterHelper = new NewsletterHelper(
							newsletter, node, req, resp, app);
					newsletterHelper.send(email);
				} catch (NewsletterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Oops, an error occured. " + e.getMessage();
				}
				return "OK";
			}
		});
		return ajaxActions;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
