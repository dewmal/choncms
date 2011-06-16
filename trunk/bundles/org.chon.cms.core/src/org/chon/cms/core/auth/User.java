package org.chon.cms.core.auth;

public class User {
	private String name;
	private int role;
	
	public User(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}
}
