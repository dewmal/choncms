package org.chon.cms.services.newsletter.impl;

import org.chon.cms.content.utils.ChonTypeUtils;
import org.chon.cms.core.model.types.ContentNode;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.services.newsletter.NewsletterSystem;
import org.osgi.framework.BundleContext;

public class NewsletterContentModelInitializer {	
	public void init(BundleContext bundleContext, IContentNode newsletterRootNode)  throws Exception {
		ChonTypeUtils.registerContnetNodeClass(bundleContext, NewsletterContentNode.class);
		ContentModel contentModel = newsletterRootNode.getContentModel();
		ChonTypeUtils.registerType(contentModel, NewsletterSystem.NEWSLETTER_CONTAINER,
				NewsletterContentNode.class, null, null, null);
		ChonTypeUtils.registerType(contentModel, NewsletterSystem.NEWSLETTER_SUBSCRIBER,
				ContentNode.class, null, null, null);
	}
}
