package org.chon.web.util;

public class UploadedFile {
	private String fileName;
	private String serverPath;
	public UploadedFile(String fileStr) {
		this.fileName = fileStr;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getServerPath() {
		return serverPath;
	}
	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
	
}
