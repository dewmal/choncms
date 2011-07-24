package org.chon.cms.core;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.json.JSONException;
import org.osgi.framework.BundleContext;

public abstract class ResTplConfiguredActivator extends JCRAppConfgEnabledActivator {
	protected abstract void registerExtensions(JCRApplication app);
	
	@Override
	protected void onAppAdded(BundleContext context, JCRApplication app) {
		try {
			
			/**
			 * TODO: Load resources for different types of setup
			 * 
			 *  1. Developement:
			 *  	- Load resources directly from source bundles
			 *  
			 *  2. Initial Deployment
			 *  	- Copy Resources to work dir
			 *  
			 *  3. Production
			 *  	- Check if resources are in work dir, if not copy as step 2
			 *  	* Problem: what if we want to update... 
			 *  
			 */
			/*
			Enumeration<URL> urls = context.getBundle().findEntries("__config", null, true);
			if(urls != null) {
				while(urls.hasMoreElements()) {
					System.out.println(" __config /  " + urls.nextElement());
				}
			}
			*/
			
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
