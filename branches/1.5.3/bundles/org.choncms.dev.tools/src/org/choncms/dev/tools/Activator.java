package org.choncms.dev.tools;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected void registerExtensions(JCRApplication app) {
		app.regExtension(getName(), new DevToolsExtenstion(app, getName(),
				getConfig(), getBundleContext()));
	}

	@Override
	protected String getName() {
		return "org.choncms.dev.tools";
	}	
}
