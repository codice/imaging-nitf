/*
 * Copyright 2015 (c) Codice Foundation
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

import org.codice.imaging.nitf.core.common.FileType;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the FileType class
 */
public class TypeTest {

    @Test
    public void textEquivalentTests() {
        assertEquals("NITF02.00", FileType.NITF_TWO_ZERO.getTextEquivalent());
        assertEquals("NITF02.10", FileType.NITF_TWO_ONE.getTextEquivalent());
        assertEquals("NSIF01.00", FileType.NSIF_ONE_ZERO.getTextEquivalent());
        assertEquals("", FileType.UNKNOWN.getTextEquivalent());
    }

    @Test
    public void textTypeTests() {
        assertEquals("NITF", FileType.NITF_TWO_ONE.getType());
        assertEquals("NITF", FileType.NITF_TWO_ZERO.getType());
        assertEquals("NSIF", FileType.NSIF_ONE_ZERO.getType());
        assertEquals("UNKNOWN", FileType.UNKNOWN.name());
    }

    @Test
    public void textVersionTests() {
        assertEquals("2.1", FileType.NITF_TWO_ONE.getVersion());
        assertEquals("2.0", FileType.NITF_TWO_ZERO.getVersion());
        assertEquals("1.0", FileType.NSIF_ONE_ZERO.getVersion());
        assertEquals("", FileType.UNKNOWN.getVersion());
    }
}
