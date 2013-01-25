package org.choncms.console.actions;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.chon.cms.model.ContentModel;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.choncms.console.CliProgram;
import org.choncms.console.ConsoleSession;
import org.choncms.console.programs.C_cd;
import org.choncms.console.programs.C_cp;
import org.choncms.console.programs.C_ls;
import org.choncms.console.programs.C_mknode;
import org.choncms.console.programs.C_rm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Command processor actrion
 * 
 * @author Jovica
 *
 */
public class EvalAjaxAction extends AbstractConsoleAction {
	public EvalAjaxAction(String prefix) {
		super(prefix);
	}

	@Override
	public String run(Application app, Request req, Response resp) {
		String cmd = req.get("cmd");
		JSONObject r = new JSONObject();
		try {
			JSONArray respArr = new JSONArray();

			ConsoleSession cs = (ConsoleSession) req.attr(ConsoleSession.KEY);
			ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
			try {
				String[] lines = proccessCommand(cmd, cs, cm);
				for (String s : lines) {
					JSONObject respLine = new JSONObject();
					respLine.put("msg", s);
					respLine.put("className", "jquery-console-message-value");
					respArr.put(respLine);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSONObject respLine = new JSONObject();
				respLine.put("msg", e.getMessage());
				respLine.put("className", "jquery-console-message-error");
				respArr.put(respLine);
			}

			r.put("lines", respArr);
			r.put("path", cs.getPath());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r.toString();
	}

	protected String[] proccessCommand(String cmd, ConsoleSession cs,
			ContentModel cm) throws ParseException {
		CommandLineParser parser = new PosixParser();
		cmd = cmd.trim();
		int firstSpaceIdx = cmd.indexOf(' ');
		String program = null;
		if (firstSpaceIdx != -1) {
			program = cmd.substring(0, firstSpaceIdx).trim();
			cmd = cmd.substring(firstSpaceIdx).trim();
		} else {
			program = cmd;
			cmd = "";
		}

		CliProgram p = getProgram(program);
		if (p == null) {
			throw new RuntimeException("Unknown command '" + program + "'");
		}
		p.init(cs, cm);
		String[] programArguments = getCommandArguments(cmd);
		int k=0;
		for(String s : programArguments) {
			System.out.println("Arg("+(k++)+")="+s);
		}
		CommandLine r = parser.parse(p.getOptions(), programArguments);
		String[] rv = p.run(r);
		return rv;
	}

	private String[] getCommandArguments(String cmd) {
		if(cmd == null) {
			return CliProgram.EMPTY;
		}
		cmd = cmd.trim();
		if(cmd.length() == 0) {
			return CliProgram.EMPTY;
		}
		//TODO: for now just split on empty spaces....
		// take care of " or {}
		return cmd.split(" ");
	}

	private CliProgram getProgram(String program) {
		//TODO: get from OSGi registry....
		CliProgram rv = null;
		program = program.trim();
		if (program.equals("ls")) {
			rv = new C_ls();
		} else if (program.equals("cd")) {
			rv = new C_cd();
		} else if (program.equals("cp")) {
			rv = new C_cp();
		} else if (program.equals("rm")) {
			rv = new C_rm();
		} else if (program.equals("mknode")) {
			rv = new C_mknode();
		}
		return rv;
	}
}