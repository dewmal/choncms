package com.choncms.webpage.forms.workflow.impl;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.json.JSONException;
import org.json.JSONObject;

import com.choncms.webpage.forms.WorkflowUtils;
import com.choncms.webpage.forms.workflow.Workflow;
import com.choncms.webpage.forms.workflow.WorkflowResult;
import com.choncms.webpage.forms.workflow.WorkflowResultError;
import com.choncms.webpage.forms.workflow.WorkflowResultOK;

/**
 * Workflow that can execute another workflows
 * serves as a parent workflow for executing child workflows
 * 
 * @author Jovica
 *
 */
public class MultiWorkflow implements Workflow {
	private static final Log log = LogFactory.getLog(MultiWorkflow.class);
	
	protected Application app;
	
	@Override
	public String getName() {
		return "MultiWorkflow";
	}

	@Override
	public void init(Application app) {
		this.app = app;
	}

	@Override
	public WorkflowResult process(IContentNode formNode, Map<String, Object> formData, JSONObject cfg) {
		try {
			JSONObject childs = cfg.getJSONObject("childs");
			
			@SuppressWarnings("unchecked")
			Iterator<String> it = childs.keys();
			
			while(it.hasNext()) {
				String k = it.next();
				JSONObject v = childs.getJSONObject(k);
				Workflow w = WorkflowUtils.getWorkflow(k);
				WorkflowResult r = w.process(formNode, formData, v);
				log.debug("Executed workflow: " + w.getName() + " result: " + r);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new WorkflowResultError(e);
		}
		
		return WorkflowResultOK.SUCCESS;
	}

	@Override
	public String getDefaultJSONConfiguration() {
		JSONObject cfg;
		try {
			cfg = new JSONObject("{ childs: { DefaultWorkflow: {}, EmailWorkflow: {emailTo: 'email'} } }");
			return cfg.toString(2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
