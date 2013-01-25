package org.chon.cms.wiki.nodes;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.chon.cms.core.model.renderers.VTplNodeRenderer;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.wiki.app.WikiApplication;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;

public class WikiNewPageNodeRenderer extends VTplNodeRenderer {

	@Override
	public void render(IContentNode contentNode, Request req, Response resp,
			Application _app, ServerInfo serverInfo) {
		String create = req.get("create");
		if(create != null) {
			//TODO: check user if it can create a page
			ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
			try {
				createNewWikiPage(cm, create);
			} catch (ItemExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Node created redirecting to " + create);
			resp.setRedirect(System.getProperty("siteUrl") + "/" + create);
		} else {
			String title = req.get("title");
			if(title != null) {
				super.render(contentNode, req, resp, _app, serverInfo);
			} else {
				//TODO: what if we have direct call to create wiki page???
			}
		}
	}

	private void createNewWikiPage(ContentModel cm, String path) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException {
		if(cm.getPublicNode(path) != null) {
			throw new ItemExistsException();
		}
		Node www = cm.getPublicNode().getNode();
		Node node = www.addNode(path);
		node.setProperty("title", "New Wiki Page");
		node.setProperty("markup", "===New Wiki Page: "+path+"===");
		node.setProperty("type", WikiApplication.WIKI_PAGE_NODE_TYPE);
		cm.getSession().save();
	}

}
