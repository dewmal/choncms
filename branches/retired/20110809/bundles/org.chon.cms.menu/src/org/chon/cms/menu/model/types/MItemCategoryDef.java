package org.chon.cms.menu.model.types;

import org.chon.cms.menu.model.IMenuItem;
import org.chon.cms.menu.model.factory.MiFactory;
import org.chon.cms.model.content.IContentNode;

public class MItemCategoryDef extends MItemCategory {
	private String name = null;
	public MItemCategoryDef(IContentNode node, IMenuItem parent,
			MiFactory factory) {
		super(node, parent, factory);
		this.name = node.getName();
		this.node = node.getContentModel().getContentNode(node.prop("category"));
	}
	@Override
	public String getName() {
		return name;
	}
}
