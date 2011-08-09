package org.chon.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class RegUtils {

	private static Map<BundleContext, List<ServiceRegistration>> srMap = new HashMap<BundleContext, List<ServiceRegistration>>();
	
	public static void reg(BundleContext context, String name,
			Object service, Hashtable<String, String> props) {
		if(!srMap.containsKey(context)) {
			srMap.put(context, new ArrayList<ServiceRegistration>());
		}
		
		List<ServiceRegistration> regList = srMap.get(context);
		System.out.println("[JOCO app] Registering service: " + service);
		ServiceRegistration reg = context.registerService(name, service, props);
		regList.add(reg);
	}

	public static void unregAll(BundleContext context) {
		List<ServiceRegistration> regList = srMap.get(context);
		for(ServiceRegistration sr : regList) {
			sr.unregister();
		}
	}
}