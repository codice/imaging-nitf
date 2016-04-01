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
 * Test for samples found on OSGEO's download site.
 */
public class RoundTripOSGEOWriterTest extends AbstractWriterTest {

    public RoundTripOSGEOWriterTest() {
    }

    @Test
    public void roundTripNITFGNC() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/fromOSGEO/0000M033.GN3");
    }

    @Test
    public void roundTripNITFONC() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/fromOSGEO/cadrg/001zc013.on1");
    }

    @Test
    public void roundTripNITFCGM() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/fromOSGEO/bugs/NITF21_CGM_ANNO_Uncompressed_unmasked.ntf");
    }

    @Test
    public void roundTripNITFbug3337() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/fromOSGEO/bugs/bug3337.ntf");
    }

    @Test
    public void roundTripNITFI3430A() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/fromOSGEO/bugs/i_3430a.ntf");
    }
}
