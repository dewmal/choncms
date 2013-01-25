package org.chon.web.util;

import java.io.File;

public class FileInfo {
	private File file;
	private String formFieldName;
	private String contentType;
	private long size;

	public FileInfo(File file, String formFieldName, String contentType,
			long size) {
		this.file = file;
		this.formFieldName = formFieldName;
		this.contentType = contentType;
		this.size = size;
	}

	public File getFile() {
		return file;
	}

	public String getFormFieldName() {
		return formFieldName;
	}

	public String getContentType() {
		return contentType;
	}

	public long getSize() {
		return size;
	}

}