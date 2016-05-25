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
package org.codice.imaging.nitf.core.image;

import java.io.BufferedInputStream;
import org.codice.imaging.nitf.core.SlottedParseStrategy;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.NitfParser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for image files that have NPPBH or NPPBV set to 0000.
 * 
 * Those values mean pixels horizontally / vertically per block is actually
 * given by NCOLS / NROWS respectively.
 */
public class ImageTileSizeSpecialCaseTest {
    
    public ImageTileSizeSpecialCaseTest() {
    }

    @Test
    public void CheckMerlion() throws NitfFormatException {
        final String testfile = "/Codice/merlionUnblocked.ntf";

        assertNotNull("Test file missing", getClass().getResource(testfile));
        
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.IMAGE_DATA);
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getClass().getResourceAsStream(testfile)));
        NitfParser.parse(reader, parseStrategy);
        assertEquals(1, parseStrategy.getDataSource().getImageSegments().size());

        ImageSegment imageSegment = parseStrategy.getDataSource().getImageSegments().get(0);
        assertEquals(1024, imageSegment.getNumberOfPixelsPerBlockHorizontal());
        assertEquals(540, imageSegment.getNumberOfPixelsPerBlockVertical());
        assertEquals(0, imageSegment.getNumberOfPixelsPerBlockHorizontalRaw());
        // assertEquals(0, imageSegment.getNumberOfPixelsPerBlockVerticalRaw());
    }
}
