package org.chon.cms.menu.model.types;

import org.chon.cms.menu.model.IMenuItem;
import org.chon.cms.menu.model.factory.MiFactory;
import org.chon.cms.model.content.IContentNode;

public class MItemExternalLink extends MItemLocalLink {
	
	public MItemExternalLink(IContentNode node, IMenuItem parent,
			MiFactory factory) {
		super(node, parent, factory);
	}

	@Override
	public boolean isInternalLink() {
		return false;
	}
}
