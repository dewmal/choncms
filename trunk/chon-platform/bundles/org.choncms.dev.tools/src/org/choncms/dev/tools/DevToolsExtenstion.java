package org.choncms.dev.tools;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.choncms.dev.tools.actions.QueryRepoAction;
import org.json.JSONObject;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class DevToolsExtenstion implements Extension {
	
	private Map<String, Action> actions = new HashMap<String, Action>();

	public DevToolsExtenstion(JCRApplication app, final String actionPrefix, JSONObject config, final BundleContext bundleContext) {
		actions.put(actionPrefix + ".queryRepo", new QueryRepoAction(actionPrefix, config));
		actions.put(actionPrefix + ".info", new Action() {

			@Override
			public String run(Application application, Request req, Response resp) {
				JCRApplication app = (JCRApplication) application;
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ext", app.getExts());
				Bundle[] bundles = bundleContext.getBundles();
				params.put("bundles", bundles);
				return resp.formatTemplate(actionPrefix + "/info.html", params);
			}
			
		});
	}

	@Override
	public Map<String, Action> getAdminActons() {
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
