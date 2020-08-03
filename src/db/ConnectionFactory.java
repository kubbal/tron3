/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

/**
 *
 * @author Balázs
 */
public class ConnectionFactory {
    private static MysqlConnectionPoolDataSource conn;
    
    public ConnectionFactory() {}

    public static java.sql.Connection getConnection() throws ClassNotFoundException, java.sql.SQLException {
    if (conn == null) {
        Class.forName("com.mysql.jdbc.Driver");
        conn = new MysqlConnectionPoolDataSource();
        conn.setServerName("localhost");
        conn.setPort(3306);
        conn.setDatabaseName("tron3");
        conn.setUser("root");
        conn.setPassword("admin");
    }
    return conn.getPooledConnection().getConnection();
   }
}
