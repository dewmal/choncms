package org.chon.core.common.db;

import java.sql.Connection;

import org.json.JSONObject;

/**
 * Interface used for simple query database
 * query method will work simular as
 * php mysq_query
 * 
 * @author Jovica Veljanovski
 *
 */
public interface Database {

	/**
	 * makes connection to a database
	 *  YOU ARE RESPONSIBLE FOR CLOSING THE CONNECTION
	 * @return
	 */
	public Connection connect();

	/**
	 * query database, simular like php mysql_query implementation only that
	 * retuls here will be returned in results callback,
	 * 
	 * @see {@link ResultCallback}
	 * @param query
	 * @param callback
	 */
	public void query(String query, ResultCallback callback);
	
	public void queryRS(String query, ResultSetCallback callback);
	
	public JSONObject queryJSON(String query);
	public String queryXML(String query);
}
