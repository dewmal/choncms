package com.choncms.maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Resource {
	private List<Resource> children = new ArrayList<Resource>();
	
	private String type;
	private String name;
	private String content;
	
	public Resource(String type, String name, String content) {
		this.type = type;
		this.name = name;
		this.content = content;
	}
	
	public void addChild(String type, String name, String content) {
		Resource c = new Resource(type, name, content);
		this.children.add(c);
	}
	public void addChild(Resource r) {
		this.children.add(r);
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
}
