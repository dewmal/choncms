package org.chon.cms.light.mvc.result;

import java.util.Map;


public class TemplateResult extends Result {
	private String baseTemplate = "base.html";
	
	private String template;
	private Map<String, Object> params;

	public TemplateResult(String baseTemplate, String template, Map<String, Object> params) {
		this.baseTemplate = template;
		this.template = template;
		this.params = params;
	}
	
	public TemplateResult(String template, Map<String, Object> params) {
		this.template = template;
		this.params = params;
	}

	public String getTemplate() {
		return template;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public String getBaseTemplate() {
		return baseTemplate;
	}

}
