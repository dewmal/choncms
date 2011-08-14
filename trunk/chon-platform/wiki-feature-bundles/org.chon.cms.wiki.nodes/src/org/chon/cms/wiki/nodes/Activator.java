package org.chon.cms.wiki.nodes;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.IContentNodeFactory;
import org.chon.cms.wiki.app.WikiApplication;
import org.osgi.framework.BundleContext;

public class Activator extends ResTplConfiguredActivator {

	
	

	private void init() {
		BundleContext context = getBundleContext();
		try {
			Helper.registerNodeRenderer(context, WikiNewPageNodeRenderer.class);
			Helper.registerNodeRenderer(context, WikiPageNodeRenderer.class);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IContentNodeFactory factory = Helper.createIContentNodeFactory(WikiPageContentNode.class);
		Helper.registerContentNodeFactory(context, factory);
		
		
		ContentModel cm = getJCRApp().createContentModelInstance(getName());
		IContentNode wiki_new_page = cm.getPublicNode(WikiApplication.WIKI_PAGE_NOT_EXISTS_REDIRECT);
		if(wiki_new_page == null) {
			WikiChonModel wcm = new WikiChonModel();
			wiki_new_page = wcm.install(cm);
		}
		assertNotNull(wiki_new_page);
		
		
		
	}
	

	private void assertNotNull(IContentNode wiki_new_page) {
		if(wiki_new_page == null) {
			throw new RuntimeException(
					WikiApplication.WIKI_PAGE_NOT_EXISTS_REDIRECT
							+ " can not be created");
		}
	}


	@Override
	protected void registerExtensions(JCRApplication app) {
		init();
		app.regExtension("wiki", new WikiChonExtension(getBundleContext()));
	}

	@Override
	protected String getName() {
		return "org.chon.cms.wiki.nodes";
	}

}
