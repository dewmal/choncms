package org.chon.cms.wiki.nodes;

import java.util.Map;

import org.chon.cms.core.Extension;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.wikimodel.wem.IWikiParser;

public class WikiChonExtension implements Extension {
	private static final String DEFAULT_WIKI_PARSER = "mediawiki";
	
	private BundleContext context;

	public WikiChonExtension(BundleContext bundleContext) {
		this.context = bundleContext;
	}

	@Override
	public Map<String, Action> getAdminActons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		IWikiParser parser = getDefaultWikiParser(context);
		if(parser != null) {
			return new WikiChon(req, resp, node, parser);
		} else {
			throw new RuntimeException(IWikiParser.class.getName() + " parser service not found in bundle context.");
		}
	}

	private IWikiParser getDefaultWikiParser(BundleContext context) {
		String type = DEFAULT_WIKI_PARSER;
		
		ServiceReference[] refs;
		try {
			refs = context.getServiceReferences(
					IWikiParser.class.getName(), "(type=" + type + ")");
			if (refs != null && refs.length > 0) {
				return (IWikiParser) context.getService(refs[0]);
			}
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
