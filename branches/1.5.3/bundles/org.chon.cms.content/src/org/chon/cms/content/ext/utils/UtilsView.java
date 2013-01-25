package org.chon.cms.content.ext.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.XML;

public class UtilsView {
	
	public String formatDate(Calendar c, String pattern) {
		if(c==null) return null;
		if(pattern == null) pattern = "MM/dd/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(c.getTime());
	}
	
	public String getPageBreak() {
		return "<div style='clear:both;' class='page-break'><!-- page break --></div>";
	}
	
	public String escapeHTML(String html) {
		if(html==null) return null;
		return XML.escape(html);
	}
}
