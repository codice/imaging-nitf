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
package org.codice.imaging.nitf.core;

import java.util.ArrayList;
import java.util.List;

/**
    NITF file data.
*/
public class Nitf extends AbstractNitfSegment {
    private FileType fileType = FileType.UNKNOWN;
    private int nitfComplexityLevel = 0;
    private String nitfStandardType = null;
    private String nitfOriginatingStationId = null;
    private NitfDateTime nitfFileDateTime = null;
    private String nitfFileTitle = null;
    private NitfFileSecurityMetadata fileSecurityMetadata = null;
    private RGBColour nitfFileBackgroundColour = null;
    private String nitfOriginatorsName = null;
    private String nitfOriginatorsPhoneNumber = null;
    private int nitfUserDefinedHeaderOverflow = 0;
    private int nitfExtendedHeaderDataOverflow = 0;

    private List<NitfImageSegment> imageSegments = new ArrayList<NitfImageSegment>();
    private List<NitfGraphicSegment> graphicSegments = new ArrayList<NitfGraphicSegment>();
    private List<NitfSymbolSegment> symbolSegments = new ArrayList<NitfSymbolSegment>();
    private List<NitfLabelSegment> labelSegments = new ArrayList<NitfLabelSegment>();
    private List<NitfTextSegment> textSegments = new ArrayList<NitfTextSegment>();
    private List<NitfDataExtensionSegment> dataExtensionSegments = new ArrayList<NitfDataExtensionSegment>();

    /**
        Default constructor.
    */
    public Nitf() {
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

    /**
        Set the standard type (STYPE) for the file.
        <p>
        For NITF 2.1 / NSIF 1.0: "Standard type or capability. A BCS-A character string BF01 which indicates that this file is
        formatted using ISO/IEC IS 12087-5. NITF02.10 is intended to be registered as a profile of ISO/IEC IS
        12087-5." [MIL-STD-2500C]
        <p>
        For NITF 2.0: "System type or capability. This field is reserved for future use and shall be filled
        with spaces (ASCII 32, decimal)."
        <p>
        That is, the only valid value for NITF 2.1 / NSIF 1.0 is "BF01", and the only valid value for NITF 2.0 is "    " (four spaces).

        @param standardType the standard type (four characters maximum).
    */
    public final void setStandardType(final String standardType) {
        nitfStandardType = standardType;
    }

    /**
        Return the standard type (STYPE) for the file.
        <p>
        For NITF 2.1 / NSIF 1.0: "Standard type or capability. A BCS-A character string BF01 which indicates that this file is
        formatted using ISO/IEC IS 12087-5. NITF02.10 is intended to be registered as a profile of ISO/IEC IS
        12087-5." [MIL-STD-2500C]
        <p>
        For NITF 2.0: "System type or capability. This field is reserved for future use and shall be filled
        with spaces (ASCII 32, decimal)."
        <p>
        That is, the only valid value for NITF 2.1 / NSIF 1.0 is "BF01", and the only valid value for NITF 2.0 is "    " (four spaces).

        @return the standard type.
    */
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
    public final void setFileDateTime(final NitfDateTime fileDateTime) {
        nitfFileDateTime = fileDateTime;
    }

    /**
        Return the date / time (FDT) for the file.

        "This field shall contain the time (UTC) (Zulu) of the file’s origination."

        @return date time for the file
    */
    public final NitfDateTime getFileDateTime() {
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

        @return background colour
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
        Set the user defined header overflow (UDHOFL) for the file.
        <p>
        This is the (1-base) index of the TRE into which user defined header data
        overflows.

        @param overflow the user defined header overflow index
    */
    public final void setUserDefinedHeaderOverflow(final int overflow) {
        nitfUserDefinedHeaderOverflow = overflow;
    }

    /**
        Return the user defined header overflow (UDHOFL) for the file.
        <p>
        This is the (1-base) index of the TRE into which user defined header data
        overflows.

        @return the user defined header overflow index
    */
    public final int getUserDefinedHeaderOverflow() {
        return nitfUserDefinedHeaderOverflow;
    }

    /**
        Set the extended header data overflow (XHDOFL) for the file.
        <p>
        This is the (1-base) index of the TRE into which extended header data
        overflows.

        @param overflow the extended header data overflow index
    */
    public final void setExtendedHeaderDataOverflow(final int overflow) {
        nitfExtendedHeaderDataOverflow = overflow;
    }

    /**
        Return the extended header overflow (XHDOFL) for the file.
        <p>
        This is the (1-base) index of the TRE into which extended header data
        overflows.

        @return the extended header data overflow index
    */
    public final int getExtendedHeaderDataOverflow() {
        return nitfExtendedHeaderDataOverflow;
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

    /**
        Add an image segment to the file.

        @param imageSegment the image segment to add
    */
    public final void addImageSegment(final NitfImageSegment imageSegment) {
        imageSegments.add(imageSegment);
    }

    /**
        Return the specified image segment.

        @param segmentNumber the index of the image segment to return (1 base).
        @return the image segment corresponding to segmentNumber.
    */
    public final NitfImageSegment getImageSegment(final int segmentNumber) {
        return getImageSegmentZeroBase(segmentNumber - 1);
    }

    /**
        Return the specified image segment.

        @param segmentNumberZeroBase the index of the image segment to return (0 base).
        @return the image segment corresponding to segmentNumberZeroBase.
    */
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

    /**
        Return the specified graphic segment.
        <p>
        Only NITF 2.1 / NSIF 1.0 files will have graphic segments.

        @param segmentNumber the index of the graphic segment to return (1 base).
        @return the graphic segment corresponding to segmentNumberZeroBase.
    */
    public final NitfGraphicSegment getGraphicSegment(final int segmentNumber) {
        return getGraphicSegmentZeroBase(segmentNumber - 1);
    }

    /**
        Return the specified graphic segment.
        <p>
        Only NITF 2.1 / NSIF 1.0 files will have graphic segments.

        @param segmentNumberZeroBase the index of the graphic segment to return (0 base).
        @return the graphic segment corresponding to segmentNumberZeroBase.
    */
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

    /**
        Return the specified symbol segment.
        <p>
        Only NITF 2.0 files will have symbol segments.

        @param segmentNumber the index of the symbol segment to return (1 base).
        @return the symbol segment corresponding to segmentNumber.
    */
    public final NitfSymbolSegment getSymbolSegment(final int segmentNumber) {
        return getSymbolSegmentZeroBase(segmentNumber - 1);
    }

    /**
        Return the specified symbol segment.
        <p>
        Only NITF 2.0 files will have symbol segments.

        @param segmentNumberZeroBase the index of the symbol segment to return (0 base).
        @return the symbol segment corresponding to segmentNumberZeroBase.
    */
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

    /**
        Return the specified label segment.
        <p>
        Only NITF 2.0 files will have label segments.

        @param segmentNumber the index of the label segment to return (1 base).
        @return the label segment corresponding to segmentNumber.
    */
    public final NitfLabelSegment getLabelSegment(final int segmentNumber) {
        return getLabelSegmentZeroBase(segmentNumber - 1);
    }

    /**
        Return the specified label segment.
        <p>
        Only NITF 2.0 files will have label segments.

        @param segmentNumberZeroBase the index of the label segment to return (0 base).
        @return the label segment corresponding to segmentNumberZeroBase.
    */
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

    /**
        Return the specified text segment.

        @param segmentNumber the index of the text segment to return (1 base).
        @return the text segment corresponding to segmentNumber.
    */
    public final NitfTextSegment getTextSegment(final int segmentNumber) {
        return getTextSegmentZeroBase(segmentNumber - 1);
    }

    /**
        Return the specified text segment.

        @param segmentNumberZeroBase the index of the text segment to return (0 base).
        @return the text segment corresponding to segmentNumberZeroBase.
    */
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

    /**
        Return the specified data extension segment (DES).

        @param segmentNumber the index of the data extension segment to return (1 base).
        @return the data extension segment corresponding to segmentNumber.
    */
    public final NitfDataExtensionSegment getDataExtensionSegment(final int segmentNumber) {
        return getDataExtensionSegmentZeroBase(segmentNumber - 1);
    }

    /**
        Return the specified data extension segment (DES).

        @param segmentNumberZeroBase the index of the data extension segment to return (0 base).
        @return the data extension segment corresponding to segmentNumberZeroBase.
    */
    public final NitfDataExtensionSegment getDataExtensionSegmentZeroBase(final int segmentNumberZeroBase) {
        return dataExtensionSegments.get(segmentNumberZeroBase);
    }

}
