package org.choncms.console.programs;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.cli.CommandLine;
import org.chon.cms.model.content.IContentNode;
import org.choncms.console.AbstractCli;
import org.choncms.console.utils.PathResolver;


public class C_rm extends AbstractCli {

	@Override
	public String getHelpUsage() {
		return "rm [OPTION]... NODE";
	}

	@Override
	public String getHelpHeader() {
		return "Permanenly remove NODE(s)";
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
		if(args == null || args.length<1) {
			throw new RuntimeException("Invalid arguments, type --help to see usage");
		}
		String p = PathResolver.getAbsPath(args[0], consoleSession.getPath());
		IContentNode [] nodes = getNodesWc(p);
		try {
			for(IContentNode node : nodes) {
				Session session = node.getNode().getSession();
				node.getNode().remove();
				session.save();
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return EMPTY;
	}
}
