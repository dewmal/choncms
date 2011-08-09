package org.chon.cms.core.auth;

public interface AuthenticationProvider {
	public User login(String username, String password) throws InvalidUserException;
	public boolean logout(User user);
}
