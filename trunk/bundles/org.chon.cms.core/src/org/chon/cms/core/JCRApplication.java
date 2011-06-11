package org.chon.cms.core;

import java.net.URL;
import java.util.Map;

import org.chon.web.api.Application;


public interface JCRApplication extends Application {

	//public MenuItem getAdminRootMenuItem();
	
	public void regExtension(String name, Extension ext); 
	
	public void unregExtension(String name);

	public void addVTemplateRoot(URL url);

	public void removeVTemplateRoot(URL url);

	public void addStaticResourceRoot(URL url);

	public void removeStaticResourceRoot(URL url);

	public Map<String, Extension> getExts();
	
}