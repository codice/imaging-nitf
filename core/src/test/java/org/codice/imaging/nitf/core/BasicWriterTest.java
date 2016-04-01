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
package org.codice.imaging.nitf.core;

import java.io.IOException;
import java.net.URISyntaxException;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.junit.Test;

/**
 * Tests for writing NITF file - basic cases.
 */
public class BasicWriterTest extends AbstractWriterTest {
    @Test
    public void roundTripSimpleFile() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/WithBE.ntf");
    }

    @Test
    public void roundTripDESFile() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/autzen-utm10.ntf");
    }

    @Test
    public void roundTripGraphicSegmentExt() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/gdal3453.ntf");
    }

    @Test
    public void roundTripMultiImageFile() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3361c.nsf");
    }

    @Test
    public void roundTripGraphicFile() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3051e.ntf");
    }

    @Test
    public void roundTripGraphic2File() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/ns3051v.nsf");
    }

    @Test
    public void roundTripTREs() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3128b.ntf");
    }
}
