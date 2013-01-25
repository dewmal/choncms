package org.chon.core.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class OsgiServiceTracker<T> {
	
	public static interface Tracker<T> {
		public void onAdd(T service);
		public void onRemove(T service);
	}
	
	public static <T> ServiceTracker track(Class<T> serviceClass, final Tracker<T> t) {
		
		BundleContext context = null;
		ServiceTracker tracker = new ServiceTracker(context, serviceClass.getName(),
				null) {

			@Override
			public Object addingService(ServiceReference reference) {
				Object service = super.addingService(reference);
				t.onAdd((T)service);
				return service;
			}
			@Override
			public void removedService(ServiceReference reference,
					Object service) {
				t.onRemove((T)service);
				super.removedService(reference, service);
			}
		};
		tracker.open();
		return tracker;
	}
}
