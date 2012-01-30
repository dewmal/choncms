package org.chon.cms.services.newsletter.impl;

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.chon.cms.model.content.IContentNode;
import org.chon.cms.services.mailer.pub.EmailSender;
import org.chon.cms.services.newsletter.Newsletter;
import org.chon.cms.services.newsletter.NewsletterSystem;
import org.chon.core.velocity.VTemplate;


public class NewsletterSystemImpl implements NewsletterSystem {
	private IContentNode newsletterRoot;
	private VTemplate template;
	private EmailSender emailSender;
	
	//private VTemplate tpl = new VTemplate((URL[])null, 0);
	
	public NewsletterSystemImpl(IContentNode newsletterRoot, VTemplate template, EmailSender emailSender) {
		this.newsletterRoot = newsletterRoot;
		this.template = template;
		this.emailSender = emailSender;
	}
	
	@Override
	public Newsletter getNewsletter(String name) {
		NewsletterContentNode newsletter = (NewsletterContentNode) newsletterRoot.getChild(name);
		if(newsletter == null) {
			newsletter = createNewsletter(name);
		}
		newsletter.setTemplate(template);
		newsletter.setEmailSender(emailSender);
		return newsletter;
	}

	private NewsletterContentNode createNewsletter(String name) {
		Node root = newsletterRoot.getNode();
		try {
			Node n = root.addNode(name);
			n.setProperty("type", NewsletterSystem.NEWSLETTER_CONTAINER);
			n.setProperty("name", name);
			n.setProperty("status", Newsletter.STATUS_DEFAULT);
			n.setProperty("createdOn", Calendar.getInstance());
			n.setProperty("percentComplete", 0);
			n.setProperty("lastSendActivation", (Calendar)null);
			n.getSession().save();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (NewsletterContentNode) getNewsletter(name);
	}
}
