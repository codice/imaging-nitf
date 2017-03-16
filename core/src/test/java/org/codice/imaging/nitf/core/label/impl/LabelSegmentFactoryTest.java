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
package org.codice.imaging.nitf.core.label.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for the LabelSegmentFactory.
 */
public class LabelSegmentFactoryTest {

    // Avoid test coverage problem
    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<LabelSegmentFactory> constructor = LabelSegmentFactory.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void checkDefaultBuild() throws NitfFormatException {
        LabelSegment segment = LabelSegmentFactory.getDefault();
        assertNotNull(segment);
        assertEquals(FileType.NITF_TWO_ZERO, segment.getFileType());
        assertEquals("", segment.getIdentifier());
        assertEquals(SecurityClassification.UNCLASSIFIED, segment.getSecurityMetadata().getSecurityClassification());
        assertEquals(0, segment.getLabelDisplayLevel());
        assertEquals(0, segment.getAttachmentLevel());
        assertEquals(0, segment.getLabelLocationRow());
        assertEquals(0, segment.getLabelLocationColumn());
        assertEquals(0, segment.getLabelCellHeight());
        assertEquals(0, segment.getLabelCellWidth());
    }
}
