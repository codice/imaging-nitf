/*
 * Copyright (C) 2016 Codice Foundation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.codice.imaging.nitf.render;

import java.io.IOException;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

/**
 * Unit tests for NitfRenderer class.
 */
public class NitfRendererTest {

    public NitfRendererTest() {
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void checkUnhandledCompressionException() throws IOException {
        NitfRenderer renderer = new NitfRenderer();

        // Mock up a render source
        NitfImageSegmentHeader mockImageSegmentHeader = Mockito.mock(NitfImageSegmentHeader.class);
        Mockito.when(mockImageSegmentHeader.getImageCompression()).thenReturn(ImageCompression.UNKNOWN);

        // Check the exception
        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage("Unhandled image compression format: UNKNOWN");
        renderer.render(mockImageSegmentHeader, null, null);
    }

}
