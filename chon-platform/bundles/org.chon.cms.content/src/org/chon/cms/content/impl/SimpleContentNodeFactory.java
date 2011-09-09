package org.chon.cms.content.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.IContentNodeFactory;

public class SimpleContentNodeFactory implements IContentNodeFactory {

		private Map<String, Class<? extends IContentNode>> cnTypesMap = new HashMap<String, Class<? extends IContentNode>>();
		
		public SimpleContentNodeFactory() {
			
		}
		
		public void addNodeWrapperClass(Class<? extends IContentNode> c) {
			cnTypesMap.put(c.getName(), c);
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