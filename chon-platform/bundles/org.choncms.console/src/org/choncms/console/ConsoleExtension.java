package org.choncms.console;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.choncms.console.actions.EvalAjaxAction;
import org.choncms.console.actions.InitAjaxAction;
import org.choncms.console.actions.MainConsoleAction;

public class ConsoleExtension implements Extension {
	
	private Map<String, Action> actions = new HashMap<String, Action>();
	private Map<String, Action> ajaxActions = new HashMap<String, Action>();
	
	public ConsoleExtension(String prefix, JCRApplication app) {
		actions.put(prefix + ".main", new MainConsoleAction(prefix));
		ajaxActions.put(prefix + ".eval", new EvalAjaxAction(prefix));
		ajaxActions.put(prefix + ".init", new InitAjaxAction(prefix));
	}


	@Override
	public Map<String, Action> getAdminActons() {
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		return ajaxActions;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
