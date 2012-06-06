package com.choncms.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.ServletContext;

/**
 * Helper class for loading resources 
 * (properties)
 * 
 * @author Jovica
 *
 */
public class ResourceLoader {
	/**
	 * 1: If dir is not null, try to read from there
	 * 2: Try classpath
	 * 3: Load from WEB-INF
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static InputStream loadResource(String path, File dir, ServletContext servletContext) throws FileNotFoundException {
		// 1: If dir is not null, try to read from there 
		if(dir != null) {
			File file = new File(dir, path);
			if(file.exists()) {
				return new FileInputStream(file);
			}
		}
		
		// 2: Try classpath
		InputStream is = ResourceLoader.class.getResourceAsStream("/"+path);
		if(is != null) {
			return is;
		}
		
		// 3: Load from WEB-INF
		is = servletContext.getResourceAsStream("/WEB-INF/" + path);
		if(is != null) {
			return is;
		}
		throw new FileNotFoundException(path);
	}
}
