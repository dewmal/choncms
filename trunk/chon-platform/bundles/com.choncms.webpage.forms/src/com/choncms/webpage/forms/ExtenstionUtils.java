package com.choncms.webpage.forms;

import org.chon.cms.core.model.renderers.ExtObj;
import org.chon.web.api.Response;

public class ExtenstionUtils {

	public static void ensureExtenstionVisible(String extName, Response resp) throws Exception {
		ExtObj exts = (ExtObj) resp.getTemplateContext().get("extenstions");
		Object o = exts.get(extName);
		if(o == null) {
			//o is FE object from Extenstion.getTplObject, it might be null even if extenstion is there...
			// so do nothing here, it is good enough to call exts.get(extensionName)
			//	throw new Exception("Extenstion " + extName + " not found");
		}
	}

}
