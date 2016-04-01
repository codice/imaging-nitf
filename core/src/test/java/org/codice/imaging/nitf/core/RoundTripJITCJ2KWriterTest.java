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
 * Tests for writing NITF files - NITF 2.1 JPEG 2000 cases.
 *
 * This includes a mix of JP2 files and J2C (codestream) content.
 */
public class RoundTripJITCJ2KWriterTest extends AbstractWriterTest {

    public RoundTripJITCJ2KWriterTest() {
    }

    @Test
    public void roundTripNitf001J2C() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/001_006_64x64_s_8_1_mono_j2c.ntf");
    }

    @Test
    public void roundTripNitf001JP2() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/001_006_64x64_s_8_1_mono_jp2.ntf");
    }

    @Test
    public void roundTripNitf046() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/046_512x512_s_8_3_rgb_j2c.ntf");
    }

    @Test
    public void roundTripNitf053() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/053_512x512_s_8_3_ycbcr_j2c.ntf");
    }

    @Test
    public void roundTripNitf056J2C() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/056_512x512_s_8_4_ms_j2c.ntf");
    }

    @Test
    public void roundTripNitf056JP2() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/056_512x512_s_8_4_ms_jp2.ntf");
    }

    @Test
    public void roundTripNitf066JP2() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/066_389x298_s_11_3_rgb_jp2.ntf");
    }

    @Test
    public void roundTripNitfFile1JP2() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file1_jp2.ntf");
    }

    @Test
    public void roundTripNitfFile2J2C() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file2_j2c.ntf");
    }

    @Test
    public void roundTripNitfFile2JP2_1place() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file2_jp2_1place.ntf");
    }

    @Test
    public void roundTripNitfFile2JP2_2places() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file2_jp2_2places.ntf");
    }

    @Test
    public void roundTripNitfFile3JP2() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file3_jp2.ntf");
    }

    @Test
    public void roundTripNitfFile4JP2() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file4_jp2.ntf");
    }

    @Test
    public void roundTripNitfFile5JP2() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file5_jp2.ntf");
    }

    @Test
    public void roundTripNitfFile6JP2() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file6_jp2.ntf");
    }

    @Test
    public void roundTripNitfFile7JP2() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file7_jp2.ntf");
    }

    @Test
    public void roundTripNitfFile8JP2() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file8_jp2.ntf");
    }

    @Test
    public void roundTripNitfFile9J2C() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file9_j2c.ntf");
    }

    @Test
    public void roundTripNitfFile9JP2_1place() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file9_jp2_1place.ntf");
    }

    @Test
    public void roundTripNitfFile9JP2_2places() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file9_jp2_2places.ntf");
    }

    @Test
    public void roundTripNitfFile9NC() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/file9_nc.ntf");
    }

    @Test
    public void roundTripNitfFileP01A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_01a.ntf");
    }
    @Test
    public void roundTripNitfFileP02A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_02a.ntf");
    }

    @Test
    public void roundTripNitfFileP03A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_03a.ntf");
    }

    @Test
    public void roundTripNitfFileP04B() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_04b.ntf");
    }

    @Test
    public void roundTripNitfFileP05A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_05a.ntf");
    }

    @Test
    public void roundTripNitfFileP06A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_06a.ntf");
    }

    @Test
    public void roundTripNitfFileP07A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_07a.ntf");
    }

    @Test
    public void roundTripNitfFileP08A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_08a.ntf");
    }

    @Test
    public void roundTripNitfFileP09A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_09a.ntf");
    }

    @Test
    public void roundTripNitfFileP10B() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_10b.ntf");
    }

    @Test
    public void roundTripNitfFileP11XA() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_11xa.ntf");
    }

    @Test
    public void roundTripNitfFileP12A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_12a.ntf");
    }

    @Test
    public void roundTripNitfFileP13XA() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_13xa.ntf");
    }

    @Test
    public void roundTripNitfFileP14B() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_14b.ntf");
    }

    @Test
    public void roundTripNitfFileP15A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_15a.ntf");
    }

    @Test
    public void roundTripNitfFileP16A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p0_16a.ntf");
    }

    @Test
    public void roundTripNitfFileP101A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p1_01a.ntf");
    }

    @Test
    public void roundTripNitfFileP102B() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p1_02b.ntf");
    }

    @Test
    public void roundTripNitfFileP103A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p1_03a.ntf");
    }

    @Test
    public void roundTripNitfFileP104A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p1_04a.ntf");
    }

    @Test
    public void roundTripNitfFileP105B() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p1_05b.ntf");
    }

    @Test
    public void roundTripNitfFileP106B() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p1_06b.ntf");
    }

    @Test
    public void roundTripNitfFileP107A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcJpeg2000/p1_07a.ntf");
    }
}
