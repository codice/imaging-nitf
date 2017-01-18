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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

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
}
