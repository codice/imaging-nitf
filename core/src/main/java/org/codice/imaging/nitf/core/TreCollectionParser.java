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
package org.codice.imaging.nitf.core;

import java.text.ParseException;
import javax.xml.transform.Source;

class TreCollectionParser {

    private final TreParser treParser;

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
    public final TreCollection parse(final NitfReader reader, final int treLength) throws ParseException {
        TreCollection treCollection = new TreCollection();
        int bytesRead = 0;
        while (bytesRead < treLength) {
            String tag = reader.readBytes(NitfConstants.TAG_LENGTH);
            bytesRead += NitfConstants.TAG_LENGTH;
            int fieldLength = reader.readBytesAsInteger(NitfConstants.TAGLEN_LENGTH);
            bytesRead += NitfConstants.TAGLEN_LENGTH;
            treCollection.add(treParser.parseOneTre(reader, tag, fieldLength));
            bytesRead += fieldLength;
        }
        return treCollection;
    }

    void registerAdditionalTREdescriptor(final Source source) throws ParseException {
        treParser.registerAdditionalTREdescriptor(source);
    }
}
