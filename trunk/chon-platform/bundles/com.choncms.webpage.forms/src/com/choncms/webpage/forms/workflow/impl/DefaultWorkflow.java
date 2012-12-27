package com.choncms.webpage.forms.workflow.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.json.JSONObject;

import com.choncms.webpage.forms.workflow.Workflow;
import com.choncms.webpage.forms.workflow.WorkflowResult;
import com.choncms.webpage.forms.workflow.WorkflowResultOK;


public class DefaultWorkflow implements Workflow {
	private static final Log log = LogFactory.getLog(DefaultWorkflow.class);
	
	protected Application app;
	
	@Override
	public String getName() {
		return "DefaultWorkflow";
	}

	@Override
	public void init(Application app) {
		this.app = app;
	}

	@Override
	public WorkflowResult process(IContentNode formNode, Map<String, Object> formData, JSONObject cfg) {
		log.debug(formData);
		return WorkflowResultOK.SUCCESS;
	}

	@Override
	public String getDefaultJSONConfiguration() {
		return "{}";
	}
}
