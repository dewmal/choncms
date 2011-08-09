package org.chon.core.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public class FileUtils {
	// public static final Logger log = Logger.getLogger(FileUtils.class);

	private static final String JGRA_PATH = "jgra/resources/";

	public static File fileFromClassPath(String fileName) {
		// log.debug("Get file from class path: " + fileName);
		if (!fileName.startsWith("/"))
			fileName = "/" + fileName;
		URL res = FileUtils.class.getResource(fileName);

		try {
			URI uri = new URI(res.toString());
			return new File(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * file from jgra url eg. jgra://js/utils.js classpath://file1.java TODO:
	 * url
	 * 
	 * @param fileName
	 * @return
	 */
	public static File fileFromJgraUrl(File parent, String url) {
		// TODO if url...
		File file = null;
		try {
			if (url.startsWith("jgra://")) {
				url = url.substring(7);
				file = FileUtils.fileFromClassPath(JGRA_PATH + url);
			} else if (url.startsWith("classpath://")) {
				url = url.substring(12);
				file = FileUtils.fileFromClassPath(url);
			} else {
				if (parent != null) {
					if (!parent.isDirectory())
						parent = parent.getParentFile();
					file = new File(parent, url);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return file;
	}

	public static File fileFromJgraUrl(String url) {
		return FileUtils.fileFromJgraUrl(null, url);
	}

	/**
	 * Relative file from parent
	 * 
	 * @param parent
	 * @param url
	 * @return
	 */
	public static File fileFromRelFile(File parent, String fpath) {
		return new File(parent, fpath);
	}

	/**
	 * Read contents of a file to string not good for huge files
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(File file) throws IOException {
		return readInputStreamToString(new FileInputStream(file));
	}

	/**
	 * Read input stream to string
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readInputStreamToString(InputStream is)
			throws IOException {
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				Charset.forName("UTF-8")));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}
}
