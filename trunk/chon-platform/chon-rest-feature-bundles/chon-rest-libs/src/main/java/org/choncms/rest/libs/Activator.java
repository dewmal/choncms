package org.choncms.rest.libs;

import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.ws.rs.Path;
import javax.ws.rs.ext.RuntimeDelegate;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Activator implements BundleActivator {
	//	String serveltRoot = System.getProperty("chon.rest.root", ALIAS);
	
	private static final String ALIAS = "/services";
	
	private ServletContainer servletContainer = null;
	private ServiceRegistration servletContainerRegRef = null;
	
	private JerseyAnnotatedServiceTracker serviceTracker = null;
	private BundleContext bundleContext;
	
	@Override
	public void start(BundleContext context) throws Exception {
		this.bundleContext = context;
		
		RuntimeDelegate.setInstance(new com.sun.jersey.server.impl.provider.RuntimeDelegateImpl());
		JerseyApplication app = new JerseyApplication(context, this);
		servletContainer = new ServletContainer(app);
		
		
		serviceTracker = new  JerseyAnnotatedServiceTracker(app, context);
		serviceTracker.open();
		
		ServiceReference[] refs = serviceTracker.getServiceReferences();
		if(refs != null) {
			for(ServiceReference ref : refs) {
				Object service = context.getService(ref);
				if(service.getClass().isAnnotationPresent(Path.class)) {
					app.regSingleton(service);
					app.reload();
				} else if(service instanceof RestServiceProvider) {
					app.regServiceProvider((RestServiceProvider)service);
					app.reload();
				}
			}
		}
		
		//context.registerService(StatusResource.class.getName(), StatusResource.class, null);
		this.reload();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if(serviceTracker != null) {
			serviceTracker.close();
		}
		if(servletContainerRegRef != null) {
			servletContainerRegRef.unregister();
		}
	}

	public synchronized void reload() throws Exception {
		if(servletContainer == null) {
			throw new Exception("Unitialised servletContainer");
		}
		
		if(servletContainerRegRef != null) {
			servletContainerRegRef.unregister();
			servletContainerRegRef = null;
		}
		
		String serveltRoot = System.getProperty("chon.rest.root", ALIAS);
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("alias", serveltRoot);
		System.out.println("Reloading jersey servler chon.rest.root=" + serveltRoot);
		servletContainerRegRef = bundleContext.registerService(Servlet.class.getName(), servletContainer, props);
	}
}
