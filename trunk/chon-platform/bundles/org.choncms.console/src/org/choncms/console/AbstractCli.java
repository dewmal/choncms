package org.choncms.console;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.chon.cms.model.ContentModel;

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
}
