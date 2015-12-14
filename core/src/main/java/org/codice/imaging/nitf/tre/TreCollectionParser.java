/**
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
 **/
package org.codice.imaging.nitf.tre;

import java.text.ParseException;

import javax.xml.transform.Source;

import org.codice.imaging.nitf.common.reader.NitfReader;

/**
 * Parser for Tre Collections.
 */
public class TreCollectionParser {
    // Tagged Record Extension (TRE) segment
    /**
     * Length of the "Unique Extension Type Identifier" field in the Registered and controlled TRE format.
     * <p>
     * See MIL-STD-2500C Table A-7.
     */
    private static final int TAG_LENGTH = 6;

    /**
     * Length of the "Length of REDATA Field" field in the Registered and controlled TRE format.
     * <p>
     * See MIL-STD-2500C Table A-7.
     */
    private static final int TAGLEN_LENGTH = 5;

    private final TreParser treParser;

    /**
     *
     * @throws ParseException default constructor.
     */
    public TreCollectionParser() throws ParseException {
        treParser = new TreParser();
    }

    /**
        Parse the TREs from the current reader.

        @param reader the reader to use.
        @param treLength the length of the TRE.
        @return TRE collection.
        @throws ParseException if the TRE parsing fails (e.g. end of file or TRE that is clearly incorrect).
    */
    public final TreCollectionImpl parse(final NitfReader reader, final int treLength) throws ParseException {
        TreCollectionImpl treCollection = new TreCollectionImpl();
        int bytesRead = 0;
        while (bytesRead < treLength) {
            String tag = reader.readBytes(TAG_LENGTH);
            bytesRead += TAG_LENGTH;
            int fieldLength = reader.readBytesAsInteger(TAGLEN_LENGTH);
            bytesRead += TAGLEN_LENGTH;
            treCollection.add(treParser.parseOneTre(reader, tag, fieldLength));
            bytesRead += fieldLength;
        }
        return treCollection;
    }

    /**
     *
     * @param source - the source for additional Tre descriptors.
     * @throws ParseException - when an unexpected value is enc
     */
    public final void registerAdditionalTREdescriptor(final Source source) throws ParseException {
        treParser.registerAdditionalTREdescriptor(source);
    }
}
