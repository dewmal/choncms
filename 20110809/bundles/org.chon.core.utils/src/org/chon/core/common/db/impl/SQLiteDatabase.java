package org.chon.core.common.db.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.chon.core.common.db.Database;
import org.chon.core.common.db.ResultCallback;
import org.chon.core.common.db.ResultSetCallback;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


public class SQLiteDatabase implements Database {

	Connection connection = null;

	String connStr;

	public SQLiteDatabase(String conn) {
		if (conn == null) {
			conn = "jdbc:sqlite:test.db";
		}
		
		this.connStr = conn;
	}

	public Connection connect() {
		if (this.connection != null)
			return this.connection;
		Connection con = null;

		try {
			Class.forName("org.sqlite.JDBC").newInstance();
			System.out.println("Connecting to: " + connStr);
			con = DriverManager.getConnection(connStr);

			if (!con.isClosed())
				System.out.println("Successfully connected to SQLite database.");
			this.close(); // close any prev connections
			this.connection = con;
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			throw new RuntimeException(e);
		}
		return this.connection;
	}

	public void close() {
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.connection = null;
	}

	public void query(String query, ResultCallback callback) {
		//log.trace(query);
		try {
			Statement stmt = this.connect().createStatement();
			stmt.execute(query);
			ResultSet rs = stmt.getResultSet();

			while (rs.next()) {
				Map<String, String> m = new HashMap<String, String>();
				for (String key : callback.getKeys()) {
					m.put(key, rs.getString(key));
				}
				callback.process(m);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String queryXML(String query) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

		try {
			Statement stmt = this.connect().createStatement();
			stmt.execute(query);
			ResultSet rs = stmt.getResultSet();

			sb.append("<resultset>\n");
			sb.append("\t<statement>" + XML.escape(query) + "</statement>\n");
			sb.append("\t<message>OK</message>\n");
			while (rs != null && rs.next()) {
				sb.append("\t<row>\n");
				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
					String colName = rs.getMetaData().getColumnName(i + 1);
					String colValue = rs.getString(colName);
					sb.append("\t\t<" + colName + ">" + (colValue!=null ? XML.escape(colValue) : "NULL")
							+ "</" + colName + ">\n");
				}
				sb.append("\t</row>\n");
			}
			sb.append("</resultset>\n");

			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//log.error("MySQL Query Error. ", e);
			sb = new StringBuffer();
			sb.append("<resultset>\n");
			sb.append("\t<statement>" + XML.escape(query) + "</statement>\n");
			sb.append("\t<message>" + XML.escape(e.getMessage())
					+ "</message>\n");
			sb.append("</resultset>\n");
		}
		//log.trace(sb);
		return sb.toString();
	}

	public JSONObject queryJSON(String query) {
		try {
			JSONObject json = XML.toJSONObject(this.queryXML(query));
			// System.out.println(json.toString(5));
			return json;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void queryRS(String query, ResultSetCallback callback) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}
}
