package org.chon.cms.ui.newsletter;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.chon.cms.content.utils.ChonTypeUtils;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.core.model.types.ContentNode;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.INodeRenderer;
import org.chon.cms.services.newsletter.NewsletterSystem;
import org.osgi.framework.BundleContext;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected void registerExtensions(JCRApplication app) {
		ContentModel contentModel = app.createContentModelInstance(getName());
		try {
			createNewletterStruct(contentModel.getPublicNode().getNode());
			registerNewsletterNodeRenderer(getBundleContext());
			Map<String, String> properties = new HashMap<String, String>();
			Map<String, String> defaultNodeProperties = new HashMap<String, String>();
			ChonTypeUtils.registerType(contentModel, NewsletterSystem.NEWSLETTER_PUBLIC_NODE, ContentNode.class, NewsletterRenderer.class, properties, defaultNodeProperties);
			contentModel.getSession().save();
			NewsletterExtension ext = new NewsletterExtension(getName(), getConfig(), getBundleContext());
			app.regExtension(getName(), ext);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void registerNewsletterNodeRenderer(BundleContext bundleContext) throws InstantiationException, IllegalAccessException {
		NewsletterRenderer newsletterRenderer = new NewsletterRenderer(getBundleContext());
		registerNodeRenderer(bundleContext, newsletterRenderer);
	}
	
	private void registerNodeRenderer(BundleContext bundleContext, INodeRenderer instance) {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("renderer", instance.getClass().getName());
		bundleContext.registerService(INodeRenderer.class.getName(), instance, props);
	}
	
	private Node createNewletterStruct(Node node) throws RepositoryException {
		Node rv = null;
		if(node.hasNode(NewsletterExtension.NEWESLETTER_PUBLIC_CONTAINER_NAME)) {
			rv = node.getNode(NewsletterExtension.NEWESLETTER_PUBLIC_CONTAINER_NAME);
		} else {
			rv = node.addNode(NewsletterExtension.NEWESLETTER_PUBLIC_CONTAINER_NAME);
			rv.setProperty("type", NewsletterSystem.NEWSLETTER_PUBLIC_CONTAINER);
			rv.getSession().save();
		}
		return rv;
	}

	@Override
	protected String getName() {
		return "org.chon.cms.ui.newsletter";
	}
	
}
