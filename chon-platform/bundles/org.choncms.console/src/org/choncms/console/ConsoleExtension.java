package org.choncms.console;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.chon.cms.core.Extension;
import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.auth.User;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.choncms.console.programs.C_cd;
import org.choncms.console.programs.C_cp;
import org.choncms.console.programs.C_ls;
import org.choncms.console.programs.C_mknode;
import org.choncms.console.programs.C_rm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConsoleExtension implements Extension {
	private String prefix;
	private JCRApplication app;
	
	private Map<String, Action> actions = new HashMap<String, Action>();
	private Map<String, Action> ajaxActions = new HashMap<String, Action>();
	
	public ConsoleExtension(String prefix, JCRApplication app) {
		this.prefix = prefix;
		this.app = app;
		
		actions.put(prefix + ".main", new Action() {
			@Override
			public String run(Application app, Request req, Response resp) {
				Map<String, Object> params = new HashMap<String, Object>();
				return formatTemplate(resp, "main.html", params);
			}
		});
		
		ajaxActions.put(prefix + ".eval", new Action() {
			@Override
			public String run(Application app, Request req, Response resp) {
				String cmd = req.get("cmd");
				JSONObject r = new JSONObject();
				try {
					JSONArray respArr = new JSONArray();
					
					ConsoleSession cs = (ConsoleSession) req.attr(ConsoleSession.KEY);
					ContentModel cm = (ContentModel) req.attr(ContentModel.KEY);
					try {
						String [] lines = proccessCommand(cmd, cs, cm);
						for(String s : lines) {
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
		});
		
		ajaxActions.put(prefix + ".init", new Action() {
			@Override
			public String run(Application app, Request req, Response resp) {
				ConsoleSession cs = (ConsoleSession) req.attr(ConsoleSession.KEY);
				if(cs == null) {
					cs = new ConsoleSession();
					User user = (User) req.getUser();
					cs.setPath("/home/" + user.getName());
					req.setSessAttr(ConsoleSession.KEY, cs);
				}
				return cs.toJSONStr();
			}
		});
	}

	protected String[] proccessCommand(String cmd, ConsoleSession cs, ContentModel cm) throws ParseException {
		CommandLineParser parser = new PosixParser();
		cmd = cmd.trim();
		int firstSpaceIdx = cmd.indexOf(' ');
		String program = null;
		if(firstSpaceIdx != -1) {
			program = cmd.substring(0, firstSpaceIdx).trim();
			cmd = cmd.substring(firstSpaceIdx).trim();
		} else {
			program = cmd;
			cmd = "";
		}
		
		CliProgram p = getProgram(program);
		if(p == null) {
			throw new RuntimeException("Unknown command '" + program + "'");
		}
		p.init(cs, cm);
		String [] programArguments = cmd.split(" ");//cmd.split("\\w+|\"[\\w\\s]*\"");
		CommandLine r = parser.parse(p.getOptions(), programArguments);
		String [] rv = p.run(r);
		return rv;
	}

	private CliProgram getProgram(String program) {
		CliProgram rv = null;
		program = program.trim();
		if(program.equals("ls")) {
			rv = new C_ls();
		} else if(program.equals("cd")) {
			rv = new C_cd();
		} else if(program.equals("cp")) {
			rv = new C_cp();
		} else if(program.equals("rm")) {
			rv = new C_rm();
		} else if(program.equals("mknode")) {
			rv = new C_mknode();
		}
		return rv;
	}

	protected String formatTemplate(Response resp, String tpl,
			Map<String, Object> params) {
		params.put("prefix", prefix);
		params.put("app", app); //not needed
		return resp.formatTemplate(getTpl(tpl), params);
	}

	protected String getTpl(String tpl) {
		return prefix + "/" + tpl;
	}

	@Override
	public Map<String, Action> getAdminActons() {
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		return ajaxActions;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
