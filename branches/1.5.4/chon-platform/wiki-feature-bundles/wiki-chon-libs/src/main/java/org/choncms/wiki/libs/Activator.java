package org.choncms.wiki.libs;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.common.CommonWikiParser;
import org.wikimodel.wem.confluence.ConfluenceWikiParser;
import org.wikimodel.wem.mediawiki.MediaWikiParser;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		registerWikiParser(context, new CommonWikiParser(), "common");
		registerWikiParser(context, new ConfluenceWikiParser(), "confluence");
		registerWikiParser(context, new MediaWikiParser(), "mediawiki");
	}

	private void registerWikiParser(BundleContext context,
			IWikiParser instance, String type) {
		Hashtable props = new Hashtable();
		props.put("type", type);
		context.registerService(IWikiParser.class.getName(), instance, props);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
