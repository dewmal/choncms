package org.chon.cms.light.mvc.result;


public class RedirectResult extends Result {

	private String redirectPath;

	public RedirectResult(String redirectPath) {
		this.redirectPath = redirectPath;
	}

	public String getRedirectPath() {
		return redirectPath;
	}
}
