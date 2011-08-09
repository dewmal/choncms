package org.chon.web.api;

import java.util.Properties;

import org.chon.core.velocity.VTemplate;
import org.chon.web.api.res.ActionResource;
import org.chon.web.api.res.ImageResource;
import org.chon.web.api.res.PluginResource;
import org.chon.web.api.res.StaticResource;


public interface Application {
	public static final String TITLE 					= "bundlo.title";
	public static final String DESCRIPTION	 			= "bundlo.description";
	public static final String IS_DEBUG_ENABLED 		= "bundlo.is_debug_enabled";
	public static final String TEMP_UPLOAD_DIR	 		= "bundlo.temp_upload_dir";
	public static final String PRIORITY			 		= "bundlo.app.priority";
	
	/**
	 * Hints for engine 
	 * - app priority: if we have more than one application registration
	 * - for system implemented resources, eg ImageResource: config
	 * 
	 *  system implemented resources will ask for this properties when do internal process
	 * 
	 * @return application properties
	 */
	public Properties getAppProperties();
	
	
	/**
	 * <pre>
	 *	Return resource for the request or null if can't find resource for the request
	 *	if returned resource is not null engine will call resource.process(ServerInfo)
	 *
	 * pre process advisor if this application can handle the request
	 * Note: aldought in preprocess req is passed, not all properties will be available
	 * for example files will not yet be uploaded in this stage,
	 * app must tell engine if it can handle req by returning not null resource
	 * and after that engine will parse all info (upload files) and continue processing
	 * by calling process on the returned resource
	 * Resource then is responsible for writing the response based on ServerInfo structore
	 * passed to its process method
	 * 
	 * if return resource is null
	 *  engine will ask other registered applications for resource
	 *  
	 * if return is not null
	 * 	then engine will try to process the resource
	 * 
	 *		if resource is ActionResource @see {@link ActionResource} 
	 *			( *.do, *.ajax, *.upload)
	 *			- app is responsibe for resolving the action (for creating action params)
	 *				can use some helpers inside engine, ActionResource.createFromRequest(req)
	 *
	 * 			- action processor is called. 
	 *  		- the moduleProcessor inside action is executed
	 *  
	 * 		if resource is StaticResource
	 * 			- simple output is rendered to response
	 * 
	 * 		if resource is ImageResource
	 *  		- image can be rendered in different dimensions then original
	 *  
	 * 		if resource is PluginResource
	 *  		- app will give the plugin to render the response
	 *   	
	 * @param req
	 * @return {@link Resource}
	 * See system implemented resources:
	 * 	{@link ActionResource}
	 * 	{@link ImageResource}
	 * 	{@link StaticResource}
	 * 	{@link PluginResource}	 
	 */
	public Resource getResource(Request req);
	
	
	/**
	 * Velocity Template, mostly used in ModulePackage .do processor 
	 * 
	 * TODO: this is outsice dependency from velocity (org.chon.core.velocity)
	 * 	this might be changed
	 * @return {@link VTemplate} object
	 */
	public VTemplate getTemplate();


	/**
	 * Before each request handler
	 * @param r
	 * @param si
	 */
	public void prepareRequest(Resource r, ServerInfo si);

	/**
	 * Exception handler
	 * @param e
	 * @param r
	 * @param si
	 */
	public void handleException(Exception e, Resource r, ServerInfo si);

	/**
	 * After each request
	 * @param r
	 * @param si
	 */
	public void finalizeRequest(Resource r, ServerInfo si);
}
