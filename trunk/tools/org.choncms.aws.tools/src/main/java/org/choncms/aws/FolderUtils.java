package org.choncms.aws;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FolderUtils {
	
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	public static long getFolderSize(File folder) {
		long s = 0;
		for(File f : getAllFilesFlat(folder)) {
			s+=f.length();
		}
		return s;
	}
	
	/**
	 * List all files in folder including all files from subfolders
	 * 
	 * @param folder
	 * @return
	 */
	public static List<File> getAllFilesFlat(File folder) {
		List<File> ls = new ArrayList<File>();
		for(File ch : folder.listFiles()) {
			if(ch.isFile()) {
				ls.add(ch);
			} else if(ch.isDirectory() && !ch.getName().startsWith(".")) {
				ls.addAll(getAllFilesFlat(ch));
			}
		}
		return ls;
	}

	public static List<String> listAllSubPaths(String folder) {
		List<String> rv = new ArrayList<String>();
		File dir = new File(folder);
		List<File> ls = getAllFilesFlat(dir);
		for (File f : ls) {
			String name = f.getAbsolutePath().substring(
					dir.getAbsolutePath().length() + 1);
			name = name.replaceAll("\\\\", "/");
			rv.add(name);
		}
		return rv;
	}
	
	public static void main(String[] args) {
		long size = getFolderSize(new File("C:/temp/apache-tomcat-6.0.35"));
		System.out.println(readableFileSize(size));
	}
}
