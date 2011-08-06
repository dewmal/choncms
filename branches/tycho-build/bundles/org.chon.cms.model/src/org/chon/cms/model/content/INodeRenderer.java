package org.chon.cms.model.content;

import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;

/**
 * Register services from this class in isgi context
 * Framework will look for node renderer by type descriptor
 * see /etc/types/* 
 * 				-renderer
 *  
 * @author Jovica
 *
 */
public interface INodeRenderer {
	public void render(IContentNode contentNode, Request req, Response resp, Application app, ServerInfo serverInfo);
}