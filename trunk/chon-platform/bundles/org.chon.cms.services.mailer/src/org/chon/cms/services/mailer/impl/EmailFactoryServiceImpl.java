package org.chon.cms.services.mailer.impl;

import java.util.Properties;

import org.chon.cms.services.mailer.pub.EmailerFactoryService;
import org.chon.cms.services.mailer.pub.EmailSender;

public class EmailFactoryServiceImpl implements EmailerFactoryService {

	@Override
	public EmailSender getEmailSender(Properties properties) {
		EmailerConfig cfg = new EmailerConfig();
		cfg.init(properties);
		return new EmailSenderImpl(cfg);
	}
}
