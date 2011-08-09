package org.chon.cms.core.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.INodeRenderer;
import org.chon.cms.model.renderers.DefaultNodeRenderer;
import org.chon.web.api.Resource;
import org.chon.web.api.ServerInfo;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public class ContentResource implements Resource {
	private static final Log log = LogFactory.getLog(ContentResource.class);
	
	private IContentNode node;
	private BundleContext bundleContext;

	public ContentResource(IContentNode node, BundleContext bundleContext) {
		this.node = node;
		this.bundleContext = bundleContext;
	}
	
	@Override
	public void process(ServerInfo si) {
		ContentModel cm = (ContentModel) si.getReq().attr(ContentModel.KEY);
		INodeRenderer nodeRenderer = findNodeRenderer(cm);
		if(nodeRenderer != null) {
			nodeRenderer.render(node, si.getReq(), si.getResp(), si.getApplication(), si);
		} else {
			throw new RuntimeException("Error getting node renderer");
		}
	}

	private INodeRenderer findNodeRenderer(ContentModel cm) {
			INodeRenderer nodeRenderer = null;
		
		IContentNode typeDesc = cm.getTypeDesc(node.getType());
		if(typeDesc == null) {
			return new DefaultNodeRenderer();
		}
		
		String renderer = typeDesc.prop("renderer");
		if (renderer != null) {
			ServiceReference[] refs = null;
			try {
				refs = bundleContext.getServiceReferences(
						INodeRenderer.class.getName(), "(renderer=" + renderer
								+ ")");
			} catch (InvalidSyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (refs != null && refs.length > 0) {
				nodeRenderer = (INodeRenderer) bundleContext.getService(refs[0]);
			} else {
				log.warn("No service found for node renderer " + renderer);
				log.warn("Trying to load from class locader");
				nodeRenderer = loadClassAndCreateObj(renderer);
			}
		}
		if(nodeRenderer == null) {
			nodeRenderer = new DefaultNodeRenderer();
		}
		return nodeRenderer;
	}

	private INodeRenderer loadClassAndCreateObj(String renderer) {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends INodeRenderer> nodeRenderererClass = (Class<? extends INodeRenderer>) Thread
					.currentThread().getContextClassLoader()
					.loadClass(renderer);
			return nodeRenderererClass.newInstance();
		} catch (Exception e) {
			log.error("Error loading node renderer " + renderer, e);
		}
		return null;
	}

}
