package org.chon.sourcecode.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.model.types.FileContentNode;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Response;

public class SourceCodeFE {
	private IContentNode node;
	private Response resp;
	private SourceCodeHelper helper;

	public SourceCodeFE(IContentNode node, Response resp, JCRApplication app) {
		this.node = node;
		this.resp = resp;
		this.helper = new SourceCodeHelper(app);
		init(resp);
	}
	
	public SourceCodeHelper getHelper() {
		return helper;
	}

	private void init(Response resp) {
		@SuppressWarnings("unchecked")
		List<String> scrips = (List<String>) resp.getTemplateContext().get("head:scripts");
		@SuppressWarnings("unchecked")
		List<String> css = (List<String>) resp.getTemplateContext().get("head:css");
		
		css.add("syntaxhighlighter_3.0.83/styles/shCore.css");
		css.add("syntaxhighlighter_3.0.83/styles/shCoreDefault.css");
		css.add("syntaxhighlighter_3.0.83/styles/shThemeDefault.css");
		//css.add("syntaxhighlighter_3.0.83/styles/shCoreEclipse.css");
		//css.add("syntaxhighlighter_3.0.83/styles/shThemeEclipse.css");
		
		
		
		scrips.add("syntaxhighlighter_3.0.83/scripts/shCore.js");
		//scrips.add("syntaxhighlighter_3.0.83/scripts/shAutoloader.js");
		//scrips.add("syntaxhighlighter_3.0.83/scripts/shBrushJava.js");
	}

	/**
	 * Show source code using SyntaxHighliter, default lang is java
	 * 
	 * @param name
	 * @return
	 */
	public String show(String name) {
		return show(name, "java");
	}
	
	/**
	 * Show source code using SyntaxHighliter
	 * 
	 * TODO: make detection on syntax based on file name if lang is not specified 
	 * 
	 * @param name
	 * @param lang
	 * @return
	 */
	public String show(String name, String lang) {
		IContentNode file =  node.getChild(name);
		if(file instanceof FileContentNode) {
			FileContentNode f = (FileContentNode) file;
			//HOW TO GET FILE DATA?
			System.out.println(f);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("lang", lang);
			params.put("file", f);
			String brash = (""+lang.charAt(0)).toUpperCase() + lang.substring(1);
			params.put("brush", "shBrush"+brash+".js");
			return resp.formatTemplate("sourcecode/tpl.html", params);
		}
		return "Invalid File";
	}
}
