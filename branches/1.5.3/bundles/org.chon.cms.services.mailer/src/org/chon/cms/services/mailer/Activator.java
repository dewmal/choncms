package org.chon.cms.services.mailer;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.services.mailer.impl.EmailFactoryServiceImpl;
import org.chon.cms.services.mailer.pub.EmailerFactoryService;
import org.osgi.framework.BundleContext;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected void registerExtensions(JCRApplication app) {
		BundleContext ctx = getBundleContext();
		EmailerFactoryService factory = new EmailFactoryServiceImpl();
		ctx.registerService(EmailerFactoryService.class.getName(), factory,
				null);
	}

	@Override
	protected String getName() {
		return "org.chon.cms.services.mailer";
	}
	
}
