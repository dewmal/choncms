package org.choncms.display.lists.actions;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class SaveListAjaxAction extends AbstractDisplayListAction {
	@Override
	public String run(Application arg0, Request req, Response arg2) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		try {
			JSONObject r = new JSONObject(req.get("req"));
			String listName = r.getString("name");
			String listType = r.getString("type");
			String listTitle = r.optString("title");
			
			if ("simple".equals(listType)) {
				saveSimpleList(listName, listTitle, r.getJSONArray("list"), cm);
			} else {
				throw new Exception("Unsuported list type: " + listType);
			}
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}
	
	protected void saveSimpleList(String listName, String listTitle, JSONArray list,
			ContentModel cm) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
		IContentNode displayListNode = getDisplayListNode(listName, cm);
		Node n = displayListNode.getNode();
		n.setProperty("listType", "simple");
		n.setProperty("title", listTitle);
		n.setProperty("items", list.toString());
		n.getSession().save();
	}

}