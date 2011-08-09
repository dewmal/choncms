package org.chon.cms.content;

import org.chon.cms.content.ext.content.ContentExtension;
import org.chon.cms.content.ext.label.LabelsExtension;
import org.chon.cms.content.ext.news.NewsExtension;
import org.chon.cms.content.ext.utils.UtilsExtension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;

public class Activator extends ResTplConfiguredActivator {

	@Override
	protected String getName() {
		return "org.chon.cms.content";
	}

	@Override
	protected void registerExtensions(JCRApplication app) {
		app.regExtension("utils", new UtilsExtension(app));
		app.regExtension("content", new ContentExtension(app));
		app.regExtension("label", new LabelsExtension(app));
		//app.regExtension("menu", new MenuExtension(app));
		app.regExtension("news", new NewsExtension(app));
	}


}
