package org.chon.cms.ui.lightbox;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.ui.lightbox.ext.LightboxExtension;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected String getName() {
		return "org.chon.cms.ui.lightbox";
	}

	@Override
	protected void registerExtensions(JCRApplication app) {
		app.regExtension("lightbox", new LightboxExtension(app));
	}


}
