package com.choncms.webpage.forms.workflow;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.chon.cms.model.content.IContentNode;
import org.chon.cms.services.mailer.pub.EmailSender;
import org.json.JSONException;
import org.json.JSONObject;


public class EmailWorkflow extends DefaultWorkflow {
	private EmailSender emailSender;
	private boolean async;

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
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						emailSender.sendHtmlMail(emailTo, subject, message);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			emailSender.sendHtmlMail(emailTo, subject, message);
		}
	}
	
	@Override
	public String process(IContentNode formNode, Map<String, Object> formData) {
		
		
		String workflowConfig = formNode.get("workflowConfig");
		try {
			JSONObject cfg = new JSONObject(workflowConfig);
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//save data??
			return Workflow.ERROR;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			super.process(formNode, formData);
			return Workflow.ERROR;
		}
		//app.getTemplate().format("email.html", params, null);
		return Workflow.SUCCESS;
	}

}
