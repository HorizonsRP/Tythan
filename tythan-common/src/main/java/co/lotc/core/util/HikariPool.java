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

	private HikariConfig config = new HikariConfig();
	private HikariDataSource ds;

	/**
	 * @param jdbc_url Create a HikariPool with the jdbc url directly.
	 */
	public HikariPool(String jdbc_url,
					  String username, String password) {
		config.setUsername(username);
		config.setPassword(password);
		config.setJdbcUrl(jdbc_url);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
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
	 */
	public HikariPool(String host, int port, String database, String flags,
					  String username, String password) {
		if (flags == null) {
			flags = "";
		}
		config.setUsername(username);
		config.setPassword(password);
		config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + flags);
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
