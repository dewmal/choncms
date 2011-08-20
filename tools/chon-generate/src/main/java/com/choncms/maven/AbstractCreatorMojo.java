package com.choncms.maven;

import java.io.BufferedReader;
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

import de.pdark.decentxml.Document;
import de.pdark.decentxml.Element;
import de.pdark.decentxml.XMLParser;

public abstract class AbstractCreatorMojo extends AbstractMojo {
	
	protected abstract String getProjectType();
	
	/**
	 * @parameter expression="${basedir}" default-value="${user.dir}"
	 */
	protected String basedir;
	
	protected Map<String, Object> templateVarsMap = new HashMap<String, Object>();

	
	private VTemplate tpl = new VTemplate((URL[]) null, 120);

	protected String getValue(String description, String defVal)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Value for '" + description + "', eg: '" + defVal
				+ "': ");
		String line = br.readLine();
		if (line == null || line.trim().length() == 0) {
			return defVal;
		}
		return line;
	}

	protected String readStreamToString(InputStream is, boolean passInVelocity,
			String tplName) throws IOException {
		StringWriter sw = new StringWriter();
		IOUtils.copy(is, sw, "UTF-8");
		if (passInVelocity) {
			return tpl.formatStr(sw.toString(), templateVarsMap, tplName);
		}
		return sw.toString();
	}
	
	/**
	 * plugin or project
	 * 
	 * @param type
	 * @return
	 * @throws IOException
	 */
	protected Resource readProjectStructure()
			throws IOException {
		String projectStrictrueXml = getProjectType() + ".structure.xml"; 
		InputStream is = AbstractCreatorMojo.class.getResourceAsStream("/" + projectStrictrueXml);
		String xmlText = readStreamToString(is, true, projectStrictrueXml);
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

	private String readProjectTempalteFile(String contentFile) throws IOException {
		contentFile = contentFile.trim();
		if (!contentFile.startsWith("/")) {
			contentFile = "/" + contentFile;
		}
		String resource = "/" + getProjectType() + "-template" + contentFile;
		// Thread.currentThread().getContextClassLoader()
		InputStream is = PluginCreatorMojo.class.getResourceAsStream(resource);
		if (is == null) {
			throw new FileNotFoundException(resource);
		}
		return readStreamToString(is, true, contentFile);
	}

}
