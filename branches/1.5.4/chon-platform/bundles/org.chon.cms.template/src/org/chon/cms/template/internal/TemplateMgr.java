package org.chon.cms.template.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResourceHelper;
import org.chon.common.configuration.ConfigurationFactory;
import org.json.JSONException;
import org.json.JSONObject;


public class TemplateMgr {
	private static Log log = LogFactory.getLog(TemplateMgr.class);
	
	private static File templateDir;
	
	private static String defaultTemplate;

	private static JCRApplication app;
	
	private static File currentTempleDir = null;
	private static String currentTemplate = null;

	private static JSONObject config = null;
	
	static {
		try {
			JSONObject cfg = ConfigurationFactory.getConfigSonInstance().getConfig("org.chon.cms.template");
			config = cfg;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static File getTemplatesDir() {
		if(templateDir==null) {
			try {
				String path = config.getString("path") + "/templates/";
				templateDir = new File(path);
				if(!templateDir.exists()) {
					log.error("Cant find template dir: " + path);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return templateDir;
	}

	public static String getDefaultTemplate() {
		if (defaultTemplate == null) {
			defaultTemplate = config.optString("defaultTemplate", "default");
			log.info("Default template: " + defaultTemplate);
		}
		return defaultTemplate;
	}

	public static void init(JCRApplication app) {
		TemplateMgr.app = app;
		//set default template
		setTemplate(null);
	}
	
	public static void setTemplate(String template) {
		if (template == null) {
			template = getDefaultTemplate();
		}
		
		try {
			log.info("Setting template: " + template);
			File dir = new File(getTemplatesDir(), template);
			if(log.isDebugEnabled()) {
				log.debug("Setting template dir: " + dir);
			}
			if(!dir.exists()) {
				log.error("Template dir not found");
			}
			
			if(currentTempleDir != null) {
				//remove previous
				app.removeVTemplateRoot(new File(currentTempleDir, "tpl").toURI().toURL());
				app.removeStaticResourceRoot(new File(currentTempleDir, "res").toURI().toURL());
			}
			currentTempleDir = dir;
			currentTemplate = template;
			ResourceHelper.addResources(app, currentTempleDir, null);
			
		} catch (MalformedURLException e) {
			log.error("Invalid url: ", e);
		}
	}
	
	public static String getTemplate() {
		return currentTemplate;
	}
	
	public static List<String> getAllTemplates() {
		List<String> templates = new ArrayList<String>();
		File[] files = getTemplatesDir().listFiles();
		for(File dir : files) {
			if(dir.isDirectory() && isValidTplDir(dir)) {
				templates.add(dir.getName());
			}
		}
		return templates;
	}
	
	private static boolean isValidTplDir(File dir) {
		File tpl = new File(dir, "tpl");
		File res = new File(dir, "res");
		if(!tpl.exists()) return false;
		if(!res.exists()) return false;
		if(!new File(tpl, "base.html").exists()) return false;
		return true;
	}

	public static void main(String[] args) {
		System.setProperty("template.bundle.dir","E:/JPROG/Work/Projects/Bundlo/ews/org.chon.core.bundlo.web.jcr.simple.cms.template");
		System.out.println(TemplateMgr.getAllTemplates());
	}
}