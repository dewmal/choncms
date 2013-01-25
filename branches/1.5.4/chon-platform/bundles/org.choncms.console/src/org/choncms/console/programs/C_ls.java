package org.choncms.console.programs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.chon.cms.model.content.IContentNode;
import org.choncms.console.AbstractCli;


public class C_ls extends AbstractCli {

	@Override
	public String getHelpUsage() {
		return "ls [OPTION]... [FILE]...";
	}

	@Override
	public String getHelpHeader() {
		return "List information about the NODEs (the current node by default).";
	}

	@Override
	public String getHelpFooter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addOptions() {
		// TODO Auto-generated method stub	
	}

	@Override
	public String[] process(CommandLine r) {
		String[] args = r.getArgs();
		String path = null;
		if(args == null || args.length==0) {
			path = consoleSession.getPath();
		} else {
			path = args[0];
			if(!path.startsWith("/")) {
				path = consoleSession.getPath() + "/" + path;
			}
		}
		IContentNode n = contentModel.getContentNode(path);
		return listInfo(n.getChilds());
	}

	private String[] listInfo(List<IContentNode> childs) {
		List<String> ls = new ArrayList<String>();
		for(IContentNode n : childs) {
			ls.add(n.getName());
		}
		return ls.toArray(new String[ls.size()]);
	}
}
