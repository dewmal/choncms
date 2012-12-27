package com.choncms.webpage.forms.workflow.impl;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.json.JSONObject;

import com.choncms.webpage.forms.workflow.Workflow;
import com.choncms.webpage.forms.workflow.WorkflowResult;
import com.choncms.webpage.forms.workflow.WorkflowResultError;
import com.choncms.webpage.forms.workflow.WorkflowResultOK;


public class SimpleSaveWorkflow implements Workflow {
	private static final Log log = LogFactory.getLog(SimpleSaveWorkflow.class);
	
	@Override
	public String getName() {
		return "SimpleSaveWorkflow";
	}

	@Override
	public WorkflowResult process(IContentNode formNode, Map<String, Object> formData, JSONObject cfg) {
		try {
			Node sf = formNode.getNode().addNode("" + System.currentTimeMillis());
			sf.setProperty("type", "form.submit");
			sf.setProperty("jcr:created", Calendar.getInstance());
			Set<String> keys = formData.keySet();
			for (String k : keys) {
				if ("ctx".equals(k)) {
					//skip ctx object
					continue;
				}
				sf.setProperty(k, (String) formData.get(k));
			}
			sf.getSession().save();
			return WorkflowResultOK.SUCCESS;
		} catch (Exception e) {
			log.error("Exception occured while processing simple save workflow for form " + formNode.getName(), e);
			return new WorkflowResultError(e);
		}
	}

	@Override
	public void init(Application app) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDefaultJSONConfiguration() {
		return "{}";
	}
}
