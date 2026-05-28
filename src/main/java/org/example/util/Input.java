package org.example.util;

import java.util.List;
import java.util.Scanner;

public class Input {

    private static final Scanner SCANNER= new Scanner(System.in);

    private Input() {}

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(SCANNER.nextLine().trim());
                if (value >= min && value <= max) return value;
                System.out.printf("  Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input — please enter a number.");
            }
        }
    }

    public static <T> T pickFromList(String prompt, List<T> items) {
        System.out.println(prompt);
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, items.get(i));
        }
        int choice = readInt("Enter choice: ", 1, items.size());
        return items.get(choice - 1);
    }

    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String in = SCANNER.nextLine().trim().toLowerCase();
            if (in.equals("y") || in.equals("yes")) return true;
            if (in.equals("n") || in.equals("no"))  return false;
            System.out.println("  Please enter 'y' or 'n'.");
        }
    }

    public static String readLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String in = SCANNER.nextLine().trim();
            if (!in.isEmpty()) return in;
            System.out.println("  Input cannot be blank.");
        }
    }

    public static void pressEnterToContinue() {
        System.out.print("\nPress ENTER to continue...");
        SCANNER.nextLine();
    }

    public static void printDivider() {
        System.out.println("----------------------------------------");
    }

    public static void printHeader(String title) {
        System.out.println("\n========================================");
        System.out.println("  " + title);
        System.out.println("========================================");
    }
}
