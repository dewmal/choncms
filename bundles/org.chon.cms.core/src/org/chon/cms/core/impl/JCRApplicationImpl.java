package org.chon.cms.core.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.Repo;
import org.chon.cms.core.auth.AuthenticationProvider;
import org.chon.cms.core.auth.User;
import org.chon.cms.core.impl.helpers.AppHelper;
import org.chon.cms.core.impl.helpers.LocalAuthenticationProvider;
import org.chon.cms.core.model.renderers.VTplNodeRenderer;
import org.chon.cms.core.setup.Setup;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.IContentNodeFactory;
import org.chon.cms.model.content.INodeRenderer;
import org.chon.cms.model.renderers.FileNodeRenderer;
import org.chon.core.velocity.VTemplate;
import org.chon.web.api.Request;
import org.chon.web.api.Resource;
import org.chon.web.api.ServerInfo;
import org.osgi.framework.BundleContext;

public class JCRApplicationImpl implements JCRApplication {
	private static Log log = LogFactory.getLog(JCRApplicationImpl.class);
	private Map<String, Extension> extensions = new HashMap<String, Extension>();
	private List<URL> vTemplateRoots = new ArrayList<URL>();
	private VTemplate tpl = null;
	
	private List<URL> staticResourcesUrls = new ArrayList<URL>();
	private AppHelper appHelper = new AppHelper();
	
	private BundleContext bundleContext;
	private Properties properties = new Properties();
	
	public JCRApplicationImpl(BundleContext bundleContext) {
		try {
			this.bundleContext = bundleContext;
			Repo.init(bundleContext);
			Session session = Repo.createSession("admin", "admin");
			new Setup().install(session);
			session.logout();
			
			//register default factory that provides www content node, file content node
			bundleContext.registerService(IContentNodeFactory.class.getName(), new ContentNodeFactory(), null);
			
			User user = new User("admin");
			ContentModelImpl cm = new ContentModelImpl(user, bundleContext);
			
			//register local auth provider that looks for user in etc/passwd
			bundleContext.registerService(AuthenticationProvider.class.getName(), new LocalAuthenticationProvider(cm), null);
		
			
			IContentNode hostCfg = cm.getConfigNode("host.config");
			properties.put("siteUrl", hostCfg.prop("siteUrl"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			registerNodeRenderer(VTplNodeRenderer.class);
			registerNodeRenderer(FileNodeRenderer.class);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initTemplate() {
		URL[] urls = vTemplateRoots.toArray(new URL[vTemplateRoots.size()]);
		String modificationInterval = System.getProperty("velocity.url.resource.loader.modificationCheckInterval", "120");
		this.tpl = new VTemplate(urls, Integer.valueOf(modificationInterval));
	}
	
	@Override
	public Properties getAppProperties() {
		return properties;
	}

	@Override
	public Resource getResource(Request req) {
		//puts ContentModel in session if not exists
		appHelper.setupContentModel(req, bundleContext);
		
		//resource locator
		Resource res = appHelper.find(req, staticResourcesUrls, bundleContext);
		return res;
	}

	@Override
	public VTemplate getTemplate() {
		return tpl;
	}

	@Override
	public void prepareRequest(Resource r, ServerInfo si) {
		
	}

	@Override
	public void handleException(Exception e, Resource r, ServerInfo si) {
		throw new RuntimeException(e);
	}

	@Override
	public void finalizeRequest(Resource r, ServerInfo si) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void regExtension(String name, Extension ext) {
		if(log.isDebugEnabled()) {
			log.debug("Register extension: " + name + " : " + ext.toString());
		}
		if(extensions.containsKey(name)) {
			log.warn("Extension with name: " + name + " already exists, overwriting!!!");
		}
		extensions.put(name, ext);
	}

	@Override
	public void unregExtension(String name) {
		if(log.isDebugEnabled()) {
			log.debug("Removing extension: " + name);
		}
		extensions.remove(name);
	}

	@Override
	public void addVTemplateRoot(URL url) {
		if(url != null) {
			vTemplateRoots.add(url);
			initTemplate();
		}
	}

	@Override
	public void removeVTemplateRoot(URL url) {
		if(url != null) {
			vTemplateRoots.remove(url);
			initTemplate();
		}
	}

	@Override
	public void addStaticResourceRoot(URL url) {
		staticResourcesUrls.add(url);
		
	}

	@Override
	public void removeStaticResourceRoot(URL url) {
		staticResourcesUrls.remove(url);
	}
	
	public Map<String, Extension> getExts() {
		return extensions;
	}
	
	public void registerNodeRenderer(Class<? extends INodeRenderer> clzz) throws InstantiationException, IllegalAccessException {
		INodeRenderer instance = clzz.newInstance();
		registerNodeRenderer(instance);
	}
	
	public void registerNodeRenderer(INodeRenderer instance) throws InstantiationException, IllegalAccessException {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("renderer", instance.getClass().getName());
		bundleContext.registerService(INodeRenderer.class.getName(), instance, props);
	}
}
