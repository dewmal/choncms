package org.chon.cms.core.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;

import org.chon.cms.core.model.types.CategoryContentNode;
import org.chon.cms.core.model.types.ContentNode;
import org.chon.cms.core.model.types.EvalVelocityHtmlContentNode;
import org.chon.cms.core.model.types.FileContentNode;
import org.chon.cms.core.model.types.RootContentNode;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.IContentNodeFactory;
import org.chon.cms.model.content.base.BaseWWWContentNode;

public class ContentNodeFactory implements IContentNodeFactory {

	private Map<String, Class<? extends IContentNode>> cnTypesMap = new HashMap<String, Class<? extends IContentNode>>();
	
	public ContentNodeFactory() {
		cnTypesMap.put(BaseWWWContentNode.class.getName(), BaseWWWContentNode.class);
		cnTypesMap.put(ContentNode.class.getName(), ContentNode.class);
		cnTypesMap.put(EvalVelocityHtmlContentNode.class.getName(), EvalVelocityHtmlContentNode.class);
		cnTypesMap.put(CategoryContentNode.class.getName(), CategoryContentNode.class);
		cnTypesMap.put(FileContentNode.class.getName(), FileContentNode.class);
		cnTypesMap.put(RootContentNode.class.getName(), RootContentNode.class);
	}
	
	@Override
	public boolean canCreate(String contentNodeClass) {
		if(contentNodeClass == null) {
			return true;
		}
		return cnTypesMap.containsKey(contentNodeClass);
	}

	@Override
	public IContentNode createIntance(String contentNodeClass,
			ContentModel contentModel, Node node, IContentNode typeDesc) {
		
		Class<? extends IContentNode> clzz = null;
		
		if (contentNodeClass == null) {
			clzz = BaseWWWContentNode.class;
		} else {
			clzz = cnTypesMap.get(contentNodeClass);
		}
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
