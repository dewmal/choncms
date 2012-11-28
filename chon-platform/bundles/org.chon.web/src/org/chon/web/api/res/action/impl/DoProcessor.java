package org.chon.web.api.res.action.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;
import org.chon.web.api.res.ActionResource;
import org.chon.web.api.res.action.ActionUtils;
import org.chon.web.api.res.action.Processor;
import org.chon.web.mpac.Action;
import org.chon.web.mpac.Module;
import org.chon.web.mpac.ModulePackage;


public class DoProcessor implements Processor {

	@Override
	public void process(ActionResource action) {
		ServerInfo si = action.getServerInfo();
		Application app = si.getApplication();
		Request req = si.getReq();
		Response resp = si.getResp();
		
		ModulePackage modulePackage = action.getModulePackage();
		
		String singleModule = req.get("module");

		if (singleModule != null) {
			Module m = modulePackage.getModule(singleModule);
			if (m != null) {
				boolean ok = ActionUtils.outAction(app, req, resp, m);
				// output by single module, eg rss
				if (ok)
					return;
			}
		}

		List<Module> modules = modulePackage.getModules();
		Iterator<Module> it = modules.iterator();
		Map<String, String> contentsMap = new HashMap<String, String>();
		resp.setContentsMap(contentsMap);
		while (it.hasNext()) {
			Module module = it.next();
			// long st = System.currentTimeMillis();
			String content = processModule(action.getAction(), app, module, req, resp);
			if(req.getServletRequset().getAttribute(ActionResource.MANUAL_SERVLET_WRITE) != null) {
				// action that controls servlet response ... 
				return;
			}
			// System.out.println("\t" + module.getName() + " - " +
			// (System.currentTimeMillis()-st));
			
			String oldVal = contentsMap.put(module.getName(), content);
			if (oldVal != null) {
				System.err.println("Wanring overriding content: "
						+ module.getName() + " " + module.getClass());
			}
		}

		doLayout(modulePackage, contentsMap, req, resp);
	}

	private void doLayout(ModulePackage mp, Map<String, String> contentsMap,
			Request req, Response resp) {
		//default content type = text/html
		resp.getServletResponse().setContentType("text/html");
		
		Map<String, StringBuffer> layoutMap = new HashMap<String, StringBuffer>();
		Iterator<String> it = contentsMap.keySet().iterator();
		while (it.hasNext()) {
			String moduleName = it.next();
			String content = contentsMap.get(moduleName);
			Module module = mp.getModule(moduleName);
			String layoutKey = module.getLayout();
			StringBuffer buffer;
			if (layoutMap.containsKey(layoutKey)) {
				buffer = layoutMap.get(layoutKey);
			} else {
				buffer = new StringBuffer();
				layoutMap.put(layoutKey, buffer);
			}
			buffer.append(content);
		}

		String body = resp.formatTemplate(mp.getLayout(), layoutMap);
		Map<String, String> m = new HashMap<String, String>();
		m.put("body", body);
		m.put("pageData", resp.getPageData().toString());
		m.put("contextPath", req.getServletRequset().getContextPath());
		m.put("path", req.getPath());
		String output = resp.formatTemplate(mp.getTemplate(), m);
		resp.getOut().write(output);
	}

	private String processModule(String action, Application app, Module module, Request req,
			Response resp) {
		Map<String, Action> moduleActions = module.getActions();
		if (moduleActions != null && moduleActions.containsKey(action)) {
			return moduleActions.get(action).run(app, req, resp);
		}
		Action defAction = module.getDefaulAction();
		if (defAction == null) {
			System.err.println("WARN: module " + module.getName()
					+ " does not have default action.");
			return "";
		}
		return defAction.run(app, req, resp);
	}
}