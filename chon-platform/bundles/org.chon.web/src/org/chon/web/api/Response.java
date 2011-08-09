package org.chon.web.api;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.chon.core.velocity.VTemplate;
import org.json.JSONObject;


public class Response {
	private Map<String, String> contentsMap;
	private JSONObject pageData = new JSONObject();
	private HttpServletResponse servletResponse;
	
	private String redirect = null;
	
	private Map<String, Object> templateContext = new HashMap<String, Object>();
	
	public Map<String, Object> getTemplateContext() {
		return templateContext;
	}

	public HttpServletResponse getServletResponse() {
		return servletResponse;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public JSONObject getPageData() {
		return pageData;
	}

	public Map<String, String> getContentsMap() {
		return contentsMap;
	}

	public void setContentsMap(Map<String, String> contentsMap) {
		this.contentsMap = contentsMap;
	}

	private ServerInfo serverInfo;
	
	public Response(ServerInfo serverInfo) {
		serverInfo.setResp(this);
		this.serverInfo = serverInfo;
		this.servletResponse = serverInfo.getResponse();
	}
	
	public PrintWriter getOut() {
		return serverInfo.getOut();
	}
	
	public String formatTemplate(String tplName, Map<String, ?> params) {
		VTemplate tpl = serverInfo.getApplication().getTemplate();
		return tpl.format(tplName, params, templateContext);
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
}
