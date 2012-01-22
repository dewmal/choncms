package com.choncms.webpage.forms.workflow;

import java.util.Map;
import java.util.Set;

import javax.jcr.Node;

import org.chon.cms.model.content.IContentNode;


public class SimpleSaveWorkflow extends DefaultWorkflow {
	
	@Override
	public String getName() {
		return "SimpleSaveWorkflow";
	}

	@Override
	public String process(IContentNode formNode, Map<String, Object> formData) {
		try {
			Node sf = formNode.getNode().addNode(
					"" + System.currentTimeMillis());
			sf.setProperty("type", "form.submit");
			Set<String> keys = formData.keySet();
			for (String k : keys) {
				if ("ctx".equals(k)) {
					//skip ctx object
					continue;
				}
				sf.setProperty(k, (String) formData.get(k));
			}
			sf.getSession().save();
			return Workflow.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return Workflow.ERROR;
		}
	}
}
