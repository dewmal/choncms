package org.chon.web.mpac;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModulePackage {
	private List<Module> modules;
	private Map<String, Module> modulesMap;
	private ActionHandler ajaxActions;
	private ActionHandler uploadActions;
	private Initializer initializer;
	private InitStatusErrorHandler initStatusErrorHandler;
	
	private String template = "template.html";
	private String layout = "layout.html";
	
	public InitStatusErrorHandler getInitStatusErrorHandler() {
		return initStatusErrorHandler;
	}

	public void setInitStatusErrorHandler(InitStatusErrorHandler initStatusErrorHandler) {
		this.initStatusErrorHandler = initStatusErrorHandler;
	}

	public Initializer getInitializer() {
		return initializer;
	}

	public void setInitializer(Initializer initializer) {
		this.initializer = initializer;
	}

	public ActionHandler getAjaxActions() {
		return ajaxActions;
	}

	public void setAjaxActions(ActionHandler ajaxActions) {
		this.ajaxActions = ajaxActions;
	}

	public ActionHandler getUploadActions() {
		return uploadActions;
	}

	public void setUploadActions(ActionHandler uploadActions) {
		this.uploadActions = uploadActions;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		if(modules!=null) {
			Collections.sort(modules, new Comparator<Module>() {
				public int compare(Module o1, Module o2) {
					return o1.getOrder()-o2.getOrder();
				}
			});
		}
		this.modules = modules;
		modulesMap = new HashMap<String, Module>();
		for(Module m : modules) {
			Module old = modulesMap.put(m.getName(), m);
			if(old != null) {
				System.err
						.println("WARN: modules with same name exist in pack ["
								+ old.getClass() + " and " + m.getClass() + "]");
			}
		}
	}

	public Module getModule(String moduleName) {
		return modulesMap.get(moduleName);
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
	
}