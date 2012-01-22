package org.chon.cms.services.mailer.impl;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.services.mailer.pub.EmailSender;

public class EmailSenderImpl implements EmailSender {
	private static final Log log = LogFactory.getLog(EmailSenderImpl.class);

	private EmailerConfig mailer;
	
	public EmailSenderImpl(EmailerConfig mailer) {
		this.mailer = mailer;
	}

	/* (non-Javadoc)
	 * @see org.chon.cms.services.mailer.IEmailSender#sendHtmlMail(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendHtmlMail(String to, String cc, String bcc,
			String subject, String message) throws MessagingException {

		// Get a Session object
		Session session = mailer.createSession();
		
		// construct the message
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(mailer.getFrom()));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

		if (cc != null) {
			msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
		}

		if (bcc != null) {
			msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
		}

		msg.setSubject(subject);

		msg.setHeader("X-Mailer", mailer.getXMailer());
		msg.setSentDate(new Date());

//		try {
//			msg.setDataHandler(new DataHandler(new ByteArrayDataSource(message, "text/html")));
//		} catch (IOException e) {
//			log.error(e);
//			return false;
//		}

		msg.setContent(message, "text/html");
		if(log.isDebugEnabled()) {
			log.debug("Sending mail to " + to);
		}
		// send the thing off
		Transport.send(msg);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.chon.cms.services.mailer.IEmailSender#sendMail(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendHtmlMail(String to, String subject, String message) throws MessagingException {
//		System.out.println();
//		System.out.println("Sending mail to: " + to);
//		System.out.println("	Subject: " + subject);
//		System.out.println("--- Message:   ------------------");
//		System.out.println(message);
//		System.out.println("---------------------------------");
//		System.out.println();
		
		sendHtmlMail(to, null, null, subject, message);
	}
}
