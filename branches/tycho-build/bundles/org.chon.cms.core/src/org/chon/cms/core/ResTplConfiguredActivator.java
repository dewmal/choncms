package org.chon.cms.core;


import java.io.File;
import java.net.URL;

import org.json.JSONException;
import org.osgi.framework.BundleContext;

/**
 * Base Activator for chon ui bundles (ResTpl Bundles)
 * 
 * chon.ui bundle structure:
 * 		chon.ui.example.plugin
 * 			__config/
 * 				chon.ui.example.plugin.json - default json configuration. This can be accessed through method getConfig()
 * 
 * 			res/*
 * 				- static resources (css, js etc...)
 * 
 * 			tpl/*
 * 				- velocity templates
 * 
 * 
 * @author Jovica.Veljanovski
 * 
 */
public abstract class ResTplConfiguredActivator extends JCRAppConfgEnabledActivator {
	protected abstract void registerExtensions(JCRApplication app);
	
	@Override
	protected void onAppAdded(BundleContext context, JCRApplication app) {
		super.onAppAdded(context, app);
		System.out.println(" -------------------------------------------------------------------------------- ");
		System.out.println("		Initializing ResTpl bundle: " + getName());
		System.out.println(" -------------------------------------------------------------------------------- ");
		try {
			File resourcesDir = new File(getConfig().getString("path"));
			if(resourcesDir.exists() && resourcesDir.isDirectory() &&
					new File(resourcesDir, "res").exists() &&
					new File(resourcesDir, "tpl").exists()) {				
				ResourceHelper.addResources(app, resourcesDir, getTplLibs());
			} else {
				URL resURL = context.getBundle().getResource("/res/");
				URL tplURL = context.getBundle().getResource("/tpl/");
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
		System.out.println(" -------------------------------------------------------------------------------- ");
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
