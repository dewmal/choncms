package com.choncms.felix.bridge;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.osgi.framework.Constants;

import com.choncms.config.ConfigReader;
import com.choncms.config.ResourceLoader;

/*
 * Part of the Felix ServletBridge Strategy: https://felix.apache.org/site/apache-felix-http-service.html#ApacheFelixHTTPService-UsingtheServletBridge
 * 
 * Code taken from https://svn.apache.org/repos/asf/felix/trunk/http/samples/bridge/src/main/java/org/apache/felix/http/samples/bridge/FrameworkService.java
 */

public final class FrameworkService {
	private final ServletContext context;
	private Felix felix;

	public FrameworkService(ServletContext context) {
		this.context = context;
	}

	public void start() {
		try {
			doStart();
		} catch (Exception e) {
			log("Failed to start framework", e);
		}
	}

	public void stop() {
		try {
			doStop();
		} catch (Exception e) {
			log("Error stopping framework", e);
		}
	}

	private void doStart() throws Exception {
		this.felix = new Felix(createConfig());
		felix.start();
		printInfo();
		log("OSGi framework started", null);
	}
 
	private void printInfo() {
		System.out
				.println("--------------------------------------------------------------------------------");
		System.out
				.println("---------------   Starting Chon CMS Application   ----------------------------------");
		System.out.println(" - Using work-dir ........ "
				+ System.getProperty("app.work.dir"));
		System.out.println(" - Plugins dir ........... "
				+ System.getProperty("chon.plugins.dir"));
		System.out.println(" - Reposotiry dir ........ "
				+ System.getProperty("repo.dir"));
		System.out.println(" - Site URL .............. "
				+ System.getProperty("siteUrl")); 
		System.out
				.println("--------------------------------------------------------------------------------");
		System.out.println("BundleContext=" + felix.getBundleContext());
		
	}

	private void doStop() throws Exception {
		if (this.felix != null) {
			this.felix.stop();
		}

		log("OSGi framework stopped", null);
	}

	private Map<String, Object> createConfig() throws Exception {
		Properties props = new Properties();
		// TODO: we actually do not need an own resource loader here: because we
		// place it anyway in WEB_INF dir
		InputStream is = ResourceLoader.loadResource("framework.properties",
				null, this.context);
		props.load(is);

		HashMap<String, Object> map = new HashMap<String, Object>();
		for (Object key : props.keySet()) {
			map.put(key.toString(), props.get(key));
		}

		map.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP,
				Arrays.asList(new ProvisionActivator(this.context)));

		/**
		 * TODO: check integrity of configuration make sure we have valid
		 * plugins dir, repository dir and resources... On initial application
		 * run (if application plugins are packaged inside war) make sure we can
		 * create chon-work-dir and unpack plugins. Repository and config should
		 * also be auto-created
		 */
		// read system.properties file
		ConfigReader.readSystemProperties(this.context);

		// set felix-cache to temp dir if felx-cache dir not found
		String felixCacheDirPath = System.getProperty("felix-cache",
				System.getProperty("java.io.tmpdir") + "/felix-cache");
		File felixCacheDir = new File(felixCacheDirPath);
		// we do not need to have non-proofed code for creating it, Felix does
		// it, see
		// https://felix.apache.org/site/apache-felix-framework-bundle-cache.html
		// felixCacheDir.mkdirs();
		felixCacheDirPath = felixCacheDir.getAbsolutePath();

		log("Using felix-cache dir: " + felixCacheDirPath, null);
		map.put(Constants.FRAMEWORK_STORAGE, felixCacheDirPath);

		return map;
	}

	private void log(String message, Throwable cause) {
		this.context.log(message, cause);
	}
}
