package util;
import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;

public class DBUtil {
	private static final String JDBC_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
	private static Connection conn = null;
	//Connection String
	private static final String connStr = "jdbc:hsqldb:file:imsdb";

	public static void dbConnect() throws SQLException, ClassNotFoundException {
		//Setting Oracle JDBC Driver
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC driver not found?");
			e.printStackTrace();
			throw e;
		}

		System.out.println("JDBC Driver Registered!");

		//Establish the Oracle Connection using Connection String
		try {
			conn = DriverManager.getConnection(connStr, "admin", "admin123@qwert");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console" + e);
			e.printStackTrace();
			throw e;
		}
	}

	public static void dbDisconnect() throws SQLException {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e){
			throw e;
		}
	}

	public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
		//Declare statement, resultSet and CachedResultSet as null
		Statement stmt = null;
		ResultSet resultSet = null;
		CachedRowSetImpl crs = null;
		try {
			//Connect to DB (Establish Oracle Connection)
			dbConnect();
			System.out.println("Select statement: " + queryStmt + "\n");

			//Create statement
			stmt = conn.createStatement();

			//Execute select (query) operation
			resultSet = stmt.executeQuery(queryStmt);

			//CachedRowSet Implementation
			//In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
			//We are using CachedRowSet
			crs = new CachedRowSetImpl();
			crs.populate(resultSet);
		} catch (SQLException e) {
			System.out.println("Problem occurred at executeQuery operation : " + e);
			throw e;
		} finally {
			if (resultSet != null) {
				//Close resultSet
				resultSet.close();
			}
			if (stmt != null) {
				//Close Statement
				stmt.close();
			}
			//Close connection
			dbDisconnect();
		}
		//Return CachedRowSet
		return crs;
	}

	public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
		//Declare statement as null
		Statement stmt = null;
		try {
			//Connect to DB (Establish Oracle Connection)
			dbConnect();
			//Create Statement
			stmt = conn.createStatement();
			//Run executeUpdate operation with given sql statement
			stmt.executeUpdate(sqlStmt);
		} catch (SQLException e) {
			System.out.println("Problem occurred at executeUpdate operation : " + e);
			throw e;
		} finally {
			if (stmt != null) {
				//Close statement
				stmt.close();
			}
			//Close connection
			dbDisconnect();
		}
	}


}
