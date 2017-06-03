/*
 * Copyright (C) 2014 Codice Foundation
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
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for JITC NITF 2.0 and NITF 2.1 test cases.
 * See:
 * http://www.gwg.nga.mil/ntb/baseline/software/testfile/Nitfv2_0/scen_2_0.html and
 * http://www.gwg.nga.mil/ntb/baseline/software/testfile/Nitfv2_1/scen_2_1.html
 */
public class RenderJitcTest extends RenderTestSupport {

    public RenderJitcTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testFILE1_JP2() throws IOException, NitfFormatException {
        testOneFile("file1_jp2.ntf", "JitcJpeg2000");
    }

    //todo (RWY) - this one doesn't work correctly
    @Test
    public void testFILE2_J2C() throws IOException, NitfFormatException {
        testOneFile("file2_j2c.ntf", "JitcJpeg2000");
    }

    @Test
    public void testFILE2_JP2_1PLACE() throws IOException, NitfFormatException {
        testOneFile("file2_jp2_1place.ntf", "JitcJpeg2000");
    }

    @Test
    public void testFILE2_JP2_2PLACES() throws IOException, NitfFormatException {
        testOneFile("file2_jp2_2places.ntf", "JitcJpeg2000");
    }

    @Ignore("Needs to be fixed")
    @Test
    public void testFILE3_JP2() throws IOException, NitfFormatException {
        // testOneFile("file3_jp2.ntf", "JitcJpeg2000");
    }

    @Test
    public void testFILE4_JP2() throws IOException, NitfFormatException {
        testOneFile("file4_jp2.ntf", "JitcJpeg2000");
    }

    //TODO (RWY) - color is off
    @Test
    public void testFILE5_JP2() throws IOException, NitfFormatException {
        testOneFile("file5_jp2.ntf", "JitcJpeg2000");
    }

    //TODO (RWY) - color is off
    @Test
    public void testFILE6_JP2() throws IOException, NitfFormatException {
        testOneFile("file6_jp2.ntf", "JitcJpeg2000");
    }

    //TODO (RWY) - color is off
    @Test
    public void testFILE7_JP2() throws IOException, NitfFormatException {
        testOneFile("file7_jp2.ntf", "JitcJpeg2000");
    }

    @Test
    public void testFILE8_JP2() throws IOException, NitfFormatException {
        testOneFile("file8_jp2.ntf", "JitcJpeg2000");
    }

    @Test
    public void testFILE9_J2C() throws IOException, NitfFormatException {
        testOneFile("file9_j2c.ntf", "JitcJpeg2000");
    }

    @Test
    public void testFILE9_JP2_1PLACE() throws IOException, NitfFormatException {
        testOneFile("file9_jp2_1place.ntf", "JitcJpeg2000");
    }

    @Test
    public void testFILE9_JP2_2PLACES() throws IOException, NitfFormatException {
        testOneFile("file9_jp2_2places.ntf", "JitcJpeg2000");
    }

    @Test
    public void testFILE9_NC() throws IOException, NitfFormatException {
        testOneFile("file9_nc.ntf", "JitcJpeg2000");
    }

    @Test
    public void testP0_01A() throws IOException, NitfFormatException {
        testOneFile("p0_01a.ntf", "JitcJpeg2000");
    }

    @Test
    public void testP0_02A() throws IOException, NitfFormatException {
        testOneFile("p0_02a.ntf", "JitcJpeg2000");
    }

    @Ignore("Needs to be fixed")
    @Test
    public void testP0_03A() throws IOException, NitfFormatException {
//        testOneFile("p0_03a.ntf", "JitcJpeg2000");
    }

    @Test
    public void testP0_04B() throws IOException, NitfFormatException {
        testOneFile("p0_04b.ntf", "JitcJpeg2000");
    }

    @Ignore("Needs to be fixed")
    @Test
    public void testP0_05A() throws IOException, NitfFormatException {
//        testOneFile("p0_05a.ntf", "JitcJpeg2000");
    }

    @Ignore("Needs to be fixed")
    @Test
    public void testP0_06A() throws IOException, NitfFormatException {
//        testOneFile("p0_06a.ntf", "JitcJpeg2000");
    }

    @Ignore("Needs to be fixed")
    @Test
    public void testP0_07A() throws IOException, NitfFormatException {
//        testOneFile("p0_07a.ntf", "JitcJpeg2000");
    }

    @Ignore("Needs to be fixed")
    @Test
    public void testP0_08A() throws IOException, NitfFormatException {
//        testOneFile("p0_08a.ntf", "JitcJpeg2000");
    }

    @Test
    public void testP0_09A() throws IOException, NitfFormatException {
        testOneFile("p0_09a.ntf", "JitcJpeg2000");
    }

    @Test
    public void testP0_10B() throws IOException, NitfFormatException {
        testOneFile("p0_10b.ntf", "JitcJpeg2000");
    }

    @Test
    public void testp0_11xa() throws IOException, NitfFormatException {
        testOneFile("p0_11xa.ntf", "JitcJpeg2000");
    }

    @Test
    public void testP0_12A() throws IOException, NitfFormatException {
        testOneFile("p0_12a.ntf", "JitcJpeg2000");
    }

    @Ignore("Needs to be fixed")
    @Test
    public void testP0_13XA() throws IOException, NitfFormatException {
//        testOneFile("p0_13xa.ntf", "JitcJpeg2000");
    }

    @Test
    public void testP0_14B() throws IOException, NitfFormatException {
        testOneFile("p0_14b.ntf", "JitcJpeg2000");
    }

    @Ignore("Needs to be fixed")
    @Test
    public void testP0_15A() throws IOException, NitfFormatException {
//        testOneFile("p0_15a.ntf", "JitcJpeg2000");
    }

    @Test
    public void testP0_16A() throws IOException, NitfFormatException {
        testOneFile("p0_16a.ntf", "JitcJpeg2000");
    }

    @Ignore("Needs to be fixed")
    @Test
    public void testP1_01A() throws IOException, NitfFormatException {
//        testOneFile("p1_01a.ntf", "JitcJpeg2000");
    }

    @Test
    public void testP1_02B() throws IOException, NitfFormatException {
        testOneFile("p1_02b.ntf", "JitcJpeg2000");
    }

    @Ignore("Needs to be fixed")
    @Test
    public void testP1_03A() throws IOException, NitfFormatException {
//        testOneFile("p1_03a.ntf", "JitcJpeg2000");
    }

    //TODO (RWY) - color is off
    @Test
    public void testP1_04A() throws IOException, NitfFormatException {
        testOneFile("p1_04a.ntf", "JitcJpeg2000");
    }

    @Ignore("Hangs needs to be fixed")
    @Test
    public void testP1_05B() throws IOException, NitfFormatException {
        //        testOneFile("p1_05b.ntf", "JitcJpeg2000");
    }

    @Test
    public void testP1_06B() throws IOException, NitfFormatException {
        testOneFile("p1_06b.ntf", "JitcJpeg2000");
    }

    @Ignore("Needs to be fixed")
    @Test
    public void testP1_07A() throws IOException, NitfFormatException {
//        testOneFile("p1_07a.ntf", "JitcJpeg2000");
    }

    @Test
    public void testNS3038A() throws IOException, NitfFormatException {
        testOneFile("ns3038a.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3041A() throws IOException, NitfFormatException {
        testOneFile("i_3041a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3050A() throws IOException, NitfFormatException {
        testOneFile("ns3050a.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testU_1036A() throws IOException, NitfFormatException {
        testOneFile("U_1036A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_1050A() throws IOException, NitfFormatException {
        testOneFile("U_1050A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_4003B() throws IOException, NitfFormatException {
        testOneFile("U_4003B.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_4004B() throws IOException, NitfFormatException {
        testOneFile("U_4004B.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_1001A() throws IOException, NitfFormatException {
        testOneFile("U_1001A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_1034A() throws IOException, NitfFormatException {
        testOneFile("U_1034A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_1101A() throws IOException, NitfFormatException {
        testOneFile("U_1101A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_1122A() throws IOException, NitfFormatException {
        testOneFile("U_1122A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_2001A() throws IOException, NitfFormatException {
        testOneFile("U_2001A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_3002A() throws IOException, NitfFormatException {
        testOneFile("U_3002A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_3010A() throws IOException, NitfFormatException {
        testOneFile("U_3010A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_3050B() throws IOException, NitfFormatException {
        testOneFile("U_3050B.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_4002A() throws IOException, NitfFormatException {
        testOneFile("U_4002A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_4005A() throws IOException, NitfFormatException {
        testOneFile("U_4005A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testU_4007A() throws IOException, NitfFormatException {
        testOneFile("U_4007A.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testI_3001A() throws IOException, NitfFormatException {
        testOneFile("i_3001a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3004G() throws IOException, NitfFormatException {
        testOneFile("i_3004g.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3201C() throws IOException, NitfFormatException {
        testOneFile("i_3201c.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3034C() throws IOException, NitfFormatException {
        testOneFile("i_3034c.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3034F() throws IOException, NitfFormatException {
        testOneFile("i_3034f.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3228C() throws IOException, NitfFormatException {
        testOneFile("i_3228c.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3228D() throws IOException, NitfFormatException {
        testOneFile("ns3228d.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3228E() throws IOException, NitfFormatException {
        testOneFile("i_3228e.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3301A() throws IOException, NitfFormatException {
        testOneFile("i_3301a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3301B() throws IOException, NitfFormatException {
        testOneFile("ns3301b.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3301E() throws IOException, NitfFormatException {
        testOneFile("ns3301e.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3301H() throws IOException, NitfFormatException {
        testOneFile("i_3301h.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3301J() throws IOException, NitfFormatException {
        testOneFile("ns3301j.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3301K() throws IOException, NitfFormatException {
        testOneFile("i_3301k.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3302A() throws IOException, NitfFormatException {
        testOneFile("ns3302a.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3303A() throws IOException, NitfFormatException {
        testOneFile("i_3303a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3310A() throws IOException, NitfFormatException {
        testOneFile("ns3310a.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3405A() throws IOException, NitfFormatException {
        testOneFile("i_3405a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3430A() throws IOException, NitfFormatException {
        testOneFile("i_3430a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3004F() throws IOException, NitfFormatException {
        testOneFile("ns3004f.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3034D() throws IOException, NitfFormatException {
        testOneFile("ns3034d.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3201A() throws IOException, NitfFormatException {
        testOneFile("ns3201a.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testV_3301F() throws IOException, NitfFormatException {
        testOneFile("v_3301f.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testU_3058B() throws IOException, NitfFormatException {
        testOneFile("U_3058B.NTF", "JitcNitf20Samples");
    }

    @Test
    public void testNS3361C() throws IOException, NitfFormatException {
        testOneFile("ns3361c.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3025b() throws IOException, NitfFormatException {
        testOneFile("i_3025b.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3309a() throws IOException, NitfFormatException {
        testOneFile("i_3309a.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testI_3113g() throws IOException, NitfFormatException {
        testOneFile("i_3113g.ntf", "JitcNitf21Samples");
    }

    @Test
    public void testNS3228b() throws IOException, NitfFormatException {
        testOneFile("ns3228b.nsf", "JitcNitf21Samples");
    }

    @Test
    public void testMultiBand() throws IOException, NitfFormatException {
        testOneFile("005_007_1024x1024_s_8_1_multi_j2c.ntf", "JitcNitf21Samples");
    }
}
