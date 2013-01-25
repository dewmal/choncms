package org.chon.jcr.client;

import java.io.File;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;

//import org.apache.jackrabbit.core.TransientRepository;
//import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.chon.jcr.client.service.RepoService;
import org.chon.jcr.client.service.impl.RepoServicesImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;



public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		//connectToRemoteRepo();
		
		RepoService repoService = new RepoServicesImpl();
		context.registerService(RepoService.class.getName(), repoService, null);
		/*
		File repoDir = new File(System.getProperty("repo.dir"));
		Repository repo = new TransientRepository(repoDir);
		context.registerService(Repository.class.getName(), repo, null);
		*/
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		//System.out.println("Goodbye World!!");
	}
	/*
	
	private void connectToRemoteRepo() throws Exception {
		Repository repository = new URLRemoteRepository("http://localhost:7200/rmi");
		Session session = repository.login();
		Node root = session.getRootNode();
		print(root, 0);
		System.out.println(root);
		System.out.println(session);
	}

	private static void print(Node node, int tabs) throws RepositoryException {
		for(int i=0; i<tabs; i++)
			System.out.print("    ");
		System.out.println(node.getName());
		NodeIterator it = node.getNodes();
		while(it.hasNext()) {
			Node child = it.nextNode();
			print(child, tabs+1);
		}
	}
	

	public static void main(String[] args) {
		Session session = null;
		try {
			Repository repo = new TransientRepository(new File("c:/temp/repo3"));
			String[] keys = repo.getDescriptorKeys();
			for(String k : keys) {
				System.out.println(k + ": " + repo.getDescriptor(k));
			}
			session = repo.login();
			//session.exportDocumentView("/", System.out, true, false);
			session.getRootNode().getNode("_drafts").checkin();
			
			VersionHistory vh = session.getRootNode().getNode("_drafts").getVersionHistory();
			VersionIterator it = vh.getAllVersions();
			while(it.hasNext()) {
				System.out.println(it.nextVersion());
			}
			System.out.println(session.getRootNode());
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(session!=null)
				session.logout();
		}
	}*/
}
