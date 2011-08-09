package org.choncms.bnd.libs;

import java.io.File;

import javax.jcr.Repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.core.TransientRepository;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static final Log log = LogFactory.getLog(Activator.class);
	
	private Repository repository = null;
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		log.debug("Activating bnd.libs ");
		
		File repoDir = new File(System.getProperty("repo.dir"));
		log.info("Registering javax.jcr.Repository service!");
		repository = new TransientRepository(repoDir);
		context.registerService(Repository.class.getName(), repository, null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if(repository != null && repository instanceof TransientRepository) {
			((TransientRepository)repository).shutdown();
		}
	}

}
