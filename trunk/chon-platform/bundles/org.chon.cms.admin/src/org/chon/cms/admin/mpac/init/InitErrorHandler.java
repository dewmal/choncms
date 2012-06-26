package org.chon.cms.admin.mpac.init;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
		} else if(status == InitStatusInfo.ACCESS_DENIED) {
			try {
				resp.getServletResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			strOut = "Access Denied";
		} else {
			strOut = this.getClass().getName() + " - Unhandled init status";
			try {
				resp.getServletResponse().sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//default content type = text/html
		resp.getServletResponse().setContentType("text/html");
		
		resp.getOut().write(strOut);
	}

}
