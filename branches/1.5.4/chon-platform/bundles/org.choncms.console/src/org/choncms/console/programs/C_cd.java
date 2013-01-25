package org.choncms.console.programs;

import org.apache.commons.cli.CommandLine;
import org.chon.cms.model.content.IContentNode;
import org.choncms.console.AbstractCli;
import org.choncms.console.utils.PathResolver;


public class C_cd extends AbstractCli {

	@Override
	public String getHelpUsage() {
		return "cd [path]";
	}

	@Override
	public String getHelpHeader() {
		return "change directory (node)";
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
		if(args == null || args.length==0) {
			return EMPTY;
		}
		String path = PathResolver.getAbsPath(args[0], consoleSession.getPath());
		IContentNode n = contentModel.getContentNode(path);
		if(n == null) {
			throw new RuntimeException("No such node ... " + path);
		}
		path = n.getAbsPath();
		consoleSession.setPath(path);
		return new String [] { path }; 
	}
}
