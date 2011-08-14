package org.chon.cms.wiki.nodes;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.xhtml.PrintListener;

public class WikiChon {

	private IContentNode node;
	private Response resp;
	private Request req;
	
	
	IWikiParser parser;
	
	public WikiChon(Request req, Response resp, IContentNode node, IWikiParser parser) {
		this.node = node;
		this.resp = resp;
		this.req = req;
		this.parser = parser;
		init(resp);
	}
	
	private void init(Response resp) {
		@SuppressWarnings("unchecked")
		List<String> scrips = (List<String>) resp.getTemplateContext().get("head:scripts");
		@SuppressWarnings("unchecked")
		List<String> css = (List<String>) resp.getTemplateContext().get("head:css");
		
		scrips.add("jquery/js/jquery-1.4.4.min.js");
		
		/*
		css.add("chon.wiki/wiky.css");
		scrips.add("chon.wiki/wiky.js");
		*/
	}
	
	public String parseWiki(String markup) throws WikiParserException {
		Reader reader = new StringReader(markup);
		
		final StringWriter sw = new StringWriter();
		IWikiPrinter printer = new IWikiPrinter() {
			public void print(String str) {
				sw.append(str);
			}

			public void println(String str) {
				sw.append(str);
				sw.append("\n");
			}
		};
		// Default XHTML print listener
		IWemListener serializer = new PrintListener(printer) {
			
		};
		parser.parse(reader, serializer);
		return sw.toString();
	}
}
