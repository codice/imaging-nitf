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
package org.codice.imaging.nitf.core.tre;

import java.io.IOException;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for parsing MTIMFA.
 *
 * This is defined in NGA.STND.0044 1.1, as part of MIE4NITF. See Section 5.9.3.5 and Table 14.
 */
public class MTIMFA_Test extends SharedTreTest {

    public MTIMFA_Test() {
    }

    @Test
    public void simpleParse() throws NitfFormatException, IOException {
        String treTag = "MTIMFA";
        String testData = "MTIMFA00138e6feb060-918c-4253-97e4-472e8756bd28003000004001dc4cb54e-1d97-4917-b13a-c25a880c702500120160827145527.89067263320160827145615.123184020007";
        int expectedLength = treTag.length() + "00138".length() + 138;
        
        Tre mtimfa = parseTRE(testData, expectedLength, treTag);
        assertEquals("e6feb060-918c-4253-97e4-472e8756bd28", mtimfa.getFieldValue("LAYER_ID"));
        assertEquals(3, mtimfa.getIntValue("CAMERA_SET_INDEX"));
        assertEquals(4, mtimfa.getIntValue("TIME_INTERVAL_INDEX"));
        assertEquals(1, mtimfa.getIntValue("NUM_CAMERAS_DEFINED"));
        assertEquals(1, mtimfa.getEntry("CAMERAS").getGroups().size());
        TreGroup camera0 = mtimfa.getEntry("CAMERAS").getGroups().get(0);
        assertEquals("dc4cb54e-1d97-4917-b13a-c25a880c7025", camera0.getFieldValue("CAMERA_ID"));
        assertEquals(1, camera0.getIntValue("NUM_TEMP_BLOCKS"));
        assertEquals(1, camera0.getEntry("TEMPORAL_BLOCKS").getGroups().size());
        TreGroup temporalBlock0 = camera0.getEntry("TEMPORAL_BLOCKS").getGroups().get(0);
        assertEquals("20160827145527.890672633", temporalBlock0.getFieldValue("START_TIMESTAMP"));
        assertEquals("20160827145615.123184020", temporalBlock0.getFieldValue("END_TIMESTAMP"));
        assertEquals(7, temporalBlock0.getIntValue("IMAGE_SEG_INDEX"));
    }
}
