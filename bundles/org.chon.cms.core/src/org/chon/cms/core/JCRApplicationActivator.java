package org.chon.cms.core;

import org.chon.web.api.Application;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;



public abstract class JCRApplicationActivator implements BundleActivator {
	
	private ServiceTracker t;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		String filter = "(&(objectclass="
			+ Application.class.getName() + ")(application="
			+ JCRApplication.class.getName() + "))"; 
		t = new ServiceTracker(context, context.createFilter(filter), null) {

			@Override
			public Object addingService(ServiceReference reference) {
				JCRApplication app = (JCRApplication)super.addingService(reference);
				onAppAdded(context, app);
				return app;
			}

			@Override
			public void removedService(ServiceReference reference,
					Object service) {
				System.gc();
				super.removedService(reference, service);
				onAppRemoved(context);
			}
		};
		t.open();
		//Initializer.redisterNodeRenderers(context);
	}
	
	protected abstract void onAppAdded(BundleContext context, JCRApplication app);
	protected abstract void onAppRemoved(BundleContext context);
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		t.close();
	}
}
