package org.chon.cms.admin.mpac.init;

import java.util.HashMap;
import java.util.Map;

import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.InitStatusErrorHandler;
import org.chon.web.mpac.InitStatusInfo;


public class InitErrorHandler implements InitStatusErrorHandler {

	public void handleStatusError(InitStatusInfo status, Application app,
			Request req, Response resp) {
		String strOut = null;
		Map<String, Object> map = new HashMap<String, Object>();
		if(status == InitStatusInfo.NOT_AUTH) {
			map.put("loginMsg", " ");
			map.put("user", " ");
			resp.getTemplateContext().put("app", app);
			strOut = resp.formatTemplate("admin/login.html", map);
		} else if(status == InitStatusInfo.LOGIN_ERROR) {
			map.put("loginMsg", "Invalid username or password");
			map.put("user", req.get("user"));
			strOut = resp.formatTemplate("admin/login.html", map);
		} else {
			strOut = "Should never get here";
		}
		resp.getOut().write(strOut);
	}

}
