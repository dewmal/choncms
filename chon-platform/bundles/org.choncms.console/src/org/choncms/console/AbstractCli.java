package org.choncms.console;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;

public abstract class AbstractCli implements CliProgram {
	protected ConsoleSession consoleSession;
	protected ContentModel contentModel;
	protected Options options = new Options();
	
	public abstract String getHelpUsage();
	public abstract String getHelpHeader();
	public abstract String getHelpFooter();
	
	public abstract void addOptions();
	public abstract String[] process(CommandLine r);
	
	@Override
	public Options getOptions() {
		return options;
	}
	@Override
	public void init(ConsoleSession cs, ContentModel cm) {
		this.consoleSession = cs;
		this.contentModel = cm;
		Option help = new Option( "help", "print this message");
		options.addOption(help);
		addOptions();
	}
	
	@Override
	public String[] run(CommandLine r) {
		if(r.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			StringWriter sw = new StringWriter();
			formatter.printHelp(new PrintWriter(sw), 1024, getHelpUsage(),
					getHelpHeader(), getOptions(), 10, 10, getHelpFooter());
			return new String [] { sw.toString() };
		}
		return process(r);
	}
	
	
	protected IContentNode[] getNodesWc(String p) {
		IContentNode[] rv = null;
		if(p.endsWith("*")) {
			p = p.substring(0, p.length()-2);
			IContentNode ct = contentModel.getContentNode(p);
			if(ct == null) {
				throw new RuntimeException("Invalid node " + p);
			}
			List<IContentNode> childs = ct.getChilds();
			rv = childs.toArray(new IContentNode[childs.size()]);
		} else {
			rv = new IContentNode[1];
			rv[0] = contentModel.getContentNode(p); 
		}
		for(IContentNode n : rv) {
			if(n == null) {
				throw new RuntimeException("Invalid node(s)");
			}
		}
		return rv;
	}
}
