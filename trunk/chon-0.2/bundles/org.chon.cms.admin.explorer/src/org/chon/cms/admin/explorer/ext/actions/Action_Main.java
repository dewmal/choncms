package org.chon.cms.admin.explorer.ext.actions;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.admin.explorer.ext.ExplorerExtension;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public class Action_Main implements Action {
	private ExplorerExtension explorerExtension;

	public Action_Main(ExplorerExtension explorerExtension) {
		this.explorerExtension = explorerExtension;
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("scrips", explorerExtension.getJsIncList());
		return resp.formatTemplate("explorer/main.html", params);
	}
}