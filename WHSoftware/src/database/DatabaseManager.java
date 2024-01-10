package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:/home/stefano/Development/IdeaProject/WHSoftware/DB_Connection/src/warehouse.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connessione al database chiusa.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
