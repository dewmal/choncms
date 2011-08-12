package org.chon.cms.menu.model.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.chon.cms.menu.model.BaseMenuItem;
import org.chon.cms.menu.model.IMenuItem;
import org.chon.cms.menu.model.types.MItemCategory;
import org.chon.cms.menu.model.types.MItemCategoryDef;
import org.chon.cms.menu.model.types.MItemExternalLink;
import org.chon.cms.menu.model.types.MItemLocalLink;
import org.chon.cms.model.content.IContentNode;

public class MiFactoryImpl implements MiFactory {
	private Map<String, Class<? extends BaseMenuItem>> typeMap = new HashMap<String, Class<? extends BaseMenuItem>>();
	public MiFactoryImpl() {
		typeMap.put("category", MItemCategory.class);
		typeMap.put("html", MItemCategory.class);
		typeMap.put("menu.item.category", MItemCategoryDef.class);
		typeMap.put("menu.item.local", MItemLocalLink.class);
		typeMap.put("menu.item.external", MItemExternalLink.class);
	}
	
	@Override
	public IMenuItem createMenuItem(IContentNode contentNode, IMenuItem parent) {
		String type = contentNode.getType();
		if(typeMap.containsKey(type)) {
			Class<? extends BaseMenuItem> clzz = typeMap.get(type);
			try {
				Constructor<? extends BaseMenuItem> ctor = clzz.getConstructor(IContentNode.class, IMenuItem.class, MiFactory.class);
				return ctor.newInstance(contentNode, parent, this);
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
		}
		return null;
	}

}
