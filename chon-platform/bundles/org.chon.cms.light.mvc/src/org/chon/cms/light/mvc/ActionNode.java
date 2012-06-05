package org.chon.cms.light.mvc;

import javax.jcr.Node;

import org.chon.cms.core.model.types.ContentNode;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;

public class ActionNode extends ContentNode {
	public static final String TYPE = "light.mvc.action";
	
	public ActionNode(ContentModel model, Node node, IContentNode typeDesc) {
		super(model, node, typeDesc);
	}
	
	//public abstract Result exec(Request req); will see
}
