package com.choncms.webpage.forms.workflow.impl;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.services.mailer.pub.EmailSender;
import org.chon.web.api.Application;
import org.json.JSONException;
import org.json.JSONObject;

import com.choncms.webpage.forms.workflow.Workflow;
import com.choncms.webpage.forms.workflow.WorkflowResult;
import com.choncms.webpage.forms.workflow.WorkflowResultError;
import com.choncms.webpage.forms.workflow.WorkflowResultOK;


public class EmailWorkflow implements Workflow {
	private static final Log log = LogFactory.getLog(EmailWorkflow.class);
	
	private EmailSender emailSender;
	private boolean async;
	private Application app;

	public EmailWorkflow(EmailSender emailSender, boolean async) {
		this.emailSender = emailSender;
		this.async = async;
	}
	
	@Override
	public String getName() {
		return "EmailWorkflow";
	}

	private void sendEmail(final String emailTo, final String  subject, final String message) throws MessagingException {
		if(this.async) {
			log.debug("Initiating async sending of email");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						log.debug("Sending email ... ");
						emailSender.sendHtmlMail(emailTo, subject, message);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			log.debug("Sending email ... ");
			emailSender.sendHtmlMail(emailTo, subject, message);
		}
	}
	
	@Override
	public WorkflowResult process(IContentNode formNode, Map<String, Object> formData, JSONObject cfg) {

		try {
			String emailTo = cfg.getString("emailTo");
			
			String subject = cfg.optString("subject", "Form " + formNode.getName() + " submit data");
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("def", formNode); //form definition
			params.put("data", formData); //form submit
			
			String message = null;
			String emailTemplate = cfg.optString("emailTemplate");
			if(emailTemplate == null) {
				String emailTemplateStr = cfg.optString("emailTemplateStr");
				message = app.getTemplate().formatStr(emailTemplateStr, params, formNode.getName() + "#dyn-template");
			} else {
				message = app.getTemplate().format(emailTemplate, params, null);
			}
			sendEmail(emailTo, subject, message);
		} catch (Exception e) {
			log.error("Exception occured while processing email workflow for form " + formNode.getName(), e);
			return new WorkflowResultError(e);
		}
		//app.getTemplate().format("email.html", params, null);
		return WorkflowResultOK.SUCCESS;
	}

	@Override
	public void init(Application app) {
		this.app = app;
	}

	@Override
	public String getDefaultJSONConfiguration() {
		JSONObject cfg;
		try {
			cfg = new JSONObject("{ " +
					"emailTo: 'mail@example.com', " +
					"subject: 'Optional', " +
					"emailTemplate: 'html/page/for/email:Optional',  " +
					"emailTemplateStr: 'inline velocity template string:Optional'" +
					"}");
			return cfg.toString(2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
