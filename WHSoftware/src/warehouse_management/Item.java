package warehouse_management;

import database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Item {

    private String title;
    private String description;
    private String itemBarcode;
    private int quantity;
    private String bin;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getBin() {
        return bin;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public Item(String itemBarcode, String title, String description, int quantity, String bin) {
        this.itemBarcode = itemBarcode;
        this.title = title;
        this.description = description;
        this.quantity = quantity;
        this.bin = bin;
    }


    public static void addItemToInventory(Item item, int quantity) {
        updateItemQuantity(item, quantity);
    }

    public static void withdrawItemFromInventory(Item item, int quantity) {
        updateItemQuantity(item, -quantity);
    }

    private static void updateItemQuantity(Item item, int quantityChange) {
        try (Connection conn = DatabaseManager.getConnection()) {
            // Aggiorna la quantità esistente nel database
            String updateQuery = "UPDATE Item SET Quantità = Quantità + ? WHERE Barcode = ? AND Quantità + ? > 0";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, quantityChange);
                updateStmt.setString(2, item.getItemBarcode());
                updateStmt.setInt(3, -quantityChange);

                updateStmt.executeUpdate();

                conn.setAutoCommit(true); // Effettua il commit della transazione
                System.out.println("Quantità dell'Item aggiornata nel database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewItemToInventory(Item item) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "INSERT INTO Item (Barcode, Titolo, Descrizione, Quantità, Container) " +
                    "VALUES (?, ?, ?, ?, NULL)";
            try (PreparedStatement insertStmt = conn.prepareStatement(query)) {
                insertStmt.setString(1, getItemBarcode());
                insertStmt.setString(2, getTitle());
                insertStmt.setString(3, getDescription());
                insertStmt.setInt(4, getQuantity());

                int affectedRow = insertStmt.executeUpdate();

                if (affectedRow > 0) {
                    // Restituisci l'oggetto Item appena inserito senza l'ID
                    item = new Item(getItemBarcode(), getTitle(), getDescription(), getQuantity(), null);

                    System.out.println("Titolo: " + getTitle() + "\nDescrizione: " + getDescription()
                    + "\nQuantità: " + getQuantity());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addItemToBin(Item item) {
        try (Connection conn = DatabaseManager.getConnection()) {
            // Inserisci l'Item nel database
            String insertQuery = "INSERT INTO Item (Barcode, Nome, Descrizione, Quantità, Container)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, item.getItemBarcode());
                insertStmt.setString(2, item.getTitle());
                insertStmt.setString(3, item.getDescription());
                insertStmt.setInt(4, item.getQuantity());
                insertStmt.setString(5, item.getBin());

                insertStmt.executeUpdate();

                Item newItem = new Item(item.getItemBarcode(), item.getTitle(), item.getDescription(),
                        item.getQuantity(), item.getBin());

                // CONTROLLARE
                addItemToInventory(newItem, item.quantity);
                System.out.println("Item added to Bin successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Item randomItem() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM Item WHERE Container = ? ORDER BY RAND() LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, getBin());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String itemID = rs.getString("Barcode");
                        String itemName = rs.getString("Nome");
                        String itemDescription = rs.getString("Descrizione");
                        int itemQuantity = rs.getInt("Quantità");
                        String item_BinID = rs.getString("Container");

                        return new Item(itemID, itemName, itemDescription, itemQuantity, item_BinID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int randomItemQuantity() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT COUNT(*) FROM Item"; // Conta il numero totale di articoli nel database
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int totalItems = rs.getInt(1);
                        if (totalItems > 0) {
                            Random rand = new Random();
                            return rand.nextInt(totalItems) + 1;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Restituisci 0 in caso di errore o se non ci sono articoli nel database
    }

    public List<Item> getItemList() {
        List<Item> itemsList = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection()){
            String query = "SELECT * FROM Item";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String barcode = rs.getString("Barcode");
                        String title = rs.getString("Titolo");
                        String description = rs.getString("Descrizione");
                        int quantity = rs.getInt("Quantità");

                        Item item = new Item (barcode, title, description, quantity, null);
                        itemsList.add(item);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemsList;
    }

}
