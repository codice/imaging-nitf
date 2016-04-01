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
package org.codice.imaging.nitf.core.tre;

import javax.xml.transform.Source;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.codice.imaging.nitf.core.tre.TreConstants.TAGLEN_LENGTH;
import static org.codice.imaging.nitf.core.tre.TreConstants.TAG_LENGTH;

/**
 * Parser for a TreCollection.
 */
public class TreCollectionParser {

    private final TreParser treParser;

    /**
     * default constructor.
     * @throws NitfFormatException when the TreParser constructor does.
     */
    public TreCollectionParser() throws NitfFormatException {
        treParser = new TreParser();
    }

    /**
     * Parse the TREs from the current reader.
     *
     * @param reader the reader to use.
     * @param treLength the length of the TRE.
     * @param sourceSegment the source segment (or segment part) for the TRE.
     * @return TRE collection.
     * @throws NitfFormatException if the TRE parsing fails (e.g. end of file or TRE that is clearly incorrect).
     */
    public final TreCollection parse(final NitfReader reader, final int treLength, final TreSource sourceSegment) throws NitfFormatException {
        TreCollection treCollection = new TreCollection();
        int bytesRead = 0;
        while (bytesRead < treLength) {
            String tag = reader.readBytes(TAG_LENGTH);
            bytesRead += TAG_LENGTH;
            int fieldLength = reader.readBytesAsInteger(TAGLEN_LENGTH);
            bytesRead += TAGLEN_LENGTH;
            treCollection.add(treParser.parseOneTre(reader, tag, fieldLength, sourceSegment));
            bytesRead += fieldLength;
        }
        return treCollection;
    }

    /**
     * Registers TreImpl descriptors for the supplied source.
     * @param source - The source for the TreImpl descriptor.
     * @throws NitfFormatException propagated from TreParser.registerAdditionalTREdescriptor.
     */
    public final void registerAdditionalTREdescriptor(final Source source) throws NitfFormatException {
        treParser.registerAdditionalTREdescriptor(source);
    }
}
