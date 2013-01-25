package org.choncms.console.programs;

import javax.jcr.RepositoryException;

import org.apache.commons.cli.CommandLine;
import org.chon.cms.model.content.IContentNode;
import org.choncms.console.AbstractCli;
import org.choncms.console.utils.PathResolver;


public class C_cp extends AbstractCli {

	@Override
	public String getHelpUsage() {
		return "cp [OPTION]... SOURCE... DEST";
	}

	@Override
	public String getHelpHeader() {
		return "Copy SOURCE to DEST";
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
		if(args == null || args.length<2) {
			throw new RuntimeException("Invalid arguments, type --help to see usage");
		}
		String source = PathResolver.getAbsPath(args[0], consoleSession.getPath());
		String dest = PathResolver.getAbsPath(args[1], consoleSession.getPath());
		
		IContentNode [] sourceNodes = getNodesWc(source);
		
		IContentNode destNode = contentModel.getContentNode(dest);
		if(destNode == null) {
			throw new RuntimeException("Invalid destination node");
		}
		try {
			for(IContentNode n : sourceNodes) {
				System.out.println("Copying " + n.getAbsPath() + " to " + destNode.getAbsPath() + "/" + n.getName());
				contentModel.getSession().getWorkspace().copy(n.getAbsPath(), destNode.getAbsPath() + "/" + n.getName());
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return EMPTY;
	}
}
