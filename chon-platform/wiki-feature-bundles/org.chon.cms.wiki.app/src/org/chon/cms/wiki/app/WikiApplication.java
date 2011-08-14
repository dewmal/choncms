package org.chon.cms.wiki.app;

import java.util.Properties;

import org.chon.core.velocity.VTemplate;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Resource;
import org.chon.web.api.ServerInfo;

public class WikiApplication implements Application {

	public static final String WIKI_PAGE_NOT_EXISTS_REDIRECT = "wiki-page-does-not-exists";
	
	public static final String WIKI_NEW_PAGE_NODE_TYPE = "wiki.new.page";
	public static final String WIKI_PAGE_NODE_TYPE = "wiki.page";
	
	private Resource wikiNewResource = new Resource() {
		@Override
		public void process(ServerInfo si) {
			// Since this app has lowest priority when we are here that means
			// resource not found
			String path = si.getReq().getPath();
			// just redirect to create new wiki page ...
			String siteUrl = System.getProperty("siteUrl");
			si.getResp().setRedirect(siteUrl + "/"+WIKI_PAGE_NOT_EXISTS_REDIRECT+"?title=" + path);
		}

	};

	private Properties props = new Properties();

	public WikiApplication() {
		props.setProperty(Application.PRIORITY, "1000");
	}

	@Override
	public Properties getAppProperties() {
		return props;
	}

	@Override
	public Resource getResource(Request req) {
		String path = req.getPath();
		if(path.startsWith(WIKI_PAGE_NOT_EXISTS_REDIRECT)) {
			//also WIKI_PAGE_NOT_EXISTS_REDIRECT does not exits, return not found
			return null;
		}
		
		return wikiNewResource;
	}

	@Override
	public VTemplate getTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepareRequest(Resource r, ServerInfo si) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleException(Exception e, Resource r, ServerInfo si) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finalizeRequest(Resource r, ServerInfo si) {
		// TODO Auto-generated method stub

	}

}
