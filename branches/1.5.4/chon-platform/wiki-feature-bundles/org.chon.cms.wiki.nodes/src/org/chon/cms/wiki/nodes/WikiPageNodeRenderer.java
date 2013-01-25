package org.chon.cms.wiki.nodes;

import javax.jcr.RepositoryException;

import org.chon.cms.core.model.renderers.VTplNodeRenderer;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;

public class WikiPageNodeRenderer extends VTplNodeRenderer {

	@Override
	public void render(IContentNode contentNode, Request req, Response resp,
			Application _app, ServerInfo serverInfo) {
		WikiPageContentNode wikiPageContentNode = (WikiPageContentNode) contentNode;
		String paramMarkup = req.get("markup");
		
		if(paramMarkup != null) {
			wikiPageContentNode.setMarkup(paramMarkup);
		}
		String save = req.get("save");
		if(save!= null && "true".equals(save)) {
			//TODO: Check permissions
			try {
				Object user = req.getUser();
				if(user == null) {
					wikiPageContentNode.setMarkup("==Illegall access==");
				} else {
					wikiPageContentNode.save();
				}	
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.render(wikiPageContentNode, req, resp, _app, serverInfo);
	}
	
}
