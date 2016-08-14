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
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for CSSHPA creation.
 */
public class CSSHPA_CreateTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public CSSHPA_CreateTest() {
    }

    @Test
    public void testCSSHPA_MULTIPOINTZ() throws NitfFormatException, IOException {
        String resourceName = "/multipointz";
        String expectedSubheader = "USER_DEF_SHAPES          MULTPOINTZSHP000000SHX000212DBF000320";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_MULTIPOINT() throws NitfFormatException, IOException {
        String resourceName = "/multipoint";
        String expectedSubheader = "USER_DEF_SHAPES          MULTIPOINTSHP000000SHX000180DBF000288";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_POLYLINEM() throws NitfFormatException, IOException {
        String resourceName = "/linestringm";
        String expectedSubheader = "USER_DEF_SHAPES          POLYLINEM SHP000000SHX000220DBF000328";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_POINTZ() throws NitfFormatException, IOException {
        String resourceName = "/pointz";
        String expectedSubheader = "USER_DEF_SHAPES          POINTZ    SHP000000SHX000172DBF000288";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_MULTIPOINTM() throws NitfFormatException, IOException {
        String resourceName = "/multipointm";
        String expectedSubheader = "USER_DEF_SHAPES          MULTPOINTMSHP000000SHX000212DBF000320";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_POINT() throws NitfFormatException, IOException {
        String resourceName = "/simplepoint";
        String expectedSubheader = "USER_DEF_SHAPES          POINT     SHP000000SHX000156DBF000272";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_POINTM() throws NitfFormatException, IOException {
        String resourceName = "/pointm";
        String expectedSubheader = "USER_DEF_SHAPES          POINTM    SHP000000SHX000172DBF000288";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_POLYGON() throws NitfFormatException, IOException {
        String resourceName = "/polygon";
        String expectedSubheader = "USER_DEF_SHAPES          POLYGON   SHP000000SHX000236DBF000344";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_POLYLINE() throws NitfFormatException, IOException {
        String resourceName = "/simplelinestring";
        String expectedSubheader = "USER_DEF_SHAPES          POLYLINE  SHP000000SHX000188DBF000296";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_POLYGONZ() throws NitfFormatException, IOException {
        String resourceName = "/polygonz";
        String expectedSubheader = "USER_DEF_SHAPES          POLYGONZ  SHP000000SHX000292DBF000400";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_POLYLINEZ() throws NitfFormatException, IOException {
        String resourceName = "/linestringz";
        String expectedSubheader = "USER_DEF_SHAPES          POLYLINEZ SHP000000SHX000220DBF000328";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_NULL() throws NitfFormatException, IOException {
        String resourceName = "/null";
        String expectedSubheader = "USER_DEF_SHAPES          NULL SHAPESHP000000SHX000112DBF000220";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_POLYGONM() throws NitfFormatException, IOException {
        String resourceName = "/polygonm";
        String expectedSubheader = "USER_DEF_SHAPES          POLYGONM  SHP000000SHX000292DBF000400";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    @Test
    public void testCSSHPA_MULTIPATCH() throws NitfFormatException, IOException {
        String resourceName = "/multipatch";
        String expectedSubheader = "USER_DEF_SHAPES          MULTIPATCHSHP000000SHX000320DBF000428";
        checkCSSHPA(resourceName, expectedSubheader);
    }

    private void checkCSSHPA(final String resourceName, final String expectedSubheader) throws IOException, NitfFormatException {
        File shapefile = new File(getClass().getResource(resourceName + ".shp").getFile());
        File shxfile = new File(getClass().getResource(resourceName + ".shx").getFile());
        File dbffile = new File(getClass().getResource(resourceName + ".dbf").getFile());
        DataExtensionSegment result = CSSHPA.createCSSHPA(FileType.NITF_TWO_ONE, shapefile, shxfile, dbffile);
        assertEquals("CSSHPA DES", result.getIdentifier());
        assertEquals(expectedSubheader, result.getUserDefinedSubheaderField());
        byte[] shp = new byte[(int) shapefile.length()];
        byte[] shx = new byte[(int) shxfile.length()];
        byte[] dbf = new byte[(int) dbffile.length()];
        result.getData().seek(0L);
        assertEquals(shp.length, result.getData().read(shp));
        byte[] shpReferenceBytes = new byte[shp.length];
        getClass().getResourceAsStream(resourceName + ".shp").read(shpReferenceBytes);
        Assert.assertArrayEquals(shpReferenceBytes, shp);
        assertEquals(shx.length, result.getData().read(shx));
        byte[] shxReferenceBytes = new byte[shx.length];
        getClass().getResourceAsStream(resourceName + ".shx").read(shxReferenceBytes);
        Assert.assertArrayEquals(shxReferenceBytes, shx);
        assertEquals(dbf.length, result.getData().read(dbf));
        byte[] dbfReferenceBytes = new byte[dbf.length];
        getClass().getResourceAsStream(resourceName + ".dbf").read(dbfReferenceBytes);
        Assert.assertArrayEquals(dbfReferenceBytes, dbf);
    }

    @Test
    public void testCSSHPA_InvalidShapeClass() throws NitfFormatException, IOException {
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Could not generate CSSHPA: Error reading shapefile, unknown shapefile geometry class: 255");
        CSSHPA.createCSSHPA(FileType.NITF_TWO_ONE, new File(getClass().getResource("/invalid.shp").getFile()), new File(getClass().getResource("/null.shp").getFile()), new File(getClass().getResource("/null.shp").getFile()));
    }

    @Test
    public void testCSSHPA_InvalidFileType() throws NitfFormatException, IOException {
        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage("Cannot make DES for unsupported FileType: U");
        CSSHPA.createCSSHPA(FileType.UNKNOWN, new File(getClass().getResource("/null.shp").getFile()), new File(getClass().getResource("/null.shp").getFile()), new File(getClass().getResource("/null.shp").getFile()));
    }

}
