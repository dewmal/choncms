package org.chon.cms.core.impl.helpers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.chon.cms.core.auth.User;
import org.chon.cms.core.impl.ContentModelImpl;
import org.chon.cms.core.impl.ContentResource;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Resource;
import org.chon.web.api.res.ActionResource;
import org.chon.web.api.res.StaticResource;
import org.chon.web.mpac.ModulePackage;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class AppHelper {
	public Resource find(Request req, List<URL> staticResourcesUrls, BundleContext bundleContext) {
		//TODO: measure performance, this is very intensive
		//if slow implement some king of caching mechanism
		
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		String path = req.getPath();
		if("/".equals(path) || path.length()==0) {
			return createJCRResource(cm.getPublicNode(), bundleContext);
		}
		
		URL res = getStaticResUrl(path, staticResourcesUrls);
		boolean isDir = false;
		if(res != null) {
			if(res.getProtocol().equals("file")) {
				try {
					isDir = new File(res.toURI()).isDirectory();
				} catch (URISyntaxException e) {}
			}
			if(!isDir) {
				//DO NOT RETURN DIR CONTENTS
				//TODO: Enable this for debug
				return StaticResource.create(res);
			}
		}
		
//		//TODO: this shouldnt be hardcoded
//		if(path.startsWith("admin")) {
//			String ext = req.getExtension();
//			if(ext == null || ext.length()==0) {
//				ext = "do";
//			}
//			//createFacade(req);
//			return ActionResource.create(req.getAction(), req.getExtension(), modulePack);
//		}
		
		
		boolean loggedInUser = cm.getUser()!=null; // || "guest".equals(cm.getUser().getName()));
		
		Resource resource = null;
		
		if(loggedInUser) {
			//resource by absulute path
			resource = createJCRResource(cm.getContentNode("/"+path), bundleContext);
			//try user home
			//resource = createJCRResource(cm.getUser().getChild(path), bundleContext);
		}
		
		if(resource == null) {
			//public path
			resource = createJCRResource(cm.getPublicNode().getChild(path), bundleContext);
		}
		
		if(resource == null) {
			resource = getModulePackRes(req, path, bundleContext);
		}
		return resource;
	}
	
	private Resource getModulePackRes(Request req, String path, BundleContext bundleContext) {
		int idx = path.indexOf('/');
		String p = path;
		if(idx>0) {
			p = path.substring(0, idx);
		}
		//bundleContext.getServiceReferences(null, "(&(objectclass="+ModulePackage.class.getName()+")(path=admin))")
		try {
			ServiceReference[] refs = bundleContext.getServiceReferences(ModulePackage.class.getName(), "(path="+p+")");
			if(refs!=null && refs.length>0) {
				ModulePackage modulePack = (ModulePackage) bundleContext.getService(refs[0]);
				return ActionResource.create(req.getAction(), req.getExtension(), modulePack);
			}
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private Resource createJCRResource(IContentNode node, BundleContext bundleContext) {
		if(node == null) return null;
		return new ContentResource(node, bundleContext);
	}
	
	private URL getStaticResUrl(String path, List<URL> staticResourcesUrls) {
		for (URL rul : staticResourcesUrls) {
			URL res = null;
			try {
				res = new URL(rul, path);
				URLConnection conn = res.openConnection();
				conn.connect();
				//TODO: check folder ???
			} catch (MalformedURLException e1) {
				//e1.printStackTrace();
				res = null;
			} catch (IOException e) {
				//e.printStackTrace();
				res = null;
			}
			if (res != null) {
				return res;
			}
		}
		return null;
	}
	
	public void setupContentModel(Request req, BundleContext bundleContext) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		if(cm == null) {
			cm = new ContentModelImpl((User) req.getUser(), bundleContext);
			req.setSessAttr(ContentModel.KEY, cm);
		}
	}
}
