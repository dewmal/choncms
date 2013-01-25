package org.chon.cms.wiki.nodes;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.chon.cms.core.model.types.EvalVelocityHtmlContentNode;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;

public class WikiPageContentNode extends EvalVelocityHtmlContentNode {

	private String markup = null;
	private boolean isDirty = false;
	
	public WikiPageContentNode(ContentModel model, Node node,
			IContentNode typeDesc) {
		super(model, node, typeDesc);
	}
	
	public void setMarkup(String markup) {
		isDirty = true;
		this.markup = markup;
	}
	
	public String getMarkup() {
		return markup!=null ? markup : prop("markup");
	}
	
	public boolean getIsDirty() {
		return isDirty;
	}
	
	public void save() throws RepositoryException {
		//TODO: title save
		this.node.setProperty("markup", getMarkup());
		this.getContentModel().getSession().save();
		isDirty = false;
	}
}
