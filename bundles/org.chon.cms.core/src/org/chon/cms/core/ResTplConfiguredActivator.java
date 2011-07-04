package org.chon.cms.core;


import java.io.File;

import org.json.JSONException;
import org.osgi.framework.BundleContext;

public abstract class ResTplConfiguredActivator extends JCRAppConfgEnabledActivator {
	protected abstract void registerExtensions(JCRApplication app);
	
	@Override
	protected void onAppAdded(BundleContext context, JCRApplication app) {
		try {
			ResourceHelper.addResources(app, new File(getConfig().getString("path")), getTplLibs());
			registerExtensions(app);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onAppRemoved(BundleContext context) {
		// TODO Auto-generated method stub
	}


	/**
	 * Override this if you need to register velocity macro libs
	 * 
	 * @return
	 */
	protected String [] getTplLibs() {
		return null;
	}
}
