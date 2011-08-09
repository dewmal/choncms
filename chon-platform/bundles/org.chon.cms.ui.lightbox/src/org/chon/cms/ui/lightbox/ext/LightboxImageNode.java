package org.chon.cms.ui.lightbox.ext;


public class LightboxImageNode {
	private String path; 
	private String title;
	public LightboxImageNode(String path, String title) {
		this.path = path;
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public String getPath() {
		return path;
	}
}
