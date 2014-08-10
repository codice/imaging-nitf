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
package org.codice.nitf.filereader;

import java.text.ParseException;

abstract class AbstractNitfSegmentParser {
    protected NitfReader reader = null;

    private static final int ENCRYP_LENGTH = 1;
    private static final int RGB_COLOUR_LENGTH = 3;

    protected final void readENCRYP() throws ParseException {
        if (!"0".equals(reader.readBytes(ENCRYP_LENGTH))) {
            throw new ParseException("Unexpected ENCRYP value", reader.getCurrentOffset());
        }
    }

    public final RGBColour readRGBColour() throws ParseException {
        byte[] rgb = reader.readBytesRaw(RGB_COLOUR_LENGTH);
        return new RGBColour(rgb);
    }
}
