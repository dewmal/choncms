package org.chon.web.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.chon.web.util.FileInfo;


public class Request {
	private static final String USER_SESSION_KEY = "org.chon.core.bundlo.web.app.user";

	private ServerInfo serverInfo;
	
	private Map<String, String> cookies = new HashMap<String, String>();
	
	private Map<String, Object> pageContext = new HashMap<String, Object>();

	private String servletPath;
	private String extension;
	private String action;
	
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public Request(ServerInfo serverInfo) {
		serverInfo.setReq(this);
		this.serverInfo = serverInfo;
		this.init();
	}
	
	private void init() {
		this.parsePath();
		this.parseCookies();
	}

	private void parseCookies() {
		Cookie[] coo = serverInfo.getRequest().getCookies();
		if(coo!=null && coo.length>0) {
			for(Cookie c : coo) {
				try {
					cookies.put(c.getName(), URLDecoder.decode(c.getValue(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Object attr(String key) {
		Object a = null;
		a = serverInfo.getRequest().getAttribute(key);
		if(a == null) {
			a = serverInfo.getSession().getAttribute(key);
		}
		return a;
	}
	
	public void setAttr(String key, Object o) {
		serverInfo.getRequest().setAttribute(key, o);
	}
	
	public void setSessAttr(String key, Object o) {
		serverInfo.getSession().setAttribute(key, o);
	}
	
	public String get(String param) {
		String v = localParams.get(param);
		if(v == null) {
			v = serverInfo.getRequest().getParameter(param);
		}
		return v;
	}
	
	public String get(String param, String def) {
		String r = get(param);
		if(r == null) {
			return def;
		}
		return r;
	}
	
	public int getInt(String param, int def) {
		try {
			return Integer.parseInt(get(param));
		} catch (Exception e) {};
		return def;
	}
	
	public Integer getInteger(String param) {
		try {
			return Integer.parseInt(get(param));
		} catch (Exception e) {};
		return null;
	}

	public String getAction() {
		return action;
	}

	public String getExtension() {
		return extension;
	}

	public String getPath() {
		return servletPath;
	}

	public HttpServletRequest getServletRequset() {
		return serverInfo.getRequest();
	}

	private void parsePath() {
		servletPath = serverInfo.getRequest().getPathInfo();
		int dot = servletPath.lastIndexOf('.');
		int slash = servletPath.lastIndexOf('/');
		String action = servletPath;
		if ((dot >= 0) && (dot > slash)) {
			action = servletPath.substring(slash + 1, dot);
		}

		if (servletPath.startsWith("/")) {
			servletPath = servletPath.substring(1);
		}

		String extension = "";
		if(dot!=-1) {
			extension = servletPath.substring(dot);
		}

		String[] pathArr = servletPath.split("/");
		if (pathArr.length > 1) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < pathArr.length - 1; i++) {
				sb.append(pathArr[i]);
				if (i < pathArr.length - 2)
					sb.append(".");
			}
			//path = sb.toString();
		}

		if (action.length() > 0)
			this.action = action;
		if (extension.length() > 0)
			this.extension = extension;
	}
	
	public Object getUser() {
		HttpSession session = getServletRequset().getSession();
		return session.getAttribute(USER_SESSION_KEY);
	}
	
	public void putUserInSession(Object user) {
		HttpSession session = getServletRequset().getSession();
		session.setAttribute(USER_SESSION_KEY, user);
	}
	
	public List<FileInfo> getFiles() {
		return serverInfo.getFiles();
	}
	
	public void removeUserFromSession() {
		HttpSession session = getServletRequset().getSession();
		session.removeAttribute(USER_SESSION_KEY);
	}

	public Map<String, Object> getPageContext() {
		return pageContext;
	}
	
	private Map<String, String> localParams = new HashMap<String, String>();
	public String set(String param, String val) {
		String old = this.get(val);
		localParams.put(param, val);
		return old;
	}
}
