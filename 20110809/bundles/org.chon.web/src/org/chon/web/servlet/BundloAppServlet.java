package org.chon.web.servlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.web.AppPropertyHelper;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Resource;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;
import org.chon.web.api.internal.ResourceProcessorRunner;
import org.chon.web.api.res.StaticResource;
import org.chon.web.util.FileInfo;
import org.chon.web.util.upload.MonitoredDiskFileItemFactory;
import org.chon.web.util.upload.UploadListener;


public class BundloAppServlet extends HttpServlet {

	private static Log log = LogFactory.getLog(BundloAppServlet.class);
	
	public static final String ALIAS = "/";

	private List<Application> apps = new LinkedList<Application>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(log.isTraceEnabled()) {
			log.trace("Servlet GET " + req.getPathInfo());
		}
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(log.isTraceEnabled()) {
			log.trace("Servlet POST " + req.getPathInfo());
		}
		process(req, resp);
	}

	private void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// measure performance
		long start = System.currentTimeMillis();

		// always get UTF-8 request
		request.setCharacterEncoding("UTF-8");

		// create nice structure from this request/response
		ServerInfo si = createServerInfoStructure(request, response);


		// TODO: check 
		// do we need explicit ask applications if we should upload the file/s
		
		// call preprocess
		if(log.isDebugEnabled()) {
			log.debug("Finding resource for path: " + si.getReq().getPath());
		}
		Resource r = preprocess(si);
		
		if(log.isDebugEnabled()) {
			log.debug("Resource: " + r);
		}
		
		// upload files if upload request
		if (isUploadRequest(request)) {
			List<FileInfo> files = processUpload(request);
			si.setFiles(files);
			if(log.isDebugEnabled()) {
				log.debug("Upload request " + files);
			}
		}

		if (r == null) {
			// no application found that can handle the request
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			ResourceProcessorRunner.process(r, si);
		}
		
		Response resp = si.getResp();
		String redirect = resp.getRedirect();
		if(redirect != null) {
			try {
				resp.getServletResponse().sendRedirect(redirect);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(si.isResponseWriterOpen()) {
			si.getOut().flush();
			si.getOut().close();
		}
		long end = System.currentTimeMillis();
		if(r instanceof StaticResource) {
			if(end-start>2000) {
				printPrfInfo(si, start, end);
			}
		} else {
			printPrfInfo(si, start, end);
		}
	}

	private void printPrfInfo(ServerInfo si, long start, long end) {
		String path = si.getReq().getPath();
		if(!path.startsWith("/")) {
			path = "/" + path;
		}
		if (si.getRequest().getQueryString() != null) {
			path += "?" + si.getRequest().getQueryString();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String time = sdf.format(new Date());
		System.out.println(String.format("%s host[%s] req[%s] time[%s]", time,
				si.getRequest().getRemoteHost(), path, (end - start) + " ms."));
	}

	private Resource preprocess(ServerInfo si) {
		List<Application> applications = getApplications();
		for (Application app : applications) {
			Resource resource = app.getResource(si.getReq());
			if (resource != null) {
				si.setApplication(app);
				return resource;
			}
		}
		return null;
	}

	private List<Application> getApplications() {
		return apps;
	}

	private ServerInfo createServerInfoStructure(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ServerInfo si = new ServerInfo();
		si.setServlet(this);
		si.setSerlvetContext(getServletContext());
		si.setRequest(request);
		si.setResponse(response);
		si.setSession(request.getSession());
		//si.setOut(response.getWriter());
		Request req = new Request(si);
		Response resp = new Response(si);
		si.setReq(req);
		si.setResp(resp);
		return si;
	}

	@SuppressWarnings("unchecked")
	private List<FileInfo> processUpload(HttpServletRequest request) {
		UploadListener listener = new UploadListener(request, 5);

		// Create a factory for disk-based file items
		FileItemFactory factory = new MonitoredDiskFileItemFactory(listener);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(1024 * 1024 * 1024); // FIXME: get max upload size from app proeprties

		List<FileInfo> files = new ArrayList<FileInfo>();

		// process uploads ..

		List<FileItem> fileItems;
		try {
			fileItems = upload.parseRequest(request);
			for (FileItem fi : fileItems) {
				if (!fi.isFormField()) {
					// we received the file
					File uploadDir = new File(System.getProperty("uploadDir", System.getProperty("java.io.tmpdir")));
					if(!uploadDir.exists()) {
						uploadDir.mkdirs();
					}
					File file = new File(uploadDir, FilenameUtils.getName(fi.getName()));
					fi.write(file);
					//file.deleteOnExit();
					FileInfo fileInfo = new FileInfo(file, fi.getFieldName(),
							fi.getContentType(), fi.getSize());
					files.add(fileInfo);
				} else {
					// FIXME: we are ignoring other fields from form, we are
					// only take
					// files
				}
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return files;
	}

	// FIXME: upload check should not be by extension
	// should be true if form is multipart/form-data
	private boolean isUploadRequest(HttpServletRequest request) {
		return request.getPathInfo().endsWith(".upload");
	}

	public void addApplication(Application app) {
		int p = AppPropertyHelper.getAppPriority(app);
		int i;
		for(i=0; i<apps.size(); i++) {
			int c = AppPropertyHelper.getAppPriority(apps.get(i));
			if(c > p) {
				break;
			}
		}
		if(i<apps.size()) {
			apps.add(i, app);
		} else {
			apps.add(app);
		}
		
		for(Application a : apps) {
			System.out.println(a + " priority: " + AppPropertyHelper.getAppPriority(a));
		}
	}

	public void removeApplication(Application app) {
		apps.remove(app);
	}
}
