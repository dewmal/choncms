package org.chon.sourcecode.plugin;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected void registerExtensions(JCRApplication app) {
		// TODO register extension
		app.regExtension("sourcecode", new SourceCodeExtension());
	}

	@Override
	protected String getName() {
		return "org.chon.sourcecode.plugin";
	}
	
}
