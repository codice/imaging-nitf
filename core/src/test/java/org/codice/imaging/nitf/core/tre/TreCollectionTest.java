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
package org.codice.imaging.nitf.core.tre;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.junit.Test;

import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import uk.org.lidalia.slf4jext.Level;

/**
 * Tests for TreCollection class.
 */
public class TreCollectionTest {

    private static final TestLogger LOGGER = TestLoggerFactory.getTestLogger(TreParser.class);

    public TreCollectionTest() {
    }

    @Test
    public void checkToString() {
        TreCollection collection = new TreCollection();
        assertEquals("TRE Collection", collection.toString());
    }

    @Test
    public void addRemove() {
        TreCollection collection = new TreCollection();
        Tre tre1 = TreFactory.getDefault("One", TreSource.TreOverflowDES);
        collection.add(tre1);
        assertEquals(1, collection.getTREs().size());
        assertEquals(1, collection.getTREsWithName("One").size());
        assertEquals(tre1, collection.getTREs().get(0));
        Tre tre2 = TreFactory.getDefault("Two", TreSource.TreOverflowDES);
        collection.add(tre2);
        assertEquals(2, collection.getTREs().size());
        assertEquals(1, collection.getTREsWithName("One").size());
        assertEquals(1, collection.getTREsWithName("Two").size());
        assertTrue(collection.remove(tre1));
        assertEquals(1, collection.getTREs().size());
        assertEquals(0, collection.getTREsWithName("One").size());
        assertEquals(1, collection.getTREsWithName("Two").size());
        assertEquals(tre2, collection.getTREs().get(0));
    }

    @Test
    public void testGracefulRecovery() throws NitfFormatException {
        LOGGER.clear();
        String treString = "ENGRDA00058LAIR                00113majorVersion00010001A1NA00000002AENGRDA00058LAIR                00112majorVersion00010001A1NA000000011";
        InputStream inputStream = new ByteArrayInputStream(treString.getBytes(StandardCharsets.ISO_8859_1));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedInputStream);
        TreCollectionParser treCollectionParser = new TreCollectionParser();
        TreCollection treCollection = treCollectionParser.parse(nitfReader, 138, TreSource.ImageExtendedSubheaderData);
        assertEquals(2, treCollection.getTREs().size());

        Tre tre0 = treCollection.getTREs().get(0);
        assertNotNull(tre0.getName());
        assertEquals(0, tre0.getEntries().size());
        assertNotNull(tre0.getRawData());

        Tre tre1 = treCollection.getTREs().get(1);
        assertNotNull(tre1.getName());
        assertEquals(3, tre1.getEntries().size());
        assertNull(tre1.getRawData());
        com.google.common.collect.ImmutableList<LoggingEvent> loggingEvents = LOGGER.getLoggingEvents();
        assertEquals(2, loggingEvents.size());
        assertEquals(Level.WARN, loggingEvents.get(0).getLevel());
        assertEquals("Failed to parse TRE {}. See debug log for exception information.", loggingEvents.get(0).getMessage());
        assertEquals(1, loggingEvents.get(0).getArguments().size());
        assertEquals("ENGRDA", loggingEvents.get(0).getArguments().get(0));
        assertEquals(Level.DEBUG, loggingEvents.get(1).getLevel());
        assertEquals(NumberFormatException.class, loggingEvents.get(1).getThrowable().get().getClass());
    }
}
