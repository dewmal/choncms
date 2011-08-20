package com.choncms.maven;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import de.pdark.decentxml.Document;
import de.pdark.decentxml.Element;
import de.pdark.decentxml.XMLParser;

public class ProjectStructure {
	private static VTemplate tpl = new VTemplate((URL[]) null, 120);
	
	
	public static Resource read(String tplXml, Map<String, Object> tplVars) throws IOException {
		InputStream is = getResource(tplXml);
		String xmlText = readStreamToString(is, true, tplXml, tplVars);
		Document doc = XMLParser.parse(xmlText);
		Element root = doc.getRootElement();
		Resource project = processResource(root, tplVars, null);
		return project;
	}
	
	private static InputStream getResource(String path) throws FileNotFoundException {
		if(!path.startsWith("/")) {
			path = "/"+path;
		}
		InputStream is = ProjectStructure.class.getResourceAsStream(path);
		if(is == null) {
			throw new FileNotFoundException(path);
		}
		return is;
	}
	
	private static String readStreamToString(InputStream is, boolean passInVelocity,
			String tplName, Map<String, Object> templateVarsMap) throws IOException {
		StringWriter sw = new StringWriter();
		IOUtils.copy(is, sw, "UTF-8");
		if (passInVelocity) {
			return tpl.formatStr(sw.toString(), templateVarsMap, tplName);
		}
		return sw.toString();
	}
	
	private static Resource processResource(Element el, Map<String, Object> tplVars, Resource parent) {
		String ref = el.getAttributeValue("ref");
		if(ref != null) {
			try {
				Resource r = read(ref, tplVars);
				parent.addChild(r);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		String name = el.getAttributeValue("name");
		String type = el.getAttributeValue("type");
		Resource r = new Resource(type, name, el.getAttributeValue("tplDir"), parent);
		for (Element c : el.getChildren()) {
			processResource(c, tplVars, r);
		}
		
		String contentFile = el.getAttributeValue("content-file");
		if ("file".equals(type) && contentFile != null) {
			try {
				String tplRoot = r.getTplRoot();
				String res = (tplRoot != null ? ("/" + tplRoot) : "") + "/" + contentFile;
				InputStream is = getResource(res);
				String data = readStreamToString(is, true, contentFile, tplVars);
				r.setFileData(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return r;
	}
}
