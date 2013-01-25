package org.chon.cms.services.mailer.impl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class EmailerConfig {
	private Properties props = System.getProperties();
	
	public void init(Properties props) {
		this.props = props;
	}
	
	public Session createSession() {
		Authenticator auth = null;
		if("true".equals(props.getProperty("mail.smtp.auth"))) {
			auth = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(props.getProperty("auth.user"), props.getProperty("auth.password"));
				}
			};
		}
		
		Session session = Session.getInstance(props, auth);
		
		if (this.isDebugEnabled()) {
			session.setDebug(true);
		}
		return session;
	}
	
	public String getFrom() {
		return props.getProperty("from", "mailer@choncms.com");
	}
	
	public String getXMailer() {
		return props.getProperty("x-mailer", "choncms.mailer");
	}
	
	public boolean isDebugEnabled() {
		return "true".equals(props.getProperty("debug"));
	}
}
