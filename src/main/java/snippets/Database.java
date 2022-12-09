package snippets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Database {

	static Logger logger = LogManager.getLogger(Database.class);

	static String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

	static String SERVR = "127.0.0.1", PORT = "3306";
	static String USER_NAME = "root", PASSWORD = "root";
	static String DB_NAME = "test";

	// @URI jdbc:mysql://server-name:server-port/database-name
	static String URI = "jdbc:mysql://" + SERVR + ":" + PORT + "/" + DB_NAME;

	/**
	 * @return Connection object if successful, otherwise null
	 */

	public static Connection connect() {
		Connection con = null;

		try {
			Class.forName(MYSQL_DRIVER);
			logger.info("Driver '" + MYSQL_DRIVER + "' loaded.");
			con = DriverManager.getConnection(URI, USER_NAME, PASSWORD);
			logger.info("Connection created.");
		} catch (ClassNotFoundException e) {
			logger.fatal("Couldn't load the Driver class, " + e);
		} catch (SQLException e) {
			logger.fatal("Invalid credentials either with uri, user or password.\n" + e);
		}

		return con;
	}

}
