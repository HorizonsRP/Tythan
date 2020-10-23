package co.lotc.core.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Creates an instance based HikariCP Data Source to be able to get SQL connections from. To
 * use effectively, create the HikariPool instance on plugin launch, then reference it to interact
 * with your SQL DB directly.
 */
public class HikariPool {

	private static long defaultIdleTimeout = 28740000;
	private static long defaultMaxLife = 28740000;
	private HikariConfig config = new HikariConfig();
	private HikariDataSource ds;

	// Host Port Database
	/**
	 * Create a HikariPool with the provided data and Tythan will build the
	 * jdbc url for you with the given parts.
	 * @param host The IP of the SQL Database
	 * @param port The port of the SQL Database
	 * @param database The name of the SQL Database
	 * @param flags Any flags to add to the end such as '?useSSL=false'
	 * @param username The username for the SQL Database
	 * @param password The password for the SQL Database
	 */
	public static HikariPool getHikariPool(String host, int port, String database, String flags,
									String username, String password) {
		return getHikariPool(host, port, database, flags, defaultIdleTimeout, defaultMaxLife, username, password);
	}

	/**
	 * Create a HikariPool with the provided data and Tythan will build the
	 * jdbc url for you with the given parts.
	 * @param host The IP of the SQL Database
	 * @param port The port of the SQL Database
	 * @param database The name of the SQL Database
	 * @param flags Any flags to add to the end such as '?useSSL=false'
	 * @param username The username for the SQL Database
	 * @param password The password for the SQL Database
	 * @param idleTimeout The idleTimeout of your SQL Database
	 * @param maxLifetime the maxLifetime of your SQL Database
	 */
	public static HikariPool getHikariPool(String host, int port, String database, String flags,
						 long idleTimeout, long maxLifetime,
						 String username, String password) {
		String jdbc_url = "jdbc:mysql://" + host + ":" + port + "/" + database + flags;
		return getHikariPool(jdbc_url, idleTimeout, maxLifetime, username, password);
	}

	// JDBC URL
	/**
	 * Create a HikariPool with the provided data
	 * @param jdbc_url The SQL connecter URL
	 * @param username The username for the SQL Database
	 * @param password The password for the SQL Database
	 */
	public static HikariPool getHikariPool(String jdbc_url, String username, String password) {
		return getHikariPool(jdbc_url, defaultIdleTimeout, defaultMaxLife, username, password);
	}

	/**
	 * Create a HikariPool with the provided data
	 * @param jdbc_url The SQL connecter URL
	 * @param username The username for the SQL Database
	 * @param password The password for the SQL Database
	 * @param idleTimeout The idleTimeout of your SQL Database
	 * @param maxLifetime the maxLifetime of your SQL Database
	 */
	public static HikariPool getHikariPool(String jdbc_url, long idleTimeout, long maxLifetime,
										   String username, String password) {
		return new HikariPool(jdbc_url, idleTimeout, maxLifetime, username, password);
	}

	private HikariPool(String jdbc_url, long idleTimeout, long maxLifetime,
					  String username, String password) {
		config.setUsername(username);
		config.setPassword(password);
		config.setJdbcUrl(jdbc_url);
		config.setIdleTimeout(idleTimeout);
		config.setMaxLifetime(maxLifetime);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
	}

	/**
	 * @return A JDBC Connection grabbed through HikariPool using the provided parameters.
	 * @throws SQLException If there is an error connecting, it will be thrown here.
	 */
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
