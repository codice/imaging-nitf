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

package org.codice.imaging.nitf.core.image;

import java.util.LinkedList;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfParseStrategy;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageSegmentParserTest {
    private NitfParseStrategy strategy;
    private NitfReader nitfReader;
    private LinkedList<String> stringValues = new LinkedList<String>();
    private LinkedList<Integer> intValues = new LinkedList<Integer>();

    private static final String IID1 = "IID1-TEST";
    private static final String IDATIM = "20150101090000";
    private static final String TGTID = "BBBBBBBBBBOOOOOCC";
    private static final String IID2 = "IID2-TEST";

    //Security Metadata
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
    private static final String ISORCE = " ";
    private static final int NROWS = 255;
    private static final int NCOLS = 512;
    private static final String PVTYPE = "INT";
    private static final String IREP = "YCbCr601";
    private static final String ICAT = "MS";
    private static final int ABPP = 101;
    private static final String PJUST = "L";
    private static final String ICORDS = "D";
    private static final String IGEOLO = "+00.001-000.002+00.001-000.002+00.001-000.002+00.001-000.002";
    private static final int NICOM = 1;
    private static final String ICOM1 = "Image Comment 1";
    private static final String IC = "M5";
    private static final String COMRAT = "1.22";
    private static final int NBANDS = 3;
    private static final String IREPBAND1 = "Y";
    private static final String IREPBAND2 = "Cb";
    private static final String IREPBAND3 = "Cr";
    private static final String ISUBCAT1 = " ";
    private static final String ISUBCAT2 = " ";
    private static final String ISUBCAT3 = " ";
    private static final int NLUTS1 = 0;
    private static final int NLUTS2 = 0;
    private static final int NLUTS3 = 0;
    private static final String ISYNC = "0";
    private static final String IMODE = "B";
    private static final int NBPR = 1;
    private static final int NBPC = 1;
    private static final int NPPBH = 1;
    private static final int NPPBV = 0;
    private static final int NBPP = 96;
    private static final int IDLVL = 1;
    private static final int IALVL = 0;
    private static final int ILOC_ROW = 0;
    private static final int ILOC_COL = 1;
    private static final String IMAG = "1.0";
    private static final int UDIDL = 0;
    private static final int IXSHDL = 0;

    @Before
    public void setup() throws NitfFormatException {
        stringValues.clear();
        intValues.clear();
        this.nitfReader = mock(NitfReader.class);
        when(nitfReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);

        stringValues.push(IMAG);
        stringValues.push(IMODE);
        stringValues.push(ISYNC);
        stringValues.push(ISUBCAT3);
        stringValues.push(IREPBAND3);
        stringValues.push(ISUBCAT2);
        stringValues.push(IREPBAND2);
        stringValues.push(ISUBCAT1);
        stringValues.push(IREPBAND1);
        stringValues.push(COMRAT);
        stringValues.push(IC);
        stringValues.push(ICOM1);
        stringValues.push(IGEOLO);
        stringValues.push(ICORDS);
        stringValues.push(PJUST);
        stringValues.push(ICAT);
        stringValues.push(IREP);
        stringValues.push(PVTYPE);
        stringValues.push(ISORCE);
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
        stringValues.push(IID2);
        stringValues.push(TGTID);
        stringValues.push(IDATIM);
        stringValues.push(IID1);

        intValues.push(IXSHDL);
        intValues.push(UDIDL);
        intValues.push(ILOC_COL);
        intValues.push(ILOC_ROW);
        intValues.push(IALVL);
        intValues.push(IDLVL);
        intValues.push(NBPP);
        intValues.push(NPPBV);
        intValues.push(NPPBH);
        intValues.push(NBPC);
        intValues.push(NBPR);
        intValues.push(NLUTS3);
        intValues.push(NLUTS2);
        intValues.push(NLUTS1);
        intValues.push(NBANDS);
        intValues.push(NICOM);
        intValues.push(ABPP);
        intValues.push(NCOLS);
        intValues.push(NROWS);

        when(nitfReader.readBytes(any(Integer.class))).thenAnswer(a -> stringValues.pop());
        when(nitfReader.readTrimmedBytes(any(Integer.class))).thenAnswer(a -> stringValues.pop());
        when(nitfReader.readBytesAsInteger(any(Integer.class))).thenAnswer(a -> intValues.pop());
        when(nitfReader.readBytesAsLong(any(Integer.class))).thenAnswer(a -> new Long(intValues.pop()));
        strategy = mock(NitfParseStrategy.class);
    }

    @Test
    public void testParse() throws NitfFormatException {
        ImageSegmentParser parser = new ImageSegmentParser();
        ImageSegment nitfImageSegmentHeader = parser.parse(nitfReader, strategy, 0);

        assertThat(nitfImageSegmentHeader.getImageMagnification(), is(IMAG));
        assertThat(nitfImageSegmentHeader.getImageMode(), is(ImageMode.BLOCKINTERLEVE));
        assertThat(nitfImageSegmentHeader.getCompressionRate(), is(COMRAT));
        assertThat(nitfImageSegmentHeader.getImageCompression(), is(ImageCompression.LOSSLESSJPEGMASK));
        assertThat(nitfImageSegmentHeader.getImageComments().get(0), is(ICOM1));
        assertThat(nitfImageSegmentHeader.getPixelJustification(), is(PixelJustification.LEFT));
        assertThat(nitfImageSegmentHeader.getImageCategory(), is(ImageCategory.MULTISPECTRAL));
        assertThat(nitfImageSegmentHeader.getImageRepresentation(), is(ImageRepresentation.ITUBT6015));
        assertThat(nitfImageSegmentHeader.getPixelValueType(), is(PixelValueType.INTEGER));
        assertThat(nitfImageSegmentHeader.getImageSource(), is(ISORCE));
        assertThat(nitfImageSegmentHeader.getImageIdentifier2(), is(IID2));
        assertThat(nitfImageSegmentHeader.getImageTargetId().getBasicEncyclopediaNumber(), is(TGTID.substring(0, 10)));
        assertThat(nitfImageSegmentHeader.getImageTargetId().getCountryCode(), is(TGTID.substring(15, 17)));
        assertThat(nitfImageSegmentHeader.getImageTargetId().getOSuffix(), is(TGTID.substring(10, 15)));
        assertThat(nitfImageSegmentHeader.getImageDateTime().getSourceString(), is(IDATIM));
        assertThat(nitfImageSegmentHeader.getIdentifier(), is(IID1));
        assertThat(nitfImageSegmentHeader.getExtendedHeaderDataOverflow(), is(IXSHDL));
        assertThat(nitfImageSegmentHeader.getImageLocationRow(), is(ILOC_ROW));
        assertThat(nitfImageSegmentHeader.getImageLocationColumn(), is(ILOC_COL));
        assertThat(nitfImageSegmentHeader.getAttachmentLevel(), is(IALVL));
        assertThat(nitfImageSegmentHeader.getImageDisplayLevel(), is(IDLVL));
        assertThat(nitfImageSegmentHeader.getNumberOfBitsPerPixelPerBand(), is(NBPP));
        assertThat(nitfImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(), is(NPPBH));
        assertThat(nitfImageSegmentHeader.getNumberOfPixelsPerBlockVertical(), is(NPPBV));
        assertThat(nitfImageSegmentHeader.getNumberOfBlocksPerColumn(), is(NBPC));
        assertThat(nitfImageSegmentHeader.getNumberOfBlocksPerRow(), is(NBPR));
        assertThat(nitfImageSegmentHeader.getImageComments().size(), is(NICOM));
        assertThat(nitfImageSegmentHeader.getActualBitsPerPixelPerBand(), is(ABPP));
        assertThat(nitfImageSegmentHeader.getNumberOfRows(), is(new Long(NROWS)));
        assertThat(nitfImageSegmentHeader.getNumberOfColumns(), is(new Long(NCOLS)));
        assertThat(nitfImageSegmentHeader.getImageCoordinatesRepresentation(), is(ImageCoordinatesRepresentation.DECIMALDEGREES));
        assertThat(nitfImageSegmentHeader.getImageBand(1).getNumLUTs(), is(NLUTS1));
        assertThat(nitfImageSegmentHeader.getImageBand(1).getImageRepresentation(), is(IREPBAND1));
        assertThat(nitfImageSegmentHeader.getImageBand(1).getSubCategory(), is(ISUBCAT1));
        assertThat(nitfImageSegmentHeader.getImageBand(2).getNumLUTs(), is(NLUTS2));
        assertThat(nitfImageSegmentHeader.getImageBand(2).getImageRepresentation(), is(IREPBAND2));
        assertThat(nitfImageSegmentHeader.getImageBand(2).getSubCategory(), is(ISUBCAT2));
        assertThat(nitfImageSegmentHeader.getImageBand(3).getNumLUTs(), is(NLUTS3));
        assertThat(nitfImageSegmentHeader.getImageBand(3).getImageRepresentation(), is(IREPBAND3));
        assertThat(nitfImageSegmentHeader.getImageBand(3).getSubCategory(), is(ISUBCAT3));

        SecurityMetadata securityMetadata = nitfImageSegmentHeader.getSecurityMetadata();
        assertThat(securityMetadata.getSecuritySourceDate(), is(SSSRDT));
        assertThat(securityMetadata.getSecurityControlNumber(), is(SSCTLN));
        assertThat(securityMetadata.getClassificationReason(), is(SSCRSN));
        assertThat(securityMetadata.getDeclassificationDate(), is(SSDCDT));
        assertThat(securityMetadata.getClassificationAuthority(), is(SSCAUT));
        assertThat(securityMetadata.getSecurityClassificationSystem(), is(SSCLSY));
        assertThat(securityMetadata.getClassificationAuthorityType(), is(SSCATP));
        assertThat(securityMetadata.getClassificationText(), is(SSCLTX));
        assertThat(securityMetadata.getDowngradeDate(), is(SSDGDT));
        assertThat(securityMetadata.getDowngrade(), is(SSDG));
        assertThat(securityMetadata.getDeclassificationExemption(), is(SSDCXM));
        assertThat(securityMetadata.getDeclassificationType(), is(SSDCTP));
        assertThat(securityMetadata.getControlAndHandling(), is(SSCTLH));
        assertThat(securityMetadata.getCodewords(), is(SSCODE));
        assertThat(securityMetadata.getSecurityClassification(), is(SecurityClassification.UNCLASSIFIED));
        assertThat(securityMetadata.getReleaseInstructions(), is(SSREL));
    }
}
