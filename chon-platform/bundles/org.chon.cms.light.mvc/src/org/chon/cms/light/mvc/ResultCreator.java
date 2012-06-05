package org.chon.cms.light.mvc;

import java.util.Map;

import org.chon.cms.light.mvc.result.ActionResult;
import org.chon.cms.light.mvc.result.RedirectResult;
import org.chon.cms.light.mvc.result.Result;
import org.chon.cms.light.mvc.result.TemplateResult;
import org.json.JSONObject;



public class ResultCreator {
	
	public Result template(String tpl) {
		return new TemplateResult(tpl, null);
	}
	
	public Result template(String tpl, Map<String, Object> params) {
		return new TemplateResult(tpl, params);
	}

	public Result redirect(String redirectPath) {
		return new RedirectResult(redirectPath);
	}

	public Result action(Class<? extends AbstractAction> action, Object model) {
		return new ActionResult(action, model);
	}
	
	public Result action(Class<? extends AbstractAction> action) {
		return action(action, null);
	}

	public Result html(String html) {
		return new TextOutputResult(html, "text/html");
	}
	
	public Result json(JSONObject json) {
		return new TextOutputResult(json.toString(), "application/json");
	}
	public Result json(String str) {
		return new TextOutputResult(str, "application/json");
	}

	public Result error(Exception e) {
		// TODO Auto-generated method stub
		return null;
	}
}
