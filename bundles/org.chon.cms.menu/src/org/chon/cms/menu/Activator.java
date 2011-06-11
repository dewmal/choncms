package org.chon.cms.menu;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.menu.model.ext.MenuExtenstion;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected String getName() {
		return "org.chon.cms.menu";
	}

	@Override
	protected void registerExtensions(JCRApplication app) {
		app.regExtension("menu", new MenuExtenstion());
	}

}
