package org.chon.core.common.db;


import java.util.Map;

/**
 * interface for retrieving results from 
 * {@link Database} query method
 * 
 * @author Jovica Veljanovski
 *
 */
public interface ResultCallback {
	/**
	 * return static keys to be 
	 * retrived in the query
	 * eg. return new String[] {"user", "pass"}
	 * will be used in map in process method
	 * @return keys (column names)
	 */
	public String [] getKeys();
	/**
	 * Will return one by one rows
	 * colums will be mapped in argument
	 * by the getKeys method
	 * @param object
	 */
	public void process(Map<String, String> object);
}
