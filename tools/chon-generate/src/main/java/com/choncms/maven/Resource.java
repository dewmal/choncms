package com.choncms.maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Resource {
	private List<Resource> children = new ArrayList<Resource>();
	private Resource parent = null;
	
	private String type;
	private String name;
	
	private String content = null;
	
	private String tplRoot = null;
	
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
			File file = new File(target, name);
			try {
				PrintStream ps = new PrintStream(file);
				ps.print(content);
				ps.flush();
				ps.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("unknown resource type: " + type);
		}
	}

	public void addChild(Resource r) {
		r.parent = this;
		this.children.add(r);
	}	
}
