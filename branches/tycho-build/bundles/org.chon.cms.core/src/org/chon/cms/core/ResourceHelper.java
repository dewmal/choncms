package org.chon.cms.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResourceHelper {
	private static final Log log = LogFactory.getLog(ResourceHelper.class);

	// TODO: resource loader must be initialized when velocity engine is created
	// when we adding a template to app, actually new velocity engine is created
	// and reinitialized with new urls
	// old preloaded template libs will not be loaded
	// so we keep them and reload each time add template is called
	// will get errors when we are removing template
	// need to find way to syncronize this set
	// best way would be if we can add root to velocity resource loader
	private static Set<String> preLoadTemplatesSet = new HashSet<String>();

	public static void addResources(JCRApplication app, URL staticResURL, URL templateResURL, String [] preLoadTemplates) {
		System.out.println("Adding templates from: " + templateResURL);
		app.addVTemplateRoot(templateResURL);

		if (preLoadTemplates != null && preLoadTemplates.length > 0) {
			for (int i = 0; i < preLoadTemplates.length; i++) {
				preLoadTemplatesSet.add(preLoadTemplates[i]);
			}
		}
		for (String t : preLoadTemplatesSet) {
			try {
				app.getTemplate().format(t, null, null);
			} catch (Exception e) {
				log.error("Error preloading template " + t, e);
			}
		}
		System.out.println("Adding static resources from: " + staticResURL);
		app.addStaticResourceRoot(staticResURL);
	}
	
	/**
	 * Adds res and tpl dirs looking from app (system properties) property name,
	 * eg. "view.bundle.dir"
	 * 
	 * @param app
	 * @param property
	 */
	public static void addResources(JCRApplication app, File dir,
			String[] preLoadTemplates) {

		// register resources
		try {
			if (!dir.exists()) {
				// TODO: ...
				//throw new FileNotFoundException(dir + " not found. ");
				throw new RuntimeException(dir + " not found. ");
			}
			
			File tplDir = new File(dir, "tpl");
			URL templateResURL = tplDir.toURI().toURL();
			URL staticResURL = new File(dir, "res").toURI().toURL();
			addResources(app, staticResURL, templateResURL, preLoadTemplates);
		} catch (MalformedURLException e) {
			log.error("Invalid url.", e);
		}
	}

	public static void addResources(JCRApplication app, String property,
			String[] preLoadTemplates) {
		Properties props = app.getAppProperties();
		String dirName = props.getProperty(property, System
				.getProperty(property));
		File dir = new File(dirName);
		addResources(app, dir, preLoadTemplates);
	}
}
