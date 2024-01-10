package warehouse_management;

import database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Tote {

    private String barcode;
    public String getBarcode() {
        return barcode;
    }

    public Tote(String barcode) {
        this.barcode = barcode;
    }


    public static Tote getToteBarcode(String barcode) {
        try (Connection conn = DatabaseManager.getConnection()) {

            String query = "SELECT * FROM Tote WHERE ToteBarcode = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, barcode);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Tote(barcode);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isToteExists(String toteID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM Tote WHERE ToteBarcode = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, toteID);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
