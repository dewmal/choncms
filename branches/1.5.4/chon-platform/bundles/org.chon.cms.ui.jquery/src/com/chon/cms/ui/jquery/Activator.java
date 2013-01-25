package com.chon.cms.ui.jquery;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;

import com.chon.cms.ui.jquery.ext.JQueryExtenstion;


public class Activator extends ResTplConfiguredActivator {

	@Override
	protected String getName() {
		return "org.chon.cms.ui.jquery";
	}

	@Override
	protected void registerExtensions(JCRApplication app) {
		app.regExtension("jquery", new JQueryExtenstion(app, getConfig()));
	}


}
