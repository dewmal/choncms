package org.choncms.console.actions;

import java.util.HashMap;
import java.util.Map;

import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

/**
 * Render initial html console template
 * @author Jovica
 *
 */
public class MainConsoleAction extends AbstractConsoleAction {
	public MainConsoleAction(String prefix) {
		super(prefix);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		Map<String, Object> params = new HashMap<String, Object>();
		return formatTemplate(resp, "main.html", params);
	}
}