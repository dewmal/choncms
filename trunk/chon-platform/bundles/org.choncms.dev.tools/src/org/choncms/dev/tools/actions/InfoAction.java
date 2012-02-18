package org.choncms.dev.tools.actions;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.core.JCRApplication;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class InfoAction implements Action {
	private final String actionPrefix;
	private final BundleContext bundleContext;

	public InfoAction(String actionPrefix, BundleContext bundleContext) {
		this.actionPrefix = actionPrefix;
		this.bundleContext = bundleContext;
	}

	@Override
	public String run(Application application, Request req, Response resp) {
		JCRApplication app = (JCRApplication) application;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ext", app.getExts());
		params.put("utils", Utils.getInstance());
		Bundle[] bundles = bundleContext.getBundles();
		params.put("bundles", bundles);
		return resp.formatTemplate(actionPrefix + "/info.html", params);
	}
}