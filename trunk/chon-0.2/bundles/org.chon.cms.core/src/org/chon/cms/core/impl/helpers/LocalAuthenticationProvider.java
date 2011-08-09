package org.chon.cms.core.impl.helpers;

import org.chon.cms.core.Utils;
import org.chon.cms.core.auth.AuthenticationProvider;
import org.chon.cms.core.auth.InvalidUserException;
import org.chon.cms.core.auth.User;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;

public class LocalAuthenticationProvider implements AuthenticationProvider {

	private ContentModel cm;

	public LocalAuthenticationProvider(ContentModel cm) {
		this.cm = cm;
	}
	@Override
	public User login(String username, String password)
			throws InvalidUserException {
		IContentNode node = cm.getConfigNode().getChild("passwd").getChild(username);
		if(node == null) throw new InvalidUserException();
		if(password == null) throw new InvalidUserException();
		if(!Utils.getMd5Digest(password).equals(node.prop("password"))) {
			throw new InvalidUserException();
		}
		return new User(username);
	}

	@Override
	public boolean logout(User user) {
		return true;
	}
}
