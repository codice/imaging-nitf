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
package org.codice.imaging.nitf.core.graphic.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.graphic.GraphicColour;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


/**
 * Tests for GraphicSegmentFactory
 */
public class GraphicSegmentFactoryTest {

    public GraphicSegmentFactoryTest() {
    }

    // Avoid test coverage problem
    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<GraphicSegmentFactory> constructor = GraphicSegmentFactory.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void checkDefaultBuild() throws NitfFormatException {
        GraphicSegment segment = GraphicSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(segment);
        assertEquals(FileType.NITF_TWO_ONE, segment.getFileType());
        assertEquals("", segment.getIdentifier());
        assertEquals("", segment.getGraphicName());
        assertEquals(SecurityClassification.UNCLASSIFIED, segment.getSecurityMetadata().getSecurityClassification());
        assertEquals(0, segment.getGraphicDisplayLevel());
        assertEquals(0, segment.getAttachmentLevel());
        assertEquals(0, segment.getGraphicLocationRow());
        assertEquals(0, segment.getGraphicLocationColumn());
        assertEquals(0, segment.getBoundingBox1Row());
        assertEquals(0, segment.getBoundingBox1Column());
        assertEquals(GraphicColour.COLOUR, segment.getGraphicColour());
        assertEquals(0, segment.getBoundingBox2Row());
        assertEquals(0, segment.getBoundingBox2Column());
    }
}
