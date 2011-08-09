package org.chon.core.common.db.impl;

import java.sql.Connection;
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


public abstract class AbstactDB implements Database {

	public void queryRS(String query, ResultSetCallback callback) {
		// log.trace(query);
		Connection connection = null;
		try {
			connection = this.connect();
			Statement stmt = connection.createStatement();
			stmt.execute(query);
			ResultSet rs = stmt.getResultSet();
			if(rs!=null && callback!=null) {
				while (rs.next()) {
					if(!callback.process(rs)) break;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(connection!=null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	public void query(String query, final ResultCallback callback) {
		queryRS(query, new ResultSetCallback() {			
			public boolean process(ResultSet rs) {
				Map<String, String> m = new HashMap<String, String>();
				for (String key : callback.getKeys()) {
					try {
						m.put(key, rs.getString(key));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				callback.process(m);
				return true;
			}
		});
	}

	public String queryXML(String query) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		Connection connection = null;
		try {
			connection = this.connect();
			Statement stmt = connection.createStatement();
			stmt.execute(query);
			ResultSet rs = stmt.getResultSet();

			sb.append("<resultset>\n");
			sb.append("\t<statement>" + XML.escape(query) + "</statement>\n");
			sb.append("\t<message>OK</message>\n");
			while (rs != null && rs.next()) {
				sb.append("\t<row>\n");
				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
					String colName = rs.getMetaData().getColumnLabel(i + 1);
					String colValue = rs.getString(colName);
					if (colValue != null) {
						sb
								.append("\t\t<" + colName + ">"
										+ XML.escape(colValue) + "</" + colName
										+ ">\n");
					}
				}
				sb.append("\t</row>\n");
			}
			sb.append("</resultset>\n");

			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// log.error("MySQL Query Error. ", e);
			System.err.println(query);
			e.printStackTrace();
			sb = new StringBuffer();
			sb.append("<resultset>\n");
			sb.append("\t<statement>" + XML.escape(query) + "</statement>\n");
			sb.append("\t<message>" + XML.escape(e.getMessage())
					+ "</message>\n");
			sb.append("</resultset>\n");
		} finally {
			try {
				if(connection!=null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// log.trace(sb);
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
}
