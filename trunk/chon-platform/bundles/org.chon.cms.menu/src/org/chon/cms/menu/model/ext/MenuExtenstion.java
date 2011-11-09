package org.chon.cms.menu.model.ext;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.AccessDeniedException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.chon.cms.core.Extension;
import org.chon.cms.menu.model.BaseMenuItem;
import org.chon.cms.menu.model.IMenuItem;
import org.chon.cms.menu.model.MenusApp;
import org.chon.cms.menu.model.ext.actions.AjaxAction_getItems;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public class MenuExtenstion implements Extension {
	private Map<String, Action> ajaxActions = new HashMap<String, Action>();
	Map<String, Action> actions = new HashMap<String, Action>();

	public MenuExtenstion() {
		actions.put("menu.view", new Action() {
			@Override
			public String run(Application app, Request req, Response resp) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("menuName", req.get("name"));
				return resp.formatTemplate("menu/menu.html", params);
			}
		});
		actions.put("menu.list", new Action() {
			@Override
			public String run(Application app, Request req, Response resp) {
				Map<String, Object> params = new HashMap<String, Object>();
				ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
				params.put("menuList", MenusApp.getAllMenus(cm));
				params.put("menuRootId", MenusApp.getRootMenuNode(cm).getId());
				return resp.formatTemplate("menu/list.html", params);
			}
		});
		ajaxActions.put("menu.getItems", new AjaxAction_getItems());
		ajaxActions.put("menu.deleteMenu", new Action() {
			
			@Override
			public String run(Application app, Request req, Response resp) {
				String name = req.get("name");
				ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
				Node menuNode = MenusApp.getRootMenuNode(cm).getChild(name).getNode();
				try {
					menuNode.remove();
					cm.getSession().save();
					return "OK";
				} catch (RepositoryException e) {
					e.printStackTrace();
				}
				return "ERROR";
			}
		});
	}

	@Override
	public Map<String, Action> getAdminActons() {
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		return ajaxActions;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		return new MenuView(req, resp, node);
	}

}
