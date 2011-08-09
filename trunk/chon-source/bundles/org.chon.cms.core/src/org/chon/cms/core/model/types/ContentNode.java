package org.chon.cms.core.model.types;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.base.BaseWWWContentNode;

public class ContentNode extends BaseWWWContentNode {

	public ContentNode(ContentModel model, Node node, IContentNode typeDesc) {
		super(model, node, typeDesc);
	}

	public List<FileContentNode> getFiles() {
		List<FileContentNode> rv = new ArrayList<FileContentNode>();
		List<IContentNode> childs = this.getChilds();
		for (IContentNode n : childs) {
			if (n instanceof FileContentNode) {
				rv.add((FileContentNode) n);
			}
		}
		return rv;
	}

	public List<FileContentNode> getImages() {
		List<FileContentNode> rv = new ArrayList<FileContentNode>();
		List<IContentNode> childs = this.getChilds();
		for (IContentNode n : childs) {
			if (n instanceof FileContentNode) {
				FileContentNode fcn = (FileContentNode) n;
				if (fcn.getMimeType().startsWith("image")) {
					rv.add((FileContentNode) n);
				}
			}
		}
		return rv;
	}
}
