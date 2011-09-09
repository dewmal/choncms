package org.chon.cms.content.utils;

import java.util.Hashtable;
import java.util.Map;

import javax.jcr.Node;

import org.chon.cms.content.impl.SimpleContentNodeFactory;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.IContentNodeFactory;
import org.chon.cms.model.content.INodeRenderer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ChonTypeUtils {
	/**
	 * Register class used for creation of content nodes
	 * 
	 * @param contentNodeClass
	 */
	public static void registerContnetNodeClass(BundleContext bundleContext, Class<? extends IContentNode> contentNodeClass) throws Exception {
		SimpleContentNodeFactory simpleContentNodeFactory = getSimpleContentNodeFactory(bundleContext);
		simpleContentNodeFactory.addNodeWrapperClass(contentNodeClass);
	}
	
	/**
	 * Creates new type in type system of chon
	 * 
	 * @param contentModel
	 * @param typeName
	 * @param contentNodeClass
	 * @param renderer
	 * @param properties
	 * @param defaultNodeProperties
	 * @throws Exception
	 */
	public static void registerType(
			ContentModel contentModel, String typeName,
			Class<? extends IContentNode> contentNodeClass,
			Class<? extends INodeRenderer> renderer,
			Map<String, String> properties,
			Map<String, String> defaultNodeProperties) throws Exception {
		
		//create type if not exists
		Node typeNode = getType(contentModel, typeName, true);
		
		// add properties to type 
		if(properties != null) {
			for(String k : properties.keySet()) {
				if("type".equals(k)) {
					continue;
				}
				typeNode.setProperty(k, properties.get(k));
			}
		}
		
		// add/edit default.properties node
		if(defaultNodeProperties != null) {
			Node defPropsNode = null;
			if(typeNode.hasNode("default.properties")) {
				defPropsNode = typeNode.addNode("default.properties");
				defPropsNode.setProperty("type", "config");
			} else {
				defPropsNode = typeNode.getNode("default.properties");
			}
			for(String k : defaultNodeProperties.keySet()) {
				if("type".equals(k)) {
					continue;
				}
				typeNode.setProperty(k, defaultNodeProperties.get(k));
			}
			
		}
		
		// set property for class that will be used for creation nodes from the type
		if(contentNodeClass != null) {
			typeNode.setProperty("contentNode", contentNodeClass.getName());
		}
		
		// set renderer class
		if(renderer != null) {
			typeNode.setProperty("renderer", renderer.getName());
		}
	}
	
	/**
	 * Created instance of node renderer and registers it to OSGi service
	 * NodeRenderers must be registered in osgi if jcr types system contains types that have 
	 * renderer=<class>  
	 * 
	 * @param renderer class
	 */
	public static void registerNodeRenderer(BundleContext bundleContext, Class<? extends INodeRenderer> clzz) throws InstantiationException, IllegalAccessException {
		INodeRenderer instance = clzz.newInstance();
		registerNodeRenderer(bundleContext, instance);
	}
	
	private static void registerNodeRenderer(BundleContext bundleContext, INodeRenderer instance) throws InstantiationException, IllegalAccessException {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("renderer", instance.getClass().getName());
		bundleContext.registerService(INodeRenderer.class.getName(), instance, props);
	}
	
	private static Node getType(ContentModel contentModel, String type, boolean createIfNotExists) throws Exception {
		Node typesNode = contentModel.getConfigNode("types").getNode();
		if(typesNode.hasNode(type)) {
			return typesNode.getNode(type);
		}
		if(createIfNotExists) {
			return createType(contentModel, type, false);
		}
		throw new Exception("Type '"+type+"' does not exists.");
	}
	
	private static Node createType(ContentModel contentModel, String type, boolean forceRemoveOldIfExists) throws Exception {
		Node typesNode = contentModel.getConfigNode("types").getNode();
		if(typesNode.hasNode(type)) {
			//type already registered, remove it
			if(forceRemoveOldIfExists) {
				typesNode.getNode(type).remove();
			} else {
				throw new Exception("Type '"+type+"' already exists.");
			}
		}
		
		//create new type
		Node typeNode = typesNode.addNode(type);
		typeNode.setProperty("type", "typeDesc");		
		contentModel.getSession().save();
		return typeNode;
	}
	
	private static SimpleContentNodeFactory getSimpleContentNodeFactory(BundleContext bundleContext) throws Exception {
		ServiceReference ref = bundleContext.getServiceReference(SimpleContentNodeFactory.class.getName());
		SimpleContentNodeFactory scnf = null;
		if(ref != null) {
			scnf = (SimpleContentNodeFactory) bundleContext.getService(ref);
		}
		if(scnf == null) {
			//helper for registering node types
			scnf = new SimpleContentNodeFactory();
			bundleContext.registerService(SimpleContentNodeFactory.class.getName(), scnf, null);
			bundleContext.registerService(IContentNodeFactory.class.getName(), scnf, null);
		}
		return scnf;
	}
}
