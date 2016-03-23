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
package org.codice.imaging.nitf.core.common;

import org.codice.imaging.nitf.core.header.NitfHeader;
import java.text.ParseException;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Strategy for parsing NITF file components.
 */
public interface NitfParseStrategy {

    /**
     * Set the file-level header for this parsed file.
     *
     * @param nitfHeader the file-level header
     */
    void setFileHeader(NitfHeader nitfHeader);

    /**
     * Return the file-level header for the parsed file.
     *
     * @return the file-level header
     */
    NitfHeader getNitfHeader();

    /**
     * Indication that the "base" file-level headers have been read.
     *
     * @param reader the reader, positioned for reading of the segments
     */
    void baseHeadersRead(NitfReader reader);

    /**
     * Parse and return the TREs.
     *
     * @param reader the reader to read the TRE data from
     * @param length the length of the TRE data (for all TREs)
     * @param source the source segment part for the TRE (where it is being read
     * from)
     * @return TRE collection for this header part
     * @throws java.text.ParseException if there is a problem loading the TRE descriptions, or in parsing TREs.
     */
    TreCollection parseTREs(NitfReader reader, int length, TreSource source) throws ParseException;

}
