package main.java.com.core.utils.connectionmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnectionManager {
    private String url = null;
    private  String username = null;
    private  String password = null;

    public DataBaseConnectionManager(){

    }
    public void DatabaseConnectionManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String dbUrl = "jdbc:mysql://localhost:3306/your_database";
        String dbUsername = "your_username";
        String dbPassword = "your_password";

//        try {
//            DatabaseConnectionManager connectionManager = new DatabaseConnectionManager(dbUrl, dbUsername, dbPassword);
//
//            // Obtain a connection
//            Connection connection = connectionManager.getConnection();
//
//            // Use the connection for database operations
//
//            // Close the connection when done
//            connectionManager.closeConnection(connection);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
