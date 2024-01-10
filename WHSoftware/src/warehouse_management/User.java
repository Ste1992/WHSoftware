package warehouse_management;

import database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private int ID;
    private String login;

    public int getID() {
        return ID;
    }

    public String getLogin() {
        return login;
    }

    public User(int ID, String login) {
        this.ID = ID;
        this.login = login;
    }


    public static User getUser(int ID) {
        try (Connection conn  = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM User WHERE ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, ID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        ID = rs.getInt("ID");
                        String login = rs.getString("Login");
                        return new User(ID, login);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
