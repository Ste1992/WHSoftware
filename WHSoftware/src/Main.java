import software.Menu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String []args) {
        initializeDatabase();

        Menu menu = new Menu();
        Scanner input = new Scanner(System.in);

        menu.softwareMenu(input);
    }


    public static void initializeDatabase() {
        try {
            // Carica il driver JDBC
            Class.forName("org.sqlite.JDBC");

            String url = "jdbc:sqlite:/home/stefano/Development/IdeaProject/WHSoftware/DB_Connection/src/warehouse.db";
            Connection conn = DriverManager.getConnection(url);

            // Altri passaggi di inizializzazione del database, se necessario
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
