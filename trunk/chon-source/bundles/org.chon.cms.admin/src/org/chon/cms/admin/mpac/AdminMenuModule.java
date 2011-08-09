package org.chon.cms.admin.mpac;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.chon.cms.core.JCRApplication;
import org.chon.common.configuration.ConfigurationFactory;
import org.chon.core.velocity.JGRAVelocity;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.chon.web.mpac.Module;
import org.json.JSONException;
import org.json.JSONObject;



public class AdminMenuModule implements Module {

	private Object menu;

	public AdminMenuModule(JCRApplication app) {
		
		//app.getAdminRootMenuItem().add(new MenuItem("Site Home", ""));
		
		//MenuItem adminMenuItam = new MenuItem("Developer Tools", null);
		//adminMenuItam.setType(MenuItem.MI_TYPE_GROUP);
		//app.getAdminRootMenuItem().add(adminMenuItam);
		
		//adminMenuItam.add(new MenuItem("Browse Repository", "admin/browse.do"));
		//adminMenuItam.add(new MenuItem("List Actions", "admin/listActions.do"));
		//adminMenuItam.add(new MenuItem("Query Repo", "admin/query.do"));
		
	}
	
	@Override
	public String getLayout() {
		return "menu";
	}

	@Override
	public String getName() {
		return "menuModule";
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Map<String, Action> getActions() {
		return null;
	}

	@Override
	public Action getDefaulAction() {
		return new Action() {
			@Override
			public String run(Application _app, Request req, Response resp) {
				//JCRApplicationImpl app = (JCRApplicationImpl) _app;
				Map<String, Object> params = new HashMap<String, Object>();
				
				Object menu = getMenuJSONArr();
				params.put("menu", menu);
				//params.put("menu", app.getAdminRootMenuItem());
				return resp.formatTemplate("admin/menu.html", params);
			}
		};
	}

	protected Object getMenuJSONArr() {
		if(menu!=null) {
			return menu;
		}
		
		try {
			JSONObject menu = ConfigurationFactory.getConfigSonInstance().getConfig("admin-menu");
			this.menu = JGRAVelocity.prepareJSONForVelocity(menu.getJSONArray("items"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return menu;
	}
}
