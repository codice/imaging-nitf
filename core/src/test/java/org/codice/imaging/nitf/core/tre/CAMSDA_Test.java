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

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author bradh
 */
public class CAMSDA_Test extends SharedTreTest {

    public CAMSDA_Test() {
    }

    @Test
    public void SimpleCAMSDA() throws Exception {
        String treTag = "CAMSDA";
        String testData = "CAMSDA001960010010010015b52e94f-5285-4374-8272-b29e7962af6eThe first and only camera in the first and only camera set                      ad955a2f-87ab-44c4-a918-8291074efcd600100000006000070000500000004000";
        int expectedLength = treTag.length() + "00196".length() + 196;

        Tre camsda = parseTRE(testData, expectedLength, treTag);
        assertEquals(4, camsda.getEntries().size());
        assertEquals(1, camsda.getIntValue("NUM_CAMERA_SETS"));
        assertEquals(1, camsda.getIntValue("NUM_CAMERA_SETS_IN_TRE"));
        assertEquals(1, camsda.getIntValue("FIRST_CAMERA_SET_IN_TRE"));
        assertEquals(1, camsda.getEntry("CAMERA_SETS").getGroups().size());
        TreGroup cameraSet1 = camsda.getEntry("CAMERA_SETS").getGroups().get(0);
        assertEquals(1, cameraSet1.getIntValue("NUM_CAMERAS_IN_SET"));
        assertEquals(1, cameraSet1.getEntry("CAMERAS").getGroups().size());
        TreGroup camera1 = cameraSet1.getEntry("CAMERAS").getGroups().get(0);
        assertEquals("5b52e94f-5285-4374-8272-b29e7962af6e", camera1.getFieldValue("CAMERA_ID"));
        assertEquals("The first and only camera in the first and only camera set", camera1.getFieldValue("CAMERA_DESC").trim());
        assertEquals("ad955a2f-87ab-44c4-a918-8291074efcd6", camera1.getFieldValue("LAYER_ID"));
        assertEquals(1, camera1.getIntValue("IDLVL"));
        assertEquals(0, camera1.getIntValue("IALVL"));
        assertEquals("0000600007", camera1.getFieldValue("ILOC"));
        assertEquals(5000, camera1.getIntValue("NROWS"));
        assertEquals(4000, camera1.getIntValue("NCOLS"));
    }
}
