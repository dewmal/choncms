package org.chon.cms.model.content;

import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;

import org.chon.cms.model.ContentModel;

public interface IContentNode {

	/**
	 * Get Content Node name
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Get Node identifier
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * similar as getPropertyAs, but type is given in string format: "int",
	 * "date" etc... supported types:
	 * 
	 * string -> PropertyType.STRING
	 * 
	 * date -> PropertyType.DATE
	 * 
	 * int integer long -> PropertyType.LONG
	 * 
	 * decimal num number -> PropertyType.DECIMAL
	 * 
	 * bool boolean bit -> PropertyType.BOOLEAN
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public Object prop(String name, String type);

	/**
	 * Get property as string
	 * 
	 * @param name
	 * @return
	 */
	public String prop(String name);

	/**
	 * Get property as specified type, see PropertyType for types:
	 * 
	 * PropertyType.STRING -> {@link java.lang.String};
	 * PropertyType.BOOLEAN -> {@link java.lang.Boolean};
	 * PropertyType.DATE -> {@link java.util.Calendar};
	 * PropertyType.LONG -> {@link java.lang.Long};
	 * PropertyType.DECIMAL -> {@link java.math.BigDecimal};
	 * PropertyType.DOUBLE -> {@link java.lang.Double};
	 * 
	 * @param name
	 * @param propertyType
	 * @return
	 */
	public Object getPropertyAs(String name, int propertyType);

	/**
	 * Get JCR property
	 * 
	 * @param name
	 * @return
	 */
	public Property getProperty(String name);

	/**
	 * get property jcr:created
	 * 
	 * @return
	 */
	public Calendar getJcrCreated();

	/**
	 * get property jcr:lastModified
	 * 
	 * @return
	 */

	public Calendar getJcrLastModified();

	/**
	 * get node relative to current
	 * 
	 * @param path
	 * @return
	 */
	public IContentNode getChild(String path);
	
	/**
	 * Get childs count
	 * 
	 * @return
	 */
	public long getChildsSize();

	/**
	 * get childs from offset (start) limit results
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<IContentNode> getChilds(int start, int limit);

	/**
	 * Get all childs
	 * 
	 * @return
	 */
	public List<IContentNode> getChilds();

	/**
	 * Get absolute path, same as jcrNode.getPath()
	 * 
	 * @return
	 */
	public String getAbsPath();

	/**
	 * Get node path, same as getAbsPath just without absolute /
	 * if node is inside public (www) then path is relative to www
	 * 	
	 * @return
	 */
	public String getPath();

	/**
	 * Get node property type *note jcrNode type is different, for keeping
	 * things simple CHON will use only nt:unstructured nodes, and mark type in
	 * property type
	 * 
	 * @return Type of the node
	 */
	public String getType();

	/**
	 * Get JCR node
	 * 
	 * @return
	 */
	public Node getNode();

	/**
	 * Get parent node
	 * 
	 * @return
	 */
	public IContentNode getParent();

	/**
	 * Helper, access to node property from Velocity
	 * 
	 * @param propertyName
	 * @return
	 */
	public String get(String propertyName);
	
	/**
	 * get content model
	 * @return
	 */
	public ContentModel getContentModel();
	

}