package org.choncms.console.programs;

import java.util.Iterator;

import javax.jcr.Node;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.chon.cms.model.content.IContentNode;
import org.choncms.console.AbstractCli;
import org.json.JSONObject;


public class C_mknode extends AbstractCli {

	@Override
	public String getHelpUsage() {
		return "mknode [OPTIONS] (JSON creation structure)";
	}

	@Override
	public String getHelpHeader() {
		return "Creates a NODE";
	}

	@Override
	public String getHelpFooter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addOptions() {
		Option nodeName = new Option("n", "Node name");
		nodeName.setArgs(1);
		nodeName.setOptionalArg(false);
		options.addOption(nodeName);
		
		Option nodeDestination = new Option("d", "Node Destnation");
		nodeDestination.setArgs(1);
		nodeDestination.setOptionalArg(false);
		options.addOption(nodeDestination);
	}

	@Override
	public String[] process(CommandLine r) {
		String[] args = r.getArgs();
		if(args == null || args.length < 1) {
			throw new RuntimeException("Invalid arguments");
		}
		/*
		String path = PathResolver.getAbsPath(args[0], consoleSession.getPath());
		IContentNode n = contentModel.getContentNode(path);
		if(n == null) {
			throw new RuntimeException("No such node ... " + path);
		}
		path = n.getAbsPath();
		*/
		String path = consoleSession.getPath();
		if(r.hasOption("d")) {
			path = r.getOptionValue("d");
			if(!path.startsWith("/")) {
				path = consoleSession.getPath() + "/" + path;
			}
		}
		IContentNode n = contentModel.getContentNode(path);
		String nodeName = null;
		if(r.hasOption('n')) {			
			 nodeName = r.getOptionValue('n');
		}
		if(nodeName == null) {
			throw new RuntimeException("Missing node name (-n) argument");
		}
		String resp = "";
		try {
			Node newNode = n.getNode().addNode(nodeName);
			JSONObject obj = new JSONObject(args[0]);
			Iterator it = obj.keys();
			while(it.hasNext()) {
				String k = (String) it.next();
				String v = obj.getString(k);
				newNode.setProperty(k, v);
			}
			newNode.getSession().save();
			resp = "Node created: " + newNode.getPath();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return new String [] { resp }; 
	}
}
