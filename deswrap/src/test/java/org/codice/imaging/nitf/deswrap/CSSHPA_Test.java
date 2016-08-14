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
package org.codice.imaging.nitf.deswrap;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import static org.codice.imaging.nitf.deswrap.CSSHPAConstants.CLOUD_SHAPES_USE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test for CSSHPA wrapper.
 */
public class CSSHPA_Test {

    public CSSHPA_Test() {
    }

    // Avoid test coverage problem
    @Test
    public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<CSSHPAConstants> constructor = CSSHPAConstants.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void baseDES() throws NitfFormatException, IOException {
        File shpFile = new File(getClass().getResource("/simplepoint.shp").getFile());
        File shxFile = new File(getClass().getResource("/simplepoint.shx").getFile());
        File dbfFile = new File(getClass().getResource("/simplepoint.dbf").getFile());
        DataExtensionSegment des = CSSHPA.createCSSHPA(FileType.NITF_TWO_ONE, shpFile, shxFile, dbfFile);
        CSSHPA csshpa = new CSSHPA(des);
        assertNotNull(csshpa);
        assertEquals("CSSHPA DES", csshpa.getIdentifier());
        assertEquals(1, csshpa.getVersion());
        assertEquals(SecurityClassification.UNCLASSIFIED, csshpa.getSecurityMetadata().getSecurityClassification());
        assertEquals("USER_DEF_SHAPES", csshpa.getShapeUse());
        assertEquals("POINT", csshpa.getShapeClass());
        assertEquals("", csshpa.getCloudCoverSourceSensor());
        assertArrayEquals(Files.readAllBytes(shpFile.toPath()), csshpa.getSHP());
        assertArrayEquals(Files.readAllBytes(shxFile.toPath()), csshpa.getSHX());
        assertArrayEquals(Files.readAllBytes(dbfFile.toPath()), csshpa.getDBF());
    }

    @Test
    public void cloudShapesDES() throws NitfFormatException, IOException {
        File shpFile = new File(getClass().getResource("/simplepoint.shp").getFile());
        File shxFile = new File(getClass().getResource("/simplepoint.shx").getFile());
        File dbfFile = new File(getClass().getResource("/simplepoint.dbf").getFile());
        DataExtensionSegment des = CSSHPA.createCSSHPA(FileType.NITF_TWO_ONE, CLOUD_SHAPES_USE, shpFile, shxFile, dbfFile);
        CSSHPA csshpa = new CSSHPA(des);
        assertNotNull(csshpa);
        assertEquals("CSSHPA DES", csshpa.getIdentifier());
        assertEquals(1, csshpa.getVersion());
        assertEquals(SecurityClassification.UNCLASSIFIED, csshpa.getSecurityMetadata().getSecurityClassification());
        assertEquals("CLOUD_SHAPES", csshpa.getShapeUse());
        assertEquals("POINT", csshpa.getShapeClass());
        assertEquals("PAN, MS", csshpa.getCloudCoverSourceSensor());
        assertArrayEquals(Files.readAllBytes(shpFile.toPath()), csshpa.getSHP());
        assertArrayEquals(Files.readAllBytes(shxFile.toPath()), csshpa.getSHX());
        assertArrayEquals(Files.readAllBytes(dbfFile.toPath()), csshpa.getDBF());
    }

    @Test
    public void cloudShapesSensorDES() throws NitfFormatException, IOException {
        File shpFile = new File(getClass().getResource("/simplepoint.shp").getFile());
        File shxFile = new File(getClass().getResource("/simplepoint.shx").getFile());
        File dbfFile = new File(getClass().getResource("/simplepoint.dbf").getFile());
        DataExtensionSegment des = CSSHPA.createCloudShapesDES(FileType.NITF_TWO_ONE, "MS", shpFile, shxFile, dbfFile);
        CSSHPA csshpa = new CSSHPA(des);
        assertNotNull(csshpa);
        assertEquals("CSSHPA DES", csshpa.getIdentifier());
        assertEquals(1, csshpa.getVersion());
        assertEquals(SecurityClassification.UNCLASSIFIED, csshpa.getSecurityMetadata().getSecurityClassification());
        assertEquals("CLOUD_SHAPES", csshpa.getShapeUse());
        assertEquals("POINT", csshpa.getShapeClass());
        assertEquals("MS", csshpa.getCloudCoverSourceSensor());
        assertArrayEquals(Files.readAllBytes(shpFile.toPath()), csshpa.getSHP());
        assertArrayEquals(Files.readAllBytes(shxFile.toPath()), csshpa.getSHX());
        assertArrayEquals(Files.readAllBytes(dbfFile.toPath()), csshpa.getDBF());
    }
}
