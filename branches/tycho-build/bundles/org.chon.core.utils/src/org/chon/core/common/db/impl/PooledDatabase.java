package org.chon.core.common.db.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class PooledDatabase extends AbstactDB {

	private DataSource ds;
	//private Connection conn;

	public PooledDatabase(DataSource ds) {
		this.ds = ds;
	}

	public void close() {
		/*
		if (this.conn != null) {
			try {
				if (!this.conn.isClosed())
					this.conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.conn = null;
		}
		*/
		
	}

	public Connection connect() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		/*
		if(this.conn!=null) {
			try {
				if(!this.conn.isClosed())
					return this.conn;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			this.conn = ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.conn;
		*/
	}
}
