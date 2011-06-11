package org.chon.core.common.db;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.chon.core.common.db.impl.DBXMLImpl;
import org.chon.core.common.db.impl.PooledDatabase;
import org.chon.core.common.db.impl.SQLiteDatabase;
import org.chon.core.common.util.FileUtils;
import org.chon.core.common.util.json.JSONHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;


public class DBFactory {
		
	//public static final Logger log = Logger.getLogger(DBFactory.class);
	
	public static Database getDatabase(String dbType, String user,
			String password, String db, String server, String port) {
		
		Database database;
		
		if ("mysql".equalsIgnoreCase(dbType)) {
			database = createMySQLDatabase(user, password, db, server, port);
		} else if("mysql-pool".equalsIgnoreCase(dbType)) {
			database = createDatabaseFromPool(db);
		} else if ("sqlite".equalsIgnoreCase(dbType)) {
			database = createSQLiteDatabase(db);
		} else {
			throw new RuntimeException("Not yet implemented DB type: " + dbType);
		}
		return database;
	}

	
	private static Database createDatabaseFromPool(String poolName) {
		InitialContext initContext;
		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/" + poolName);
			Database db = new PooledDatabase(ds);
			return db;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static Database createSQLiteDatabase(String db) {
		if (db == null)
			db = "test.db";
		return new SQLiteDatabase("jdbc:sqlite:" + db);
	}

	private static Database createMySQLDatabase(String user, String password,
			String db, String server, String port) {
		if (db == null)
			db = "";
		if (server == null || server.trim().length() == 0)
			server = "localhost";
		if (port == null || port.trim().length() == 0)
			port = "3306";
		
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername(user);
		ds.setPassword(password);
		String connectURI = "jdbc:mysql://" + server + ":" + port + "/" + db;
		ds.setUrl(connectURI);
		//return new MysqlDatabase("jdbc:mysql://" + server + ":" + port + "/" + db, user, password);
		return new PooledDatabase(ds);
	}

	public static DBXML getDBXML(File xml) throws Exception {
		String s = FileUtils.readFileToString(xml);
		return getDBXML(s);
	}
	
	public static DBXML getDBXML(String xml) throws Exception {
		
		JSONObject xj = XML.toJSONObject(xml).getJSONObject("database");

		JSONHelper jh = new JSONHelper(xj);
		final Database database = getDatabase(jh.get("type"), jh.get("user"),
				jh.get("password"), jh.get("db"), jh.get("server"), jh
						.get("port"));

		final Map<String, String> queries = new HashMap<String, String>();
	
		JSONArray jaq = ensureJsonArray(xj.getJSONObject("queries"), "q");
		for (int i = 0; i < jaq.length(); i++) {
			JSONObject o = jaq.getJSONObject(i);
			queries.put(o.getString("name"), o.getString("content"));
		}
	
		return new DBXMLImpl(queries, database);
	}
	
	private static JSONArray ensureJsonArray(JSONObject object, String string) {
		Object obj = object.opt(string);
		if(obj instanceof JSONArray) 
			return (JSONArray) obj;
		JSONArray jsonarr = new JSONArray().put(obj);
		return jsonarr;
	}
}
