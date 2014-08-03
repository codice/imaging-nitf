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
package org.codice.nitf.filereader;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public class NitfFile extends AbstractNitfSegment {
    private FileType fileType = FileType.UNKNOWN;
    private int nitfComplexityLevel = 0;
    private String nitfStandardType = null;
    private String nitfOriginatingStationId = null;
    private Date nitfFileDateTime = null;
    private String nitfFileTitle = null;
    private NitfFileSecurityMetadata fileSecurityMetadata = null;
    private RGBColour nitfFileBackgroundColour = null;
    private String nitfOriginatorsName = null;
    private String nitfOriginatorsPhoneNumber = null;

    private List<NitfImageSegment> imageSegments = new ArrayList<NitfImageSegment>();
    private List<NitfGraphicSegment> graphicSegments = new ArrayList<NitfGraphicSegment>();
    private List<NitfSymbolSegment> symbolSegments = new ArrayList<NitfSymbolSegment>();
    private List<NitfLabelSegment> labelSegments = new ArrayList<NitfLabelSegment>();
    private List<NitfTextSegment> textSegments = new ArrayList<NitfTextSegment>();
    private List<NitfDataExtensionSegment> dataExtensionSegments = new ArrayList<NitfDataExtensionSegment>();

    public NitfFile() {
    }

    public final void parse(final InputStream nitfInputStream) throws ParseException {
        new NitfFileParser(nitfInputStream, EnumSet.noneOf(ParseOption.class), this);
    }

    public final void parse(final InputStream nitfInputStream, final EnumSet<ParseOption> parseOptions) throws ParseException {
        new NitfFileParser(nitfInputStream, parseOptions, this);
    }

    /**
        Sets the file type (NITF/NSIF version) for the file.

        @see FileType FileType enumeration for valid values of the type
        @param type the file type to set
    */
    public final void setFileType(final FileType type) {
        fileType = type;
    }

    /**
        Return the file type (NITF/NSIF version) for the file.

        @see FileType FileType enumeration for valid value of the type
        @return the file type
    */
    public final FileType getFileType() {
        return fileType;
    }

    /**
        Set the complexity level (CLEVEL) for the file.

        This should not be done arbitrarily - it needs to match the reality of the file properties,
        including all images. See MIL-STD-2500C Table A-10 for NITF 2.1 and NSIF 1.0 files.

        @param complexityLevel the complexity level of the document.
    */
    public final void setComplexityLevel(final int complexityLevel) {
        nitfComplexityLevel = complexityLevel;
    }

    /**
        Return the complexity level (CLEVEL) for the file.

        Complexity level is provided in MIL-STD-2500C Table A-10 for NITF 2.1 and NSIF 1.0 files.

        @return complexity level
    */
    public final int getComplexityLevel() {
        return nitfComplexityLevel;
    }

    public final void setStandardType(final String standardType) {
        nitfStandardType = standardType;
    }

    public final String getStandardType() {
        return nitfStandardType;
    }

    public final void setOriginatingStationId(final String originatingStationId) {
        nitfOriginatingStationId = originatingStationId;
    }

    public final String getOriginatingStationId() {
        return nitfOriginatingStationId;
    }

    public final void setFileDateTime(final Date fileDateTime) {
        nitfFileDateTime = fileDateTime;
    }

    public final Date getFileDateTime() {
        return nitfFileDateTime;
    }

    public final void setFileTitle(final String fileTitle) {
        nitfFileTitle = fileTitle;
    }

    public final String getFileTitle() {
        return nitfFileTitle;
    }

    public final void setFileSecurityMetadata(final NitfFileSecurityMetadata nitfFileSecurityMetadata) {
        fileSecurityMetadata = nitfFileSecurityMetadata;
    }

    public final NitfFileSecurityMetadata getFileSecurityMetadata() {
        return fileSecurityMetadata;
    }

    public final void setFileBackgroundColour(final RGBColour backgroundColour) {
        nitfFileBackgroundColour = backgroundColour;
    }

    public final RGBColour getFileBackgroundColour() {
        return nitfFileBackgroundColour;
    }

    public final void setOriginatorsName(final String originatorsName) {
        nitfOriginatorsName = originatorsName;
    }

    public final String getOriginatorsName() {
        return nitfOriginatorsName;
    }

    public final void setOriginatorsPhoneNumber(final String originatorsPhoneNumber) {
        nitfOriginatorsPhoneNumber = originatorsPhoneNumber;
    }

    public final String getOriginatorsPhoneNumber() {
        return nitfOriginatorsPhoneNumber;
    }

    public final int getNumberOfImageSegments() {
        return imageSegments.size();
    }

    public final int getNumberOfGraphicSegments() {
        return graphicSegments.size();
    }

    public final int getNumberOfSymbolSegments() {
        return symbolSegments.size();
    }

    public final int getNumberOfTextSegments() {
        return textSegments.size();
    }

    public final int getNumberOfDataExtensionSegments() {
        return dataExtensionSegments.size();
    }

//     public final int getNumberOfReservedExtensionSegments() {
//         return ;
//     }

    public final void addImageSegment(final NitfImageSegment imageSegment) {
        imageSegments.add(imageSegment);
    }

    public final NitfImageSegment getImageSegment(final int segmentNumber) {
        return getImageSegmentZeroBase(segmentNumber - 1);
    }

    public final NitfImageSegment getImageSegmentZeroBase(final int segmentNumberZeroBase) {
        return imageSegments.get(segmentNumberZeroBase);
    }

    public final void addGraphicSegment(final NitfGraphicSegment graphicSegment) {
        graphicSegments.add(graphicSegment);
    }

    public final NitfGraphicSegment getGraphicSegment(final int segmentNumber) {
        return getGraphicSegmentZeroBase(segmentNumber - 1);
    }

    public final NitfGraphicSegment getGraphicSegmentZeroBase(final int segmentNumberZeroBase) {
        return graphicSegments.get(segmentNumberZeroBase);
    }

    public final void addSymbolSegment(final NitfSymbolSegment symbolSegment) {
        symbolSegments.add(symbolSegment);
    }

    public final NitfSymbolSegment getSymbolSegment(final int segmentNumber) {
        return getSymbolSegmentZeroBase(segmentNumber - 1);
    }

    public final NitfSymbolSegment getSymbolSegmentZeroBase(final int segmentNumberZeroBase) {
        return symbolSegments.get(segmentNumberZeroBase);
    }

    public final void addLabelSegment(final NitfLabelSegment labelSegment) {
        labelSegments.add(labelSegment);
    }

    public final NitfLabelSegment getLabelSegment(final int segmentNumber) {
        return getLabelSegmentZeroBase(segmentNumber - 1);
    }

    public final NitfLabelSegment getLabelSegmentZeroBase(final int segmentNumberZeroBase) {
        return labelSegments.get(segmentNumberZeroBase);
    }

    public final void addTextSegment(final NitfTextSegment textSegment) {
        textSegments.add(textSegment);
    }

    public final NitfTextSegment getTextSegment(final int segmentNumber) {
        return getTextSegmentZeroBase(segmentNumber - 1);
    }

    public final NitfTextSegment getTextSegmentZeroBase(final int segmentNumberZeroBase) {
        return textSegments.get(segmentNumberZeroBase);
    }

    public final void addDataExtensionSegment(final NitfDataExtensionSegment dataExtensionSegment) {
        dataExtensionSegments.add(dataExtensionSegment);
    }

    public final NitfDataExtensionSegment getDataExtensionSegment(final int segmentNumber) {
        return getDataExtensionSegmentZeroBase(segmentNumber - 1);
    }

    public final NitfDataExtensionSegment getDataExtensionSegmentZeroBase(final int segmentNumberZeroBase) {
        return dataExtensionSegments.get(segmentNumberZeroBase);
    }

//     public final void setNumberOfReservedExtensionSegments(final int numberOfReservedExtensionSegments) {
//         numberReservedExtensionSegments = numberOfReservedExtensionSegments;
//     }
}
