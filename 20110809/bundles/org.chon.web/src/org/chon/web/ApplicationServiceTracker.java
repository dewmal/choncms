package org.chon.web;

import org.chon.web.api.Application;
import org.chon.web.servlet.BundloAppServlet;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;


public class ApplicationServiceTracker extends ServiceTracker {
	
	private BundloAppServlet appServlet;
	
	public ApplicationServiceTracker(BundleContext context,
			BundloAppServlet appServlet) {
		super(context, Application.class.getName(), null);
		this.appServlet = appServlet;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		Object service = super.addingService(reference);
		System.out.println("[JOCO] Registered Application: " + service);
		Application app = (Application) service;
		this.appServlet.addApplication(app);
		return service;
	}

	@Override
	public void removedService(ServiceReference reference,
			Object service) {
		this.appServlet.removeApplication((Application)service);
		super.removedService(reference, service);
	}
}
