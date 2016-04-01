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

import java.io.DataOutput;
import java.util.Arrays;
import static org.hamcrest.Matchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Tests for shared writing code
 */
public class AbstractSegmentWriterTest {

    private static final TestLogger LOGGER = TestLoggerFactory.getTestLogger(AbstractSegmentWriter.class);

    public AbstractSegmentWriterTest() {
    }

    @After
    public void clearLoggers() {
        LOGGER.clear();
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testWriteENCRYP() throws Exception {
        DataOutput mockOutput = mock(DataOutput.class);
        AbstractSegmentWriter testWriter = new AbstractSegmentWriterImpl(mockOutput);
        testWriter.writeENCRYP();
        verify(mockOutput).writeBytes("0");
    }

    @Test
    public void testWriteFixedLengthString() throws Exception {
        DataOutput mockOutput = mock(DataOutput.class);
        AbstractSegmentWriter testWriter = new AbstractSegmentWriterImpl(mockOutput);
        testWriter.writeFixedLengthString("Test", 4);
        verify(mockOutput).writeBytes("Test");

        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));
        testWriter.writeFixedLengthString("Too Long", 6);
        verify(mockOutput).writeBytes("Too Lo");
        assertThat(LOGGER.getLoggingEvents(), is(Arrays.asList(
                LoggingEvent.warn("Truncated string \"Too Long\", max length is 6"))));
        LOGGER.clear();

        testWriter.writeFixedLengthString("Short", 7);
        verify(mockOutput).writeBytes("Short  ");
        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));

    }

    @Test
    public void testWriteFixedLengthNumber() throws Exception {
        DataOutput mockOutput = mock(DataOutput.class);
        AbstractSegmentWriter testWriter = new AbstractSegmentWriterImpl(mockOutput);
        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));

        testWriter.writeFixedLengthNumber(3, 2);
        verify(mockOutput).writeBytes("03");
        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));

        testWriter.writeFixedLengthNumber(23, 2);
        verify(mockOutput).writeBytes("23");
        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));

        try {
            exception.expect(IllegalArgumentException.class);
            exception.expectMessage("Fixed length number 235 cannot fit into length 2");
            testWriter.writeFixedLengthNumber(235, 2);
        } finally {
            assertThat(LOGGER.getLoggingEvents(), is(Arrays.asList(
                    LoggingEvent.error("Fixed length number 235 cannot fit into length 2"))));
        }
    }

    @Test
    public void testIncorrectStringLengthWrite() throws Exception {
        DataOutput mockOutput = mock(DataOutput.class);
        AbstractSegmentWriter testWriter = new AbstractSegmentWriterImpl(mockOutput);
        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));
        try {
            exception.expect(IllegalArgumentException.class);
            exception.expectMessage("String Too Long was not of expected length 6");
            testWriter.writeBytes("Too Long", 6);
        } finally {
            assertThat(LOGGER.getLoggingEvents(), is(Arrays.asList(
                    LoggingEvent.error("String Too Long was not of expected length 6"))));
        }
        verify(mockOutput, never()).writeBytes(any());
        verify(mockOutput, never()).write(any());
    }

    @Test
    public void testIncorrectByteArrayLengthWrite() throws Exception {
        DataOutput mockOutput = mock(DataOutput.class);
        AbstractSegmentWriter testWriter = new AbstractSegmentWriterImpl(mockOutput);
        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));
        try {
            exception.expect(IllegalArgumentException.class);
            exception.expectMessage("Array was length 5, and not expected length 6");
            testWriter.writeBytes(new byte[5], 6);
        } finally {
            assertThat(LOGGER.getLoggingEvents(), is(Arrays.asList(
                    LoggingEvent.error("Array was length 5, and not expected length 6"))));
        }
        verify(mockOutput, never()).writeBytes(any());
        verify(mockOutput, never()).write(any());
    }

    @Test
    public void testWriteDateTimeValidLength() throws Exception {
        DataOutput mockOutput = mock(DataOutput.class);
        AbstractSegmentWriter testWriter = new AbstractSegmentWriterImpl(mockOutput);
        DateTime testDateTime = new DateTime();
        testDateTime.setSourceString("20160302021155");
        testWriter.writeDateTime(testDateTime);
        verify(mockOutput).writeBytes("20160302021155");
        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));
    }

    @Test
    public void testWriteDateTimeShortLength() throws Exception {
        DataOutput mockOutput = mock(DataOutput.class);
        AbstractSegmentWriter testWriter = new AbstractSegmentWriterImpl(mockOutput);
        DateTime testDateTime = new DateTime();
        testDateTime.setSourceString("2016030202");
        testWriter.writeDateTime(testDateTime);
        verify(mockOutput).writeBytes("2016030202----");
        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));
    }

    @Test
    public void testWriteDateTimeLongLength() throws Exception {
        DataOutput mockOutput = mock(DataOutput.class);
        AbstractSegmentWriter testWriter = new AbstractSegmentWriterImpl(mockOutput);
        DateTime testDateTime = new DateTime();
        testDateTime.setSourceString("201603020211567");
        testWriter.writeDateTime(testDateTime);
        verify(mockOutput).writeBytes("--------------");
        assertThat(LOGGER.getLoggingEvents(), is(Arrays.asList(
                LoggingEvent.warn("Invalid date format \"201603020211567\""))));
    }

    public class AbstractSegmentWriterImpl extends AbstractSegmentWriter {

        public AbstractSegmentWriterImpl(DataOutput output) {
            super(output, null);
        }
    }

}
