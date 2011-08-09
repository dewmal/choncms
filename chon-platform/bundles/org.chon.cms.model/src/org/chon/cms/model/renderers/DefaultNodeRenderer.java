package org.chon.cms.model.renderers;

import java.io.IOException;
import java.io.OutputStream;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletResponse;

import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.INodeRenderer;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;

//TODO
public class DefaultNodeRenderer implements INodeRenderer {
	

	@Override
	public void render(IContentNode contentNode, Request req, Response resp,
			Application app, ServerInfo serverInfo) {
		OutputStream os;
		try {
			HttpServletResponse servletResp = resp.getServletResponse();
			servletResp.setContentType("text/xml");
			os = servletResp.getOutputStream();
			contentNode.getNode().getSession().exportDocumentView(contentNode.getAbsPath(), os, true, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PathNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
