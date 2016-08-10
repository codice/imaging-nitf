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
package org.codice.imaging.nitf.trewrap;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for the CAMSDA wrapper.
 */
public class CAMSDA_WrapTest extends SharedTreTestSupport {

    private final String mTestData = "CAMSDA001960010010010015b52e94f-5285-4374-8272-b29e7962af6eThe first and only camera in the first and only camera set                      ad955a2f-87ab-44c4-a918-8291074efcd600100000006000070000500000004000";
    private final String mOffsetTestData = "CAMSDA001960010010040015b52e94f-5285-4374-8272-b29e7962af6eThe first and only camera in the first and only camera set                      ad955a2f-87ab-44c4-a918-8291074efcd600100000006000070000500000004000";

    public CAMSDA_WrapTest() {
    }

    @Test
    public final void testGetters() throws NitfFormatException {
        Tre tre = parseTRE(mTestData, "CAMSDA");
        CAMSDA camsda = new CAMSDA(tre);
        assertTrue(camsda.getValidity().isValid());
        assertEquals(1, camsda.getNumberOfCameraSetsInCollection());
        assertEquals(1, camsda.getNumberOfCameraSetsInTRE());
        assertEquals(1, camsda.getIndexOfFirstCameraSetInTRE());
        assertEquals(1, camsda.getNumberOfCamerasInCameraSet(0));
        assertEquals(1, camsda.getNumberOfCamerasInCameraSetForTRE(0));
        assertEquals("5b52e94f-5285-4374-8272-b29e7962af6e", camsda.getCameraID(0, 0));
        assertEquals("5b52e94f-5285-4374-8272-b29e7962af6e", camsda.getCameraIDForCameraSetInTRE(0, 0));
        assertEquals("The first and only camera in the first and only camera set", camsda.getCameraDescription(0, 0));
        assertEquals("The first and only camera in the first and only camera set", camsda.getCameraDescriptionForCameraSetInTRE(0, 0));
        assertEquals("ad955a2f-87ab-44c4-a918-8291074efcd6", camsda.getLayerID(0, 0));
        assertEquals("ad955a2f-87ab-44c4-a918-8291074efcd6", camsda.getLayerIDForCameraSetInTRE(0, 0));
        assertEquals(1, camsda.getImageDisplayLevel(0, 0));
        assertEquals(1, camsda.getImageDisplayLevelForCameraSetInTRE(0, 0));
        assertEquals(0, camsda.getImageAttachmentLevel(0, 0));
        assertEquals(0, camsda.getImageAttachmentLevelForCameraSetInTRE(0, 0));
        assertEquals(6, camsda.getImageLocationRow(0, 0));
        assertEquals(6, camsda.getImageLocationRowForCameraSetInTRE(0, 0));
        assertEquals(7, camsda.getImageLocationColumn(0, 0));
        assertEquals(7, camsda.getImageLocationColumnForCameraSetInTRE(0, 0));
        assertEquals(5000, camsda.getNumRows(0, 0));
        assertEquals(5000, camsda.getNumRowsForCameraSetInTRE(0, 0));
        assertEquals(4000, camsda.getNumColumns(0, 0));
        assertEquals(4000, camsda.getNumColumnsForCameraSetInTRE(0, 0));
    }

    @Test
    public final void testGettersOffset() throws NitfFormatException {
        Tre tre = parseTRE(mOffsetTestData, "CAMSDA");
        CAMSDA camsda = new CAMSDA(tre);
        assertTrue(camsda.getValidity().isValid());
        assertEquals(1, camsda.getNumberOfCameraSetsInCollection());
        assertEquals(1, camsda.getNumberOfCameraSetsInTRE());
        assertEquals(4, camsda.getIndexOfFirstCameraSetInTRE());
        assertEquals(1, camsda.getNumberOfCamerasInCameraSet(3));
        assertEquals(1, camsda.getNumberOfCamerasInCameraSetForTRE(0));
        assertEquals("5b52e94f-5285-4374-8272-b29e7962af6e", camsda.getCameraID(3, 0));
        assertEquals("5b52e94f-5285-4374-8272-b29e7962af6e", camsda.getCameraIDForCameraSetInTRE(0, 0));
        assertEquals("The first and only camera in the first and only camera set", camsda.getCameraDescription(3, 0));
        assertEquals("The first and only camera in the first and only camera set", camsda.getCameraDescriptionForCameraSetInTRE(0, 0));
        assertEquals("ad955a2f-87ab-44c4-a918-8291074efcd6", camsda.getLayerID(3, 0));
        assertEquals("ad955a2f-87ab-44c4-a918-8291074efcd6", camsda.getLayerIDForCameraSetInTRE(0, 0));
        assertEquals(1, camsda.getImageDisplayLevel(3, 0));
        assertEquals(1, camsda.getImageDisplayLevelForCameraSetInTRE(0, 0));
        assertEquals(0, camsda.getImageAttachmentLevel(3, 0));
        assertEquals(0, camsda.getImageAttachmentLevelForCameraSetInTRE(0, 0));
        assertEquals(6, camsda.getImageLocationRow(3, 0));
        assertEquals(6, camsda.getImageLocationRowForCameraSetInTRE(0, 0));
        assertEquals(7, camsda.getImageLocationColumn(3, 0));
        assertEquals(7, camsda.getImageLocationColumnForCameraSetInTRE(0, 0));
        assertEquals(5000, camsda.getNumRows(3, 0));
        assertEquals(5000, camsda.getNumRowsForCameraSetInTRE(0, 0));
        assertEquals(4000, camsda.getNumColumns(3, 0));
        assertEquals(4000, camsda.getNumColumnsForCameraSetInTRE(0, 0));
    }
}
