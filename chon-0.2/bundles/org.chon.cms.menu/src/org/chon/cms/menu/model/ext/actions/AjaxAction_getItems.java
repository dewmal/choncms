package org.chon.cms.menu.model.ext.actions;

import java.util.List;

import org.chon.cms.menu.model.IMenuItem;
import org.chon.cms.menu.model.MenusApp;
import org.chon.cms.model.ContentModel;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AjaxAction_getItems implements Action {


	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		//menu name
		String name = req.get("name");
		IMenuItem menu = MenusApp.getMenu(cm, name);
		
		List<IMenuItem> items = null;
		String param = req.get("node");
		
		
		boolean expanded = false;
		if("root".equals(param)) {
			expanded = true;
			items = menu.getChilds();
		} else {
			items = menu.getChilds(param);
		}
		
		try {
			return toJSON(items, expanded);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String toJSON(List<IMenuItem> items, boolean expanded) throws JSONException {
		JSONArray arr = new JSONArray();
		for(IMenuItem it : items) {
			JSONObject o = new JSONObject();
			o.put("text", it.getName());
			o.put("id", it.getPath());
			o.put("icon", "images/icons/menuIcon1.gif");
			o.put("iconCls", "file");
			o.put("cls", "menu-item");
			o.put("expanded", expanded);
			arr.put(o);
		}
		return arr.toString();
	}
}
