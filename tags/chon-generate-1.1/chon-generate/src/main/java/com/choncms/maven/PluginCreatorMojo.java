package com.choncms.maven;

import java.io.IOException;
import java.util.Map;

import org.apache.maven.project.MavenProject;

/**
 * Goal which creates new chon based OSGi bundle
 * 
 * @requiresProject false
 * @goal new-plugin
 */
public class PluginCreatorMojo extends AbstractCreatorMojo {
	private String projectGroupId = "org.choncms";
	private String projectPackage = "org.chon.my.plugin";
	private String projectName = "My Plugin";
	private String projectVersion = "1.0.0-SNAPSHOT";

	private String projectParentGroupId = "org.choncms";
	private String projectParentArtifactId = "bundles";
	private String projectParentVersion = "1.0.0-SNAPSHOT";
	private String projectParentPomRelPath = "../pom.xml";

	@Override
	protected Map<String, Object> getTemplateVariables() {
		
		Map<String, Object> tplVars = super.getTemplateVariables();
		try {
			MavenProject parent = project; //usually bundles pom project
			
			if(parent != null && !"org.apache.maven".equals(parent.getGroupId())) {
				projectGroupId = parent.getGroupId();
				projectPackage = parent.getGroupId() + ".plugin";
				projectParentGroupId = parent.getGroupId();
				projectParentVersion = parent.getVersion();
				projectParentArtifactId = parent.getArtifactId();
				System.out.println(" *** Don't forget to add newly created bundle in modules " +
						"in parent project to enable automatic build. *** ");
			} else {
				System.out.println("Please enter values from your new plugin. "
						+ "Leave blank for default");

				projectGroupId = getValue("Project GroupId", projectGroupId);
				projectName = getValue("Project Name", projectName);
				projectVersion = getValue("Project Version", projectVersion);

				System.out
						.println("Describe parent project. "
								+ "Chon plugin mush have parent pom where target platform is defined. "
								+ "Leave blank for default");
				projectParentGroupId = getValue("Parent GroupId",
						projectParentGroupId);
				projectParentArtifactId = getValue("Parent ArtifactId",
						projectParentArtifactId);
				projectParentVersion = getValue("Parent Verstion",
						projectParentVersion);
				projectParentPomRelPath = getValue("Parent Pom Relative Path",
						projectParentPomRelPath);
			}
			
			System.out.println("Please Enter fully qualified package name, eg: com.choncms.example.my.plugin.one");
			projectPackage = getValue("Project Package", projectPackage);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		populateTemplateVariables(tplVars);
		return tplVars;
	}

	private void populateTemplateVariables(Map<String, Object> templateVarsMap) {
		templateVarsMap.put("project-groupId", projectGroupId);
		templateVarsMap.put("project-package", projectPackage);
		templateVarsMap.put("project-name", projectName);
		templateVarsMap.put("project-version", projectVersion);

		templateVarsMap.put("project-parent-groupId", projectParentGroupId);
		templateVarsMap.put("project-parent-artifactId",
				projectParentArtifactId);
		templateVarsMap.put("project-parent-version", projectParentVersion);
		templateVarsMap.put("project-parent-pom-relativePath",
				projectParentPomRelPath);
	}

	@Override
	protected String getProjectType() {
		return "plugin";
	}
}
