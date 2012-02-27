package org.choncms.console;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected void registerExtensions(JCRApplication app) {
		app.regExtension(getName(), new ConsoleExtension(getName(), app));
	}

	@Override
	protected String getName() {
		return "org.choncms.console";
	}
	
}
