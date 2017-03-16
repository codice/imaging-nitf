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
package org.codice.imaging.nitf.core.image.impl;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.codice.imaging.nitf.core.image.ImageCoordinatesRepresentation;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Tests for coordinate parsing.
 *
 * This has additional cases for RFC-NTB-050.
 */
public class CoordinateParseTest {

    private static final String VALID_SUBHEADER_ENDING = "0NC1M       N   00B00010001102410240800100000000000001.0 0000000000";
    private static final String VALID_SUBHEADER_INTRO = "IMMissing ID19961217102630                 - BASE IMAGE -                                                                  U                                                                                                                                                                      0Unknown                                   0000102400001024INTMONO    VIS     08R";
    private static final String UPS_PART       = "PS22071391783242S22070631783169S22055291784776S22056041784848";
    private static final String UTM_SOUTH_PART = "S556897516085687556899196085688556899246085816556897556085806";
    private static final String MGRS_PART      = "U53HNB273080473653HNB282190473353HNB282150362453HNB2730503627";
    private static final String INVALID_PART   = "A                                                            ";

    public CoordinateParseTest() {
    }

    @Test
    public void parseUTMSouth() throws NitfFormatException {
        ImageSegment imageSegment = parseImageDataSegment(VALID_SUBHEADER_INTRO + UTM_SOUTH_PART + VALID_SUBHEADER_ENDING);
        assertEquals(ImageCoordinatesRepresentation.UTMSOUTH, imageSegment.getImageCoordinatesRepresentation());
        assertEquals("556897516085687", imageSegment.getImageCoordinates().getCoordinate00().getSourceFormat());
        assertEquals("556899196085688", imageSegment.getImageCoordinates().getCoordinate0MaxCol().getSourceFormat());
        assertEquals("556899246085816", imageSegment.getImageCoordinates().getCoordinateMaxRowMaxCol().getSourceFormat());
        assertEquals("556897556085806", imageSegment.getImageCoordinates().getCoordinateMaxRow0().getSourceFormat());
    }

    @Test
    public void parseUPS() throws NitfFormatException {
        ImageSegment imageSegment = parseImageDataSegment(VALID_SUBHEADER_INTRO + UPS_PART + VALID_SUBHEADER_ENDING);
        assertEquals(ImageCoordinatesRepresentation.UPS, imageSegment.getImageCoordinatesRepresentation());
        assertEquals("S22071391783242", imageSegment.getImageCoordinates().getCoordinate00().getSourceFormat());
        assertEquals("S22070631783169", imageSegment.getImageCoordinates().getCoordinate0MaxCol().getSourceFormat());
        assertEquals("S22055291784776", imageSegment.getImageCoordinates().getCoordinateMaxRowMaxCol().getSourceFormat());
        assertEquals("S22056041784848", imageSegment.getImageCoordinates().getCoordinateMaxRow0().getSourceFormat());
    }

    @Test
    public void parseMGRS() throws NitfFormatException {
        ImageSegment imageSegment = parseImageDataSegment(VALID_SUBHEADER_INTRO + MGRS_PART + VALID_SUBHEADER_ENDING);
        assertEquals(ImageCoordinatesRepresentation.MGRS, imageSegment.getImageCoordinatesRepresentation());
        assertEquals("53HNB2730804736", imageSegment.getImageCoordinates().getCoordinate00().getSourceFormat());
        assertEquals("53HNB2821904733", imageSegment.getImageCoordinates().getCoordinate0MaxCol().getSourceFormat());
        assertEquals("53HNB2821503624", imageSegment.getImageCoordinates().getCoordinateMaxRowMaxCol().getSourceFormat());
        assertEquals("53HNB2730503627", imageSegment.getImageCoordinates().getCoordinateMaxRow0().getSourceFormat());
    }

    private ImageSegment parseImageDataSegment(String imageHeaderData) throws NitfFormatException {
        ImageSegmentParser parser = new ImageSegmentParser();
        NitfReader nitfReader  = new NitfInputStreamReader(new ByteArrayInputStream(imageHeaderData.getBytes(StandardCharsets.ISO_8859_1)));
        nitfReader.setFileType(FileType.NITF_TWO_ONE);
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy();
        ImageSegment imageSegment = parser.parse(nitfReader, parseStrategy, 0);
        assertNotNull(imageSegment);
        return imageSegment;
    }

}
