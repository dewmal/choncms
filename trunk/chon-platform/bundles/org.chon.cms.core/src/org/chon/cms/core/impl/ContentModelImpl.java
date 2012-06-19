package org.chon.cms.core.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.core.auth.User;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.IContentNodeFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ContentModelImpl implements ContentModel {
	private static final Log log = LogFactory.getLog(ContentModelImpl.class);
	
	private String username = "guest";
	private Session session;
	
	//references to important node are cached on ContentModel instance creation
	private Map<String, IContentNode> cache = new HashMap<String, IContentNode>();

	private BundleContext bundleContext;
	
	public ContentModelImpl(User user, BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		
		if(user != null) {
			username = user.getName();
		}
		ServiceReference ref = bundleContext.getServiceReference(Repository.class.getName());
		Repository repository = (Repository) bundleContext.getService(ref);
		try {
			this.session = repository.login(new SimpleCredentials(username, username.toCharArray()));
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
		
		//cache.put("home", getContentNode("/home/"));
		//cache.put("var", getContentNode("/var/"));
		cache.put("etc", getContentNode("/etc/"));
		cache.put("tmp", getContentNode("/tmp/"));
		
		cache.put("usr.local", getContentNode("/usr/local"));
		cache.put("usr.local.etc", getContentNode("/usr/local/etc"));
		
		if("root".equals(username)) {
			cache.put("userHome", getContentNode("/root/"));
		} else {
			//TODO: create user dir if not exists?
			cache.put("userHome", getContentNode("/home/"+username));
		}
		
		cache.put("www", getContentNode("/www/"));
		cache.put("user", getConfigNode().getChild("passwd").getChild(username));
	}
	
	@Override
	public IContentNode getContentNode(String path) {
		try {
			Node node = session.getNode(path);
			return getContentNode(node);
		} catch (PathNotFoundException e) {
			log.warn("No node found at path: " + path);
		} catch (RepositoryException e) {
			log.error("Repository Error", e);
		}
		return null;
	}

	@Override
	public IContentNode getContentNode(Node node) {
		String type = getNodeType(node);
		IContentNode typeDesc = getTypeDesc(type);
		
		String rendererClass = null;
		String contentNodeClass = null;
		if(typeDesc != null) {
			rendererClass = typeDesc.prop("renderer");
			contentNodeClass = typeDesc.prop("contentNode");
		}
		return createContentNode(node, typeDesc, rendererClass, contentNodeClass);
	}

	private IContentNode createContentNode(Node node, IContentNode typeDesc,
			String rendererClass, String contentNodeClass) {

		String path = null;
		// TODO: factories with properties
		ServiceReference[] refs;
		try {
			path = node.getPath();
			refs = bundleContext.getServiceReferences(
					IContentNodeFactory.class.getName(), null);
			if (refs != null && refs.length > 0) {
				for (ServiceReference r : refs) {
					IContentNodeFactory factory = (IContentNodeFactory) bundleContext
							.getService(r);
					if (factory.canCreate(contentNodeClass)) {
						return factory.createIntance(contentNodeClass, this,
								node, typeDesc);
					}
				}
			}
		} catch (Exception e) {
			log.error("Unknownd error while creating content node ", e);
		}
		log.error("Cant create IContentNode for " + path + " and type " + typeDesc);
		return null;
	}

	@Override
	public String getNodeType(Node node) {
		try {
			return node.getProperty("type").getString();
		} catch (Exception e) {
			
			try {
				String jcrNodeType = node.getPrimaryNodeType().getName();
				if(!"nt:unstructured".equals(jcrNodeType)) {
					return jcrNodeType;
				} else {
					return "unknown";
				}
			} catch (RepositoryException e1) {
				log.error("Error getting node type", e);
			}
		}
		return ".";
	}

	@Override
	public IContentNode getPublicNode() {
		return cache.get("www");
	}

	@Override
	public IContentNode getPublicNode(String path) {
		return getPublicNode().getChild(path);
	}

	@Override
	public IContentNode getTmpNode(String path) {
		return cache.get("tmp").getChild(path);
	}

	@Override
	public IContentNode getUserHome() {
		return cache.get("userHome");
	}

	@Override
	public IContentNode getConfigNode() {
		return cache.get("etc");
	}

	@Override
	public IContentNode getConfigNode(String path) {
		return getConfigNode().getChild(path);
	}
	
	@Override
	public IContentNode getTypeDesc(String type) {
		
		if (type == null || 
				(type.startsWith("sys") && !type.equals("sys.public"))
				|| 
				type.equals("typeDesc")) {
			return null;
		}
		IContentNode typeDesc = getConfigNode("types/" + type);
		if(typeDesc == null) {
			typeDesc = getConfigNode("types/unknown");
		}
		return typeDesc;
	}

	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public QueryResult query(String q, String type, Integer start, Integer limit) throws RepositoryException {
		log.debug(q);
		Workspace ws = session.getWorkspace();
		QueryManager qm = ws.getQueryManager();
		Query query = qm.createQuery(q, type);
		if(start != null) {
			query.setOffset(start);
		}
		if(limit != null) {
			query.setLimit(limit);
		}
		QueryResult result = query.execute();
		return result;
	}

	@Override
	public IContentNode getUser() {
		return cache.get("user");
	}

	@Override
	public IContentNode getConfigNode(String path, boolean createIfNotExists) {
		IContentNode node = getConfigNode(path);
		if(createIfNotExists && node==null) {
			node = createConfigNode(path, getConfigNode());
		}
		return node;
	}

	private IContentNode createConfigNode(String path, IContentNode cfgRoot) {
		IContentNode rv = null;
		try {
			Node etcNode = cfgRoot.getNode(); 
			Node n = etcNode.addNode(path);
			n.setProperty("type", "config");
			etcNode.getSession().save();
			rv = getContentNode(n);
		} catch (PathNotFoundException e) {
			log.error("Path not found, it must exists we are trying to create new node at that path", e);
		} catch (RepositoryException e) {
			log.error("Unknown repositorry error", e);
		}
		return rv;
	}

	@Override
	public IContentNode getAppsNode() {
		return cache.get("usr.local");
	}

	@Override
	public IContentNode getAppsNode(String path) {
		return getAppsNode().getChild(path);
	}

	@Override
	public IContentNode getAppsConfigNode() {
		return cache.get("usr.local.etc");
	}

	@Override
	public IContentNode getAppsConfigNode(String app) {
		return getAppsConfigNode().getChild(app);
	}

	@Override
	public IContentNode getAppsConfigNode(String app, boolean createIfNotExists) {
		IContentNode node = getAppsConfigNode(app);
		if(createIfNotExists && node==null) {
			node = createConfigNode(app, getAppsConfigNode());
		}
		return node;
	}

	@Override
	protected void finalize() throws Throwable {
		if(this.session != null && this.session.isLive()) {
			this.session.logout();
		}
		super.finalize();
	}
	
}