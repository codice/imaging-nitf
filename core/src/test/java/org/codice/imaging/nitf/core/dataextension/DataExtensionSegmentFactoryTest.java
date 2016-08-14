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
package org.codice.imaging.nitf.core.dataextension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for the DES Factory.
 */
public class DataExtensionSegmentFactoryTest {

    public DataExtensionSegmentFactoryTest() {
    }

    // Avoid test coverage problem
    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<DataExtensionSegmentFactory> constructor = DataExtensionSegmentFactory.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetDefault() {
        DataExtensionSegment result = DataExtensionSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(result);
        assertEquals("", result.getIdentifier());
    }

    @Test
    public void testGetOverflow21() {
        DataExtensionSegment result = DataExtensionSegmentFactory.getOverflow(FileType.NITF_TWO_ONE, "UDHD", 0);
        assertNotNull(result);
        assertEquals("TRE_OVERFLOW", result.getIdentifier());
        assertEquals(1, result.getDESVersion());
        assertEquals(SecurityClassification.UNCLASSIFIED, result.getSecurityMetadata().getSecurityClassification());
        assertEquals("UDHD", result.getOverflowedHeaderType());
        assertEquals(0, result.getItemOverflowed());
    }

    @Test
    public void testGetOverflow21WrongValue() {
        DataExtensionSegment result = DataExtensionSegmentFactory.getOverflow(FileType.NITF_TWO_ONE, "UDHD", 3);
        assertNotNull(result);
        assertEquals("TRE_OVERFLOW", result.getIdentifier());
        assertEquals(1, result.getDESVersion());
        assertEquals(SecurityClassification.UNCLASSIFIED, result.getSecurityMetadata().getSecurityClassification());
        assertEquals("UDHD", result.getOverflowedHeaderType());
        assertEquals(0, result.getItemOverflowed());
    }

    @Test
    public void testGetOverflow20() {
        DataExtensionSegment result = DataExtensionSegmentFactory.getOverflow(FileType.NITF_TWO_ZERO, "IXSHD", 3);
        assertNotNull(result);
        assertEquals("Controlled Extensions", result.getIdentifier());
        assertEquals("IXSHD", result.getOverflowedHeaderType());
        assertEquals(3, result.getItemOverflowed());
    }

    @Test
    public void testGetOverflow20Registered() {
        DataExtensionSegment result = DataExtensionSegmentFactory.getOverflow(FileType.NITF_TWO_ZERO, "UDHD", 0);
        assertNotNull(result);
        assertEquals("Registered Extensions", result.getIdentifier());
        assertEquals("UDHD", result.getOverflowedHeaderType());
        assertEquals(0, result.getItemOverflowed());
    }

    @Test
    public void testGetOverflowNSIF() {
        DataExtensionSegment result = DataExtensionSegmentFactory.getOverflow(FileType.NSIF_ONE_ZERO, "SXSHD", 2);
        assertNotNull(result);
        assertEquals("TRE_OVERFLOW", result.getIdentifier());
        assertEquals(1, result.getDESVersion());
        assertEquals("SXSHD", result.getOverflowedHeaderType());
        assertEquals(2, result.getItemOverflowed());
    }

    @Test
    public void testBadFileType() {
        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage("Cannot make DES for unsupported FileType: U");
        DataExtensionSegment result = DataExtensionSegmentFactory.getOverflow(FileType.UNKNOWN, "SXSHD", 2);
    }
}
