package software;

import warehouse_management.Bin;
import warehouse_management.Item;
import warehouse_management.Tote;

import java.util.Scanner;

public class Menu {

    public void softwareMenu(Scanner input) {Tote tote = new Tote("");
        Bin bin = new Bin("");
        Item item = new Item("", "", "", 0, "");
        Login login = new Login();
        Pick pick = new Pick(tote, bin, item);
        Receive receive = new Receive(tote, bin, item);

        login.getUserLoggedIn(input);

        System.out.println("\n\t\tMenù\n1. Receive\n2. Pick");
        System.out.print("Input: ");
        int choice = input.nextInt();
        input.nextLine(); // Consuma il carattere di riga residuo
        switch (choice) {
            case 1:
                receive.receiveItemTool();
                break;
            case 2:
                pick.pickItemTool(input);
                break;
            default:
                System.out.println("Invalid input.");
                break;
        }

        login.getUserLoggedOut(input);

        input.close();
    }


}
