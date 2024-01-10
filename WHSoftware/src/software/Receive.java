package software;

import ext.CloseTote;
import ext.QuitProcess;
import ext.ScanItem;
import ext.ScanTote;
import org.jetbrains.annotations.NotNull;
import warehouse_management.Bin;
import warehouse_management.Item;
import warehouse_management.Tote;

import java.util.Scanner;

public class Receive implements ScanTote, ScanItem, CloseTote, QuitProcess {

    private final ProcessUtilities processUtilities;
    private final Scanner input;
    private static final String INVALID_INPUT_MESSAGE = "Invalid input. Please enter Y or N";
    private static final String INSERT_QUANTITY = "Insert quantity:";
    private static final String SCAN_TOTE = "Scan tote:";

    public Receive(Tote tote, Bin bin, Item item) {
        this.processUtilities = new ProcessUtilities(tote, bin, item);
        this.input = new Scanner(System.in);
    }


    public void receiveItemTool() {
        scanItemInterface(input);
    }

    @Override
    public void scanToteInterface(@NotNull Scanner input) {
        System.out.println(SCAN_TOTE);
        boolean toteExists;
        do {
            processUtilities.setScannedTote(input.nextLine().trim());
            input.nextLine(); // Consuma il carattere di nuova riga residuo
            toteExists = processUtilities.getTote().isToteExists(processUtilities.getScannedTote());
            if (toteExists) {
                System.out.println("Container: " + processUtilities.getScannedTote());

            } else {
                System.out.println("Invalid tote scanned.\n" + SCAN_TOTE);
            }
        } while (!toteExists);
    }

    @Override
    public void scanItemInterface(@NotNull Scanner input) {
        System.out.println();
        System.out.println("Scan barcode: ");
        String barcode = input.nextLine().trim();
        input.nextLine(); // Consuma l'invio residuo

        // Cerca l'articolo nel database
        Item foundItem = null;
        for (Item itemFromList : processUtilities.getItem().getItemList()) {
            if (barcode.equals(itemFromList.getItemBarcode())) {
                foundItem = itemFromList;
                break;
            }
        }

        if (foundItem != null) {
            System.out.println("Loading item's details...\n" +
                    "Title: " + foundItem.getTitle() + "\n" + "Description: " + foundItem.getDescription());

            System.out.println(INSERT_QUANTITY);
            int quantity = 0;
            quantity = input.nextInt();
            input.nextLine(); // Consuma il carattere di riga residuo

            Item item1 = new Item(foundItem.getItemBarcode(), foundItem.getTitle(),
                    foundItem.getDescription(), quantity, null);

            processUtilities.getItem().addNewItemToInventory(item1);

        } else {
            System.out.println("Add Title: ");
            String title = input.nextLine().trim();
            System.out.println("Add Description: ");
            String description = input.nextLine().trim();

            int quantity = 0;
            while (!input.hasNextInt()) {
                System.out.println(INSERT_QUANTITY);
                if (input.hasNextInt()) {
                    quantity = input.nextInt();
                    input.nextLine(); // Consuma il carattere di riga residuo
                    break;

                } else {
                    System.out.println("Invalid input, numbers allowed only.");
                    input.nextLine(); // Consuma l'invio residuo
                }
            }

            Item item2 = new Item(barcode, title, description, quantity, null);
            Item.addItemToInventory(item2, quantity);
        }
    }

    @Override
    public boolean closeToteInterface(@NotNull Scanner input) {
        System.out.println();
        System.out.println("Continue with " + processUtilities.getScannedTote() + "? Y/N");
        String choice = input.nextLine().trim();
        input.nextLine(); // Consuma il carattere di riga


        if(choice.equalsIgnoreCase("Y")) {
            return true;

        } else if (choice.equalsIgnoreCase("N")) {
            System.out.println(processUtilities.getScannedTote() + " closed.");
            return false;

        } else {
            System.out.println(INVALID_INPUT_MESSAGE);
            return closeToteInterface(input);
        }
    }

    @Override
    public boolean quitProcessInterface(@NotNull Scanner input) {
        System.out.println();
        System.out.println("Do you want to quit the process? Y/N");
        String choice = input.nextLine().trim();
        input.nextLine(); // Consuma il carattere di riga

        if(choice.equalsIgnoreCase("Y")) {
            System.out.println("Loading menu...");
            return true;

        } else if (choice.equalsIgnoreCase("N")){
            return false;
        } else {
            System.out.println(INVALID_INPUT_MESSAGE);
            return quitProcessInterface(input);
        }
    }

}
