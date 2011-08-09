package org.chon.web;

import java.util.Hashtable;

import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.chon.web.servlet.BundloAppServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;


public class Activator implements BundleActivator {

	private ServiceTracker applicationTracker;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		BundloAppServlet appServlet = new BundloAppServlet();
		registerServlet(context, "/", appServlet);
		applicationTracker = new ApplicationServiceTracker(context, appServlet);
		applicationTracker.open();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		applicationTracker.close();
	}

	/**
	 * TODO: Whiteboad can accept:
	 * 
	 *		alias - Servlet alias to register with.
	 * 		contextId - Id of context to register with.
	 * 		init.* - Servlet initialization values.
	 * 
	 * @param context
	 * @param alias
	 * @param servlet
	 */
	private void registerServlet(BundleContext context, String alias, Servlet servlet) {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("alias", alias);
		RegUtils.reg(context, Servlet.class.getName(), servlet, props);
	}
	
	/**
	 * TODO: Whiteboad can accept
	 * 
	 * 		pattern - Regular expression pattern to register filter with.
	 * 		contextId - Id of context to register with.
	 * 		service.ranking - Where in the chain this filter should be placed.
	 * 		init.* - Filter initialization values.
	 * 
	 * @param context
	 * @param alias
	 * @param filter
	 */
	private void registerFilter(BundleContext context, String pattern, Filter filter) {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("pattern", pattern);
		RegUtils.reg(context, Filter.class.getName(), filter, props);
	}
}
