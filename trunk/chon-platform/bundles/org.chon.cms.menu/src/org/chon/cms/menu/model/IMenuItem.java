package org.chon.cms.menu.model;

import java.util.List;

/**
 * Menu Item interface used in velocity
 * 
 * @author Jovica.Veljanovski
 *
 */
public interface IMenuItem {
	/**
	 * get link for this menu
	 * absolute link to siteUrl
	 * isInternalLink flag determinates if this 
	 * should be rendered with 
	 * 
	 * @return
	 */
	public String getLink();
	
	/**
	 * Get menu title
	 * @return
	 */
	public String getTitle();
	
	/**
	 * 
	 * @return
	 */
	public boolean isInternalLink();
	
	/**
	 * Get full link (including siteUrl)
	 * @return
	 */
	public String getFullLink();
	
	/**
	 * get only one child
	 * 
	 * @param name
	 * @return
	 */
	public IMenuItem getChild(String name);
	
	/**
	 * get all childs
	 *  implementations should cache child items
	 *  ui may call this as 
	 *  	$mi.childs.size()
	 *  	$mi.childs.get(0)
	 * @return
	 */
	public List<IMenuItem> getChilds();
	
	/**
	 * get menu childs at path
	 * 
	 * @param path
	 * @return
	 */
	public List<IMenuItem> getChilds(String path);
	
	/**
	 * path to this menu item relative to root menu item
	 * @return
	 */
	public String getPath();
	
	/**
	 * get parent menu item or null if this is root item
	 * @return
	 */
	public IMenuItem getParent();
	
	/**
	 * get menu item name (same as IContentNode#getName)
	 * @return
	 */
	public String getName();
	
	/**
	 * Get property order
	 * @return
	 */
	public int getOrder();
}