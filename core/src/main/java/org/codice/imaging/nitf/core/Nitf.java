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

    private final List<Integer> lish = new ArrayList<>();
    private final List<Long> li = new ArrayList<>();
    private final List<Integer> lssh = new ArrayList<>();
    private final List<Integer> ls = new ArrayList<>();
    private final List<Integer> llsh = new ArrayList<>();
    private final List<Integer> ll = new ArrayList<>();
    private final List<Integer> ltsh = new ArrayList<>();
    private final List<Integer> lt = new ArrayList<>();
    private final List<Integer> ldsh = new ArrayList<>();
    private final List<Integer> ld = new ArrayList<>();

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
     * Return the number of image segment subheader lengths.
     *
     * @return the number of image segment subheader lengths
     */
    public final int getNumberOfImageSegmentSubHeaderLengths() {
        return lish.size();
    }

    final void setImageSegmentSubHeaderLength(final int subheaderIndex, final Integer length) {
        lish.set(subheaderIndex, length);
    }

    final void addImageSegmentSubHeaderLength(final Integer length) {
        lish.add(length);
    }

    /**
     * Return the number of image segment data lengths.
     *
     * @return the number of image segment data lengths
     */
    public final int getNumberOfImageSegmentLengths() {
        return li.size();
    }

    /**
     * Return the length of the data in an image segment.
     * <p>
     * This does not include the size of the header, it is the size of the "Date Field" that follows the header.
     *
     * @param i the index of the image segment, zero base
     * @return the length of the data for the image segment (bytes)
     */
    public final long getLengthOfImageSegment(final int i) {
        return li.get(i);
    }

    final void setImageSegmentLength(final int subheaderIndex, final Long length) {
        li.set(subheaderIndex, length);
    }

    final void addImageSegmentLength(final Long length) {
        li.add(length);
    }

    /**
     * Return the number of graphic or symbol segment data lengths.
     *
     * @return the number of graphic / symbol segment data lengths
     */
    public final int getNumberOfGraphicSegmentsLengths() {
        return ls.size();
    }

    /**
     * Return the length of the data in a graphic or symbol segment.
     * <p>
     * This does not include the size of the header, it is the size of the "Date Field" that follows the header.
     * This is the size of the graphic segment data in NITF 2.1 (or NSIF 1.0), or the size of the symbol segment in NITF 2.0.
     *
     * @param i the index of the graphic or symbol segment, zero base
     * @return the length of the data for the graphic / symbol segment (bytes)
     */
    public final int getLengthOfGraphicSegment(final int i) {
        return ls.get(i);
    }

    final void addGraphicSegmentSubHeaderLength(final Integer length) {
        lssh.add(length);
    }

    final void addGraphicSegmentLength(final Integer length) {
        ls.add(length);
    }

    /**
     * Return the number of label segment subheader lengths.
     *
     * This will always be zero for a NITF 2.1 / NSIF 1.0 file, which do not have label segments.
     *
     * @return the number of label segment subheader lengths
     */
    public final int getNumberOfLabelSegmentSubHeaderLengths() {
        return llsh.size();
    }

    /**
     * Return the number of label segment data lengths.
     *
     * This will always be zero for a NITF 2.1 / NSIF 1.0 file, which do not have label segments.
     *
     * @return the number of label segment data lengths
     */
    public final int getNumberOfLabelSegmentLengths() {
        return ll.size();
    }

    /**
     * Return the length of the data in a label segment.
     * <p>
     * This does not include the size of the header, it is the size of the "Date Field" that follows the header.
     * This is only valid for NITF 2.0 files.
     *
     * @param i the index of the image segment, zero base
     * @return the length of the data for the image segment (bytes)
     */
    public final int getLengthOfLabelSegment(final int i) {
        return ll.get(i);
    }

    final void addLabelSegmentSubHeaderLength(final Integer length) {
        llsh.add(length);
    }

    final void addLabelSegmentLength(final Integer length) {
        ll.add(length);
    }

    /**
     * Return the number of text segment subheader lengths.
     *
     * @return the number of text segment subheader lengths
     */
    public final int getNumberOfTextSegmentSubHeaderLengths() {
        return ltsh.size();
    }

    /**
     * Return the number of text segment lengths.
     *
     * @return the number of text segment lengths
     */
    public final int getNumberOfTextSegmentLengths() {
        return lt.size();
    }

    /**
     * Return the length of the data in a text segment.
     * <p>
     * This does not include the size of the header, it is the size of the "Date Field" that follows the header.
     *
     * @param i the index of the text segment, zero base
     * @return the length of the data for the text segment (bytes)
     */
    public final int getLengthOfTextSegment(final int i) {
        return lt.get(i);
    }

    final void addTextSegmentSubHeaderLength(final Integer length) {
        ltsh.add(length);
    }

    final void addTextSegmentLength(final Integer length) {
        lt.add(length);
    }

    /**
     * Return the number of data extension segment subheader lengths.
     *
     * @return the number of data extension segment subheader lengths
     */
    public final int getNumberOfDataExtensionSegmentSubHeaderLengths() {
        return ldsh.size();
    }

    /**
     * Return the length of the data in a date extension segment.
     * <p>
     * This does not include the size of the header, it is the size of the "Date Field" that follows the header.
     *
     * @param i the index of the data extension segment, zero base
     * @return the length of the data for the data extension segment (bytes)
     */
    public final int getLengthOfDataExtensionSegment(final int i) {
        return ld.get(i);
    }

    final void addDataExtensionSegmentSubHeaderLength(final Integer length) {
        ldsh.add(length);
    }

    final void addDataExtensionSegmentLength(final Integer length) {
        ld.add(length);
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

}
