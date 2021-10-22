package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    // default settings
    private static final String HOST = "localhost";
    private static final String DB_NAME = "sakila";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";


    public static Connection getDBConnection() throws SQLException {
        return getDBConnection(HOST, DB_NAME, USERNAME, PASSWORD);
    }

    public static Connection getDBConnection(String HOST, String DB_NAME, String USERNAME, String PASSWORD)
            throws SQLException {

        String connectionURL = "jdbc:mysql://" + HOST + ":3306/" + DB_NAME;
        return DriverManager.getConnection(connectionURL, USERNAME, PASSWORD);
    }
}
