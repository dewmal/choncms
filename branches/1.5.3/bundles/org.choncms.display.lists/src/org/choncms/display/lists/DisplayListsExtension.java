package org.choncms.display.lists;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.choncms.display.lists.actions.DashboardAction;
import org.choncms.display.lists.actions.DeleteListAjaxAction;
import org.choncms.display.lists.actions.EditListAction;
import org.choncms.display.lists.actions.SaveListAjaxAction;

public class DisplayListsExtension implements Extension {


	public static final String EXTENSION_NAME = "list";
	public static final String DATA_NODE_NAME = "display.lists";
	
	public DisplayListsExtension(JCRApplication app) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Map<String, Action> getAdminActons() {
		Map<String, Action> actions = new HashMap<String, Action>();
		actions.put("display.lists.dashboard", new DashboardAction());
		actions.put("display.lists.editlist", new EditListAction());
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		Map<String, Action> ajaxActions = new HashMap<String, Action>();
		ajaxActions.put("display.lists.savelist", new SaveListAjaxAction());
		ajaxActions.put("display.lists.deletelist", new DeleteListAjaxAction());
		return ajaxActions;
	}

	
	@Override
	public Object getTplObject(Request req, Response resp, IContentNode contentNode) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		IContentNode displayListsNode = cm.getAppsConfigNode(DATA_NODE_NAME, true);
		return new DisplayListFEContainer(displayListsNode);
	}

}
