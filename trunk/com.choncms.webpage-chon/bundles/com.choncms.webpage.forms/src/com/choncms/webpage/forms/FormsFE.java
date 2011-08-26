package com.choncms.webpage.forms;

import java.util.HashMap;
import java.util.Map;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

public class FormsFE {

	private IContentNode appFormDataNode;
	private Response resp;
	private String prefix;
	private Request req;
	private FormsExtension formsExtension;

	public FormsFE(String prefix, Request req, Response resp, IContentNode appFormDataNode, FormsExtension formsExtension) {
		this.formsExtension = formsExtension;
		this.req = req;
		this.prefix = prefix;
		this.resp = resp;
		this.appFormDataNode = appFormDataNode;
	}
	
	public String getFormData(String formName) {
		IContentNode formNode = appFormDataNode.getChild(formName);
		if(formNode == null) {
			return "Error: form with name " + formName + " does not exists";
		}
		return formNode.get("data");
	}
	
	public String render(String formName) {
		String sf_val = req.get("__submit_form");
		
		//We have form submit
		if("true".equals(sf_val) && formName.equals(req.get("__formName"))) {
			IContentNode formNode = appFormDataNode.getChild(formName);
			if(formNode == null) {
				return "ERROR: invalid form post";
			}
			formsExtension.processFormSubmition(formNode, req);
			return formNode.get("successData");
		}
		
		//Render initial form
		String formData = getFormData(formName);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formName", formName);
		params.put("formData", formData);
		return resp.formatTemplate(prefix + "/form.html", params);
	}
}
