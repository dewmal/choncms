package org.chon.cms.light.mvc;

import org.chon.cms.light.mvc.result.Result;

public class TextOutputResult extends Result {

	private String txt;
	private String mime;

	public TextOutputResult(String txt, String mime) {
		this.txt = txt;
		this.mime = mime;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}
	
}
