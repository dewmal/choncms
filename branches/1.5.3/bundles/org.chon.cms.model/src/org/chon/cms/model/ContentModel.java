package org.chon.cms.model;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.QueryResult;

import org.chon.cms.model.content.IContentNode;


/**
 * <pre>
 *  Instance for accessing nodes in system
 *  Each user (even anonymous will have ContentModel in http session)
 *  
 * 	TODO: ACL, check jackrabbit for ACL impl
 * 
 * Structure of the repository should be the following:
 * 	/jcr:system/
 * 		* jcr specific node
 * 
 * 	/www/
 * 		* public content (accessible from anonymous unless node renderer restrict it
 * 
 * 	/home/
 * 		* users folders
 * 
 * 	/root/
 * 		root user dir
 * 
 *	/usr/
 *		/local/
 *		*plugins specifc dirs
 *  
 * 	/etc/
 * 		host.config
 * 			-siteUrl
 * 			
 * 		/types/
 * 			* node type configuration
 * 			eg. 
 * 			/html/
 * 				-renderer: 'org.chon.web.jcr.cms.view.internal.HtmlContentNodeRenderer'
 * 				-contentNode: 'org.chon.web.jcr.cms.view.internal.model.HtmlContentNode'
 * 				-bubbleToParent: true
 * 				/default-properties/
 * 					template: 'pages/view.html',
 * 				 
 * 		*configuration files
 * 
 * 	/var/
 * 		/log/
 * 		/stats/
 * 		/cache/
 * 		* all varibale content
 * 
 *	/tmp/
 *		/upload/ (temp upload dir)
 *		* temporary nodes will be deleted on system reboot
 * </pre> 
 * @author Jovica
 */
public interface ContentModel {
	public static final String KEY = ContentModel.class.getName();
	
	/**
	 * get node at abs path
	 * 
	 * @param path
	 * @return
	 */
	public IContentNode getContentNode(String path);
	
	/**
	 * Basicly convert JCR node to content node,
	 * get content node implementation from registry and create it
	 * 
	 * @param node
	 * @return
	 */
	public IContentNode getContentNode(Node node);
	
	/**
	 * get /www/ node
	 * @return
	 */
	public IContentNode getPublicNode();
	
	/**
	 * get node relative to /www/ node
	 * 
	 * @param path
	 * @return
	 */
	public IContentNode getPublicNode(String path);
	
	
	/**
	 * get reposotiry temp node /tmp/
	 * @param path
	 * @return
	 */
	public IContentNode getTmpNode(String path);
	
	/**
	 * Get user info node under /etc/passwd
	 * 
	 * returns null if there is no record in /etc/passwd
	 * 
	 * @param username
	 * @return
	 */
	public IContentNode getUser();
	
	/**
	 * get home directory for logged in user if exists
	 * if user is root, returned /root node
	 * otherwise /home/<current_user> node is returned
	 * 
	 * returns null if directory does not exists
	 */
	public IContentNode getUserHome();
	
	
	
	/**
	 * Get /etc/ node
	 * @param username
	 * @return
	 */
	public IContentNode getConfigNode();
	
	/**
	 * Get /etc/ node child
	 * @param path
	 * @return
	 */
	public IContentNode getConfigNode(String path);
	
	/**
	 * Get /etc/ node child
	 * create if not exists
	 * 
	 * @param path
	 * @return
	 */
	public IContentNode getConfigNode(String path, boolean createIfNotExists);
	
	/**
	 *  /usr/local
	 *  
	 * @return
	 */
	public IContentNode getAppsNode();
	
	/**
	 * get child under /usr/local/
	 * 
	 * @param path
	 * @return
	 */
	public IContentNode getAppsNode(String path);
	
	/**
	 *  /usr/local/etc
	 *  
	 * @return
	 */
	public IContentNode getAppsConfigNode();
	
	/**
	 * get child node under /usr/local/etc
	 *  
	 * @param app
	 * @return
	 */
	public IContentNode getAppsConfigNode(String app);
	
	/**
	 * get child node under /usr/local/etc,
	 * use second argument, set to true to create if not exists
	 * 
	 * @param app
	 * @param createIfNotExists
	 * @return
	 */
	public IContentNode getAppsConfigNode(String app, boolean createIfNotExists);
	
	/**
	 * Gets type descriptor from /etc/types/
	 * 
	 * @param type
	 */
	public IContentNode getTypeDesc(String type);


	/**
	 * Returns node type, type model is not used fron jcr instead 
	 * property type is searched in the node
	 * (if not exists then jcr type is returned eg, nt:file)
	 * 
	 * @param node
	 * @return
	 */
	public String getNodeType(Node node);
	
	/**
	 * Query repository, if start and limit are null - all nodes will be returned
	 * 
	 * @param q
	 * @param type
	 * @param start
	 * @param limit
	 * @return
	 * @throws RepositoryException
	 */
	public QueryResult query(String q, String type, Integer start, Integer limit) throws RepositoryException;
	
	/**
	 * get jcr session
	 * @return
	 */
	public Session getSession();
}
