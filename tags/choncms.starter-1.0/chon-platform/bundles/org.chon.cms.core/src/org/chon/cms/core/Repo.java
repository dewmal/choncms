package org.chon.cms.core;

import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.jcr.client.service.RepoService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


public class Repo {
	private static Log log = LogFactory.getLog(Repo.class);
	private static BundleContext bundleContext;
	
	public static void init(BundleContext ctx) throws Exception {
		bundleContext = ctx;
	}
	
	public static RepoService getRepoService() {
		ServiceReference ref = bundleContext.getServiceReference(RepoService.class.getName());
		RepoService repoService = (RepoService) bundleContext.getService(ref);
		return repoService;
	}
	
	public static Session createSession(String user, String pass) {
		ServiceReference ref = bundleContext.getServiceReference(Repository.class.getName());
		Repository repository = (Repository) bundleContext.getService(ref);
		try {
			return repository.login(new SimpleCredentials(user, pass.toCharArray()));
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchWorkspaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
