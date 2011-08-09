package org.chon.cms.admin.explorer.ext.actions;

import org.chon.cms.admin.explorer.ext.ExplorerExtension;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONException;
import org.json.JSONObject;

public class Action_Init implements Action {
	private ExplorerExtension explorerExtension;

	public Action_Init(ExplorerExtension explorerExtension) {
		this.explorerExtension = explorerExtension;
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		String cfgStr = req.get("config");
		try {
			JSONObject o = new JSONObject(cfgStr);
			req.setSessAttr(ExplorerExtension.SESSION_KEY, o);
			JSONObject rv = new JSONObject();
			rv.put("status", "OK");
			rv.put("uiConf", o);
			rv.put("config", explorerExtension.getConfig());
			return rv.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{status: 'Error'}";
	}
}