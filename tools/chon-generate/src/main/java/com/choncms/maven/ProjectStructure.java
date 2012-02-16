package com.choncms.maven;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import de.pdark.decentxml.Document;
import de.pdark.decentxml.Element;
import de.pdark.decentxml.XMLParser;

public class ProjectStructure {
	private static VTemplate tpl = new VTemplate((URL[]) null, 120);
	
	
	public static Resource read(String tplXml, Map<String, Object> tplVars, String resourcesPrefix) throws IOException {
		if("local".equals(resourcesPrefix)) {
			resourcesPrefix = null;
		}
		
		InputStream is = getResource(resourcesPrefix, tplXml);
		String xmlText = readStreamToString(is, true, tplXml, tplVars);
		Document doc = XMLParser.parse(xmlText);
		Element root = doc.getRootElement();
		Resource project = processResource(resourcesPrefix, root, tplVars, null);
		return project;
	}
	
	private static InputStream getResource(String resourcesPrefix, String path) throws MalformedURLException, IOException {
		if(!path.startsWith("/")) {
			path = "/"+path;
		}
		if(resourcesPrefix != null) {
			path = resourcesPrefix + path;
		}
		System.out.println("Reading file from: " + path);
		InputStream is = null;
		if(path.startsWith("http")) {
			is = new URL(path).openStream();
		} else {
			is = ProjectStructure.class.getResourceAsStream(path);
		}
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
	
	private static Resource processResource(String resourcesPrefix, Element el, Map<String, Object> tplVars, Resource parent) {
		String condition = el.getAttributeValue("condition");
		if(condition!=null && condition.trim().length()>0) {
			if(tplVars.containsKey(condition) && tplVars.get(condition)== Boolean.TRUE) {
				//if condition exists and if it true
			} else {
				//if condition value does not exists in tplVars or it is false
				return null;
			}
		}
		
		String ref = el.getAttributeValue("ref");
		if(ref != null) {
			try {
				Resource r = read(ref, tplVars, resourcesPrefix);
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
			processResource(resourcesPrefix, c, tplVars, r);
		}
		
		String contentFile = el.getAttributeValue("content-file");
		if ("file".equals(type) && contentFile != null) {
			try {
				String tplRoot = r.getTplRoot();
				String res = (tplRoot != null ? ("/" + tplRoot) : "") + "/" + contentFile;
				InputStream is = getResource(resourcesPrefix, res);
				String data = readStreamToString(is,
						!"false".equals(el.getAttributeValue("eval")),
						contentFile, tplVars);
				r.setFileData(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String contentStream = el.getAttributeValue("content-stream");
		if (("file".equals(type) || "zip".equals(type)) && contentStream != null) {
			String tplRoot = r.getTplRoot();
			String res = (tplRoot != null ? ("/" + tplRoot) : "") + "/" + contentStream;
			InputStream is;
			try {
				is = getResource(resourcesPrefix, res);
				r.setInputStream(is);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return r;
	}
}
