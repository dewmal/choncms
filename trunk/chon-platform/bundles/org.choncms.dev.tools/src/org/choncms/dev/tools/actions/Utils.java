package org.choncms.dev.tools.actions;

import org.json.XML;
import org.osgi.framework.Bundle;

public class Utils {
	private Utils() { }
	
	public String escapeXml(String str) {
		if(str == null) return null;
		return XML.escape(str);
	}
	
	public String statusToString(int bundleStatus) {
		switch(bundleStatus) {
		case Bundle.ACTIVE:
			return "ACTIVE";
		case Bundle.INSTALLED:
			return "INSTALLED";
		case Bundle.RESOLVED:
			return "RESOLVED";
		case Bundle.STARTING:
			return "STARTING";
		case Bundle.STOPPING:
			return "STOPPING";
		case Bundle.UNINSTALLED:
			return "UNINSTALLED";
		}
		return "UNKNOWN";
	}
	
	private static Utils instance = new Utils();
	public static Utils getInstance() {
		return instance;
	}
}
