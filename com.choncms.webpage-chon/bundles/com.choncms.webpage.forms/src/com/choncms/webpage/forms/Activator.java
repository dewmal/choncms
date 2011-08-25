package com.choncms.webpage.forms;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected void registerExtensions(JCRApplication app) {
		// TODO register extension
		app.regExtension("forms", new FormsExtension(app));
	}

	@Override
	protected String getName() {
		return "com.choncms.webpage.forms";
	}
	
}
