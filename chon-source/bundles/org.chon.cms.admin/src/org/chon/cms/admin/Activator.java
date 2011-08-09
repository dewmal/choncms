package org.chon.cms.admin;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.chon.cms.admin.mpac.AdminAjax;
import org.chon.cms.admin.mpac.AdminMenuModule;
import org.chon.cms.admin.mpac.AdminUpload;
import org.chon.cms.admin.mpac.MainAdminModule;
import org.chon.cms.admin.mpac.init.Init;
import org.chon.cms.admin.mpac.init.InitErrorHandler;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.web.mpac.Module;
import org.chon.web.mpac.ModulePackage;
import org.json.JSONObject;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected String getName() {
		return "org.chon.cms.admin";
	}

	@Override
	protected void registerExtensions(JCRApplication app) {
		regModulePack(app);
		
	}

	private void regModulePack(JCRApplication app) {
		JSONObject config = this.getConfig();
		ModulePackage modulePack = new ModulePackage();
		modulePack.setInitializer(new Init(this.getBundleContext()));
		modulePack.setInitStatusErrorHandler(new InitErrorHandler());
		modulePack.setTemplate(config.optString("template", "admin/template.html"));
		modulePack.setLayout(config.optString("layout", "admin/layout.html"));
		modulePack.setAjaxActions(new AdminAjax(app));
		modulePack.setUploadActions(new AdminUpload());
		List<Module> modules = new ArrayList<Module>();
		modules.add(new AdminMenuModule(app));
		modules.add(new MainAdminModule(app, modulePack));
		modulePack.setModules(modules);
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("path", config.optString("listenOn", "admin"));
		this.getBundleContext().registerService(ModulePackage.class.getName(), modulePack, props);
	}
}
