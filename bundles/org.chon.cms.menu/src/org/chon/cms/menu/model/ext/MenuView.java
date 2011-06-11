package org.chon.cms.menu.model.ext;

import org.chon.cms.menu.model.IMenuItem;
import org.chon.cms.menu.model.MenusApp;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class MenuView {

	private ContentModel cm;

	public MenuView(Request req, Response resp, IContentNode node) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		this.cm = cm;
	}
	
	public IMenuItem get(String name) {
		return MenusApp.getMenu(cm, name);
	}

}
