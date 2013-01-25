package org.chon.cms.services.newsletter.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.core.Utils;
import org.chon.cms.core.model.types.ContentNode;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.PropertyType;
import org.chon.cms.services.mailer.pub.EmailSender;
import org.chon.cms.services.newsletter.Newsletter;
import org.chon.cms.services.newsletter.NewsletterException;
import org.chon.cms.services.newsletter.NewsletterSystem;
import org.chon.core.velocity.VTemplate;


public class NewsletterContentNode extends ContentNode implements Newsletter {
	private static final Log log = LogFactory.getLog(NewsletterContentNode.class);
	private VTemplate template;
	private EmailSender emailSender;
	
	public NewsletterContentNode(ContentModel model, Node node,
			IContentNode typeDesc) {
		super(model, node, typeDesc);
	}
	
	
	@Override
	public void subscribe(String email, Map<String, String> additionalInfo)
			throws NewsletterException {
		if(email == null) {
			throw new NewsletterException("Invalid email: null");
		}
		String pattern = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
		if(!email.matches(pattern)) {
			throw new NewsletterException("Invalid email: " + email);
		}
		
		if(isSubscribed(email)) {
			throw new NewsletterException("Email " + email + " already subscribed.");
		}
		if(log.isDebugEnabled()) {
			log.debug("Adding new subscriber " + email +" in newsletter "  + this.getName() + "; ");
		}
		try {
			Node subscriber = getNode().addNode(email);
			if(additionalInfo != null) {
				for(String k : additionalInfo.keySet()) {
					String v = additionalInfo.get(k);
					subscriber.setProperty(k, v);
				}
			}
			subscriber.setProperty("type", NewsletterSystem.NEWSLETTER_SUBSCRIBER);
			subscriber.setProperty("code", Utils.getMd5Digest(email + "|"
					+ System.currentTimeMillis()));
			subscriber.getSession().save();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void unsubscribe(String email, String code)
			throws NewsletterException {
		try {
			if(isSubscribed(email)) {
				IContentNode n = this.getChild(email);
				if(code == null) {
					//TODO: shall we allow unsubscribe without valid code
					throw new NewsletterException("code null not supported");
				}
				if(code.equals(n.prop("code"))) {					
					n.getNode().remove();
					n.getContentModel().getSession().save();
				} else {
					throw new NewsletterException("Invalid code");
				}
			} else {
				throw new NewsletterException("Email " + email + " not subscribed.");
			}
		} catch (RepositoryException e) {
			throw new NewsletterException(e);
		}
	}

	@Override
	public boolean isSubscribed(String email) throws NewsletterException {
		return this.getChild(email) != null;
	}

	@Override
	public List<String> getSubscribers(Integer start, Integer limmit) {
		List<IContentNode> ls = this.getChilds(start, limmit);
		List<String> rv = new ArrayList<String>();
		for(IContentNode n : ls) {
			rv.add(n.getName());
		}
		return rv;
	}

	@Override
	public long getTotalSubscribers() {
		return this.getChildsSize();
	}

	@Override
	public Map<String, String> getSubscriberInfo(String email)
			throws NewsletterException {
		Map<String, String> rv = new HashMap<String, String>();
		IContentNode subsriber = this.getChild(email);
		if(subsriber == null) {
			return null;
		}
		try {
			PropertyIterator pi = subsriber.getNode().getProperties();
			while (pi.hasNext()) {
				Property p = pi.nextProperty();
				String k = p.getName();
				if ("type".equals(k)) {
					continue;
				}
				String v = p.getString();
				rv.put(k, v);
			}
		} catch (Exception e) {
			throw new NewsletterException(e);
		}
		return rv;
	}

	@Override
	public void send(String subject, String template, Map<String, Object> params)
			throws NewsletterException {
		if (this.getStatus() == Newsletter.STATUS_SENDING) {
			throw new NewsletterException("Newsletter is already in status SENDING");
		}
		
		MailSenderRunner mailSenderRunner = new MailSenderRunner(subject, template, params, this);
		Thread thread = new Thread(mailSenderRunner);
		thread.start();
		
/*
		final int GR_SIZE = 1;
		final long total = getTotalSubscribers();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<String> failedEmails = new ArrayList<String>();
					NewsletterContentNode.this.getNode().setProperty("failedEmails", failedEmails.toString());
					NewsletterContentNode.this.getNode().setProperty("total", total);
					
					int i = 0;
					while (i < total) {
						List<String> emails = getSubscribers(i, GR_SIZE);
						i += GR_SIZE;
						List<Fault> faults = sendMails(emails, subject, template, params);
						if(faults!= null && faults.size() > 0) {
							for(Fault f : faults) {
								failedEmails.add(f.email);
							}
						}
						infoMap.put("sent", i-failedEmails.size());
						infoMap.put("percentComplete", 100*(float)i/(float)total);
					}
					infoMap.put("percentComplete", 100);
					NewsletterImpl.this.status = Newsletter.STATUS_DONE;
				} catch (Exception e) {
					log.error("Unknown error while sending newsletter", e);
					NewsletterImpl.this.status = Newsletter.STATUS_ERROR;
				}
			}
		});
		thread.setName("bundlo-newsletter-sender");
		thread.setPriority(Thread.MIN_PRIORITY);
		infoMap = new HashMap<String, Object>();
		infoMap.put("percentComplete", 1);
		this.status = Newsletter.STATUS_SENDING;
		thread.start();
		*/
	}
	
	@Override
	public void send(String email, String subject, String template,
			Map<String, Object> params) throws NewsletterException {
		if(params == null) {
			params = new HashMap<String, Object>();
		}
		Map<String, String> si = getSubscriberInfo(email);
		if(si == null) {
			si = new HashMap<String, String>();
		}
		si.put("email", email);
		params.put("subscriber", si);
		String msg = this.template.formatStr(template, params, "#newsletter.send");
		try {
			emailSender.sendHtmlMail(email, subject, msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NewsletterException(e);
		}
	}

	public void setPercentComplete(int percentComplete) {
		try {
			this.getNode().setProperty("percentComplete", percentComplete);
			this.getContentModel().getSession().save();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPercentComplete() {
		Long rv = (Long) getPropertyAs("percentComplete", PropertyType.LONG);
		if(rv == null) {
			return -1;
		}
		return rv.intValue();
	}
	
	public void setStatus(int status) {
		try {
			this.getNode().setProperty("status", status);
			this.getContentModel().getSession().save();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getStatus() {
		Long status = (Long) getPropertyAs("status", PropertyType.LONG);
		if(status == null) {
			return -100;
		}
		return status.intValue();
	}
	
	public Calendar getCreatedOn() {
		return (Calendar) getPropertyAs("createdOn", PropertyType.DATE);
	}
	
	public void setLastSendActiovationNow() {
		try {
			this.getNode().setProperty("lastSendActivation", Calendar.getInstance());
			this.getContentModel().getSession().save();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Calendar getLastSendActivation() {
		return (Calendar) getPropertyAs("lastSendActivation", PropertyType.DATE);
	}

	@Override
	public Map<String, Object> getInfo() {
		Map<String, Object> rv = new HashMap<String, Object>();
		rv.put("status", getStatus());
		rv.put("percentComplete", getPercentComplete());
		rv.put("lastSendActivation", getLastSendActivation());
		rv.put("createdOn", getCreatedOn());
		return rv;
	}


	public void setTemplate(VTemplate template) {
		this.template = template;
	}


	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}


	public void reset() {
		setStatus(STATUS_DEFAULT);
	}
}
