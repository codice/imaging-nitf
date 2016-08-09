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
package org.codice.imaging.nitf.core.symbol;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for the SymbolSegmentFactory.
 */
public class SymbolSegmentFactoryTest {

    public SymbolSegmentFactoryTest() {
    }

    // Avoid test coverage problem
    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<SymbolSegmentFactory> constructor = SymbolSegmentFactory.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void checkDefaultBuild() throws NitfFormatException {
        SymbolSegment segment = SymbolSegmentFactory.getDefault();
        assertNotNull(segment);
        assertEquals(FileType.NITF_TWO_ZERO, segment.getFileType());
        assertEquals("", segment.getIdentifier());
        assertEquals(SecurityClassification.UNCLASSIFIED, segment.getSecurityMetadata().getSecurityClassification());
        assertEquals(0, segment.getAttachmentLevel());
        assertEquals(SymbolType.CGM, segment.getSymbolType());
        assertEquals(0, segment.getNumberOfLinesPerSymbol());
        assertEquals(0, segment.getNumberOfPixelsPerLine());
        assertEquals(0, segment.getLineWidth());
        assertEquals(0, segment.getNumberOfBitsPerPixel());
        assertEquals(1, segment.getSymbolDisplayLevel());
        assertEquals(0, segment.getSymbolLocationColumn());
        assertEquals(0, segment.getSymbolLocationRow());
        assertEquals(0, segment.getSymbolLocation2Column());
        assertEquals(0, segment.getSymbolLocation2Row());
        assertEquals(SymbolColour.NOT_APPLICABLE, segment.getSymbolColour());
        assertEquals("000000", segment.getSymbolNumber());
        assertEquals(0, segment.getSymbolRotation());
    }
}
