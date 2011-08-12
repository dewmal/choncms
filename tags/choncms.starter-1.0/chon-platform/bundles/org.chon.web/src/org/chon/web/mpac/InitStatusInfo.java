package org.chon.web.mpac;
public class InitStatusInfo {

	public static final InitStatusInfo DEFAULT = new InitStatusInfo(0, "DEFAULT");
	public static final InitStatusInfo OK = new InitStatusInfo(1, "Ok");
	public static final InitStatusInfo NOT_AUTH = new InitStatusInfo(-10, "User is not authenticated");
	public static final InitStatusInfo ACCESS_DENIED = new InitStatusInfo(-15, "Access Denied");
	public static final InitStatusInfo LOGIN_ERROR = new InitStatusInfo(-20, "Invalid username or password");
	public static final InitStatusInfo REDIRECT = new InitStatusInfo(-30, "Redirect");

	private int status;
	private String info;	

	private InitStatusInfo(int status, String info) {
		this.status = status;
		this.info = info;
	}

	public int getStatus() {
		return status;
	}

	public String getInfo() {
		return info;
	}
}
