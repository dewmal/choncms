package com.choncms.webpage.forms;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class FormsFE {

	private IContentNode appFormDataNode;

	public FormsFE(Request req, Response resp, IContentNode node, IContentNode appFormDataNode) {
		this.appFormDataNode = appFormDataNode;
	}
	
	public String getFormData(String formName) {
		IContentNode formNode = appFormDataNode.getChild(formName);
		if(formNode == null) {
			return "Error: form with name " + formName + " does not exists";
		}
		return formNode.get("data");
	}

}
