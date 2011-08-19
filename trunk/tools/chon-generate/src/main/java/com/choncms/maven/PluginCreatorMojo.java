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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import de.pdark.decentxml.Document;
import de.pdark.decentxml.Element;
import de.pdark.decentxml.XMLParser;

/**
 * Goal which touches a timestamp file.
 * 
 * @requiresProject false
 * @goal new-plugin
 */
public class PluginCreatorMojo extends AbstractMojo {
	
	/**
	 * @parameter expression="${basedir}" default-value="${user.dir}"
	 */
	private String basedir;

	private VTemplate tpl = null;
	private Map<String, Object> templateVarsMap = null;
	
	private String projectPackage = "org.chon.my.plugin";
	private String projectName = "My New Plugin";
	
	public void execute() throws MojoExecutionException {
		
		
		try {
			projectPackage = getValue("Project Package", projectPackage);
			projectName = getValue("Project Name", projectName);
			
			initTemplate();
			File base = new File(basedir);
			File target = new File(base, projectPackage);
			target.mkdirs();
		
			Resource project = readProjectStructure(PluginCreatorMojo.class
					.getResourceAsStream("/project.structure.xml"));
			project.create(target);
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating project", e);
		}
	}



	private String getValue(String description, String defVal) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Value for '" + description + "', eg: '"+defVal+"': ");
		return br.readLine();
	}



	private void initTemplate() {
		tpl = new VTemplate((URL []) null, 120);
		templateVarsMap = new HashMap<String, Object>();
		templateVarsMap.put("project-name", projectName);
		templateVarsMap.put("project-package", projectPackage);
	}

	
	private String readStreamToString(InputStream is, boolean passInVelocity, String tplName) throws IOException {
		StringWriter sw = new StringWriter();
		IOUtils.copy(is, sw, "UTF-8");
		if(passInVelocity) {
			return tpl.formatStr(sw.toString(), templateVarsMap, tplName);
		}
		return sw.toString();
	}

	private Resource readProjectStructure(InputStream is) throws IOException {
		String xmlText = readStreamToString(is, true, "project.structure.xml");	
		Document doc = XMLParser.parse(xmlText);
		Element root = doc.getRootElement();
		Resource project = processResource(root);
		return project;
	}

	private Resource processResource(Element el) {
		String name = el.getAttributeValue("name");
		String type = el.getAttributeValue("type");
		String content = null;

		String contentFile = el.getAttributeValue("content-file");
		if ("file".equals(type) && contentFile != null) {
			try {
				content = readProjectTempalteFile(contentFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Resource r = new Resource(type, name, content);
		for (Element c : el.getChildren()) {
			Resource rc = processResource(c);
			r.addChild(rc);
		}
		return r;
	}

	private static final String templatePath = "/plugin-template";

	private String readProjectTempalteFile(String contentFile)
			throws IOException {
		contentFile = contentFile.trim();
		if (!contentFile.startsWith("/")) {
			contentFile = "/" + contentFile;
		}
		String resource = templatePath + contentFile;
		// Thread.currentThread().getContextClassLoader()
		InputStream is = PluginCreatorMojo.class.getResourceAsStream(resource);
		if (is == null) {
			throw new FileNotFoundException(resource);
		}
		return readStreamToString(is, true, contentFile);
	}

	public static void main(String[] args) {
		PluginCreatorMojo mojo = new PluginCreatorMojo();
		mojo.initTemplate();
		try {
			Resource project = mojo.readProjectStructure(PluginCreatorMojo.class
					.getResourceAsStream("/project.structure.xml"));
			project.create(new File("c:/temp/joco"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
