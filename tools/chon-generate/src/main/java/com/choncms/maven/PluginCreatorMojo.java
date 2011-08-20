package com.choncms.maven;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which touches a timestamp file.
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
	private String projectParentVersion = "0.0.1-SNAPSHOT";
	private String projectParentPomRelPath = "../pom.xml";
	
	public void execute() throws MojoExecutionException {		
		try {
			System.out.println("Please enter values from your new plugin. " +
					"Leave blank for default");
			
			projectGroupId = getValue("Project GroupId", projectGroupId);
			projectPackage = getValue("Project Package", projectPackage);
			projectName = getValue("Project Name", projectName);
			projectVersion = getValue("Project Version", projectVersion);
			
			System.out.println("Describe parent project. " +
					"Chon plugin mush have parent pom where target platform is defined. " +
					"Leave blank for default");
			projectParentGroupId = getValue("Parent GroupId", projectParentGroupId);
			projectParentArtifactId = getValue("Parent ArtifactId", projectParentArtifactId);
			projectParentVersion = getValue("Parent Verstion", projectParentVersion);
			projectParentPomRelPath = getValue("Parent Pom Relative Path", projectParentPomRelPath);
			
			populateTemplateVariables();
			File base = new File(basedir);
			base.mkdirs();
			
			Resource project = readProjectStructure();
			
			project.create(base);
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating project", e);
		}
	}

	private void populateTemplateVariables() {
		templateVarsMap.put("project-groupId", projectGroupId);
		templateVarsMap.put("project-package", projectPackage);
		templateVarsMap.put("project-name", projectName);
		templateVarsMap.put("project-version", projectVersion);
		
		templateVarsMap.put("project-parent-groupId", projectParentGroupId);
		templateVarsMap.put("project-parent-artifactId", projectParentArtifactId);
		templateVarsMap.put("project-parent-version", projectParentVersion);
		templateVarsMap.put("project-parent-pom-relativePath", projectParentPomRelPath);
	}

	@Override
	protected String getProjectType() {
		return "plugin";
	}
}
