package org.example.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private FileManager() {}

    public static boolean writeFile(String filePath, String content) {
        try {
            Path path = Paths.get(filePath);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, content);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing file [" + filePath + "]: " + e.getMessage());
            return false;
        }
    }

    public static String readFile(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading file [" + filePath + "]: " + e.getMessage());
            return null;
        }
    }

    public static List<String> listReceipts(String folderPath) {
        List<String> names = new ArrayList<>();
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) return names;

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) return names;

        java.util.Arrays.sort(files, (a, b) -> b.getName().compareTo(a.getName()));
        for (File f : files) names.add(f.getName());
        return names;
    }

    public static boolean ensureDirectory(String path) {
        try {
            Files.createDirectories(Paths.get(path));
            return true;
        } catch (IOException e) {
            System.err.println("Could not create directory [" + path + "]: " + e.getMessage());
            return false;
        }
    }
}
