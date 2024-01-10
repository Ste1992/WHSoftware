package software;

import ext.*;
import org.jetbrains.annotations.NotNull;
import warehouse_management.Bin;
import warehouse_management.Item;
import warehouse_management.Tote;

import java.util.List;
import java.util.Scanner;

public class Pick implements ScanTote, ScanItem, ScanBin, CloseTote, QuitProcess {

    private final ProcessUtilities utilities;
    private static final String INVALID_INPUT_MESSAGE = "Invalid input. Please enter Y or N";
    private static final String INSERT_QUANTITY = "Insert quantity:";
    private static final String WRONG_BARCODE_SCANNED = "Wrong barcode scanned.";
    private static final String SCAN_BIN = "Scan bin: ";
    private static final String SCAN_TOTE = "Scan tote:";

    public Pick(Tote tote, Bin bin, Item item) {
        this.utilities = new ProcessUtilities(tote, bin, item);
    }

    public void pickItemTool(Scanner input) {
        boolean quit;
        do {
            processTote(input);
            quit = quitProcessInterface(input);
        } while (!quit);
    }

    private void processTote(Scanner input) {
        scanToteInterface(input);
        utilities.setRandomBin(utilities.getBin().randomBin());
        do {
            processBin(input);
        } while (closeToteInterface(input));
    }

    private void processBin(Scanner input) {
        scanBinInterface(input);
        scanItemInterface(input);
    }

    @Override
    public void scanToteInterface(@NotNull Scanner input) {
        System.out.println(SCAN_TOTE);
        boolean toteExists;
        do {
            utilities.setScannedTote(input.nextLine().trim());
            toteExists = utilities.getTote().isToteExists(utilities.getScannedTote());
            if (toteExists) {
                System.out.println("Container: " + utilities.getScannedTote());

            } else {
                System.out.println(WRONG_BARCODE_SCANNED);
                System.out.println(SCAN_TOTE);
            }
        } while (!toteExists);
    }

    @Override
    public void scanBinInterface(Scanner input) {
        utilities.setRandomBin(utilities.getBin().randomBin());

        if (utilities.getRandomBin() != null) {
            System.out.println("Bin " + utilities.getRandomBin().getBinBarcode());
            System.out.println(SCAN_BIN);
            String binScanned = input.next().trim();
            input.nextLine();

            while(!utilities.getBin().isBinExists(binScanned)) {
                System.out.println(WRONG_BARCODE_SCANNED);
                System.out.println(SCAN_BIN);
                binScanned = input.nextLine().trim();
                input.nextLine();
            }
        } else {
            System.out.println("No bins in the database");
        }
    }

    @Override
    public void scanItemInterface(Scanner input) {
        if (utilities.getRandomBin() != null) {
            List<Item> itemsInBin = utilities.getRandomBin().getItemToPick(utilities.getRandomBin());

            if (!itemsInBin.isEmpty()) {
                System.out.println("Scan from bin " + utilities.getRandomBin().getBinBarcode() + ":");
                for (Item item : itemsInBin) {
                    System.out.println(item.getItemBarcode() + ": " + item.getTitle() + " - " + item.getDescription() + " x" + item.getQuantity());
                }

                System.out.println("Scan item:");
                String scannedItem = input.nextLine().trim();

                // Quantity management
                for (Item item : itemsInBin) {
                    if (item.getItemBarcode().equals(scannedItem)) {
                        System.out.println(INSERT_QUANTITY);
                        utilities.setQuantityInput(input.nextLine().trim());

                        int quantity = Integer.parseInt(utilities.getQuantityInput());

                        // Input check
                        while (quantity == 0 || quantity > item.getQuantity()) {
                            System.out.println(INSERT_QUANTITY);
                            utilities.setQuantityInput(input.nextLine().trim());

                            if (quantity != 0 || quantity < item.getQuantity()) {
                                break;
                            }
                        }

                        quantity = Integer.parseInt(utilities.getQuantityInput());

                        if (quantity == item.getQuantity()) {
                            System.out.println(quantity + " picked.");

                            utilities.setAux(utilities.getAux() + quantity);
                            System.out.println("Units in tote: " + quantity);

                            Item.withdrawItemFromInventory(item, quantity);

                        } else if (quantity != item.getQuantity()) {
                            int amountLeftToPick = item.getQuantity() - quantity;
                            System.out.println(item.getItemBarcode() + ": " + item.getTitle() + " - " + item.getDescription() + " x" + amountLeftToPick);

                            utilities.setTotalUnitsInTote(utilities.getAux() + quantity);
                            System.out.println("Units in tote: " + utilities.getTotalUnitsInTote());

                            Item.withdrawItemFromInventory(item, quantity);
                        }

                    } else {
                        System.out.println(WRONG_BARCODE_SCANNED);
                    }
                }
            } else {
                System.out.println("No items available in the specified bin.");
            }
        } else {
            System.out.println("No bins available in the database.");
        }
    }

    @Override
    public boolean closeToteInterface(@NotNull Scanner input) {
        System.out.println();
        System.out.println("Continue with " + utilities.getScannedTote() + "? Y/N");
        String choice = input.nextLine().trim();

        if(choice.equalsIgnoreCase("Y")) {
            return true;

        } else if (choice.equalsIgnoreCase("N")) {
            System.out.println(utilities.getScannedTote() + " closed.");
            System.out.println("Total units in " + utilities.getScannedTote() + ": " + utilities.getTotalUnitsInTote());
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
