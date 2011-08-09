package org.chon.cms.admin.editor.ext.actions;

import javax.jcr.Node;
import javax.jcr.Session;

import org.chon.cms.model.ContentModel;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public class AjaxActionContentPublish implements Action {
	@Override
	public String run(Application app, Request req, Response resp) {
		ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
		Session session = cm.getSession();
		
		String dest = req.get("destPath");
		String srcId = req.get("srcId");
		
		try {
			Node destNode = session.getNode(dest);
			Node srcNode = session.getNodeByIdentifier(srcId);
			
			if(destNode.hasNode(srcNode.getName())) {
				throw new RuntimeException("Node with the same name exists in destination folder");
			}
			
			session.getWorkspace().move(srcNode.getPath(), destNode.getPath() + "/" + srcNode.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "OK";
	}
}