package org.chon.cms.services.newsletter;

import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.services.mailer.pub.EmailSender;
import org.chon.cms.services.mailer.pub.EmailerFactoryService;
import org.chon.cms.services.newsletter.impl.NewsletterContentModelInitializer;
import org.chon.cms.services.newsletter.impl.NewsletterSystemImpl;
import org.chon.web.api.Application;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

public class Activator extends ResTplConfiguredActivator {
	
	private static final Log log = LogFactory.getLog(Activator.class);
	
	class EmailerFactoryServiceTracker extends ServiceTracker {
		public EmailerFactoryServiceTracker(BundleContext context) {
			super(context, EmailerFactoryService.class.getName(), null);
		}
		
		@Override
		public Object addingService(ServiceReference reference) {
			Object service = super.addingService(reference);
			onEmailerFactoryServiceAdded((EmailerFactoryService) service);
			return service;
		}
	}
	
	EmailerFactoryServiceTracker st = null;
	private ContentModel contentModel;
	
	protected void onEmailerFactoryServiceAdded(EmailerFactoryService factory) {
		log.debug("onEmailerFactoryServiceAdded");
		try {
			JSONObject cfg = getConfig();
			JSONObject emailerConfig = cfg.getJSONObject("emailer");
			Iterator<?> it = emailerConfig.keys();
			Properties emailerProps = new Properties();
			while (it.hasNext()) {
				String k = (String) it.next();
				String v = emailerConfig.getString(k);
				emailerProps.put(k, v);
			}
			EmailSender emailSender = factory.getEmailSender(emailerProps);
			log.debug("Found emailer service ...");
			
			String nodeName = cfg.getJSONObject("config").optString("newsletterSystemNodeName", "newsletter");
			IContentNode newsletterRootNode = contentModel.getAppsConfigNode(nodeName, true);
			NewsletterContentModelInitializer initializer = new NewsletterContentModelInitializer();
			initializer.init(getBundleContext(), newsletterRootNode);
			registerNewsletterSystem(emailSender, newsletterRootNode, this.getJCRApp());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void registerNewsletterSystem(EmailSender emailSender, IContentNode newletterRoot, Application app) {
		log.debug("registering newsletter system");
		NewsletterSystem newsletterSystem = new NewsletterSystemImpl(newletterRoot, app.getTemplate(), emailSender);
		getBundleContext().registerService(NewsletterSystem.class.getName(),
				newsletterSystem, null);
	}

	@Override
	protected void registerExtensions(JCRApplication app) {
		this.contentModel = app.createContentModelInstance(getName());
		ServiceReference ref = getBundleContext().getServiceReference(
				EmailerFactoryService.class.getName());
		if (ref != null) {
			EmailerFactoryService factory = (EmailerFactoryService) getBundleContext()
					.getService(ref);
			onEmailerFactoryServiceAdded(factory);
		} else {
			st = new EmailerFactoryServiceTracker(getBundleContext());
			st.open();
		}
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		if(st != null) {
			st.close();
		}
		super.stop(context);
	}

	@Override
	protected String getName() {
		return "org.chon.cms.services.newsletter";
	}
	
}
