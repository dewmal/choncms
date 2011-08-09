package org.chon.web.api.res;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.chon.web.api.Resource;
import org.chon.web.api.ServerInfo;


public class StaticResource implements Resource {

	private URL url;

	private StaticResource(URL staticResUrl) {
		this.url = staticResUrl;
	}

	@Override
	public void process(ServerInfo si) {
		try {
			handle(si.getSerlvetContext(), si.getRequest(), si.getResponse(), url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Resource create(URL staticResUrl) {
		return new StaticResource(staticResUrl);
	}

	private void handle(ServletContext servletContext, HttpServletRequest req, HttpServletResponse res,
			URL url) throws IOException {
		//URLConnection conn = url.openConnection();
		String contentType = null; //conn.getContentType();
		//if(contentType == null || "content/unknown".equals(contentType)) {
			String name =  FilenameUtils.getName(url.getFile());
			contentType = servletContext.getMimeType(name);
		//}
		
		if (contentType != null) {
			res.setContentType(contentType);
		}

		long lastModified = getLastModified(url);
		if (lastModified != 0) {
			res.setDateHeader("Last-Modified", lastModified);
		}

		if (!resourceModified(lastModified, req
				.getDateHeader("If-Modified-Since"))) {
			res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		} else {
			copyResource(url, res);
		}
	}

	private long getLastModified(URL url) {
		long lastModified = 0;

		try {
			URLConnection conn = url.openConnection();
			lastModified = conn.getLastModified();
		} catch (Exception e) {
			// Do nothing
		}

		if (lastModified == 0) {
			String filepath = url.getPath();
			if (filepath != null) {
				File f = new File(filepath);
				if (f.exists()) {
					lastModified = f.lastModified();
				}
			}
		}

		return lastModified;
	}

	private boolean resourceModified(long resTimestamp, long modSince) {
		modSince /= 1000;
		resTimestamp /= 1000;

		return resTimestamp == 0 || modSince == -1 || resTimestamp > modSince;
	}

	private void copyResource(URL url, HttpServletResponse res)
			throws IOException {
		OutputStream os = null;
		InputStream is = null;

		try {
			os = res.getOutputStream();
			URLConnection conn = url.openConnection();
			int len = conn.getContentLength();
			res.setContentLength(len);
			
			is = conn.getInputStream();
			len = 0;
			byte[] buf = new byte[1024];
			int n;

			while ((n = is.read(buf, 0, buf.length)) >= 0) {
				os.write(buf, 0, n);
				len += n;
			}

		} finally {
			if (is != null) {
				is.close();
			}

			if (os != null) {
				os.close();
			}
		}
	}
}
