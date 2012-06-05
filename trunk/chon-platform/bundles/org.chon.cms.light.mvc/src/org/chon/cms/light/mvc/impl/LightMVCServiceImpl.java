package org.chon.cms.light.mvc.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.light.mvc.AbstractAction;
import org.chon.cms.light.mvc.ActionNode;
import org.chon.cms.light.mvc.ActionsRoot;
import org.chon.cms.light.mvc.LightMVCService;
import org.chon.cms.model.ContentModel;


public class LightMVCServiceImpl implements LightMVCService {
	private static final Log log = LogFactory.getLog(LightMVCServiceImpl.class);
	
	private ContentModel cm;
	
	private Map<String, Class<? extends AbstractAction>> actions = new HashMap<String, Class<? extends AbstractAction>>();
	
	public LightMVCServiceImpl(ContentModel cm) {
		this.cm = cm;
	}

	@Override
	public void setupController(String root_path,
			Map<String, Class<? extends AbstractAction>> actions) throws Exception {
		
		if(cm.getPublicNode().getNode().hasNode(root_path)) {
			Node existingNode = cm.getPublicNode().getNode().getNode(root_path); 
			if( ! ActionsRoot.TYPE.equals( existingNode.getProperty("type").getString() ) ) {
				throw new Exception("Can't override node " + root_path + "; node type is not mvc created node. ");
			}
			log.info("Removing old node " + existingNode.getPath() );
			existingNode.remove();
		}
		
		Node www = cm.getPublicNode().getNode();
		Node n = www.addNode(root_path);
		n.setProperty("type", ActionsRoot.TYPE);
		for(String k : actions.keySet()) {
			Node a = n.addNode(k);
			a.setProperty("type", ActionNode.TYPE);
			a.setProperty("info", "Action node for action " + actions.get(k));
			this.actions.put(a.getPath(), actions.get(k));
		}
		n.getSession().save();
		log.info("Created controller at: " + n.getPath());
	}

	public AbstractAction createActionInstance(String path) throws InstantiationException, IllegalAccessException {
		Class<? extends AbstractAction> a = this.actions.get(path);
		return a.newInstance();
	}
}
