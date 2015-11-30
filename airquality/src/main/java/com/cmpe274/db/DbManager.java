package com.cmpe274.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 */

/**
 * @author madhur
 *
 */
public class DbManager {
	public Connection connection;
	public Statement statement;
	public static DbManager dbManager;
	private DbManager() {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "AirQuality";
		String userName = "java";
		String password = "password";
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver).newInstance();
			this.connection = DriverManager.getConnection(url+dbName, userName, password);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized DbManager getDbCon() {
		if ( dbManager == null ) {
			dbManager = new DbManager();
		}
		return dbManager;
	}

	public int insert(String insertQuery) throws SQLException {
		statement = dbManager.connection.createStatement();
		int result = statement.executeUpdate(insertQuery);
		return result;
	}

}
