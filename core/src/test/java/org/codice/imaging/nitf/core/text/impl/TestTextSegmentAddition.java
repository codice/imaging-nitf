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
package org.codice.imaging.nitf.core.text.impl;

import java.io.File;
import org.codice.imaging.nitf.core.DataSource;
import org.codice.imaging.nitf.core.impl.NitfFileWriter;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.codice.imaging.nitf.core.impl.SlottedStorage;
import org.codice.imaging.nitf.core.common.impl.FileReader;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.header.impl.NitfHeaderFactory;
import org.codice.imaging.nitf.core.header.impl.NitfParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.codice.imaging.nitf.core.text.TextFormat;
import org.codice.imaging.nitf.core.text.TextSegment;
import org.junit.Test;

/**
 * Test for adding / modifying test segments.
 */
public class TestTextSegmentAddition {

    public TestTextSegmentAddition() {
    }

    @Test
    public void writeSimpleHeader() throws NitfFormatException {
        final String OUTFILE_NAME = "textsegment_simple.ntf";
        if (new File(OUTFILE_NAME).exists()) {
            new File(OUTFILE_NAME).delete();
        }

        SlottedStorage store = createBasicStore();
        TextSegment textSegment = TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
        textSegment.setTextTitle("Some Title");
        textSegment.setData("This is the body.\r\nIt has two lines.");
        textSegment.setTextFormat(TextFormat.getFormatUsedInString(textSegment.getData()));
        store.getTextSegments().add(textSegment);
        NitfFileWriter writer = new NitfFileWriter(store, OUTFILE_NAME);
        writer.write();
        DataSource dataSource = verifyBasicHeader(OUTFILE_NAME);
        assertEquals(0, dataSource.getImageSegments().size());
        assertEquals(0, dataSource.getGraphicSegments().size());
        assertEquals(1, dataSource.getTextSegments().size());
        assertEquals(0, dataSource.getDataExtensionSegments().size());

        TextSegment testSegment = dataSource.getTextSegments().get(0);
        assertNotNull(testSegment);
        assertEquals("Some Title", testSegment.getTextTitle());
        assertTrue(TextFormat.BASICCHARACTERSET.equals(testSegment.getTextFormat()));
        assertEquals("This is the body.\r\nIt has two lines.", testSegment.getData());
        assertEquals(textSegment.getTextDateTime().getSourceString(), testSegment.getTextDateTime().getSourceString());

        new File(OUTFILE_NAME).delete();
    }

    @Test
    public void writeSegmentWithId() throws NitfFormatException {
        final String OUTFILE_NAME = "textsegment_withId.ntf";
        if (new File(OUTFILE_NAME).exists()) {
            new File(OUTFILE_NAME).delete();
        }

        SlottedStorage store = createBasicStore();
        TextSegment textSegment = TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
        textSegment.setTextTitle("Some Title");
        textSegment.setData("This is the body.\r\nIt has two lines.");
        textSegment.setTextFormat(TextFormat.BASICCHARACTERSET);
        textSegment.setIdentifier("XyZZy03");
        store.getTextSegments().add(textSegment);
        NitfFileWriter writer = new NitfFileWriter(store, OUTFILE_NAME);
        writer.write();
        DataSource dataSource = verifyBasicHeader(OUTFILE_NAME);
        assertEquals(0, dataSource.getImageSegments().size());
        assertEquals(0, dataSource.getGraphicSegments().size());
        assertEquals(1, dataSource.getTextSegments().size());
        assertEquals(0, dataSource.getDataExtensionSegments().size());

        TextSegment testSegment = dataSource.getTextSegments().get(0);
        assertNotNull(testSegment);
        assertEquals("XyZZy03", testSegment.getIdentifier());
        assertEquals("Some Title", testSegment.getTextTitle());
        assertTrue(TextFormat.BASICCHARACTERSET.equals(testSegment.getTextFormat()));
        assertEquals("This is the body.\r\nIt has two lines.", testSegment.getData());
        assertEquals(textSegment.getTextDateTime().getSourceString(), testSegment.getTextDateTime().getSourceString());

        new File(OUTFILE_NAME).delete();
    }

    @Test
    public void writeTwoTextSegments() throws NitfFormatException {
        final String OUTFILE_NAME = "twotextsegments.ntf";
        TextSegment textSegment2 = makeTwoSegmentFile(OUTFILE_NAME);
        DataSource dataSource = verifyBasicHeader(OUTFILE_NAME);
        assertEquals(0, dataSource.getImageSegments().size());
        assertEquals(0, dataSource.getGraphicSegments().size());
        assertEquals(2, dataSource.getTextSegments().size());
        assertEquals(0, dataSource.getDataExtensionSegments().size());

        TextSegment testSegment1 = dataSource.getTextSegments().get(0);
        assertNotNull(testSegment1);
        assertEquals("TXT001", testSegment1.getIdentifier().trim());
        assertEquals("Some Title", testSegment1.getTextTitle());
        assertTrue(TextFormat.BASICCHARACTERSET.equals(testSegment1.getTextFormat()));
        assertEquals("This is the body.\r\nIt has two lines.", testSegment1.getData());

        TextSegment testSegment2 = dataSource.getTextSegments().get(1);
        assertNotNull(testSegment2);
        assertEquals("TXT002", testSegment2.getIdentifier().trim());
        assertEquals("A strange message", testSegment2.getTextTitle());
        assertEquals(TextFormat.USMTF, testSegment2.getTextFormat());
        assertEquals(textSegment2.getData(), testSegment2.getData());

        new File(OUTFILE_NAME).delete();
    }

    @Test
    public void modifyTwoTextSegments() throws NitfFormatException {
        final String OUTFILE_NAME = "twotextsegments_mod.ntf";
        TextSegment textSegment2 = makeTwoSegmentFile(OUTFILE_NAME);
        DataSource dataSource = verifyBasicHeader(OUTFILE_NAME);
        new File(OUTFILE_NAME).delete();

        assertEquals(0, dataSource.getImageSegments().size());
        assertEquals(0, dataSource.getGraphicSegments().size());
        assertEquals(2, dataSource.getTextSegments().size());
        assertEquals(0, dataSource.getDataExtensionSegments().size());

        TextSegment testSegment1 = dataSource.getTextSegments().get(0);
        assertNotNull(testSegment1);
        assertEquals("TXT001", testSegment1.getIdentifier().trim());
        assertEquals("Some Title", testSegment1.getTextTitle());
        assertTrue(TextFormat.BASICCHARACTERSET.equals(testSegment1.getTextFormat()));
        assertEquals("This is the body.\r\nIt has two lines.", testSegment1.getData());

        TextSegment testSegment2 = dataSource.getTextSegments().get(1);
        assertNotNull(testSegment2);
        assertEquals("TXT002", testSegment2.getIdentifier().trim());
        assertEquals("A strange message", testSegment2.getTextTitle());
        assertEquals(TextFormat.USMTF, testSegment2.getTextFormat());
        assertEquals(textSegment2.getData(), testSegment2.getData());

        testSegment1.setData("A bit longer body.\nStill the body.\nIt has three lines.");
        testSegment1.setIdentifier("Text001");
        testSegment1.setTextTitle("A somewhat longer title");
        assertEquals("Text001", dataSource.getTextSegments().get(0).getIdentifier());

        testSegment2.setTextTitle("Motto");
        testSegment2.setData("//GENTEXT/REMARKS/(U) TO LEAD. TO EXCEL.//");

        final String OUTFILE_UPDATED_NAME = "twotextsegments_mod_updated.ntf";

        NitfFileWriter writer = new NitfFileWriter(dataSource, OUTFILE_UPDATED_NAME);
        writer.write();
        DataSource dataSourceUpdated = verifyBasicHeader(OUTFILE_UPDATED_NAME);
        assertEquals(0, dataSourceUpdated.getImageSegments().size());
        assertEquals(0, dataSourceUpdated.getGraphicSegments().size());
        assertEquals(2, dataSourceUpdated.getTextSegments().size());
        assertEquals(0, dataSourceUpdated.getDataExtensionSegments().size());
        TextSegment testUpdated1 = dataSourceUpdated.getTextSegments().get(0);
        assertNotNull(testUpdated1);
        assertEquals("Text001", testUpdated1.getIdentifier().trim());
        assertEquals("A somewhat longer title", testUpdated1.getTextTitle());
        assertTrue(TextFormat.BASICCHARACTERSET.equals(testUpdated1.getTextFormat()));
        assertEquals(testSegment1.getData(), testUpdated1.getData());

        TextSegment testUpdated2 = dataSourceUpdated.getTextSegments().get(1);
        assertNotNull(testUpdated2);
        assertEquals("TXT002", testUpdated2.getIdentifier().trim());
        assertEquals("Motto", testUpdated2.getTextTitle());
        assertEquals(TextFormat.USMTF, testUpdated2.getTextFormat());
        assertEquals("//GENTEXT/REMARKS/(U) TO LEAD. TO EXCEL.//", testUpdated2.getData());

        new File(OUTFILE_UPDATED_NAME).delete();
    }

    @Test
    public void deleteTextSegment() throws NitfFormatException {
        final String OUTFILE_NAME = "twotextsegments_delete.ntf";
        TextSegment textSegment2 = makeTwoSegmentFile(OUTFILE_NAME);
        DataSource dataSource = verifyBasicHeader(OUTFILE_NAME);
        new File(OUTFILE_NAME).delete();
        assertEquals(0, dataSource.getImageSegments().size());
        assertEquals(0, dataSource.getGraphicSegments().size());
        assertEquals(2, dataSource.getTextSegments().size());
        assertEquals(0, dataSource.getDataExtensionSegments().size());

        TextSegment testSegment1 = dataSource.getTextSegments().get(0);
        assertNotNull(testSegment1);
        assertEquals("TXT001", testSegment1.getIdentifier().trim());
        assertEquals("Some Title", testSegment1.getTextTitle());
        assertTrue(TextFormat.BASICCHARACTERSET.equals(testSegment1.getTextFormat()));
        assertEquals("This is the body.\r\nIt has two lines.", testSegment1.getData());

        TextSegment testSegment2 = dataSource.getTextSegments().get(1);
        assertNotNull(testSegment2);
        assertEquals("TXT002", testSegment2.getIdentifier().trim());
        assertEquals("A strange message", testSegment2.getTextTitle());
        assertEquals(TextFormat.USMTF, testSegment2.getTextFormat());
        assertEquals(textSegment2.getData(), testSegment2.getData());

        dataSource.getTextSegments().remove(0);
        final String OUTFILE_UPDATED_NAME = "twotextsegments_delete_updated.ntf";

        NitfFileWriter writer = new NitfFileWriter(dataSource, OUTFILE_UPDATED_NAME);
        writer.write();
        DataSource dataSourceUpdated = verifyBasicHeader(OUTFILE_UPDATED_NAME);
        assertEquals(0, dataSourceUpdated.getImageSegments().size());
        assertEquals(0, dataSourceUpdated.getGraphicSegments().size());
        assertEquals(1, dataSourceUpdated.getTextSegments().size());
        assertEquals(0, dataSourceUpdated.getDataExtensionSegments().size());
        TextSegment updated = dataSource.getTextSegments().get(0);
        assertNotNull(updated);
        assertEquals("TXT002", updated.getIdentifier().trim());
        assertEquals("A strange message", updated.getTextTitle());
        assertEquals(TextFormat.USMTF, updated.getTextFormat());
        assertEquals(textSegment2.getData(), updated.getData());
        new File(OUTFILE_UPDATED_NAME).delete();
    }

    private TextSegment makeTwoSegmentFile(final String OUTFILE_NAME) {
        if (new File(OUTFILE_NAME).exists()) {
            new File(OUTFILE_NAME).delete();
        }
        SlottedStorage store = createBasicStore();
        TextSegment textSegment1 = TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
        textSegment1.setTextTitle("Some Title");
        textSegment1.setData("This is the body.\r\nIt has two lines.");
        textSegment1.setTextFormat(TextFormat.BASICCHARACTERSET);
        textSegment1.setIdentifier("TXT001");
        store.getTextSegments().add(textSegment1);
        TextSegment textSegment2 = TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
        textSegment2.setIdentifier("TXT002");
        textSegment2.setTextTitle("A strange message");
        textSegment2.setTextFormat(TextFormat.USMTF);
        textSegment2.setData("//GENTEXT/REMARKS/(U) THIS IS AN EXAMPLE USMTF  \nMESSAGE TEXT.//");
        store.getTextSegments().add(textSegment2);
        NitfFileWriter writer = new NitfFileWriter(store, OUTFILE_NAME);
        writer.write();
        return textSegment2;
    }

    private SlottedStorage createBasicStore() {
        SlottedStorage store = new SlottedStorage();
        NitfHeader nitf = NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(nitf);
        assertEquals(FileType.NITF_TWO_ONE, nitf.getFileType());
        store.setNitfHeader(nitf);
        return store;
    }

    private DataSource verifyBasicHeader(final String OUTFILE_NAME) throws NitfFormatException {
        FileReader reader = new FileReader(OUTFILE_NAME);
        assertNotNull(reader);
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.ALL_SEGMENT_DATA);
        NitfParser.parse(reader, parseStrategy);
        assertEquals(FileType.NITF_TWO_ONE, parseStrategy.getNitfHeader().getFileType());
        return parseStrategy.getDataSource();
    }
}
