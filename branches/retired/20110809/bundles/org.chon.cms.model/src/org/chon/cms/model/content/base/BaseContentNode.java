package org.chon.cms.model.content.base;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.PropertyType;

/**
 * Base content node mostly used in velocity templating
 * 	Content node can be everything, see extended BaseWWWContentNode that is 
 * 	default node wrap class
 * 
 * @author Jovica
 *
 */
public abstract class BaseContentNode implements IContentNode {
	
	private static final Log log = LogFactory.getLog(BaseContentNode.class);

	protected Node node;
	
	protected IContentNode typeDesc;

	protected ContentModel model;

	public BaseContentNode(ContentModel model, Node node, IContentNode typeDesc) {
		this.model = model;
		this.node = node;
		this.typeDesc = typeDesc;
	}
	
	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getName()
	 */
	@Override
	public String getName() {
		try {
			return node.getName();
		} catch (RepositoryException e) {
			log.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getId()
	 */
	@Override
	public String getId() {
		try {
			return node.getIdentifier();
		} catch (RepositoryException e) {
			log.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#prop(java.lang.String, java.lang.String)
	 */
	@Override
	public Object prop(String name, String type) {
		if("string".equalsIgnoreCase(type)) {
			return getPropertyAs(name, PropertyType.STRING);
		}
		
		if("date".equalsIgnoreCase(type)) {
			return getPropertyAs(name, PropertyType.DATE);
		}
		
		if("int".equalsIgnoreCase(type) 
			|| "integer".equalsIgnoreCase(type)
			|| "long".equalsIgnoreCase(type)) {
			return getPropertyAs(name, PropertyType.LONG);
		}
		
		if("decimal".equalsIgnoreCase(type) 
			|| "num".equalsIgnoreCase(type) 
			|| "number".equalsIgnoreCase(type)) {
			return getPropertyAs(name, PropertyType.DECIMAL);
		}
		
		if("bool".equalsIgnoreCase(type) 
			|| "boolean".equalsIgnoreCase(type) 
			|| "bit".equalsIgnoreCase(type)) {
			return getPropertyAs(name, PropertyType.BOOLEAN);
		}	
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#prop(java.lang.String)
	 */
	@Override
	public String prop(String name) {
		return (String) getPropertyAs(name, PropertyType.STRING);
	}
	
	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getPropertyAs(java.lang.String, int)
	 */
	@Override
	public Object getPropertyAs(String name, int propertyType) {
		Property p = getProperty(name);
		if(p != null) {
			try {
				switch (propertyType) {
				case PropertyType.STRING:
					return p.getString();
				case PropertyType.BOOLEAN:
					return p.getBoolean();
				case PropertyType.DATE:
					return p.getDate();
				case PropertyType.LONG:
					return p.getLong();
				case PropertyType.DECIMAL:
					return p.getDecimal();
				case PropertyType.DOUBLE:
					return p.getDouble();
				default:
					log.warn("Invalid property type, see: " + PropertyType.class);
					break;
				}
				return p.getString();
			} catch (ValueFormatException e) {
				log.warn(e);
			} catch (RepositoryException e) {
				log.error(e);
			}
		}
		//get from default properties in conf
		//or if bubbling enablef ask parent node
		return getUnknownProp(name, propertyType);
	}
	
	private Object getUnknownProp(String name, int propertyType) {
		Object p = null;
		//if proparty not found in a node
		if(typeDesc != null) {
			//try type descriptor to see if a property is there
			IContentNode defProperties = typeDesc.getChild("default.properties");
			if(defProperties != null) {
				//try in default.properties
				p = defProperties.getPropertyAs(name, propertyType);
			}
			if(p == null) {
				//see if property should be asked from parent
				Boolean bubbleToParent = (Boolean)typeDesc.getPropertyAs("bubbleToParnet", PropertyType.BOOLEAN);
				if(bubbleToParent!=null && bubbleToParent==Boolean.TRUE) {
					p = this.getParent().getPropertyAs(name, propertyType);
				}
			}
		}
		return p;
	}

	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getProperty(java.lang.String)
	 */
	@Override
	public Property getProperty(String name) {
		try {
			if (node.hasProperty(name)) {
				return node.getProperty(name);
			}
		} catch (RepositoryException e) {
			log.error(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getJcrCreated()
	 */
	@Override
	public Calendar getJcrCreated() {
		return (Calendar) getPropertyAs("jcr:created", PropertyType.DATE);
	}

	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getJcrLastModified()
	 */
	@Override
	public Calendar getJcrLastModified() {
		return (Calendar) getPropertyAs("jcr:lastModified", PropertyType.DATE);
	}

	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getChildsSize()
	 */
	@Override
	public long getChildsSize() {
		NodeIterator ni;
		try {
			ni = node.getNodes();
			return ni.getSize();
		} catch (RepositoryException e) {
			log.error(e);
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getChilds(int, int)
	 */
	@Override
	public List<IContentNode> getChilds(int start, int limit) {
		try {
			NodeIterator ni = node.getNodes();
			if(ni.hasNext()) {
				ni.skip(start);
	
				int k = 0;
				List<IContentNode> childs = new ArrayList<IContentNode>();
				while (ni.hasNext()) {
					IContentNode cn = this.model.getContentNode(ni.nextNode());
					childs.add(cn);
					if (limit != -1 && k >= limit) {
						break;
					}
					k++;
				}
				return childs;
			}
		} catch (RepositoryException e) {
			log.error(e);
		}
		return new ArrayList<IContentNode>();
	}

	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getChilds()
	 */
	@Override
	public List<IContentNode> getChilds() {
		return getChilds(0, -1);
	}

	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getAbsPath()
	 */
	@Override
	public String getAbsPath() {
		try {
			return node.getPath();
		} catch (RepositoryException e) {
			log.error(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getPath()
	 */
	@Override
	public String getPath() {
		String path = getAbsPath();
		while (path.startsWith("/")) {
			path = path.substring(1);
		}
		return path;
	}
	
	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getType()
	 */
	@Override
	public String getType() {
		return model.getNodeType(node);
	}

	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getNode()
	 */
	@Override
	public Node getNode() {
		return node;
	}
	
	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#getParent()
	 */
	@Override
	public IContentNode getParent() {
		try {
			return this.model.getContentNode(this.node.getParent());
		} catch (AccessDeniedException e) {
			log.error("Access denied", e);
		} catch (ItemNotFoundException e) {
			log.error("Item not found", e);
		} catch (RepositoryException e) {
			log.error("Unknown repository error", e);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.chon.web.jcr.cms.content.model.IContentNode#get(java.lang.String)
	 */
	@Override
	public String get(String propertyName) {
		return prop(propertyName);
	}

	@Override
	public IContentNode getChild(String path) {
		return model.getContentNode(this.getAbsPath() + "/" + path);
	}

	@Override
	public ContentModel getContentModel() {
		return model;
	}
}
