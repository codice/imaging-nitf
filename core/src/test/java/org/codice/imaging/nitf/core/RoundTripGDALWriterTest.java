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
 * Tests for some files from the GDAL test suite.
 */
public class RoundTripGDALWriterTest extends AbstractWriterTest {

    public RoundTripGDALWriterTest() {
    }

    @Test
    public void roundTripNITFU0006A() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/fromGDAL/U_0006A.NTF");
    }

    @Test
    public void roundTripNITFtwoimages() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/fromGDAL/two_images_jp2.ntf");
    }

    @Test
    public void roundTripNITFtwoimagesjpeg() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/fromGDAL/two_images_jpeg.ntf");
    }
}
