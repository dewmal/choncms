package org.chon.cms.light.mvc;

import java.util.Hashtable;
import java.util.Map;

import org.chon.cms.content.utils.ChonTypeUtils;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.light.mvc.impl.ActionNodeRenderer;
import org.chon.cms.light.mvc.impl.LightMVCServiceImpl;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.INodeRenderer;


public class Activator extends ResTplConfiguredActivator {

	@Override
	protected void registerExtensions(JCRApplication app) {
		// TODO register extensions
		
		try {
			ContentModel cm = app.createContentModelInstance(getName());
			
			Map<String, String> properties = null;
			Map<String, String> defaultNodeProperties = null;
			
			ChonTypeUtils.registerType(cm, ActionsRoot.TYPE, ActionsRoot.class,
					ActionsRootRenderer.class, properties,
					defaultNodeProperties);
			
			ChonTypeUtils.registerContnetNodeClass(getBundleContext(), ActionsRoot.class);
			ChonTypeUtils.registerNodeRenderer(getBundleContext(), ActionsRootRenderer.class);
			
			ChonTypeUtils.registerType(cm, ActionNode.TYPE, ActionNode.class,
					ActionNodeRenderer.class, properties,
					defaultNodeProperties);
			
			ChonTypeUtils.registerContnetNodeClass(getBundleContext(), ActionNode.class);
			
			LightMVCServiceImpl mvc = new LightMVCServiceImpl(cm);
			registerNodeRenderer(new ActionNodeRenderer(mvc));
			
			getBundleContext().registerService(LightMVCService.class.getName(), mvc, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		getBundleContext().registerService(Application.class.getName(),
				new MVCApplication(app), null);
		*/
	}

	@Override
	protected String getName() {
		return "org.chon.cms.light.mvc";
	}
	
	private void registerNodeRenderer(INodeRenderer instance) {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("renderer", instance.getClass().getName());
		getBundleContext().registerService(INodeRenderer.class.getName(), instance, props);
	}
	
}
