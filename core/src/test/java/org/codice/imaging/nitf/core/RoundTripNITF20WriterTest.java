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
 * Tests for writing NITF files - NITF 2.0 cases
 */
public class RoundTripNITF20WriterTest extends AbstractWriterTest {

    public RoundTripNITF20WriterTest() {
    }
    @Test
    public void roundTripNitf20_U1001A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1001A.NTF");
    }

    @Test
    public void roundTripNitf20_U1034A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1034A.NTF");
    }

    @Test
    public void roundTripNitf20_U1036A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1036A.NTF");
    }

    @Test
    public void roundTripNitf20_U1050A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1050A.NTF");
    }

    @Test
    public void roundTripNitf20_U1060A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1060A.NTF");
    }

    @Test
    public void roundTripNitf20_U1101A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1101A.NTF");
    }

    @Test
    public void roundTripNitf20_U1114A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1114A.NTF");
    }

    @Test
    public void roundTripNitf20_U1122A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1122A.NTF");
    }

    // Ideally U_1123A would appear here, but it has a bad LFS value (0x31, instead of 0x20) which
    // we won't generate on writing. Hence, it won't round trip.
    @Test
    public void roundTripNitf20_U1130F() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1130F.NTF");
    }

    @Test
    public void roundTripNitf20_U2001A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_2001A.NTF");
    }

    @Test
    public void roundTripNitf20_U3002A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_3002A.NTF");
    }

    @Test
    public void roundTripNitf20_U3010A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_3010A.NTF");
    }

    @Test
    public void roundTripNitf20_U3050B() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_3050B.NTF");
    }

    @Test
    public void roundTripNitf20_U3058B() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_3058B.NTF");
    }

    @Test
    public void roundTripNitf20_U4002A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_4002A.NTF");
    }

    @Test
    public void roundTripNitf20_U4003B() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_4003B.NTF");
    }

    @Test
    public void roundTripNitf20_U4004B() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_4004B.NTF");
    }

    @Test
    public void roundTripNitf20_U4005A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_4005A.NTF");
    }

    @Test
    public void roundTripNitf20_U4007A() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_4007A.NTF");
    }
}
