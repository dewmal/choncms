package org.chon.cms.core;

import java.util.Hashtable;

import javax.jcr.Repository;

import org.chon.cms.core.impl.JCRApplicationImpl;
import org.chon.web.api.Application;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	class RepoServiceTracker extends ServiceTracker {
		public RepoServiceTracker(BundleContext context) {
			super(context, Repository.class.getName(), null);
		}
		
		@Override
		public Object addingService(ServiceReference reference) {
			Object service = super.addingService(reference);
			JCRApplication app = new JCRApplicationImpl(context);
			Hashtable<String, String> props = new Hashtable<String, String>();
			props.put("application", JCRApplication.class.getName());
			context.registerService(Application.class.getName(), app, props);
			return service;
		}
	}
	
	RepoServiceTracker st = null;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		RepoServiceTracker st = new RepoServiceTracker(context);
		st.open();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if(st != null) {
			st.close();
		}
	}
}
