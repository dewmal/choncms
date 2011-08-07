package org.chon.cms.core;


import java.io.File;
import java.net.URL;

import org.json.JSONException;
import org.osgi.framework.BundleContext;

public abstract class ResTplConfiguredActivator extends JCRAppConfgEnabledActivator {
	protected abstract void registerExtensions(JCRApplication app);
	
	@Override
	protected void onAppAdded(BundleContext context, JCRApplication app) {
		super.onAppAdded(context, app);
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
			
			File resourcesDir = new File(getConfig().getString("path"));
			if(resourcesDir.exists() && resourcesDir.isDirectory() &&
					new File(resourcesDir, "res").exists() &&
					new File(resourcesDir, "tpl").exists()) {				
				ResourceHelper.addResources(app, resourcesDir, getTplLibs());
			} else {
				URL resURL = context.getBundle().getResource("/res");
				URL tplURL = context.getBundle().getResource("/tpl");
				if(resURL == null || tplURL==null) {
					throw new RuntimeException("Invalid configuration for ResTplBundle!" +
							" It must contain res and tpl folders inside or configured outside through $resources variable in config.");
				}
				ResourceHelper.addResources(app, resURL, tplURL, getTplLibs());
			}
			registerExtensions(app);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onAppRemoved(BundleContext context) {
		super.onAppRemoved(context);
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
