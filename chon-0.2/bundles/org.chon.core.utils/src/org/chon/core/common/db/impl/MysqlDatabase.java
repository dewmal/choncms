package org.chon.core.common.db.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * TODO: see if we need optimization here currnet implementation opens
 * connection and closes on every query TODO: Logger
 * 
 * @author Jovica Veljanovski
 * 
 * 
 */

@Deprecated
public class MysqlDatabase extends AbstactDB {

	// public static final Logger log = Logger.getLogger(MysqlDatabase.class);

	Connection connection = null;
	DataSource ds = null;

	String connStr;
	String user;
	String password;

	public MysqlDatabase(DataSource ds) throws NamingException {
		this.ds=ds;
	}

	public MysqlDatabase(String conn, String user, String password) {
		System.err.println("Creating mysql database instance!!");
		if (conn == null) {
			conn = "jdbc:mysql://jpc:3306/jgra";
		}
		if (user == null) {
			user = "joco";
		}
		if (password == null) {
			password = "";
		}
		this.connStr = conn;
		this.user = user;
		this.password = password;

	}

	public Connection connect() {
		if (this.ds != null) {
			try {
				this.connection = ds.getConnection();
				System.out.println("Returning Pooled Connection");
				return this.connection;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (this.connection != null) {
			try {
				if (this.connection.isClosed()) {
					this.close();
					return this.connect();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Connection con = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// TODO:
			// TODO:
			// READ FROM PROPERTIES FILE DATABASE CONNECTION
			con = DriverManager.getConnection(connStr, user, password);

			// if (!con.isClosed())
			// System.out.println("Successfully connected to "
			// + "MySQL server using TCP/IP...");

			this.close(); // close any prev connections
			this.connection = con;
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			throw new RuntimeException(e);
		}
		return this.connection;
	}

	public void close() {
		if (this.ds == null) {
			if (this.connection != null) {
				try {
					this.connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.connection = null;
	}

	

	@Override
	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}

}
