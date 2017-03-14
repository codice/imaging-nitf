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
package org.codice.imaging.nitf.core.header.impl;

import org.codice.imaging.nitf.core.common.FileType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.codice.imaging.nitf.core.header.NitfHeader;
import org.junit.Test;

/**
 * Test setting Header complexity level
 */
public class TestHeaderComplexityLevel {

    public TestHeaderComplexityLevel() {
    }

    @Test
    public void checkDefault() {
        NitfHeader nitf = NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(nitf);
        assertEquals(3, nitf.getComplexityLevel());
    }

    @Test
    public void checkSet() {
        NitfHeader nitf = NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(nitf);
        nitf.setComplexityLevel(5);
        assertEquals(5, nitf.getComplexityLevel());
        nitf.setComplexityLevel(50);
        assertEquals(50, nitf.getComplexityLevel());
    }
}
