package org.choncms.console.actions;

import java.util.Map;

import org.chon.web.api.Response;
import org.chon.web.mpac.Action;

public abstract class AbstractConsoleAction implements Action {
	private String prefix;
	public AbstractConsoleAction(String prefix) {
		this.prefix = prefix;
	}
	
	protected String formatTemplate(Response resp, String tpl,
			Map<String, Object> params) {
		params.put("prefix", prefix);
		return resp.formatTemplate(getTpl(tpl), params);
	}
	
	protected String getTpl(String tpl) {
		return prefix + "/" + tpl;
	}
}
