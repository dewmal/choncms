package org.chon.sourcecode.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.chon.cms.core.JCRApplication;
import org.chon.core.common.util.FileUtils;

public class SourceCodeHelper {
	private JCRApplication app;

	public SourceCodeHelper(JCRApplication app) {
		this.app = app;
	}
	
	public String getTpl(String tpl) throws IOException {
		if(!app.getTemplate().exists(tpl)) {
			throw new RuntimeException("Template " + tpl + " does not exists!");
		}
		URL[] urls = app.getTemplate().getResourcesUrls();
		if(urls == null) {
			throw new RuntimeException("Invalid URL[] while calling app.getTemplate().getResourcesUrls() ");
		}
		for(URL u : urls) {
			InputStream is = u.openStream();
			return FileUtils.readInputStreamToString(is);
		}
		//why? should never get here
		return "Error!!!";
	}
}
