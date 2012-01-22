package com.choncms.webpage.forms.actions;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.mpac.Action;

public abstract class AbstractFormsAction implements Action {
	protected String prefix;
	protected IContentNode appFormDataNode;
	public AbstractFormsAction(String prefix, IContentNode appFormDataNode) {
		this.prefix = prefix;
		this.appFormDataNode = appFormDataNode;
	}
}
