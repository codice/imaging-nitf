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
import java.io.IOException;
import java.io.InputStream;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.header.NitfParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Tests for the special NITF 2.0 FBKGC handling described in the IPON Section 3.6.
 */
public class FBKGCTest {


    @Test
    public void testU1125C() throws IOException, NitfFormatException {
        final String nitf20File = "/JitcNitf20Samples/U_1125C.NTF";

        assertNotNull("Test file missing", getClass().getResource(nitf20File));

        InputStream is = getClass().getResourceAsStream(nitf20File);
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy();
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(is));
        NitfParser.parse(reader, parseStrategy);
        NitfHeader nitfHeader = parseStrategy.getNitfHeader();
        assertEquals(FileType.NITF_TWO_ZERO, nitfHeader.getFileType());
        assertEquals(0x00, nitfHeader.getFileBackgroundColour().getRed());
        assertEquals(0x00, nitfHeader.getFileBackgroundColour().getGreen());
        assertEquals((byte) 0xFF, nitfHeader.getFileBackgroundColour().getBlue());
        assertEquals("JITC Ft Huachuca, AZ", nitfHeader.getOriginatorsName());
        assertEquals("(520) 538-5458", nitfHeader.getOriginatorsPhoneNumber());
    }
}
