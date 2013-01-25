package org.choncms.display.lists;

import java.util.List;

import org.chon.cms.model.content.IContentNode;

public class DisplayListFEContainer {
	
	private IContentNode displayListsNode;

	public DisplayListFEContainer(IContentNode displayListsNode) {
		this.displayListsNode = displayListsNode;
	}

	public List<IContentNode> get(String name) {
		DisplayListNode n = (DisplayListNode) displayListsNode.getChild(name);
		if(n == null) {
			return null;
		}
		return n.getItems();
	}
}
