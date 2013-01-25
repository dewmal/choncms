package org.choncms.display.lists.actions;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.mpac.Action;
import org.choncms.display.lists.DisplayListNode;
import org.choncms.display.lists.DisplayListsExtension;

public abstract class AbstractDisplayListAction implements Action {
	protected IContentNode getDisplayListContainer(ContentModel cm) {
		return cm.getAppsConfigNode(DisplayListsExtension.DATA_NODE_NAME, true);
	}

	protected DisplayListNode getDisplayListNode(String name, ContentModel cm) {
		return (DisplayListNode) getDisplayListContainer(cm).getChild(name);
	}

	protected String getExtenstionName() {
		return DisplayListsExtension.EXTENSION_NAME;
	}
}