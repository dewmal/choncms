package org.chon.cms.core;


import java.io.File;
import java.io.FileNotFoundException;

import org.chon.common.configuration.ConfigSon;
import org.chon.common.configuration.ConfigurationFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;

public abstract class ResTplConfiguredActivator extends JCRApplicationActivator {
	private BundleContext context;
	private JCRApplication app;
	
	protected abstract String getName();
	protected abstract void registerExtensions(JCRApplication app);
	
	
	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;
		super.start(context);
	}
	
	public BundleContext getBundleContext() {
		return context; 
	}
	public JCRApplication getJCRApp() {
		return app;
	}
	
	public ConfigSon getConfigSonService() {
		return ConfigurationFactory.getConfigSonInstance();
	}
	public JSONObject getConfig() {
		try {
			return getConfigSonService().getConfig(getName());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
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
