package org.chon.cms.core.model.renderers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.core.Extension;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
/**
 * Object used in Velocity for getting extenstions
 * this object in {@link VTplNodeRenderer} will put in velocity context under $ext valirable
 * 
 * @author Jovica
 *
 */
public class ExtObj {
	private static final Log log = LogFactory.getLog(ExtObj.class);
	//Cached extenstion tpl object, per request only one call to Extenstion#getTplObject
	private Map<String, Object> _cache = new HashMap<String, Object>();
	
	private Map<String, Extension> extensions;
	private IContentNode node;
	private Response resp;
	private Request req;
	
	public ExtObj(Map<String, Extension> extensions, Request req, Response resp, IContentNode node) {
		this.extensions = extensions;
		this.req = req;
		this.resp = resp;
		this.node = node;
	}
	
	public Object get(String name) {
		if(_cache.containsKey(name)) {
			return _cache.get(name);
		}
		
		if(extensions.containsKey(name)) {
			Extension ext = extensions.get(name);
			Object obj = ext.getTplObject(req, resp, node);
			_cache.put(name, obj);
			return obj;
		} else {
			log.warn("No such extenstion " + name);
		}
		return null;
	}
}
