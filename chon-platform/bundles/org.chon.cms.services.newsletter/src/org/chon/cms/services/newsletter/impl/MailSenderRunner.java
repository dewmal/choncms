package org.chon.cms.services.newsletter.impl;

import java.util.Map;

import org.chon.cms.services.newsletter.Newsletter;

public class MailSenderRunner implements Runnable {
	private String subject;
	private String template;
	private Map<String, Object> params;
	private NewsletterContentNode newsletterContentNode;
	
	public MailSenderRunner(String subject, String template,
			Map<String, Object> params,
			NewsletterContentNode newsletterContentNode) {
		this.subject = subject;
		this.template = template;
		this.params = params;
		this.newsletterContentNode = newsletterContentNode;
	}



	@Override
	public void run() {
		newsletterContentNode.setStatus(Newsletter.STATUS_SENDING);
		newsletterContentNode.setLastSendActiovationNow();
		try {
			//TODO//////
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			newsletterContentNode.setStatus(Newsletter.STATUS_ERROR);
		} finally {
			newsletterContentNode.setStatus(Newsletter.STATUS_DEFAULT);
		}
	}

}
