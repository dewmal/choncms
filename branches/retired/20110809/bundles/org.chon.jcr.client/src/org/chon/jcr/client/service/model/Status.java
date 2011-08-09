package org.chon.jcr.client.service.model;

public class Status {
	public static final Status DEFAULT = new Status("DEFAULT");
	public static final Status TRUE = new Status("true");
	public static final Status FALSE = new Status("false");
	
	public static final Status OK = new Status(null);
	
	public static final Status ERROR = new Status(-1, "General Error");
	
	private int code;
	
	private String description;
	
	private String data;
	
	public Status(String data) {
		this(0, "Success");
		this.data = data;
	}
	
	public Status(int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getData() {
		return data;
	}
}
