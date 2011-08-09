package org.chon.libs.spring;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class BundleApplicationContext extends AbstractXmlApplicationContext {
	
	private Resource[] resources;
	
	public BundleApplicationContext(BundleActivator activator, BundleContext ctx, String [] ctxLocations) {
		if(ctxLocations==null) {
			ctxLocations = new String[] {"applicationContext.xml"};
		}
		
		resources = new Resource[ctxLocations.length];
		for(int i=0; i<ctxLocations.length; i++) {
			String res = ctxLocations[i];
			resources[i] = new UrlResource(ctx.getBundle().getResource(res));
		}
		this.setClassLoader(activator.getClass().getClassLoader());
		refresh();
	}

	@Override
	protected Resource[] getConfigResources() {
		return resources;
	}
}
