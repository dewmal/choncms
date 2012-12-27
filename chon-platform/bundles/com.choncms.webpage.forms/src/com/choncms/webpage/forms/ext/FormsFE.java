package com.choncms.webpage.forms.ext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.PropertyType;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;

import com.choncms.webpage.forms.ExtenstionUtils;
import com.choncms.webpage.forms.workflow.WorkflowResult;

public class FormsFE {

	private IContentNode appFormDataNode;
	private Response resp;
	private String prefix;
	private Request req;
	private FormsExtension formsExtension;
	private JCRApplication app;

	public FormsFE(JCRApplication app, String prefix, Request req, Response resp, IContentNode appFormDataNode, FormsExtension formsExtension) {
		this.app = app;
		this.formsExtension = formsExtension;
		this.req = req;
		this.prefix = prefix;
		this.resp = resp;
		this.appFormDataNode = appFormDataNode;
		init();
	}
	
	private void init() {
		@SuppressWarnings("unchecked")
		List<String> scrips = (List<String>) resp.getTemplateContext().get("head:scripts");
		//@SuppressWarnings("unchecked")
		//List<String> css = (List<String>) resp.getTemplateContext().get("head:css");
		
		scrips.add("com.choncms.webpage.forms/form.validation.js");
		scrips.add(formsExtension.getAjaxFormSubmitNode() + "?init=js");
	}
	
	public String getFormData(String formName) {
		IContentNode formNode = appFormDataNode.getChild(formName);
		if(formNode == null) {
			return "Error: form with name " + formName + " does not exists";
		}
		return formNode.get("data");
	}
	
	private Boolean getIsFileUploadEnabled(String formName) {
		IContentNode formNode = appFormDataNode.getChild(formName);
		if(formNode != null) {
			return (Boolean) formNode.getPropertyAs("isFileUploadEnabled", PropertyType.BOOLEAN);
		}
		return null;
	}
	
	public String render(String formName) throws Exception {
		String sf_val = req.get("__submit_form");
		
		//We have form submit
		if("true".equals(sf_val) && formName.equals(req.get("__formName"))) {
			IContentNode formNode = appFormDataNode.getChild(formName);
			return getFormSubmissionResponse(app, req, formNode);
		}
		
		//Render initial form
		String formData = getFormData(formName);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formName", formName);
		params.put("formData", formData);
		params.put("isFileUploadEnabled", getIsFileUploadEnabled(formName));
		params.put("formId", "f_" + Math.round(Math.random()*1000) + "" + System.currentTimeMillis());
		
		//make sure jquery is there
		ExtenstionUtils.ensureExtenstionVisible("jquery", resp);
		
		return resp.formatTemplate(prefix + "/form.html", params);
	}


	@SuppressWarnings("unchecked")
	public static String getFormSubmissionResponse(Application app, Request req, IContentNode formNode) {
		if(formNode == null) {
			return "ERROR: invalid form post";
		}
		
		Map<String, Object> formsData = FormsExtension.processFormSubmition(formNode, req);
		WorkflowResult workflowResult = (WorkflowResult) ((Map<String, Object>)formsData.get("ctx")).get("workflowResult");
		
		
		//TODO: make better handling for form submission here,
		// after workflow had been executed we may show vast results based on WorkflowResult
		
		String returnFormValue;
		if(workflowResult.hasError()) {
			returnFormValue = formNode.get("errorData");
		} else {
			returnFormValue = formNode.get("successData");
		}
		return app.getTemplate().formatStr(returnFormValue, formsData, formNode.getName() + "#form-dyn-output-template");
	}
}
