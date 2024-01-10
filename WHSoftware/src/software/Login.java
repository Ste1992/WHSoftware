package software;

import org.jetbrains.annotations.NotNull;
import warehouse_management.User;

import java.util.Scanner;

public class Login {

    public boolean getUserLoggedIn(@NotNull Scanner input) {
        System.out.println("Scan badge: ");
        while (!input.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid ID.");
            input.nextLine(); // Consuma l'input non valido
        }
        int userID = input.nextInt();

        // Richiama il metodo getUser dal warehouse_management.User
        User user = User.getUser(userID);

        if (user != null) {
            System.out.println("Login Successful! Welcome, " + user.getLogin());
            return false;

        } else {
            System.out.println("Login failed. Invalid ID.");
            return getUserLoggedIn(input);
        }
    }

    public void getUserLoggedOut(@NotNull Scanner input) {
        System.out.println("Do you want to logout? Y/N");
        String choice = input.nextLine().trim();

        while (true) {
            Menu menu = new Menu();
            if (choice.equalsIgnoreCase("Y")) {
                System.out.println("Successfully logged out.");
                System.exit(0);

            } else if (choice.equalsIgnoreCase("N")) {
                menu.softwareMenu(input);
                return;

            } else {
                System.out.println("Invalid input.");
            }

            System.out.println("Do you want to logout? Y/N");
            choice = input.nextLine().trim();
        }
    }

}
