package org.chon.cms.light.mvc;

import javax.jcr.Node;

import org.chon.cms.core.model.types.ContentNode;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;

public class ActionsRoot extends ContentNode {
	public static final String TYPE = "light.mvc.actions.root";
	
	public ActionsRoot(ContentModel model, Node node, IContentNode typeDesc) {
		super(model, node, typeDesc);
	}

}
