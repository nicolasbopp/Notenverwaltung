package io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class MajorMapReader {
    public static HashMap readMajorData(File majorMapFile) {
        HashMap<String, String> majorMap = new HashMap<String, String>();
        try {
            Scanner scanner = new Scanner(majorMapFile);
            while (scanner.hasNextLine()) {
                String text = scanner.nextLine();
                String[] tokens = text.split("\\s+");
                majorMap.put(tokens[0], tokens[1]);
            }
        } catch (IOException e) {
            System.out.println("Unable to read major data, sorry.");
        }
        return majorMap;
    }
}
