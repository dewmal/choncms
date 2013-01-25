package org.choncms.display.lists.actions;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class EditListAction extends AbstractDisplayListAction {
	@Override
	public String run(Application app, Request req, Response resp) {
		Map<String, Object> params = new HashMap<String, Object>();
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		IContentNode displayListNode = getDisplayListNode(req.get("name"), cm);
		params.put("displayListNode", displayListNode);
		params.put("extName", getExtenstionName());
		return resp.formatTemplate("display.lists/editlist.html", params);
	}
}