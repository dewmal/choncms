package org.choncms.display.lists;

import org.chon.cms.content.utils.ChonTypeUtils;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.model.ContentModel;

public class Activator extends ResTplConfiguredActivator {
	@Override
	protected void registerExtensions(JCRApplication app) {
		ContentModel contentModel = app.createContentModelInstance(getName());
		try {
			//create type node
			ChonTypeUtils.registerType(
					contentModel, 
					DisplayListNode.DISPLAY_LIST_TYPE, 
					DisplayListNode.class, null, null, null);
			contentModel.getSession().save();
			
			//register node type class in osgi context
			ChonTypeUtils.registerContnetNodeClass(getBundleContext(), DisplayListNode.class);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// $ext.list
		app.regExtension(DisplayListsExtension.EXTENSION_NAME, new DisplayListsExtension(app));
	}

	@Override
	protected String getName() {
		return "org.choncms.display.lists";
	}
	
}
