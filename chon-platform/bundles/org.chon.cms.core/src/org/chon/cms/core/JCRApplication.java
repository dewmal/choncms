package org.chon.cms.core;

import java.net.URL;
import java.util.Map;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.INodeRenderer;
import org.chon.web.api.Application;


public interface JCRApplication extends Application {

	/**
	 * Register new extenstion
	 * 
	 * @param name
	 * @param ext
	 */
	public void regExtension(String name, Extension ext); 
	
	/**
	 * unregister extenstension 
	 * @param name
	 */
	public void unregExtension(String name);

	
	public void addVTemplateRoot(URL url);

	public void removeVTemplateRoot(URL url);

	public void addStaticResourceRoot(URL url);

	public void removeStaticResourceRoot(URL url);

	public Map<String, Extension> getExts();

	public ContentModel createContentModelInstance(String user);
	
	public void registerNodeRenderer(Class<? extends INodeRenderer> clzz) throws InstantiationException, IllegalAccessException;
}