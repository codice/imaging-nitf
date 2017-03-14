/**
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
 **/
package org.codice.imaging.nitf.core.graphic.impl;

import java.util.LinkedList;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.ParseStrategy;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.graphic.GraphicColour;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import org.codice.imaging.nitf.core.tre.impl.TreCollectionImpl;
import org.codice.imaging.nitf.core.tre.TreSource;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class GraphicSegmentParserTest {
    private ParseStrategy strategy;
    private NitfReader nitfReader;
    private final LinkedList<String> stringValues = new LinkedList<>();
    private final LinkedList<Integer> intValues = new LinkedList<>();

    private static final String SSCLAS = "U";
    private static final String SSCLSY = "S1";
    private static final String SSCODE = "S2";
    private static final String SSCTLH = "S3";
    private static final String SSREL = "S4";
    private static final String SSDCTP = "S5";
    private static final String SSDCDT = "S6";
    private static final String SSDCXM = "S7";
    private static final String SSDG = "S8";
    private static final String SSDGDT = "20150101";
    private static final String SSCLTX = "S10";
    private static final String SSCATP = "S11";
    private static final String SSCAUT = "S12";
    private static final String SSCRSN = "S13";
    private static final String SSSRDT = "S14";
    private static final String SSCTLN = "S15";

    private static final String ENCRYP = "0";
    private static final String SCOLOR = "C";
    private static final String SNAME = "12345678901234567890";
    private static final String SY = "";

    private static final int SXSOFL = 0;
    private static final int SXSHDL = 10;
    private static final int SBND2_ROW = 20;
    private static final int SBND2_COL = 21;
    private static final int SBND1_ROW = 30;
    private static final int SBND1_COL = 31;
    private static final int SLOC_ROW = 40;
    private static final int SLOC_COL = 41;
    private static final int SALVL = 50;
    private static final int SDLVL = 60;

    @Before
    public void setup() throws NitfFormatException {
        stringValues.clear();
        intValues.clear();
        this.nitfReader = mock(NitfReader.class);
        when(nitfReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);

        stringValues.push(SCOLOR);
        stringValues.push(ENCRYP);

        stringValues.push(SSCTLN);
        stringValues.push(SSSRDT);
        stringValues.push(SSCRSN);
        stringValues.push(SSCAUT);
        stringValues.push(SSCATP);
        stringValues.push(SSCLTX);
        stringValues.push(SSDGDT);
        stringValues.push(SSDG);
        stringValues.push(SSDCXM);
        stringValues.push(SSDCDT);
        stringValues.push(SSDCTP);
        stringValues.push(SSREL);
        stringValues.push(SSCTLH);
        stringValues.push(SSCODE);
        stringValues.push(SSCLSY);
        stringValues.push(SSCLAS);

        stringValues.push(SNAME);
        stringValues.push(SY);
        when(nitfReader.readBytes(any(Integer.class))).thenAnswer(a -> stringValues.pop());
        when(nitfReader.readTrimmedBytes(any(Integer.class))).thenAnswer(a -> stringValues.pop());

        intValues.push(SXSOFL);
        intValues.push(SXSHDL);
        intValues.push(SBND2_COL);
        intValues.push(SBND2_ROW);
        intValues.push(SBND1_COL);
        intValues.push(SBND1_ROW);
        intValues.push(SLOC_ROW);
        intValues.push(SLOC_COL);
        intValues.push(SALVL);
        intValues.push(SDLVL);
        when(nitfReader.readBytesAsInteger(any(Integer.class))).thenAnswer(a -> intValues.pop());

        strategy = mock(ParseStrategy.class);
        when(strategy.parseTREs(any(NitfReader.class), any(Integer.class), eq(TreSource.GraphicExtendedSubheaderData))).thenReturn(new TreCollectionImpl());
    }

    @Test
    public void testParse() throws NitfFormatException {
        GraphicSegmentParser parser = new GraphicSegmentParser();
        GraphicSegment header = parser.parse(nitfReader, strategy, 0);
        SecurityMetadata securityMetaData = header.getSecurityMetadata();

        assertThat(header, notNullValue());
        assertThat(header.getBoundingBox1Column(), is(SBND1_COL));
        assertThat(header.getBoundingBox1Row(), is(SBND1_ROW));
        assertThat(header.getBoundingBox2Column(), is(SBND2_COL));
        assertThat(header.getBoundingBox2Row(), is(SBND2_ROW));
        assertThat(header.getGraphicColour(), is(GraphicColour.COLOUR));
        assertThat(header.getGraphicDisplayLevel(), is(SDLVL));
        assertThat(header.getGraphicName(), is(SNAME));
        assertThat(header.getAttachmentLevel(), is(SALVL));
        assertThat(header.getExtendedHeaderDataOverflow(), is(SXSOFL));

        assertThat(securityMetaData, notNullValue());
        assertThat(securityMetaData.getClassificationReason(), is(SSCRSN));
        assertThat(securityMetaData.getClassificationAuthority(), is(SSCAUT));
        assertThat(securityMetaData.getClassificationAuthorityType(), is(SSCATP));
        assertThat(securityMetaData.getClassificationText(), is(SSCLTX));
        assertThat(securityMetaData.getCodewords(), is(SSCODE));
        assertThat(securityMetaData.getControlAndHandling(), is(SSCTLH));
        assertThat(securityMetaData.getDeclassificationDate(), is(SSDCDT));
        assertThat(securityMetaData.getDeclassificationExemption(), is(SSDCXM));
        assertThat(securityMetaData.getDeclassificationType(), is(SSDCTP));
        assertThat(securityMetaData.getDowngrade(), is(SSDG));
        assertThat(securityMetaData.getDowngradeDate(), is(SSDGDT));
        assertThat(securityMetaData.getDowngradeDateOrSpecialCase(), nullValue());
        assertThat(securityMetaData.getReleaseInstructions(), is(SSREL));
        assertThat(securityMetaData.getDowngradeEvent(), nullValue());
        assertThat(securityMetaData.getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
        assertThat(securityMetaData.getSecurityClassificationSystem(), is(SSCLSY));
        assertThat(securityMetaData.getSecurityControlNumber(), is(SSCTLN));
        assertThat(securityMetaData.getSecuritySourceDate(), is(SSSRDT));
    }
}
