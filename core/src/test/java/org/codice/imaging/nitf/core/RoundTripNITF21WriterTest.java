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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author bradh
 */
public class RoundTripNITF21WriterTest extends AbstractWriterTest {

    public RoundTripNITF21WriterTest() {
    }

    @Test
    public void roundTripNITF21_I3001A() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/i_3001a.ntf");
    }

    @Test
    public void roundTripNITF21_I3004G() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/i_3004g.ntf");
    }

    @Test
    public void roundTripNITF21_I3034C() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/i_3034c.ntf");
    }

    @Test
    public void roundTripNITF21_I3034F() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/i_3034f.ntf");
    }

    @Test
    public void roundTripNITF21_I3041A() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/i_3041a.ntf");
    }

    @Test
    public void roundTripNITF21_I3051E() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3051e.ntf");
    }

    @Test
    public void roundTripNITF21_I3052A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3052a.ntf");
    }

    @Test
    public void roundTripNITF21_I3060A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3060a.ntf");
    }

    @Test
    public void roundTripNITF21_I3063F() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3063f.ntf");
    }

    @Test
    public void roundTripNITF21_I3068A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3068a.ntf");
    }

    @Test
    public void roundTripNITF21_I3128B() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3128b.ntf");
    }

    @Test
    public void roundTripNITF21_I3201C() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3201c.ntf");
    }

    @Test
    public void roundTripNITF21_I3228C() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3228c.ntf");
    }

    @Test
    public void roundTripNITF21_I3228E() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3228e.ntf");
    }

    @Test
    public void roundTripNITF21_I3301H() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3301h.ntf");
    }

    @Test
    public void roundTripNITF21_I3301K() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3301k.ntf");
    }

    @Test
    public void roundTripNITF21_I3303A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3303a.ntf");
    }

    @Test
    public void roundTripNITF21_I3405A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3405a.ntf");
    }

    @Test
    public void roundTripNITF21_I3430A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3430a.ntf");
    }

    @Test
    public void roundTripNITF21_NS3004F() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/ns3004f.nsf");
    }

    @Test
    public void roundTripNITF21_NS3010A() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3010a.nsf");
    }

    @Test
    public void roundTripNITF21_NS3034D() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/ns3034d.nsf");
    }

    @Test
    public void roundTripNITF21_NS3038A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/ns3038a.nsf");
    }

    @Test
    public void roundTripNITF21_NS3050A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/ns3050a.nsf");
    }

    @Test
    public void roundTripNITF21_NS3051V() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/ns3051v.nsf");
    }

    @Test
    public void roundTripNITF21_NS3059A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/ns3059a.nsf");
    }

    @Test
    public void roundTripNITF21_NS3073A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/ns3073a.nsf");
    }

    @Test
    public void roundTripNITF21_NS3101B() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/ns3101b.nsf");
    }

    @Test
    public void roundTripNITF21_NS3201A() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3201a.nsf");
    }

    @Test
    public void roundTripNITF21_NS3228D() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3228d.nsf");
    }

    @Test
    public void roundTripNITF21_NS3301B() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3301b.nsf");
    }

    @Test
    public void roundTripNITF21_NS3301E() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3301e.nsf");
    }

    @Test
    public void roundTripNITF21_NS3302A() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3302a.nsf");
    }

    @Test
    public void roundTripNITF21_NS3310A() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3310a.nsf");
    }

    @Test
    public void roundTripNITF21_NS3361C() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3361c.nsf");
    }

    @Test
    public void roundTripNITF21_V3301F() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/v_3301f.ntf");
    }

}
