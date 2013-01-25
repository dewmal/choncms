package org.chon.cms.ui.fragments;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected void registerExtensions(JCRApplication app) {
		ContentModel contentModel = app.createContentModelInstance(getName());
		IContentNode node = contentModel.getAppsConfigNode("fragments", true);
		app.regExtension("fragments", new FragmentsExtenstion(app, getName(), node, getConfig()));
	}

	@Override
	protected String getName() {
		return "org.chon.cms.ui.fragments";
	}
	
}
