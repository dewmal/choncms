package org.chon.web.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.chon.web.util.FileInfo;


public class ServerInfo {
	private Servlet servlet;
	private ServletContext serlvetContext;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	//gets valuse on first getOut()
	private PrintWriter out = null;
	
	private HttpSession session;
	
	private List<FileInfo> files;
	
	private Application application;
	
	private Request req;
	private Response resp;
	public Servlet getServlet() {
		return servlet;
	}
	public void setServlet(Servlet servlet) {
		this.servlet = servlet;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	public PrintWriter getOut() {
		if(out != null) {
			return out;
		}
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	public List<FileInfo> getFiles() {
		return files;
	}
	public void setFiles(List<FileInfo> files) {
		this.files = files;
	}
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	public Request getReq() {
		return req;
	}
	public void setReq(Request req) {
		this.req = req;
	}
	public Response getResp() {
		return resp;
	}
	public void setResp(Response resp) {
		this.resp = resp;
	}
	public ServletContext getSerlvetContext() {
		return serlvetContext;
	}
	public void setSerlvetContext(ServletContext serlvetContext) {
		this.serlvetContext = serlvetContext;
	}
	
	public boolean isResponseWriterOpen() {
		return out!=null;
	}
}
