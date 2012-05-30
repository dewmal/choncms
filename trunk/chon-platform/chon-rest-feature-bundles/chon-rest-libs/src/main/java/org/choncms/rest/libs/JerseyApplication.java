package org.choncms.rest.libs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;

public class JerseyApplication extends Application {
	
	private static final Log log = LogFactory.getLog(Activator.class);
	
	private List<RestServiceProvider> serviceProviders = null;
	private Set<Object> singletons = null;
	
	private Activator activator; //for reload
	
	public JerseyApplication(BundleContext context, Activator activator) {
		this.activator = activator;
		
		serviceProviders = new LinkedList<RestServiceProvider>();
		singletons = new HashSet<Object>();
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> rv = new HashSet<Class<?>>();
		for(RestServiceProvider sp : serviceProviders) {
			rv.addAll(sp.getServices());
		}
		return rv;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

	public void regSingleton(Object service) {
		log.debug("Registering service singleton: " + service.getClass().getName());
		singletons.add(service);
	}

	public void reload() {
		try {
			activator.reload();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void unregSingleton(Object service) {
		log.debug("Unregistering service singleton: " + service.getClass().getName());
		singletons.remove(service);
	}

	public void regServiceProvider(RestServiceProvider sp) {
		log.debug("Registering service provider: " + sp.getClass().getName());
		serviceProviders.add(sp);
	}

	public void unregServiceProvider(RestServiceProvider sp) {
		log.debug("Unregistering service provider: " + sp.getClass().getName());
		serviceProviders.remove(sp);
	}

	public boolean containsServices() {
		return serviceProviders.size()>0 || singletons.size()>0;
	}
}
