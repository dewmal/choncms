package org.choncms.rest.libs;

import javax.ws.rs.Path;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class JerseyAnnotatedServiceTracker extends ServiceTracker {
	private JerseyApplication app;
	public JerseyAnnotatedServiceTracker(JerseyApplication app, BundleContext ctx) throws InvalidSyntaxException {
		super(ctx,  ctx.createFilter("(objectClass=*)"), null);
		this.app = app;
	}
	
	public JerseyAnnotatedServiceTracker(BundleContext context,
			Filter filter, ServiceTrackerCustomizer customizer) {
		super(context, filter, customizer);
	}

	@Override
	public Object addingService(ServiceReference reference) {
		Object svc = super.addingService(reference);
		if(svc == null)  return null;
		if(svc.getClass().isAnnotationPresent(Path.class)) {
			app.regSingleton(svc);
			app.reload();
		} else if(svc instanceof RestServiceProvider) {
			app.regServiceProvider((RestServiceProvider)svc);
			app.reload();
		}
		return svc;
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
		if(service.getClass().isAnnotationPresent(Path.class)) {
			app.unregSingleton(service);
			app.regSingleton(service);
			app.reload();
		} else if(service instanceof RestServiceProvider) {
			app.unregServiceProvider((RestServiceProvider)service);
			app.reload();
		}
		super.modifiedService(reference, service);
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		if(service.getClass().isAnnotationPresent(Path.class)) {
			app.unregSingleton(service);
			app.reload();
		} else if(service instanceof RestServiceProvider) {
			app.unregServiceProvider((RestServiceProvider)service);
			app.reload();
		}
		super.removedService(reference, service);
	}
}