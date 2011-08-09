package org.chon.web.api.res.action;

import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.chon.web.mpac.ActionHandler;

public class ActionUtils {
	public static boolean outAction(Application application, Request req, Response resp, ActionHandler handler) {
		if(handler != null) {
			String actionName = req.getAction();
			Action action = null;
			if(handler.getActions() != null) {
				action = handler.getActions().get(actionName);
			}
			if(action == null) {
				action = handler.getDefaulAction();
			}
			if(action == null) {
				System.err.println("Handler does not have default action!");
				return false;
			}
			resp.getOut().write(action.run(application, req, resp));
			return true;
		}
		return false;
	}
}