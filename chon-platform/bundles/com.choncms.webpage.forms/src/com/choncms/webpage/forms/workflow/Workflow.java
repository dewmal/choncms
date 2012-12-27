package com.choncms.webpage.forms.workflow;

import java.util.Map;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.json.JSONObject;

public interface Workflow {
	/**
	 * Initialize workflow
	 * 
	 * @param formNode
	 * @param submittedFormData
	 */
	public void init(Application app);
	
	/**
	 * Returns workflow name
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Start workflow... return value to respond on UI... usuall successData, or errorData...
	 * depends on property in form definition
	 * @return
	 */
	public WorkflowResult process(IContentNode formNode, Map<String, Object> formData, JSONObject workflowConfig);
	
	/**
	 * Default json configuratiuon object
	 * @return
	 */
	public String getDefaultJSONConfiguration();
}
