/*
 * Copyright 2015 (c) Codice Foundation
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
package org.codice.imaging.nitf.core;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A NitfWriter implementation that works on files.
 */
public class NitfFileWriter extends SharedNitfWriter {

    static final String WRITE_MODE = "rw";

    private String mOutputFileName = null;

    /**
     * Construct a file-based NITF writer.
     *
     * @param nitfDataSource the source of data to be written out
     * @param outputFileName the name (including path) of the target file
     */
    public NitfFileWriter(final NitfDataSource nitfDataSource, final String outputFileName) {
        super(nitfDataSource);
        mOutputFileName = outputFileName;
    }

    @Override
    public final void write() {
        try {
            try (RandomAccessFile outputFile = new RandomAccessFile(mOutputFileName, WRITE_MODE)) {
                outputFile.setLength(0);
                mOutput = outputFile;
                writeData();
            }
        } catch (IOException | ParseException ex) {
            Logger.getLogger(NitfFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
