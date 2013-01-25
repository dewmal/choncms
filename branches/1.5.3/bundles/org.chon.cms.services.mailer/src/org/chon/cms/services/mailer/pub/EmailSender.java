package org.chon.cms.services.mailer.pub;

import javax.mail.MessagingException;

public interface EmailSender {

	public boolean sendHtmlMail(String to, String cc, String bcc,
			String subject, String message) throws MessagingException;

	public void sendHtmlMail(String to, String subject, String message)
			throws MessagingException;

}