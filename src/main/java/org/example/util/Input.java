package org.example.util;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.List;

public class Input {
    private static final TextIO       textIO   = TextIoFactory.getTextIO();
    private static final TextTerminal terminal = textIO.getTextTerminal();

    private Input() {}

    public static int readInt(String prompt, int min, int max) {
        return textIO.newIntInputReader()
                .withMinVal(min)
                .withMaxVal(max)
                .read(prompt);
    }

    public static boolean readYesNo(String prompt) {
        return textIO.newBooleanInputReader()
                .withTrueInput("y")
                .withFalseInput("n")
                .read(prompt);
    }

    public static String readLine(String prompt) {
        return textIO.newStringInputReader()
                .withMinLength(1)
                .read(prompt);
    }

    public static <T> T pickFromList(String prompt, List<T> items) {
        if (!prompt.isEmpty()) {
            terminal.println(prompt);
        }
        for (int i = 0; i < items.size(); i++) {
            terminal.printf("  %d. %s%n", i + 1, items.get(i));
        }
        int choice = readInt("Enter choice: ", 1, items.size());
        return items.get(choice - 1);
    }

    public static void println(String message) {
        terminal.println(message);
    }

    public static void printf(String format, Object... args) {
        terminal.printf(format, args);
    }

    public static void pressEnterToContinue() {
        textIO.newStringInputReader()
                .withMinLength(0)
                .read("\nPress ENTER to continue...");
    }

    public static void printDivider() {
        printWithColor("----------------------------------------", "cyan");
    }

    public static void printHeader(String title) {
        printWithColor("\n========================================", "cyan");
        printWithColor("  " + title,                               "cyan");
        printWithColor("========================================",  "cyan");
    }

    public static void printWithColor(String message, String color) {
        terminal.getProperties().setPromptColor(color);
        terminal.println(message);
        terminal.getProperties().setPromptColor((String) null);
    }

    public static void printSuccess(String message) {
        printWithColor(message, "green");
    }

    public static void printError(String message) {
        printWithColor(message, "red");
    }

    public static void printHighlight(String message) {
        printWithColor(message, "yellow");
    }

    public static void resetColor() {
        terminal.getProperties().setPromptColor("cyan");
    }

    public static void dispose() {
        textIO.dispose();
    }
}