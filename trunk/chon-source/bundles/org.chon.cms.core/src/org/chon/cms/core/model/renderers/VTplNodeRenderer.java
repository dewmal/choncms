package org.chon.cms.core.model.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.model.types.EvalVelocityHtmlContentNode;
import org.chon.cms.core.model.types.RootContentNode;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.INodeRenderer;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;

public class VTplNodeRenderer implements INodeRenderer {
	private static final Log log = LogFactory.getLog(VTplNodeRenderer.class);
	
	public static void render(String baseTpl, String tpl, IContentNode node, Request req, Response resp, ServerInfo serverInfo, Map<String, Object> params) {
		log.trace("Entering rendering node");
		
		Application app = serverInfo.getApplication();
		
		prepareParams(node, req, resp, params, app);
		params.put("ctx", resp.getTemplateContext());
		if(node instanceof EvalVelocityHtmlContentNode) {
			EvalVelocityHtmlContentNode n = (EvalVelocityHtmlContentNode)node;
			n.eval(app, params);
		} else if(node instanceof RootContentNode) {
			RootContentNode n = (RootContentNode) node;
			IContentNode redirTo = n.getHtmlNode();
			if(redirTo != null) {
				resp.setRedirect(redirTo.getPath());
				return;
			}
		}
		
		String body = app.getTemplate().format(tpl, params, resp.getTemplateContext());
		params.put("BODY", body);
		
		resp.getServletResponse().setContentType("text/html");
		app.getTemplate().format(baseTpl, params, resp.getTemplateContext(), resp.getOut());
		log.trace("Finished rendering node");
	}
	
	public static void prepareParams(IContentNode node, Request req,
			Response resp, Map<String, Object> params, Application application) {
		Properties props = application.getAppProperties();
		if(props != null) {
			Iterator<Object> it = props.keySet().iterator();
			while(it.hasNext()) {
				String key = (String) it.next();
				resp.getTemplateContext().put(key, props.get(key));
			}
		}
		String siteUrl = application.getAppProperties().getProperty("siteUrl");
		
		String path = node.getPath();
		if(path.length()>0) {
			//TODO: always folder
			path += "/";
		}
		String base = siteUrl + "/" + path;
		if(log.isDebugEnabled()) {
			log.debug("Setting template context base='" + base + "'");
		}
		Map<String, Object> tplCtx = resp.getTemplateContext();
		putIfNotExists(tplCtx, "base", base);
		putIfNotExists(tplCtx, "head:scripts", new ArrayList<String>());
		putIfNotExists(tplCtx, "head:css", new ArrayList<String>());
		
		params.put("this", node);
		params.put("req", req);
		params.put("resp", resp);
		params.put("app", application);
		
		if(application instanceof JCRApplication) {
			if(tplCtx.containsKey("extenstions")) {
				params.put("ext", tplCtx.get("extenstions"));
			} else {
				//only one instace per request
				JCRApplication app = (JCRApplication)application;
				ExtObj extenstions = new ExtObj(app.getExts(), req, resp, node);
				params.put("ext", extenstions);
				tplCtx.put("extenstions", extenstions);
			}
		}
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		params.put("user", cm.getUser());
		params.put("cm", cm);
	}
	
	private static void putIfNotExists(Map<String, Object> tplCtx,
			String key, Object value) {
		if(!tplCtx.containsKey(key)) {
			tplCtx.put(key, value);
		}
	}


	@Override
	public void render(IContentNode contentNode, Request req, Response resp,
			Application _app, ServerInfo serverInfo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String tpl = contentNode.prop("template");
		if(tpl == null) {
			//if node does not have property template
			//ask overriding class for default template
			tpl = getTemplate();
		}
		String baseTemplate = contentNode.prop("baseTemplate");
		if(baseTemplate == null) {
			baseTemplate = getBaseTemplate();
		}
		preRender(params, contentNode, req, resp, _app, serverInfo);
		VTplNodeRenderer.render(baseTemplate, tpl, contentNode, req, resp, serverInfo, params);
	}
	
	/**
	 * Override this, put what you need in params, except reserved this, req, resp variables
	 * 
	 */
	protected void preRender(Map<String, Object> params, IContentNode contentNode, Request req,
			Response resp, Application _app, ServerInfo serverInfo) {
		
	}

	protected String getTemplate() {
		return "pages/index.html";
	}
	
	protected String getBaseTemplate() {
		return "base.html";
	}
}
