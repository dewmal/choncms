package org.chon.cms.model.content.base;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.PropertyType;

public class BaseWWWContentNode extends BaseContentNode {

	public BaseWWWContentNode(ContentModel model, Node node, IContentNode typeDesc) {
		super(model, node, typeDesc);
	}
	
	/**
	 * If node should not be shown to anonymous users
	 *  (aldough it is in public folder)
	 * @return
	 */
	public boolean isPrivate() {
		return (Boolean)getPropertyAs("isPrivate", PropertyType.BOOLEAN);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getPath()
	 */
	@Override
	public String getPath() {
		String www_node_name = null;
		try {
			www_node_name = this.model.getPublicNode().getNode().getPath();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (www_node_name != null) {
			// remove www_node_name from path
			String path = getAbsPath().replace(www_node_name, "");
			while (path.startsWith("/")) {
				path = path.substring(1);
			}
			return path;
		}
		return getAbsPath();
	}
}
