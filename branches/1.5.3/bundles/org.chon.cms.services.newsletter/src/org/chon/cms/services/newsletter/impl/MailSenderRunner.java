package org.chon.cms.services.newsletter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;

import org.chon.cms.services.newsletter.Newsletter;
import org.chon.cms.services.newsletter.NewsletterException;

public class MailSenderRunner implements Runnable {
	public class Fault {
		private String email;
		private NewsletterException exception;
		public Fault(String email, NewsletterException e) {
			this.email = email;
			this.exception = e;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public NewsletterException getException() {
			return exception;
		}
		public void setException(NewsletterException exception) {
			this.exception = exception;
		}		
	}



	private static final Integer GR_SIZE = 1;
	
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
		newsletterContentNode.setPercentComplete(1);
		try {
			Node n = newsletterContentNode.getNode();
			
			List<String> failedEmails = new ArrayList<String>();
			
			long total = newsletterContentNode.getTotalSubscribers();
			int i = 0;
			while (i < total) {
				List<String> emails = newsletterContentNode.getSubscribers(i, GR_SIZE);
				i += GR_SIZE;
				
				List<Fault> faults = sendMails(emails, subject, template, params);
				if(faults!= null && faults.size() > 0) {
					for(Fault f : faults) {
						failedEmails.add(f.email);
					}
				}
				n.setProperty("mailsSent", i-failedEmails.size());
				int p = Math.round(100*(float)i/(float)total);
				newsletterContentNode.setPercentComplete(p>0 ? p : 1);
			}
			
			newsletterContentNode.setPercentComplete(100);
			n.setProperty("failedEmails", failedEmails.toString());
			newsletterContentNode.setStatus(Newsletter.STATUS_DONE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			newsletterContentNode.setStatus(Newsletter.STATUS_ERROR);
		} finally {
			newsletterContentNode.setStatus(Newsletter.STATUS_DEFAULT);
		}
	}



	private List<Fault> sendMails(List<String> emails, String subject,
			String template, Map<String, Object> params) {
		List<Fault> faults = new ArrayList<Fault>();
		for(String email : emails) {
			try {
				newsletterContentNode.send(email, subject, template, params);
			} catch (NewsletterException e) {
				faults.add(new Fault(email, e));
			}
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return faults;
	}

}
