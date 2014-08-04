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

    /**
        Set the originating station identifier (OSTAID) for the file.

        "This field shall contain the identification code or name of the originating
        organization, system, station, or product. It shall not be
        filled with BCS spaces (0x20)."

        This field should not exceed 10 characters in length.

        @param originatingStationId the originating station identifier to set
    */
    public final void setOriginatingStationId(final String originatingStationId) {
        nitfOriginatingStationId = originatingStationId;
    }

    /**
        Return the originating station identifier (OSTAID) for the file.

        "This field shall contain the identification code or name of the originating
        organization, system, station, or product. It shall not be
        filled with BCS spaces (0x20)."

        @return the originating station identifier
    */
    public final String getOriginatingStationId() {
        return nitfOriginatingStationId;
    }

    /**
        Set the date / time (FDT) for the file.

        "This field shall contain the time (UTC) (Zulu) of the file’s origination."

        @param fileDateTime the date and time for the file
    */
    public final void setFileDateTime(final Date fileDateTime) {
        nitfFileDateTime = fileDateTime;
    }

    /**
        Return the date / time (FDT) for the file.

        "This field shall contain the time (UTC) (Zulu) of the file’s origination."

        @return date time for the file
    */
    public final Date getFileDateTime() {
        return nitfFileDateTime;
    }

    /**
        Set the title (FTITLE) for the file.

        The file title is 80 characters maximum.

        @param fileTitle the file title
    */
    public final void setFileTitle(final String fileTitle) {
        nitfFileTitle = fileTitle;
    }

    /**
        Return the title (FTITLE) for the file.

        @return the title of the file, or an empty string if not set
    */
    public final String getFileTitle() {
        return nitfFileTitle;
    }

    /**
        Set the file security metadata elements for the file.

        See NitfFileSecurityMetadata and NitfSecurityMetadata for the various elements, which
        differ slightly between NITF 2.0 and NITF 2.1/NSIF 1.0.

        @param nitfFileSecurityMetadata the security metadata values to set.
    */
    public final void setFileSecurityMetadata(final NitfFileSecurityMetadata nitfFileSecurityMetadata) {
        fileSecurityMetadata = nitfFileSecurityMetadata;
    }

    /**
        Return the file security metadata for the file.

        @return file security metadata.
    */
    public final NitfFileSecurityMetadata getFileSecurityMetadata() {
        return fileSecurityMetadata;
    }

    /**
        Set the background colour (FBKGC) for the file.

        This is only valid for NITF 2.1 and NSIF 1.0 files.

        @param backgroundColour colour to set
    */
    public final void setFileBackgroundColour(final RGBColour backgroundColour) {
        nitfFileBackgroundColour = backgroundColour;
    }

    /**
        Return the background colour (FBKGC) for the file.

        This is only valid for NITF 2.1 and NSIF 1.0 files.
    */
    public final RGBColour getFileBackgroundColour() {
        return nitfFileBackgroundColour;
    }

    /**
        Set the originator's name (ONAME) for the file.

        The originator's name is 24 characters maximum.

        @param originatorsName the originator's name.
    */
    public final void setOriginatorsName(final String originatorsName) {
        nitfOriginatorsName = originatorsName;
    }

    /**
        Return the originator's name (ONAME) for the file.

        @return originator's name
    */
    public final String getOriginatorsName() {
        return nitfOriginatorsName;
    }

    /**
        Set the originator's phone number (OPHONE) for the file.

        The originator's phone is 18 characters maximum.

        @param originatorsPhoneNumber the originator's phone number.
    */
    public final void setOriginatorsPhoneNumber(final String originatorsPhoneNumber) {
        nitfOriginatorsPhoneNumber = originatorsPhoneNumber;
    }

    /**
        Return the originator's phone number (OPHONE) for the file.

        @return the originator's phone number
    */
    public final String getOriginatorsPhoneNumber() {
        return nitfOriginatorsPhoneNumber;
    }

    /**
        Return the number of image segments attached to this file.

        @return the number of image segments
    */
    public final int getNumberOfImageSegments() {
        return imageSegments.size();
    }

    /**
        Return the number of graphic segments attached to this file.

        This will only be non-zero for NITF 2.1 / NSIF 1.0 files. The
        equivalent for NITF 2.0 files is getNumberOfSymbolSegments().

        @return the number of graphic segments
    */

    public final int getNumberOfGraphicSegments() {
        return graphicSegments.size();
    }

    /**
        Return the number of symbol segments attached to this file.

        This will only be non-zero for NITF 2.0 files. The equivalent
        for NITF 2.1 / NSIF 1.0 files is getNumberOfGraphicSegments().

        @return the number of graphic segments
    */
    public final int getNumberOfSymbolSegments() {
        return symbolSegments.size();
    }

    /**
        Return the number of label segments attached to this file.

        This will only be non-zero for NITF 2.0 files. There is no
        equivalent for NITF 2.1 / NSIF 1.0 files.

        @return the number of label segments
    */
    public final int getNumberOfLabelSegments() {
        return labelSegments.size();
    }

    /**
        Return the number of text segments attached to this file.

        @return the number of text segments
    */
    public final int getNumberOfTextSegments() {
        return textSegments.size();
    }

    /**
        Return the number of data extension segments (DES) attached to this file.

        Note that DES can also be attached to image, graphic, symbol, label and
        text segments. This method only counts the number attached at the file level.

        @return the number of data extension segments
    */
    public final int getNumberOfDataExtensionSegments() {
        return dataExtensionSegments.size();
    }

//     public final int getNumberOfReservedExtensionSegments() {
//         return ;
//     }

    /**
        Add an image segment to the file

        @param imageSegment the image segment to add
    */
    public final void addImageSegment(final NitfImageSegment imageSegment) {
        imageSegments.add(imageSegment);
    }

    public final NitfImageSegment getImageSegment(final int segmentNumber) {
        return getImageSegmentZeroBase(segmentNumber - 1);
    }

    public final NitfImageSegment getImageSegmentZeroBase(final int segmentNumberZeroBase) {
        return imageSegments.get(segmentNumberZeroBase);
    }

    /**
        Add a graphic segment to the file.

        This is only supported for NITF 2.1 / NSIF 1.0 files.

        @param graphicSegment the graphic segment to add
    */
    public final void addGraphicSegment(final NitfGraphicSegment graphicSegment) {
        graphicSegments.add(graphicSegment);
    }

    public final NitfGraphicSegment getGraphicSegment(final int segmentNumber) {
        return getGraphicSegmentZeroBase(segmentNumber - 1);
    }

    public final NitfGraphicSegment getGraphicSegmentZeroBase(final int segmentNumberZeroBase) {
        return graphicSegments.get(segmentNumberZeroBase);
    }

    /**
        Add a symbol segment to the file.

        This is only supported for NITF 2.0 files.

        @param symbolSegment the symbol segment to add
    */
    public final void addSymbolSegment(final NitfSymbolSegment symbolSegment) {
        symbolSegments.add(symbolSegment);
    }

    public final NitfSymbolSegment getSymbolSegment(final int segmentNumber) {
        return getSymbolSegmentZeroBase(segmentNumber - 1);
    }

    public final NitfSymbolSegment getSymbolSegmentZeroBase(final int segmentNumberZeroBase) {
        return symbolSegments.get(segmentNumberZeroBase);
    }

    /**
        Add a label segment to the file.

        This is only supported for NITF 2.0 files.

        @param labelSegment the label segment to add
    */
    public final void addLabelSegment(final NitfLabelSegment labelSegment) {
        labelSegments.add(labelSegment);
    }

    public final NitfLabelSegment getLabelSegment(final int segmentNumber) {
        return getLabelSegmentZeroBase(segmentNumber - 1);
    }

    public final NitfLabelSegment getLabelSegmentZeroBase(final int segmentNumberZeroBase) {
        return labelSegments.get(segmentNumberZeroBase);
    }

    /**
        Add a text segment to the file.

        @param textSegment the text segment to add
    */
    public final void addTextSegment(final NitfTextSegment textSegment) {
        textSegments.add(textSegment);
    }

    public final NitfTextSegment getTextSegment(final int segmentNumber) {
        return getTextSegmentZeroBase(segmentNumber - 1);
    }

    public final NitfTextSegment getTextSegmentZeroBase(final int segmentNumberZeroBase) {
        return textSegments.get(segmentNumberZeroBase);
    }

    /**
        Add a data extension segment (DES) to the file.

        @param dataExtensionSegment the data extension segment to add
    */
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
