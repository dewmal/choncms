package org.chon.cms.content.ext.content.actions;

import javax.jcr.Node;

import org.chon.cms.admin.utils.JSONUtils;
import org.chon.cms.model.ContentModel;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public class AjaxAction_removeFile implements Action {

	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		
		String id = req.get("nodeId");
		try {
			Node node = cm.getSession().getNodeByIdentifier(id);
			node.getNode(req.get("file")).remove();
			cm.getSession().save();
			return JSONUtils.msg("OK").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONUtils.msg("ERROR").toString();
	}

}
