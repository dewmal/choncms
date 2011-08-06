package org.chon.core.common.db.test;

import org.chon.core.common.db.DBFactory;
import org.chon.core.common.db.DBXML;
import org.chon.core.common.util.FileUtils;
import org.json.JSONObject;


public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		DBXML dbxml = DBFactory.getDBXML(FileUtils
				.fileFromClassPath("com/joco/common/db/sample/mysql1.xml"));
		
		JSONObject j;
		j = dbxml.qJSON("getVestById");
		System.out.println(j.toString(2));
		
		//j = dbxml.qJSON("test2");
		//System.out.println(j.toString(2));
		
		/*
		 * Database db = DBFactory.getDatabase("mysql", "joco", "azspmme147",
		 * "j_spider", "localhost", "3306"); db.connect();
		 * 
		 * String q = String.format("INSERT INTO page VALUES ('%s', '%s')" ,
		 * "http://www.on.net.mk", "Тестирање на инсерт во база");
		 * 
		 * 
		 * 
		 * System.out.println(db.queryJSON(q).getJSONObject("resultset").getString
		 * ("message"));
		 * 
		 * db.close();
		 */
	}

}
