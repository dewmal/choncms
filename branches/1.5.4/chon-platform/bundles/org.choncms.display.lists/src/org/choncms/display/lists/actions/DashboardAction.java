package org.choncms.display.lists.actions;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.choncms.display.lists.DisplayListNode;

public class DashboardAction extends AbstractDisplayListAction {
	@Override
	public String run(Application app, Request req, Response resp) {
		Map<String, Object> params = new HashMap<String, Object>();
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		IContentNode dlNode = getDisplayListContainer(cm);
		String displayListRootId = dlNode.getId();
		params.put("displayListRootId", displayListRootId);
		params.put("list", dlNode.getChilds());
		params.put("extName", getExtenstionName());
		params.put("nodeType", DisplayListNode.DISPLAY_LIST_TYPE);
		return resp.formatTemplate("display.lists/dashboard.html", params);
	}
}