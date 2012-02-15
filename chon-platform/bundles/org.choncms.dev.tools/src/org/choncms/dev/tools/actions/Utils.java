package org.choncms.dev.tools.actions;

import org.json.XML;

public class Utils {
	public String escapeXml(String str) {
		if(str == null) return null;
		return XML.escape(str);
	}
}
