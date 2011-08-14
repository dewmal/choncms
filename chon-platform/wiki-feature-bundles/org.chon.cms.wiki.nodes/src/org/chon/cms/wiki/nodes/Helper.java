package org.chon.cms.wiki.nodes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jcr.Node;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.IContentNodeFactory;
import org.chon.cms.model.content.INodeRenderer;
import org.osgi.framework.BundleContext;

public class Helper {
	public static void registerNodeRenderer(BundleContext context, Class<? extends INodeRenderer> clzz) throws InstantiationException, IllegalAccessException {
		
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("renderer", clzz.getName());
		context.registerService(INodeRenderer.class.getName(),
				
				clzz.newInstance(), props);
	}
	
	public static void registerContentNodeFactory(BundleContext context, IContentNodeFactory factory) {
		context.registerService(IContentNodeFactory.class.getName(), factory, null);
	}
	
	public static IContentNodeFactory createIContentNodeFactory(Class<? extends IContentNode> ... nodeTypes) {
		return new DefFactory(nodeTypes);
	}
	
	
	/**
	 * Factory that keeps map of IContentNode classes
	 * Based on class name this factory can create IContentNode from jcrNode
	 * @author Jovica
	 *
	 */
	private static class DefFactory implements IContentNodeFactory {

		private Map<String, Class<? extends IContentNode>> cnTypesMap = new HashMap<String, Class<? extends IContentNode>>();
		
		public DefFactory(Class<? extends IContentNode> ... nodeTypes) {
			for(Class<? extends IContentNode> c : nodeTypes) {
				cnTypesMap.put(c.getName(), c);
			}
		}
		
		@Override
		public boolean canCreate(String contentNodeClass) {
			return cnTypesMap.containsKey(contentNodeClass);
		}

		@Override
		public IContentNode createIntance(String contentNodeClass,
				ContentModel contentModel, Node node, IContentNode typeDesc) {
			Class<? extends IContentNode> clzz = cnTypesMap.get(contentNodeClass);
			try {
				Constructor<? extends IContentNode> ctor = clzz.getConstructor(ContentModel.class, Node.class, IContentNode.class);
				return ctor.newInstance(contentModel, node, typeDesc);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}
