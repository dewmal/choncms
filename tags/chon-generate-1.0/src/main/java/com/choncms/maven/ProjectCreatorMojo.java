package com.choncms.maven;

import java.io.IOException;
import java.util.Map;

/**
 * Goal which creates new chon based project
 * 
 * @requiresProject false
 * @goal new-project
 */
public class ProjectCreatorMojo extends AbstractCreatorMojo {
	
	private String projectName = "chon.based.project";
	
	
	@Override
	protected Map<String, Object> getTemplateVariables() {
		Map<String, Object> tplVars = super.getTemplateVariables();
		try {
			projectName = getValue("Project Name", projectName);
			tplVars.put("project-name", projectName);
			tplVars.put("project-groupId", projectName);
			tplVars.put("project-package", projectName);
			tplVars.put("project-parent-groupId", projectName);
			
			tplVars.put("isSimpleTemplate", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tplVars;
	}


	protected String getProjectType() {
		return "project";
	}
}
