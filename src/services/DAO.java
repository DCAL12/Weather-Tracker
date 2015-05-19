package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract class DAO {

    private static class DatabaseConfiguration {
        private static final String URI = "jdbc:mysql://localhost:3306/weather";
        private static final String USERNAME = "user";
        private static final String PASSWORD = "RLXREL4Z3VfWZV54";
        private static final String DRIVER = "com.mysql.jdbc.Driver";
    }

    private Connection connection;

    protected DAO() {
        try {
            Class.forName(DatabaseConfiguration.DRIVER);
            connection = DriverManager.getConnection(DatabaseConfiguration.URI,
                    DatabaseConfiguration.USERNAME, DatabaseConfiguration.PASSWORD);
        } catch (SQLException e) {
            System.out.println("services.SensorDAO SQL error: " + e.getMessage());
            connection = null;
        } catch (ClassNotFoundException c) {
            System.out.println("services.SensorDAO Driver error: " + c.getMessage());
            connection = null;
        }
    }

    protected Connection getConnection() {
        return connection;
    }
}
