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
package org.codice.imaging.nitf.core.common;

import java.text.ParseException;
import java.util.Arrays;
import static org.hamcrest.Matchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

public class AbstractParserTest {

    TestLogger logger = TestLoggerFactory.getTestLogger(AbstractNitfSegmentParser.class);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testParseOfENCRYP() throws ParseException {
        AbstractNitfSegmentParser parser = mock(AbstractNitfSegmentParser.class, CALLS_REAL_METHODS);

        NitfReader mockReader = mock(NitfReader.class);
        parser.reader = mockReader;
        when(mockReader.readBytes(1)).thenReturn("0");
        parser.readENCRYP();
        assertThat(logger.getLoggingEvents().isEmpty(), is(true));

        try {
            when(mockReader.readBytes(1)).thenReturn("1");
            exception.expect(ParseException.class);
            exception.expectMessage("Unexpected ENCRYP value");
            parser.readENCRYP();
        } finally {
            assertThat(logger.getLoggingEvents(), is(Arrays.asList(LoggingEvent.warn("Mismatch while reading ENCRYP"))));
        }
    }


    @After
    public void clearLoggers() {
        TestLoggerFactory.clear();
    }
}
