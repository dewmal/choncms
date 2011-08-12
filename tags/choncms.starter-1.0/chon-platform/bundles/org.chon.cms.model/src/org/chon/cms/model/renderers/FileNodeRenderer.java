package org.chon.cms.model.renderers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.INodeRenderer;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Resource;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;
import org.chon.web.api.res.ImageResource;

public class FileNodeRenderer implements INodeRenderer {
	
	@Override
	public void render(IContentNode contentNode, Request req, Response resp, Application app, ServerInfo serverInfo) {
		Node fileNode = contentNode.getNode();
		try {
			Node res = fileNode.getNode("jcr:content");
			if(checkLastModified(res, req.getServletRequset(), resp.getServletResponse())) {
				return;
			}
			
			Property data = res.getProperty("jcr:data");
			InputStream is = data.getBinary().getStream();
			int contentLength = (int) data.getBinary().getSize();
			String mime;
			if(res.hasProperty("jcr:mimeType")) {
				mime = res.getProperty("jcr:mimeType").getString();
			} else {
				mime = serverInfo.getSerlvetContext().getMimeType(fileNode.getName());
			}
			if(mime!=null && mime.startsWith("image")) {
				int w = req.getInt("w", 0);
				int h = req.getInt("h", 0);
				String fmt = req.get("fmt");
				if(w!=0 || h!=0 || fmt!=null) {
					Resource imgRes = ImageResource.create(is, mime.substring(6), w, h, req.getInt("cut", 0), fmt);
					imgRes.process(serverInfo);
					return;
				}
			}
			
			resp.getServletResponse().setContentType(mime);
			resp.getServletResponse().setContentLength(contentLength);
			OutputStream os = resp.getServletResponse().getOutputStream();
			IOUtils.copy(is, os);
			os.flush();
			os.close();
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean checkLastModified(Node node, HttpServletRequest req, HttpServletResponse res) {
		long lastModified = getLastModified(node);
		if (lastModified != 0) {
			res.setDateHeader("Last-Modified", lastModified);
		}

		if (!resourceModified(lastModified, req.getDateHeader("If-Modified-Since"))) {
			res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return true;
		}
		return false;
	}

	private boolean resourceModified(long resTimestamp, long modSince) {
		modSince /= 1000;
		resTimestamp /= 1000;
		return resTimestamp == 0 || modSince == -1 || resTimestamp > modSince;
	}

	private long getLastModified(Node node) {
		try {
			return node.getProperty("jcr:lastModified").getDate().getTimeInMillis();
		} catch (ValueFormatException e) {
			e.printStackTrace();
		} catch (PathNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
