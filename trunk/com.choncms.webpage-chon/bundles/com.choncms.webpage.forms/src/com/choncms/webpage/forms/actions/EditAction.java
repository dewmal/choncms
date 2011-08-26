package com.choncms.webpage.forms.actions;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.json.XML;

public class EditAction extends AbstractFormsAction {

	public EditAction(String prefix, IContentNode appFormDataNode) {
		super(prefix, appFormDataNode);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		String formName = req.get("name");
		IContentNode formNode = appFormDataNode.getChild(formName);
		Map<String, Object> params = new HashMap<String, Object>();
		if (formName != null) {
			String formData = formNode.get("data");
			params.put("formName", formName);
			params.put("formData", XML.escape(formData));
			
			String successData = formNode.get("successData");
			if(successData != null) {
				params.put("successData", XML.escape(successData));
			}
			String errorData = formNode.get("errorData");
			if(errorData != null) {
				params.put("errorData", XML.escape(errorData));
			}
		}
		return resp.formatTemplate(prefix + "/editForm.html", params);
	}
}