package org.chon.cms.admin.mpac;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.chon.web.mpac.Module;
import org.chon.web.mpac.ModulePackage;


public class MainAdminModule implements Module {

	private final class Action_browse implements Action {
		@Override
		public String run(Application app, Request req, Response resp) {
			return resp.formatTemplate("admin/fragments/tree/tree.html", null);
		}
	}

	private final class Action_upload implements Action {
		@Override
		public String run(Application app, Request req, Response resp) {
			return "upload";
		}
	}

	private final class Action_listActions implements Action {
		@Override
		public String run(Application _app, Request req, Response resp) {
			JCRApplication app = (JCRApplication) _app;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("actions", MainAdminModule.this.getActions().keySet());
			params.put("ajaxActions", MainAdminModule.this.modulePack.getAjaxActions().getActions().keySet());
			params.put("extensions", app.getExts().keySet());
			//params.put("sessionPoolSize", Repo.getSessionPoolSize());
			//params.put("busySessions", Repo.getBusySessions());
			return resp.formatTemplate("admin/fragments/listActions.html", params);
		}
	}

	private final class Action_tree implements Action {
		@Override
		public String run(Application app, Request req, Response resp) {
			Map<String, Object> params = new HashMap<String, Object>();
			return resp.formatTemplate("admin/fragments/tree/tree.html", params);
		}
	}

	private final class Action_query implements Action {
		@Override
		public String run(Application app, Request req, Response resp) {
//			String q = req.get("q");
//			List<IContentNode> results = null; 
//			String error = null;
//			
//			if (q != null) {
//				try {
//					Facade facade = (Facade) req.attr(Facade.KEY);
//					QueryResult r = facade.query(q, Query.JCR_SQL2, null, null);
//					NodeIterator ni = r.getNodes();
//					results = new ArrayList<ContentNode>();
//					while (ni.hasNext()) {
//						results.add(new ContentNode(ni.nextNode()));
//					}
//				} catch (RepositoryException e) {
//					// TODO Auto-generated catch block
//					// e.printStackTrace();
//					error = e.getMessage();
//				}
//			}
//			
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("q", q);
//			params.put("results", results);
//			params.put("error", error);
//			return resp.formatTemplate("admin/fragments/query.html", params);
			return "TODO";
		}
	}
	
	private final class Action_createNode implements Action {
		@Override
		public String run(Application app, Request req, Response resp) {
			//Facade facade = (Facade) req.attr(Facade.KEY);
//			if(req.get("save") != null) {
//				//create node command
//				String path = req.get("path");
//				String pid = req.get("pid");
//				try {
//					Node parent = facade.getNodeByIdentifier(pid);
//					//Node node =
//					parent.addNode(path, req.get("type"));
//					facade.commitSessionChanges();
//					return MainAdminModule.this.getDefaulAction().run(app, req, resp);
//				} catch (Exception e) {
//					e.printStackTrace();
//					return e.getMessage();
//				}				
//			} else {
//				//create node page
//				String pid = req.get("pid");
//				Node parrent = null;
//				if(pid!=null) {
//					parrent = facade.getNodeByIdentifier(pid);
//				}
//				if(parrent == null) {
//					parrent = facade.getRootNode();
//				}
//				
				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("parent", parrent);
				return resp.formatTemplate("admin/fragments/createNode.html", params);
//			}
		}
	}

	private HashMap<String, Action> actions;
	private JCRApplication app;
	private ModulePackage modulePack;
	
	public MainAdminModule(JCRApplication app, ModulePackage modulePack) {
		this.app = app;
		this.modulePack = modulePack;
		actions = new HashMap<String, Action>();
		actions.put("createNode", new Action_createNode());
		actions.put("upload", new Action_upload());
		actions.put("query", new Action_query());
		actions.put("listActions", new Action_listActions());
		actions.put("tree", new Action_tree());
		actions.put("browse", new Action_browse());
	}
	
	@Override
	public String getLayout() {
		return "main";
	}

	@Override
	public String getName() {
		return "mainAdminModule";
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Map<String, Action> getActions() {
		Map<String, Extension> exts = app.getExts();
		Iterator<String> keys = exts.keySet().iterator();
		while(keys.hasNext()) {
			String k = keys.next();
			Extension ext = exts.get(k);
			Map<String, Action> adminActons = ext.getAdminActons();
			if(adminActons != null) {
				actions.putAll(adminActons);
			}
		}
		return actions;
	}

	@Override
	public Action getDefaulAction() {
		return new Action_tree();
	}
}
