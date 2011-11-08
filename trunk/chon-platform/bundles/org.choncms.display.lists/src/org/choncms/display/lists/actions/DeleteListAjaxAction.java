package org.choncms.display.lists.actions;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class DeleteListAjaxAction extends AbstractDisplayListAction {
	@Override
	public String run(Application app, Request req, Response resp) {
		try {
			String listName = req.get("name");
			ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
			IContentNode displayListNode =  getDisplayListNode(listName, cm);
			displayListNode.getNode().remove();
			cm.getSession().save();
			return "SUCCESS";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERROR: " + e.getMessage();
		}
	}
}