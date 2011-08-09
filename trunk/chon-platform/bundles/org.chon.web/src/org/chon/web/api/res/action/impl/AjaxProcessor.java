package org.chon.web.api.res.action.impl;

import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;
import org.chon.web.api.res.ActionResource;
import org.chon.web.api.res.action.ActionUtils;
import org.chon.web.api.res.action.Processor;
import org.chon.web.mpac.ActionHandler;

public class AjaxProcessor implements Processor {
	
	@Override
	public void process(ActionResource action) {
		ActionHandler actions = action.getModulePackage().getAjaxActions();
		ServerInfo si = action.getServerInfo();
		Application app = si.getApplication();
		Request req = si.getReq();
		Response resp = si.getResp();
		ActionUtils.outAction(app, req, resp, actions);
	}
}
