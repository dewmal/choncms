package org.chon.cms.core.model.types;

import javax.jcr.Node;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;

public class RootContentNode extends ContentNode {

	public RootContentNode(ContentModel model, Node node, IContentNode typeDesc) {
		super(model, node, typeDesc);
	}
	
	/**
	 * Node to redirect to
	 * @return
	 */
	public IContentNode getHtmlNode() {
		String htmlNode = prop("htmlNode");
		if(htmlNode == null) {
			return null;
		}
		return this.getContentModel().getContentNode(htmlNode);
	}
	
}
