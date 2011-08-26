package com.choncms.webpage.forms;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.json.JSONException;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected void registerExtensions(JCRApplication app) {
		// TODO register extension
		try {
			String submitFormNode = getConfig().getString("submitFormNode");
			
			String appFormDataNodeName = getConfig().getString("appFormDataNode");
			ContentModel cm = app.createContentModelInstance(getName());
			IContentNode appFormDataNode = cm.getAppsConfigNode(appFormDataNodeName, true);
			if(cm.getPublicNode().getChild(submitFormNode) == null) {
				//TODO: create node that will handle form submits
			}
			app.regExtension("forms", new FormsExtension(app, getName(), appFormDataNode));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected String getName() {
		return "com.choncms.webpage.forms";
	}
	
}
