package org.chon.cms.menu.model;

import java.util.ArrayList;
import java.util.List;

import org.chon.cms.menu.model.factory.MiFactory;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;

public class MenusApp {
	private static final String MENU_NODE_NAME = "menu";

	public static IContentNode getRootMenuNode(ContentModel cm) {
		return cm.getAppsConfigNode(MENU_NODE_NAME, true);
	}
	
	public static IMenuItem getMenu(ContentModel cm, IContentNode node) {
		return MiFactory.Instance.createMenuItem(node, null);
	}
	
	public static IMenuItem getMenu(ContentModel cm, String name) {
		IContentNode menu = getRootMenuNode(cm).getChild(name);
		if(menu!=null) {
			return getMenu(cm, menu);			
		}
		return null;
	}
	
	public static List<IMenuItem> getAllMenus(ContentModel cm) {
		List<IMenuItem> ls = new ArrayList<IMenuItem>();
		IContentNode rootMenuNode = getRootMenuNode(cm);
		for(IContentNode c : rootMenuNode.getChilds()) {
			ls.add(getMenu(cm, c));
		}
		return ls;
	}
}
