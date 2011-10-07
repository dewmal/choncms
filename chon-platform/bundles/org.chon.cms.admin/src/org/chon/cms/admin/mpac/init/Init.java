package org.chon.cms.admin.mpac.init;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.cms.core.auth.AuthenticationProvider;
import org.chon.cms.core.auth.InvalidUserException;
import org.chon.cms.core.auth.User;
import org.chon.cms.model.ContentModel;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.InitStatusInfo;
import org.chon.web.mpac.Initializer;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;



public class Init implements Initializer {
	private static final Log log = LogFactory.getLog(Init.class);
	private BundleContext bundleContext;
	private JSONObject config;
	
	public Init(BundleContext bundleContext, JSONObject config) {
		this.bundleContext = bundleContext;
		this.config = config;
	}
	
	public InitStatusInfo process(Application app, Request req, Response resp) {
		if ("logout".equals(req.getAction())) {
			req.removeUserFromSession();
			req.getServletRequset().getSession().removeAttribute(ContentModel.KEY);
		} else if ("login".equals(req.getAction())) {
			boolean loginOk = false;
			List<AuthenticationProvider> authProviders = getAuthenticationProvider(bundleContext);
			for (AuthenticationProvider authProvider : authProviders) {
				try {
					User u = authProvider.login(req.get("user", ""), req.get("pass", ""));
					req.putUserInSession(u);
					req.getServletRequset().getSession().removeAttribute(ContentModel.KEY);
					resp.getTemplateContext().put("user", req.getUser());
					resp.setRedirect("index.do");
					loginOk = true;
					break;
				} catch (InvalidUserException e) {
				}
			}

			return loginOk ? InitStatusInfo.REDIRECT
					: InitStatusInfo.LOGIN_ERROR;

		}

		if (req.getUser() == null) {
			return InitStatusInfo.NOT_AUTH;
		}
		
		return checkUserPrivileges((User) req.getUser(), app, req, resp);
	}

	private InitStatusInfo checkUserPrivileges(User user, Application app,
			Request req, Response resp) {
		if(user.getRole() >= config.optInt("minAdminRole")) { 
			return InitStatusInfo.OK;
		}
		return InitStatusInfo.ACCESS_DENIED;
	}

	private List<AuthenticationProvider> getAuthenticationProvider(BundleContext ctx) {
		ServiceReference[] refs;
		try {
			refs = ctx.getServiceReferences(AuthenticationProvider.class.getName(), null);
			List<AuthenticationProvider> authProviders = new ArrayList<AuthenticationProvider>();
			if(refs != null && refs.length>0) {
				for(ServiceReference ref : refs) {
					AuthenticationProvider authenticationProvider = (AuthenticationProvider) ctx.getService(ref);
					authProviders.add(authenticationProvider);
				}
			}
			if(authProviders.size()<=0) {
				log.warn("No AuthenticationProvider found in context, register OSGI service org.chon.cms.core.auth.AuthenticationProvider");
			}
			return authProviders;
		} catch (InvalidSyntaxException e) {
			log.error("Error getting auth providers", e);
		}
		return null;
	}
}
