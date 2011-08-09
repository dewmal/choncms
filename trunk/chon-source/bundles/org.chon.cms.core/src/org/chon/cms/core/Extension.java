package org.chon.cms.core;

import java.util.Map;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;


public interface Extension {
	
	/**
	 * Actions in admin part
	 * @return
	 */
	public Map<String, Action> getAdminActons();
	
	/**
	 * ajax actions in admin part
	 * @return
	 */
	public Map<String, Action> getAjaxActons();
	
	/**
	 * The Object that will be put in $ext map in output tempates
	 * @param node 
	 * @param resp 
	 * @return
	 */
	public Object getTplObject(Request req, Response resp, IContentNode node);
}
