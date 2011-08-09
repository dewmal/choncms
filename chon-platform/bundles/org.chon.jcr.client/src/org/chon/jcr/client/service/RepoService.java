package org.chon.jcr.client.service;

import java.io.File;

import javax.jcr.Session;

import org.chon.jcr.client.service.model.NodeAttribute;
import org.chon.jcr.client.service.model.NodeInfo;
import org.chon.jcr.client.service.model.Status;


public interface RepoService {

	/**
	 * Check if node exists at passed absolute path
	 * 
	 * @param session
	 * @param path
	 * 
	 * @return Status.TRUE if exists, Status.FALSE if not, Status.ERROR if there is an error
	 */
	public Status nodeExists(Session session, String path);
	
	/**
	 * Check if node has property
	 * 
	 * @param id - Node ID
	 * @param attrName - property name
	 * 
	 * @return Status.TRUE if exists, Status.FALSE if not, Status.ERROR if there is an error
	 */
	public Status attrExists(Session session, String id, String attrName);
	
	/**
	 * Creates New Node under Node with id - parentId
	 *  
	 * @param parentId
	 * @param name
	 * @param attributes
	 * 
	 * @return Status.OK if successfully created, with id from newly created node, or Status.ERROR if there is an error
	 */
	public Status createNode(Session session, String parentId, String name, NodeAttribute [] attributes);
	
	/**
	 * Adds file node to node with passed id
	 *  (File must be on file system, TODO: maybe get from url)
	 *  
	 * @param session
	 * @param nodeId
	 * @param file
	 * @param mimeType - mime type of file, can be null
	 * 
	 * @return Status.OK if successfully created, with id from newly created node, or Status.ERROR if there is an error
	 */
	public Status addFile(Session session, String nodeId, File file, String mimeType);
	
	/**
	 * Edit Node attributes
	 * 
	 * @param id
	 * @param attributes
	 * @return Status.TRUE if successful edit, or Status.ERROR if there is an error
	 */
	public Status editNode(Session session, String id, NodeAttribute [] attributes);
	
	/**
	 * Rename Node
	 * 
	 * @param id
	 * @param newName
	 * @return Status.TRUE if successful rename, or Status.ERROR if there is an error
	 */
	public Status renameNode(Session session, String id, String newName);
	
	/**
	 * Move node (change its parent)
	 * 
	 * @param id
	 * @param newParentId
	 * @return Status.TRUE if successful move, or Status.ERROR if there is an error
	 */
	public Status moveNode(Session session, String id, String newParentId);
	
	/**
	 * Remove property from node
	 * 
	 * @param id
	 * @param attrName
	 * @return Status.TRUE if successful, or Status.ERROR if there is an error
	 */
	public Status deleteAttr(Session session, String id, String attrName);
	
	/**
	 * Remove Node
	 * 
	 * @param session
	 * @param id
	 * @return Status.TRUE if successful, or Status.ERROR if there is an error
	 */
	public Status deleteNode(Session session, String id);
	
	/**
	 * Get Node Properties
	 * 
	 * @param id
	 * @return NodeAttribute array
	 */
	public NodeAttribute [] listAttr(Session session, String id) throws Exception;
	
	/**
	 * Get Node with childs to given depth,
	 * if depth is 0 then no childs are returned
	 * if depth is 1 only first childs are returned
	 * 
	 * @param session
	 * @param id
	 * @param depth
	 * @param includeAttributes
	 * 
	 * @return
	 */
	public NodeInfo getNode(Session session, String id, int depth, boolean includeAttributes) throws Exception;
}