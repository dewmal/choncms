package com.choncms.webpage.forms;

import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.services.mailer.pub.EmailSender;
import org.chon.cms.services.mailer.pub.EmailerFactoryService;
import org.chon.web.api.Application;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.choncms.webpage.forms.workflow.Workflow;
import com.choncms.webpage.forms.workflow.impl.DefaultWorkflow;
import com.choncms.webpage.forms.workflow.impl.EmailWorkflow;
import com.choncms.webpage.forms.workflow.impl.MultiWorkflow;
import com.choncms.webpage.forms.workflow.impl.SimpleSaveWorkflow;

public class WorkflowUtils {
	private static final Log log = LogFactory.getLog(WorkflowUtils.class);
	
	static class WorkflowServiceTracker extends ServiceTracker {
		public WorkflowServiceTracker(BundleContext context) {
			super(context, Workflow.class.getName(), null);
		}
		
		@Override
		public Object addingService(ServiceReference reference) {
			Object service = super.addingService(reference);
			onWorkflowServiceAdded((Workflow) service);
			return service;
		}
	}
	static class EmailerFactoryServiceTracker extends ServiceTracker {
		public EmailerFactoryServiceTracker(BundleContext context) {
			super(context, EmailerFactoryService.class.getName(), null);
		}
		
		@Override
		public Object addingService(ServiceReference reference) {
			Object service = super.addingService(reference);
			onEmailerFactoryServiceAdded((EmailerFactoryService) service);
			return service;
		}
	}
	
	private static final Workflow DEFAULT_WORKFLOW = new DefaultWorkflow();
	
	private static BundleContext bundleContext;
	private static Application app;
	
	private static WorkflowServiceTracker workflowServiceTracker; 
	private static EmailerFactoryServiceTracker emailerFactoryServiceTracker;
	private static JSONObject config;
	
	public static void init(BundleContext bundleContext, Application app, JSONObject config) throws Exception {
		WorkflowUtils.bundleContext = bundleContext;
		WorkflowUtils.app = app;
		WorkflowUtils.config = config;
		
		bundleContext.registerService(Workflow.class.getName(), DEFAULT_WORKFLOW, null);
		bundleContext.registerService(Workflow.class.getName(), new SimpleSaveWorkflow(), null);
		bundleContext.registerService(Workflow.class.getName(), new MultiWorkflow(), null);
		
//		Workflow[] wfs = getRegisteredWorkflows();
//		for(int i=0; i<wfs.length; i++) {
//			wfs[i].init(app);
//		}
		
		emailerFactoryServiceTracker = new EmailerFactoryServiceTracker(bundleContext);
		emailerFactoryServiceTracker.open();
		
		workflowServiceTracker = new WorkflowServiceTracker(bundleContext);
		workflowServiceTracker.open();
	}
	
	protected static void onEmailerFactoryServiceAdded(EmailerFactoryService service) {
		Properties properties = new Properties();
		try {
			JSONObject emailWorkflowConfig = config.getJSONObject("emailWorkflowConfig");
			boolean isAsync = emailWorkflowConfig.optBoolean("async", true);
			@SuppressWarnings("rawtypes")
			Iterator it = emailWorkflowConfig.keys();
			while(it.hasNext()) {
				String k = (String) it.next();
				String v = emailWorkflowConfig.getString(k);
				properties.put(k, v);
			}
			
			log.debug("Initializing email service with properties " + properties);
			EmailSender emailSender = service.getEmailSender(properties);
			bundleContext.registerService(Workflow.class.getName(), new EmailWorkflow(emailSender, isAsync), null);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void close() {
		WorkflowUtils.bundleContext = null;
		WorkflowUtils.app = null;
		WorkflowUtils.config = null;
		if(workflowServiceTracker != null) {
			workflowServiceTracker.close();
			emailerFactoryServiceTracker.close();
		}
	}
	
	protected static void onWorkflowServiceAdded(Workflow service) {
		if(app != null) {
			log.info(" -- Init workflow " + service.getName());
			service.init(app);
		} else {
			log.warn("Warning: Workflow registered without init...");
		}
	}

	public static Workflow [] getRegisteredWorkflows() throws Exception {
		Workflow [] rv = null;
		String filter = "(objectclass=" + Workflow.class.getName() + ")";
		if(bundleContext == null) {
			throw new Exception("Invalid " + WorkflowUtils.class.getName() + " state. BundleContext is null. Please call init.");
		}
		ServiceReference[] refs = bundleContext.getServiceReferences(null, filter);
		if(refs != null) {
			rv = new Workflow[refs.length];
			for(int i=0; i<refs.length; i++) {
				rv[i] = (Workflow) bundleContext.getService(refs[i]);
			}
		}
		return rv;
	}
	
	public static Workflow getWorkflow(String name) {
		if(name == null) {
			return DEFAULT_WORKFLOW;
		}
		try {
			Workflow[] wfs = getRegisteredWorkflows();
			
			if(wfs == null) {
				return DEFAULT_WORKFLOW;
			}
			
			for(int i=0; i<wfs.length; i++) {
				if(name.equals(wfs[i].getName())) {
					return wfs[i];
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return DEFAULT_WORKFLOW;
	}
}
