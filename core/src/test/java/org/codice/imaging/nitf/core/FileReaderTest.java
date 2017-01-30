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

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.codice.imaging.nitf.core.common.FileReader;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

public class FileReaderTest {

    TestLogger LOGGER = TestLoggerFactory.getTestLogger(FileReader.class);

    private final String testfile = "/WithBE.ntf";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testBadFilenameConstructorArgument() throws NitfFormatException, URISyntaxException {
        assertNotNull("Test file missing", getClass().getResource(testfile));

        FileReader goodReader = new FileReader(getClass().getResource(testfile).toURI().getPath());
        assertNotNull(goodReader);

        exception.expect(NitfFormatException.class);
        exception.expectMessage("no such file not found: no such file");
        FileReader badReader = new FileReader("no such file");
    }

    @Test
    public void testBadFileConstructorArgument() throws NitfFormatException, URISyntaxException {
        assertNotNull("Test file missing", getClass().getResource(testfile));

        FileReader goodReader = new FileReader(new File(getClass().getResource(testfile).toURI()));
        assertNotNull(goodReader);

        exception.expect(NitfFormatException.class);
        exception.expectMessage("no such file not found: no such file");
        FileReader badReader = new FileReader(new File("no such file"));
    }

    @Test
    public void testGetCurrentOffset() throws NitfFormatException, URISyntaxException {
        assertNotNull("Test file missing", getClass().getResource(testfile));

        FileReader reader = new FileReader(new File(getClass().getResource(testfile).toURI()));
        assertNotNull(reader);
        assertEquals(0L, reader.getCurrentOffset());
        reader.readBytesRaw(10);
        assertEquals(10L, reader.getCurrentOffset());
    }

    @Test
    public void testSeekBackwards() throws NitfFormatException, URISyntaxException {
        assertNotNull("Test file missing", getClass().getResource(testfile));

        FileReader reader = new FileReader(new File(getClass().getResource(testfile).toURI()));
        assertNotNull(reader);
        assertEquals(0L, reader.getCurrentOffset());
        reader.readBytesRaw(10);
        assertEquals(10L, reader.getCurrentOffset());
        reader.seekBackwards(4);
        assertEquals(6L, reader.getCurrentOffset());
    }

    @Test
    public void testSeekBackwardsException() throws NitfFormatException, URISyntaxException {
        assertNotNull("Test file missing", getClass().getResource(testfile));

        FileReader reader = new FileReader(new File(getClass().getResource(testfile).toURI()));
        assertNotNull(reader);
        assertEquals(0L, reader.getCurrentOffset());
        assertEquals(0, LOGGER.getLoggingEvents().size());
        try {
            exception.expect(NitfFormatException.class);
            exception.expectMessage("Unable to seek backwards: Negative seek offset");
            reader.seekBackwards(4);
        } finally {
            assertEquals(1, LOGGER.getLoggingEvents().size());
            assertEquals("IO Exception seeking backwards", LOGGER.getLoggingEvents().get(0).getMessage());
        }
    }

    @Test
    public void testSkipException() throws NitfFormatException, URISyntaxException {
        assertNotNull("Test file missing", getClass().getResource(testfile));

        FileReader reader = new FileReader(new File(getClass().getResource(testfile).toURI()));
        assertNotNull(reader);
        assertEquals(0L, reader.getCurrentOffset());
        assertEquals(0, LOGGER.getLoggingEvents().size());
        try {
            reader.skip(1);
            assertEquals(1L, reader.getCurrentOffset());
            assertEquals(0, LOGGER.getLoggingEvents().size());
            reader.close();
            exception.expect(NitfFormatException.class);
            exception.expectMessage("Error reading from NITF file: Stream Closed");
            reader.skip(1);
        } finally {
            assertEquals(1, LOGGER.getLoggingEvents().size());
            assertEquals("IO Exception skipping bytes", LOGGER.getLoggingEvents().get(0).getMessage());
        }
    }

    @Test
    public void testSeekToEndOfFileException() throws NitfFormatException, URISyntaxException {
        assertNotNull("Test file missing", getClass().getResource(testfile));

        FileReader reader = new FileReader(new File(getClass().getResource(testfile).toURI()));
        assertNotNull(reader);
        assertEquals(0, LOGGER.getLoggingEvents().size());
        try {
            reader.close();
            exception.expect(NitfFormatException.class);
            exception.expectMessage("Unable to seek to end of file: Stream Closed");
            reader.seekToEndOfFile();
        } finally {
            assertEquals(1, LOGGER.getLoggingEvents().size());
            assertEquals("IO Exception seeking to end of file", LOGGER.getLoggingEvents().get(0).getMessage());
        }
    }

    @Test
    public void testReadBytesRawException() throws NitfFormatException, URISyntaxException {
        assertNotNull("Test file missing", getClass().getResource(testfile));

        FileReader reader = new FileReader(new File(getClass().getResource(testfile).toURI()));
        assertNotNull(reader);
        assertEquals(0, LOGGER.getLoggingEvents().size());
        try {
            reader.close();
            exception.expect(NitfFormatException.class);
            exception.expectMessage("Error reading from NITF file: Stream Closed");
            reader.readBytesRaw(3);
        } finally {
            assertEquals(1, LOGGER.getLoggingEvents().size());
            assertEquals("IO Exception reading raw bytes", LOGGER.getLoggingEvents().get(0).getMessage());
        }
    }

    @Test
    public void testSeekToAbsoluteOffsetException() throws NitfFormatException, URISyntaxException {
        assertNotNull("Test file missing", getClass().getResource(testfile));

        FileReader reader = new FileReader(new File(getClass().getResource(testfile).toURI()));
        assertNotNull(reader);
        assertEquals(0, LOGGER.getLoggingEvents().size());
        try {
            reader.close();
            exception.expect(NitfFormatException.class);
            exception.expectMessage("Unable to seek to absolute offset: Stream Closed");
            reader.seekToAbsoluteOffset(2L);
        } finally {
            assertEquals(1, LOGGER.getLoggingEvents().size());
            assertEquals("IO Exception seeking to absolute offset", LOGGER.getLoggingEvents().get(0).getMessage());
        }
    }

    @Before
    public void clearLoggers()
    {
        TestLoggerFactory.clearAll();
    }

}
