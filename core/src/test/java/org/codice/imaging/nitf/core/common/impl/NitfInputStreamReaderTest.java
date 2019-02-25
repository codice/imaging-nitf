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
package org.codice.imaging.nitf.core.common.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Tests for NitfInputStreamReader class
 */
public class NitfInputStreamReaderTest {

    @Test
    public void testSuccessPartialRead() throws IOException, NitfFormatException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read(new byte[anyInt()], anyInt(), anyInt())).thenReturn(30)
                .thenReturn(70);

        NitfReader reader = new NitfInputStreamReader(mockInputStream);
        byte[] result = reader.readBytesRaw(100);

        assertThat(result, is(new byte[100]));
    }

    @Test(expected = NitfFormatException.class)
    public void testEndOfFileException() throws IOException, NitfFormatException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read(new byte[anyInt()], anyInt(), anyInt())).thenReturn(30)
                .thenReturn(-1);

        NitfReader reader = new NitfInputStreamReader(mockInputStream);
        reader.readBytes(100);
    }

    @Test(timeout = 500)
    public void testSkip() throws IOException, NitfFormatException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.skip(anyInt())).thenReturn(5L).thenReturn(7L);
        NitfReader reader = new NitfInputStreamReader(mockInputStream);
        reader.skip(12);
    }

    @Test(timeout = 2000, expected = NitfFormatException.class)
    public void testSkipEndOfFileException() throws IOException, NitfFormatException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.skip(anyInt())).thenReturn(0L);
        NitfInputStreamReader reader = new NitfInputStreamReader(mockInputStream);
        reader.setSkipTimeout(1000);
        reader.skip(1);
    }

    @Test(timeout = 4000)
    public void testDelayedInputStream() throws IOException, NitfFormatException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.skip(anyInt())).then(
            new Answer<Long>() {
                private long timeUntilData = System.currentTimeMillis() + 2000;
                @Override
                public Long answer(InvocationOnMock invocationOnMock) {
                    if (System.currentTimeMillis() <= timeUntilData) {
                        return 0L;
                    } else {
                        return (Long) invocationOnMock.getArguments()[0];
                    }
                }
            }
        );
        NitfInputStreamReader reader = new NitfInputStreamReader(mockInputStream);
        reader.setSkipTimeout(3000);
        reader.skip(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipZeroIllegalArgumentException() throws NitfFormatException {
        InputStream mockInputStream = mock(InputStream.class);
        NitfReader reader = new NitfInputStreamReader(mockInputStream);
        reader.skip(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeIllegalArgumentException() throws NitfFormatException {
        InputStream mockInputStream = mock(InputStream.class);
        NitfReader reader = new NitfInputStreamReader(mockInputStream);
        reader.skip(-5);
    }
}
