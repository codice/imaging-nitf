package org.codice.nitf.metadatacomparison;

import java.io.File;

public class FileComparison {

    private FileComparison() {
    }

    public static void main( String[] args ) {
        if (args.length == 0) {
            System.out.println("No file provided, not comparing");
            return;
        }
        for (String arg : args) {
            if (new File(arg).isDirectory()) {
                System.out.println("Walking contents of " + arg);
                File[] files = new File(arg).listFiles();
                for (File file : files) {
                    handleFile(arg + "/" + file.getName());
                }
            } else {
                handleFile(arg);
            }
        }
    }

    private static void handleFile(String filename) {
        if (new File(filename).isFile() && (! filename.endsWith(".txt"))) {
            System.out.println("Dumping output of " + filename);
            compareOneFile(filename);
        }
    }

    private static void compareOneFile(String filename) {
        new FileComparer(filename);
    }
}
