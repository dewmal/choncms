package org.chon.cms.light.mvc.result;

import org.chon.cms.light.mvc.AbstractAction;


public class ActionResult extends Result {
	
	private Class<? extends AbstractAction> action;
	private Object model;
	
	public ActionResult(Class<? extends AbstractAction> action, Object model) {
		this.action = action;
		this.model = model;
	}

	public Class<? extends AbstractAction> getAction() {
		return action;
	}

	public Object getModel() {
		return model;
	}
	
}
