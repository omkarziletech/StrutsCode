package com.logiware.common.dao;

import com.logiware.common.form.DbMonitorForm;
import com.logiware.common.model.DbMonitorModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class DbMonitorDAO {

    private static final Logger log = Logger.getLogger(DbMonitorDAO.class);
    private static Properties properties = null;
    private Connection connection;

    private static void init() throws Exception {
	properties = new Properties();
	properties.load(DbMonitorDAO.class.getResourceAsStream("/hibernate.properties"));
    }

    public static String getProperty(String property) throws Exception {
	if (properties == null) {
	    init();
	}
	return properties.getProperty(property);
    }

    private void connect() throws Exception {
	Class.forName("com.mysql.jdbc.Driver");
	String url = getProperty("hibernate.connection.url");
	String username = getProperty("hibernate.connection.username");
	String password = getProperty("hibernate.connection.password");
	connection = DriverManager.getConnection(url, username, password);
    }

    private void disconnect() throws Exception {
	if (connection != null) {
	    connection.close();
	}
    }

    public Integer getTotalRows() throws Exception {
	PreparedStatement statement = null;
	ResultSet result = null;
	try {
	    connect();
	    String query = "select count(*) as process_count from information_schema.processlist";
	    statement = connection.prepareStatement(query);
	    result = statement.executeQuery();
	    return result.next() ? Integer.parseInt(result.getString("process_count")) : 0;
	} catch (Exception e) {
	    throw e;
	} finally {
	    if (null != result) {
		result.close();
	    }
	    if (null != statement) {
		statement.close();
	    }
	    disconnect();
	}
    }

    public void search(DbMonitorForm dbMonitorForm) throws Exception {
	PreparedStatement statement = null;
	ResultSet result = null;
	try {
	    int totalRows = getTotalRows();
	    if (totalRows > 0) {
		connect();
		int limit = dbMonitorForm.getLimit();
		int start = limit * (dbMonitorForm.getSelectedPage() - 1);
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select p.id as id,");
		queryBuilder.append("p.user as user,");
		queryBuilder.append("p.host as host,");
		queryBuilder.append("p.db as db,");
		queryBuilder.append("p.command as command,");
		queryBuilder.append("p.time as time,");
		queryBuilder.append("p.state as state,");
		queryBuilder.append("p.info as info,");
		queryBuilder.append("m.connection_time as connection_time,");
		queryBuilder.append("m.user_name as user_name,");
		queryBuilder.append("m.path as path,");
		queryBuilder.append("m.action as action");
		queryBuilder.append(" from information_schema.processlist p");
		queryBuilder.append(" left join db_monitor m");
		queryBuilder.append(" on p.id = m.connection_id");
		queryBuilder.append(" order by ").append(dbMonitorForm.getSortBy()).append(" ").append(dbMonitorForm.getOrderBy());
		queryBuilder.append(" limit ").append(start).append(",").append(limit);
		statement = connection.prepareStatement(queryBuilder.toString());
		result = statement.executeQuery();
		List<DbMonitorModel> processes = new ArrayList<DbMonitorModel>();
		while (result.next()) {
		    processes.add(new DbMonitorModel(result));
		}
		dbMonitorForm.setProcesses(processes);
		dbMonitorForm.setSelectedRows(processes.size());
		int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
		dbMonitorForm.setTotalPages(totalPages);
		dbMonitorForm.setTotalRows(totalRows);
	    }
	} catch (Exception e) {
	    log.error(e);
	} finally {
	    if (null != result) {
		result.close();
	    }
	    if (null != statement) {
		statement.close();
	    }
	    disconnect();
	}
    }

    public void killProcess(DbMonitorForm dbMonitorForm) throws Exception {
	PreparedStatement statement = null;
	try {
	    connect();
	    String query = "kill connection " + dbMonitorForm.getId();
	    statement = connection.prepareStatement(query);
	    statement.executeUpdate();
	} catch (SQLException e) {
	    log.error(e);
	    throw e;
	} catch (Exception e) {
	    log.error(e);
	} finally {
	    if (null != statement) {
		statement.close();
	    }
	    disconnect();
	}
    }

    public void saveConnectionDetails(String connectionId, String userName, String path, String action) throws Exception {
	PreparedStatement statement = null;
	try {
	    connect();
	    StringBuilder queryBuilder = new StringBuilder();
	    queryBuilder.append("insert into db_monitor");
	    queryBuilder.append(" (connection_id, connection_time, user_name, path, action)");
	    queryBuilder.append(" values (?, ?, ?, ?, ?)");
	    statement = connection.prepareStatement(queryBuilder.toString());
	    statement.setString(1, connectionId);
	    statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
	    statement.setString(3, userName);
	    statement.setString(4, path);
	    statement.setString(5, action);
	    statement.executeUpdate();
	} catch (Exception e) {
	    log.error(e);
	} finally {
	    if (null != statement) {
		statement.close();
	    }
	    disconnect();
	}
    }

    public void removeConnectionDetails(String connectionId) throws Exception {
	PreparedStatement statement = null;
	try {
	    connect();
	    StringBuilder queryBuilder = new StringBuilder();
	    queryBuilder.append("delete from db_monitor");
	    queryBuilder.append(" where connection_id = ?");
	    statement = connection.prepareStatement(queryBuilder.toString());
	    statement.setString(1, connectionId);
	    statement.executeUpdate();
	} catch (Exception e) {
	    log.error(e);
	} finally {
	    if (null != statement) {
		statement.close();
	    }
	    disconnect();
	}
    }
}
