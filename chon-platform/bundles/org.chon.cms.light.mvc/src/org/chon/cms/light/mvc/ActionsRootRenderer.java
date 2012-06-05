package org.chon.cms.light.mvc;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.INodeRenderer;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;

public class ActionsRootRenderer implements INodeRenderer {

	@Override
	public void render(IContentNode node, Request req, Response resp,
			Application app, ServerInfo si) {
		List<IContentNode> childs = node.getChilds();
		if("true".equals(req.get("debug"))) {			
			resp.getOut().println("<pre>");
			resp.getOut().println(this.getClass() + ":> ");
			for(IContentNode n : childs) {
				resp.getOut().println("\t" + n.getName() + "");
			}
			resp.getOut().println("</pre>");
		} else {
			resp.getServletResponse().setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
		}
	}

}
