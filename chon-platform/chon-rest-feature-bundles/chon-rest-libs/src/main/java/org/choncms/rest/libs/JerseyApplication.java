package org.choncms.rest.libs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.osgi.framework.BundleContext;

public class JerseyApplication extends Application {
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

	public void regSingleton(Object svc) {
		singletons.add(svc);
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
		singletons.remove(service);
	}

	public void regServiceProvider(RestServiceProvider sp) {
		serviceProviders.add(sp);
	}

	public void unregServiceProvider(RestServiceProvider sp) {
		serviceProviders.remove(sp);
	}
}
