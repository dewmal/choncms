package org.chon.cms.services.mailer.pub;

import java.util.Properties;

public interface EmailerFactoryService {
	/**
	 * Creates EmailSender instance with mailer properties:
	 * 
	 * mail.smtp.auth -> boolean 					<br />
	 * auth.user 									<br />
	 * auth.password								<br />
	 * from -> String, default mailer@choncms.com 	<br />
	 * x-mailer -> String choncms.mailer			<br />
	 * debug -> boolean							<br />
	 * 
	 * @param properties
	 * @return
	 */
	public EmailSender getEmailSender(Properties properties);
}
