/*
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 */
package org.codice.nitf.metadatacomparison;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileComparison {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileComparison.class);

    private FileComparison() {
    }

    public static void main( String[] args ) {
        if (args.length == 0) {
            LOGGER.error("No file provided, not comparing");
            return;
        }
        for (String arg : args) {
            if (new File(arg).isDirectory()) {
                LOGGER.info("Walking contents of " + arg);
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
            LOGGER.info("Dumping output of " + filename);
            compareOneFile(filename);
        }
    }

    private static void compareOneFile(String filename) {
        new FileComparer(filename);
    }
}
