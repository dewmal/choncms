package com.choncms.webpage.forms.workflow;

import java.util.Map;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;


public class DefaultWorkflow implements Workflow {
	protected Application app;
	
	@Override
	public String getName() {
		return "DefaultWorkflow";
	}

	@Override
	public void init(Application app) {
		System.out.println("--- Init workflow " + this.getName());
		this.app = app;
	}

	@Override
	public String process(IContentNode formNode, Map<String, Object> formData) {
		System.out.println(formData);
		return Workflow.SUCCESS;
	}
}
