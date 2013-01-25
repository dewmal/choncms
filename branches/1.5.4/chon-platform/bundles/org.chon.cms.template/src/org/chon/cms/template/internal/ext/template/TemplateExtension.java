package org.chon.cms.template.internal.ext.template;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.template.internal.TemplateMgr;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class TemplateExtension implements Extension {
	private static final Log log = LogFactory.getLog(TemplateExtension.class);
	
	public TemplateExtension(JCRApplication app) {
		//TODO: Better templating mechanzm
		//TemplateMgr.init(app);
		//MenuItem mi = new MenuItem("Templates", "admin/templates.do");
		//app.getAdminRootMenuItem().add(mi);
	}

	private Map<String, Action> actions = new HashMap<String, Action>();
	@Override
	public Map<String, Action> getAdminActons() {
		actions.put("templates", new Action() {
			@Override
			public String run(Application app, Request req, Response resp) {
				
				return resp.formatTemplate("ext/template/admin/template.html", null);
			}
		});
		return actions;
	}

	private Map<String, Action> ajaxActions = new HashMap<String, Action>();
	@Override
	public Map<String, Action> getAjaxActons() {
		ajaxActions.put("tpl_get_current", new Action() {
			
			@Override
			public String run(Application app, Request req, Response resp) {
				String path = req.get("node", "/");
				File dir = new File(TemplateMgr.getTemplatesDir(), TemplateMgr.getTemplate() + "" + path);
				JSONArray arr = new JSONArray();
				File[] ls = dir.listFiles();
				for(File f : ls) {
					if(f.isHidden()) continue;
					JSONObject n = new JSONObject();
					try {
						boolean leaf = !f.isDirectory(); 
						n.put("text", f.getName());
						n.put("id", path+f.getName()+(leaf?"":"/"));
						n.put("leaf", leaf);
						if(leaf) {
							n.put("cls", FilenameUtils.getExtension(f.getName()));
						}
						if(path.equals("/")) {
							n.put("expanded", true);
						}
						arr.put(n);
					} catch (JSONException e) {
						log.error("Error creattng json from file", e);
					}
				}
				return arr.toString();
			}
		});
		ajaxActions.put("tpl_get_current_file", new Action() {
			
			@Override
			public String run(Application app, Request req, Response resp) {
				String path = req.get("file", "/");
				File file = new File(TemplateMgr.getTemplatesDir(), TemplateMgr.getTemplate() + "" + path);
				
				JSONObject n = new JSONObject();
				try {
					n.put("content", FileUtils.readFileToString(file, "UTF-8"));
				} catch (JSONException e) {
					log.error("Error creattng json for file", e);
				} catch (IOException e) {
					log.error(e);
				}
				return n.toString();
			}
		});
		return ajaxActions;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		return null;
	}
}
