package org.chon.core.velocity;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class VTemplate {
	private static final Log log = LogFactory.getLog(VTemplate.class);
	
	private VelocityEngine velocityEngine;

	public VTemplate(URL[] urls, int modificationInterval) {
		init(urls, modificationInterval);
	}
	
	public VTemplate(URL url, int modificationInterval) {
		init(new URL[] { url}, modificationInterval);
	}
	
	public VTemplate(File tempalteDir, int modificationInterval) {
		URL url = null;
		if(tempalteDir!=null) {
			try {
				url = tempalteDir.toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		init(new URL[] { url}, modificationInterval);
	}
	
	private void init(URL [] urls, int modificationInterval) {
		log.debug("Creating velocity engine");
		velocityEngine = new VelocityEngine();
		try {
			Properties p = new Properties();
			if(urls!=null && urls.length>0) {
				p.setProperty("resource.loader", "url");
				p.setProperty("url.resource.loader.class", "org.apache.velocity.runtime.resource.loader.URLResourceLoader");
				String urlsProp = "";
				for(int i=0; i<urls.length; i++) {
					urlsProp += urls[i].toString();
					if(i<urls.length-1) {
						urlsProp += ",";
					}
				}
				p.setProperty("url.resource.loader.root", urlsProp);
			}
			if(modificationInterval>0) {
				p.setProperty("url.resource.loader.cache", "true");
				p.setProperty("url.resource.loader.modificationCheckInterval", Integer.toString(modificationInterval));
			} else {
				p.setProperty("url.resource.loader.cache", "false");
			}
			
			p.setProperty("input.encoding", "UTF-8");
			p.setProperty("output.encoding", "UTF-8");
			p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.Log4JLogSystem");
			p.setProperty("velocimacro.library", "");
			p.setProperty("runtime.log", "");
			p.setProperty("velocimacro.context.localscope", "true");
			log.info("Init velocity engine. Properties: " + p);
			velocityEngine.init(p);
		} catch (Exception e) {
			log.error("Error creating velocity engine.", e);
			throw new RuntimeException("Error creating velocity engine.", e);
		}
	}
	
	public String format(String tplName, Map<String, ?> params, Map<String, Object> ctx) {
		StringWriter sw = new StringWriter();
		format(tplName, params, ctx, sw);
		return sw.toString();
	}
	
	public void format(String tplName, Map<String, ?> params, Map<String, Object> ctx, Writer writer) {
		try {
			Template t = velocityEngine.getTemplate(tplName);
			//t.setEncoding("UTF-8");
			//EventCartridge ec = new EventCartridge();
			//ec.addInvalidReferenceEventHandler(new InvalidRefEHJ());
			Context context = new VelocityContext(params);
			//ec.attachToContext(context);
			if(ctx != null) {
				context.put("ctx", ctx);
			}
			t.merge(context , writer);
		} catch (ResourceNotFoundException e) {
			log.error("Missing " + tplName, e);
		} catch (ParseErrorException e) {
			log.error("Invalid syntax in " + tplName, e);
		} catch (Exception e) {
			log.error("Error while merge template " + tplName, e);
		}
	}
	
	public String formatStr(String str,  Map<String, ?> map, String tplName) {
		try {
			Context context = new VelocityContext(map);
			StringWriter sw = new StringWriter();
			StringReader sr = new StringReader(str);
			velocityEngine.evaluate(context, sw, tplName, sr);
			return sw.toString();
		} catch (Exception e) {
			log.error("Error while evaluating velocity template string " + str, e);
		}
		return null;
	}
	
	public boolean exists(String resourceName) {
		return velocityEngine.resourceExists(resourceName);
	}
	
}
