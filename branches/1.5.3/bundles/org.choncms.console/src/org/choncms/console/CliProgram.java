package org.choncms.console;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.chon.cms.model.ContentModel;

public interface CliProgram {
	public static String [] EMPTY = new String [] {};
	
	public Options getOptions();
	
	public void init(ConsoleSession cs, ContentModel cm);
	public String[] run(CommandLine r);
}
