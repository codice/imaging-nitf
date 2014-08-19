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
package org.codice.imaging.nitf.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.EnumSet;
import java.util.Set;

/**
    Factory class for NitfFile instances.
 */
public final class NitfFileFactory {

    /**
        Private constructor.
        <p>
        This class is not intended to be instantiated.
    */
    private NitfFileFactory() {
    }

    /**
        Parse NITF file headers out of an InputStream.
        <p>
        This only extracts the headers from the main file, and the attached segment headers. The actual
        data segments are skipped over.
        <p>
        Because this takes an InputStream (which is not seekable), it cannot work on NITF files that use
        "streaming mode", where the sizes are only provided at the end of the file. An exception will be
        thrown in this case.

        @param nitfInputStream the input stream to parse over.
        @return NitfFile structure corresponding to the input stream.
        @throws ParseException on detecting an invalid file or other parse error.
    */
    public static NitfFile parseHeadersOnly(final InputStream nitfInputStream) throws ParseException {
        NitfFile file = new NitfFile();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(nitfInputStream));
        new NitfFileParser(reader, EnumSet.noneOf(ParseOption.class), file);
        return file;
    }

    /**
        Parse NITF file headers out of a File.
        <p>
        This only extracts the headers from the main file, and the attached segment headers. The actual
        data segments are skipped over.
        <p>
        This takes a File, so it can handle the "streaming mode" layout.

        @param nitfFile the file to parse.
        @return NitfFile structure corresponding to the input stream.
        @throws FileNotFoundException if the file is not present or not able to be opened.
        @throws ParseException on detecting an invalid file or other parse error.
    */
    public static NitfFile parseHeadersOnly(final File nitfFile) throws ParseException, FileNotFoundException {
        NitfFile file = new NitfFile();
        NitfReader reader = new NitfFileReader(nitfFile);
        new NitfFileParser(reader, EnumSet.noneOf(ParseOption.class), file);
        return file;
    }

    /**
        Parse NITF file headers and selected data out of an InputStream.
        <p>
        This extracts the headers and any selected data segments. Any segments that are not selected will be skipped over.
        <p>
        Because this takes an InputStream (which is not seekable), it cannot work on NITF files that use
        "streaming mode", where the sizes are only provided at the end of the file. An exception will be
        thrown in this case.

        @param nitfInputStream the input stream to parse over.
        @param parseOptions the data segments to extract.
        @return NitfFile structure corresponding to the input stream.
        @throws ParseException on detecting an invalid file or other parse error.
    */
    public static NitfFile parseSelectedDataSegments(final InputStream nitfInputStream, final Set<ParseOption> parseOptions) throws ParseException {
        NitfFile file = new NitfFile();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(nitfInputStream));
        new NitfFileParser(reader, parseOptions, file);
        return file;
    }

    /**
        Parse NITF file headers and selected data from a File.
        <p>
        This extracts the headers and any selected data segments. Any segments that are not selected will be skipped over.
        <p>
        This takes a File, so it can handle the "streaming mode" layout

        @param nitfFile the file to parse.
        @param parseOptions the data segments to extract.
        @return NitfFile structure corresponding to the input stream.
        @throws FileNotFoundException if the file is not present or not able to be opened.
        @throws ParseException on detecting an invalid file or other parse error.
    */
    public static NitfFile parseSelectedDataSegments(final File nitfFile, final Set<ParseOption> parseOptions)
        throws ParseException, FileNotFoundException {
        NitfFile file = new NitfFile();
        NitfReader reader = new NitfFileReader(nitfFile);
        new NitfFileParser(reader, parseOptions, file);
        return file;
    }
}
