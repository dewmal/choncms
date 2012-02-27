package org.choncms.console.actions;

import org.chon.cms.core.auth.User;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.choncms.console.ConsoleSession;

/**
 * Called on init on console, creates console session
 * 
 * @author Jovica
 *
 */
public class InitAjaxAction extends AbstractConsoleAction {
	public InitAjaxAction(String prefix) {
		super(prefix);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		ConsoleSession cs = (ConsoleSession) req.attr(ConsoleSession.KEY);
		if (cs == null) {
			cs = new ConsoleSession();
			User user = (User) req.getUser();
			cs.setPath("/home/" + user.getName());
			req.setSessAttr(ConsoleSession.KEY, cs);
		}
		return cs.toJSONStr();
	}
}