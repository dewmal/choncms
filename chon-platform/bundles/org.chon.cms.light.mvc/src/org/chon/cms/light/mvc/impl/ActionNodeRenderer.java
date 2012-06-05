package org.chon.cms.light.mvc.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.chon.cms.core.model.renderers.VTplNodeRenderer;
import org.chon.cms.light.mvc.AbstractAction;
import org.chon.cms.light.mvc.TextOutputResult;
import org.chon.cms.light.mvc.result.ActionResult;
import org.chon.cms.light.mvc.result.RedirectResult;
import org.chon.cms.light.mvc.result.Result;
import org.chon.cms.light.mvc.result.TemplateResult;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.INodeRenderer;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;


public class ActionNodeRenderer implements INodeRenderer {

	
	private LightMVCServiceImpl mvc;

	public ActionNodeRenderer(LightMVCServiceImpl mvc) {
		this.mvc = mvc;
	}

	public void render(AbstractAction action, IContentNode theThisNode, Request req, Response resp,
			Application app, ServerInfo si) throws Exception {		
		Result r = action.exec();
		if (r instanceof TemplateResult) {
			TemplateResult result = (TemplateResult) r;
			Map<String, Object> params = result.getParams();
			if(params == null) {
				params = new HashMap<String, Object>();
			}
			VTplNodeRenderer.prepareParams(theThisNode, req, resp, params, app);
			VTplNodeRenderer.render(result.getBaseTemplate(), result.getTemplate(), theThisNode, req, resp, si, params);
		} else if (r instanceof RedirectResult) {
			RedirectResult result = (RedirectResult) r;
			String redirect = result.getRedirectPath();
			String siteUrl = app.getAppProperties().getProperty("siteUrl");
			resp.setRedirect(siteUrl + (redirect.startsWith("/") ? redirect : "/" + redirect));
		} else if (r instanceof ActionResult) {
			ActionResult result = (ActionResult) r;
			action = result.getAction().newInstance();
			inject(action, "req", req);
			inject(action, "resp", resp);
			render(action, theThisNode, req, resp, app, si);
		} else if(r instanceof TextOutputResult) {
			TextOutputResult tor = (TextOutputResult) r;
			String mime = tor.getMime();
			if(mime != null) {
				resp.getServletResponse().setContentType(mime);
			}
			resp.getOut().print(tor.getTxt());
			resp.getOut().flush();
			resp.getOut().close();
		}
	}
	
	@Override
	public void render(IContentNode node, Request req, Response resp,
			Application app, ServerInfo si) {		
		try {
			AbstractAction action = mvc.createActionInstance(node.getAbsPath());
			inject(action, "req", req);
			inject(action, "resp", resp);
			//inject(action, "model", ?)
			render(action, node, req, resp, app, si);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Field getField(Class<?> clazz, String fieldName)
			throws NoSuchFieldException {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				throw e;
			} else {
				return getField(superClass, fieldName);
			}
		}
	}
	private void inject(Object obj, String prop, Object val) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field f = getField(obj.getClass(), prop);
		f.setAccessible(true);
		f.set(obj, val);
	}

}
