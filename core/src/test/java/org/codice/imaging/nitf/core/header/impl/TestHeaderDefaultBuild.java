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
package org.codice.imaging.nitf.core.header.impl;

import java.io.File;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.impl.NitfFileWriter;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.codice.imaging.nitf.core.impl.SlottedStorage;
import org.codice.imaging.nitf.core.common.impl.FileReader;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test build of Header segment data
 */
public class TestHeaderDefaultBuild {

    public TestHeaderDefaultBuild() {
    }

    @Test
    public void checkNitf21() {
        NitfHeader nitf = NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(nitf);
        assertEquals(FileType.NITF_TWO_ONE, nitf.getFileType());
        checkEmptyHeader(nitf);
    }

    @Test
    public void writeEmptyHeader() throws NitfFormatException {
        final String OUTFILE_NAME = "empty.ntf";
        if (new File(OUTFILE_NAME).exists()) {
            new File(OUTFILE_NAME).delete();
        }

        SlottedStorage store = new SlottedStorage();

        NitfHeader nitf = NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(nitf);
        assertEquals(FileType.NITF_TWO_ONE, nitf.getFileType());

        store.setNitfHeader(nitf);
        NitfFileWriter writer = new NitfFileWriter(store, OUTFILE_NAME);
        writer.write();

        FileReader reader = new FileReader(OUTFILE_NAME);
        assertNotNull(reader);
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.HEADERS_ONLY);
        NitfParser.parse(reader, parseStrategy);
        assertEquals(FileType.NITF_TWO_ONE, parseStrategy.getNitfHeader().getFileType());

        new File(OUTFILE_NAME).delete();
    }

    @Test
    public void checkNsif10() {
        NitfHeader nitf = NitfHeaderFactory.getDefault(FileType.NSIF_ONE_ZERO);
        assertNotNull(nitf);
        assertEquals(FileType.NSIF_ONE_ZERO, nitf.getFileType());
        checkEmptyHeader(nitf);
    }

    private void checkEmptyHeader(NitfHeader nitf) {
        assertEquals(3, nitf.getComplexityLevel());
        assertEquals("BF01", nitf.getStandardType());
        assertNotNull(nitf.getOriginatingStationId());
        assertFalse(nitf.getOriginatingStationId().isEmpty());

        assertNotNull(nitf.getFileDateTime());
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        assertFalse(nitf.getFileDateTime().getZonedDateTime().isAfter(now));
        assertTrue(nitf.getFileDateTime().getZonedDateTime().plusMinutes(1).isAfter(now));

        assertEquals("", nitf.getFileTitle());
        assertNotNull(nitf.getFileSecurityMetadata());

        assertNotNull(nitf.getFileBackgroundColour());
        assertEquals(0, nitf.getFileBackgroundColour().getRed());
        assertEquals(59, nitf.getFileBackgroundColour().getGreen());
        assertEquals(121, nitf.getFileBackgroundColour().getBlue());

        assertEquals("", nitf.getOriginatorsName());
        assertEquals("", nitf.getOriginatorsPhoneNumber());
    }

}
