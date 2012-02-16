package com.choncms.maven;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

public class Resource {
	private List<Resource> children = new ArrayList<Resource>();
	private Resource parent = null;
	
	private String type;
	private String name;
	
	private String content = null;
	
	private String tplRoot = null;
	
	private InputStream inputStream;
	
	public String getTplRoot() {
		Resource r = this;
		while(r != null) {
			String t = r.tplRoot;
			if(t != null) {
				return t;
			}
			r = r.parent;
		}
		
		//should never get here
		return null;
	}
	
	public Resource(String type, String name, String tplRoot, Resource parent) {
		this.type = type;
		this.name = name;
		if(tplRoot != null && tplRoot.trim().length()>0) {
			this.tplRoot = tplRoot;
		}
		if(parent != null) {
			this.parent = parent;
			parent.children.add(this);
		}
	}
	
	public void setFileData(String data) {
		this.content = data;
	}

	public void create(File target) {
		if("dir".equals(type)) {
			File dir = new File(target, name);
			dir.mkdirs();
			for(Resource c : children) {
				c.create(dir);
			}
		} else if("package".equals(type)) {
			String [] dirs = name.split("\\.");
			File parent = target;
			for(String d : dirs) {
				parent = new File(parent, d);
			}
			parent.mkdirs();
			for(Resource c : children) {
				c.create(parent);
			}
		} else if("file".equals(type)) {
			InputStream is = getInputStream();
			File file = new File(target, name);
			try {
				if(is != null) {
					FileOutputStream fos = new FileOutputStream(file);
					IOUtils.copy(is, fos);
					fos.flush();
					fos.close();
				} else {
					PrintStream ps = new PrintStream(file);
					ps.print(content);
					ps.flush();
					ps.close();				
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if("zip".equals(type)) {
			try {
				unzip(target);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("unknown resource type: " + type);
		}
	}

	 
	private void unzip(File target) throws FileNotFoundException, IOException {
		BufferedOutputStream dest = null;
		final int BUFFER = 2048;
		ZipInputStream zis = new ZipInputStream(this.getInputStream());
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null) {
			System.out.println("Extracting: " + entry);
			File file = new File(target, entry.getName());
			if(entry.isDirectory()) {
				file.mkdirs();
			} else {
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				FileOutputStream fos = new FileOutputStream(file);
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
		}
		zis.close();
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void addChild(Resource r) {
		r.parent = this;
		this.children.add(r);
	}	
}
