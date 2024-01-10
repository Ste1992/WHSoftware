package warehouse_management;

import database.DatabaseManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Bin {

    private String binBarcode;

    public String getBinBarcode() {
        return binBarcode;
    }

    public void setBinBarcode(String binBarcode) {
        this.binBarcode = binBarcode;
    }

    public Bin(String binBarcode) {
        this.binBarcode = binBarcode;
    }



    public static @Nullable Bin getBinId(String binID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM Bin WHERE BinBarcode = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, binID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Bin(binID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Item> getItemToPick(@NotNull Bin bin) {
        List<Item> items = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection()) {
            // Seleziona gli articoli nel bin specificato
            String selectQuery = "SELECT Bin.*, Item.Barcode, Item.Titolo, Item.Descrizione,  " +
                    "(ABS(RANDOM() % (Item.Quantità - 1 + 1) + 1)) AS RandomQuantity " + // Genera un numero randomico tra 1 e Quantità massima
                    "FROM Bin " +
                    "JOIN Item ON Bin.BinBarcode = Item.Container " +
                    "WHERE Bin.BinBarcode = ? " +
                    "ORDER BY RANDOM() <= 0.5 " +
                    "LIMIT 1;";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setString(1, bin.getBinBarcode());

                try (ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        String itemName = rs.getString("Titolo");
                        String itemDescription = rs.getString("Descrizione");
                        String itemID = rs.getString("Barcode");

                        int itemQuantity;
                        do {
                            itemQuantity = rs.getInt("RandomQuantity");
                        } while (itemQuantity == 0);

                        String item_BinID = rs.getString("Barcode");

                        Item item = new Item(itemID, itemName, itemDescription, itemQuantity, item_BinID);
                        items.add(item);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public boolean isBinExists(String binID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM Bin WHERE BinBarcode = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, binID);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next(); // Restituisce true se il bin esiste, altrimenti false
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Gestisci eventuali eccezioni e ritorna false in caso di errore
        }
    }

    public Bin randomBin() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM Bin ORDER BY RANDOM() LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String binID = rs.getString("BinBarcode");
                        return new Bin(binID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void countItemsInBin(String binID) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT SUM(Quantità) AS TotalQuantity FROM Item WHERE Container = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, binID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int totalQuantity = rs.getInt("totalQuantity");
                        System.out.println("Total quantity in " + binID + ": " + totalQuantity);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
