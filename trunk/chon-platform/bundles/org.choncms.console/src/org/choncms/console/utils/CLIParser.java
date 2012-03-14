package org.choncms.console.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CLIParser {

	public static String [] tokenize(String s) throws Exception {
		Lexer lexer = new Lexer(s,
				new String[] {
				/*
				 * "[\\w\\.]+", "\\w+", "\\d+", "\\|", "\\&",
				 */
				"\\\"[^\\\"]+\\\"", "[^\\s]+" }, "\\s+");
		List<String> rv = new ArrayList<String>();
		String token = null;
		while ((token = lexer.nextToken()) != null) {
			if(token.startsWith("\"") && token.endsWith("\"")) {
				token = token.substring(1, token.length()-1);
			}
			rv.add(token);
		}
		return rv.toArray(new String[rv.size()]);
	}
}

class Lexer {
	private String str;
	private String[] rules;
	private String delimiters;

	private String processed;
	private Pattern delim;
	private Pattern[] rulesp;

	public Lexer(String str, String[] rules, String delimiters) {
		super();
		this.str = str;
		this.rules = rules;
		this.delimiters = "^" + delimiters;
		this.delim = Pattern.compile(this.delimiters);
		rulesp = new Pattern[rules.length];
		int i = 0;
		for (String rule : rules) {
			rulesp[i] = Pattern.compile(rule);
			i++;
		}
		processed = str;
	}

	public String nextToken() throws Exception {
		Matcher m = delim.matcher(processed);
		if (m.find()) {
			processed = processed.substring(m.end());
		}
		for (Pattern rule : rulesp) {
			Matcher rm = rule.matcher(processed);
			if (rm.find() && rm.start() == 0) {
				String token = rm.group();
				processed = processed.substring(rm.end());
				return token;
			}
		}
		if (processed.length() > 0) {
			throw new Exception(String.format("Invalid syntax near ...%s",
					processed));
		}
		return null;
	}

	public void reset() {
		processed = str;
	}
}