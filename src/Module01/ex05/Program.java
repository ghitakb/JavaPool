//import java.awt.*;
//import java.awt.*;
import java.awt.*;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean devMode = args.length == 1 && args[0].equals("--profile=dev");

        Menu menu = new Menu(devMode);

        try {
            while (true)
            {
                menu.printMenu();
                String option = input.nextLine();
                menu.handleMenuChoice(option, input);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        finally {
            input.close();
        }
    }
}























