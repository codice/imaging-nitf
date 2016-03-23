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
package org.codice.imaging.nitf.core.header;

import java.util.ArrayList;
import java.util.List;
import org.codice.imaging.nitf.core.RGBColour;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfDateTime;
import org.codice.imaging.nitf.core.common.TaggedRecordExtensionHandlerImpl;
import org.codice.imaging.nitf.core.security.FileSecurityMetadata;

/**
 * NITF header data.
 */
class NitfHeaderImpl extends TaggedRecordExtensionHandlerImpl implements NitfHeader {
    private FileType fileType = FileType.UNKNOWN;
    private int nitfComplexityLevel = 0;
    private String nitfStandardType = null;
    private String nitfOriginatingStationId = null;
    private NitfDateTime nitfFileDateTime = null;
    private String nitfFileTitle = null;
    private FileSecurityMetadata fileSecurityMetadata = null;
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
    NitfHeaderImpl() {
    }

    /**
        Sets the file type (NITF/NSIF version) for the file.

        @see FileType FileType enumeration for valid values of the type
        @param type the file type to set
    */
    @Override
    public final void setFileType(final FileType type) {
        fileType = type;
    }

    /**
        Return the file type (NITF/NSIF version) for the file.

        @see FileType FileType enumeration for valid value of the type
        @return the file type
    */
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
    public final String getOriginatingStationId() {
        return nitfOriginatingStationId;
    }

    /**
        Set the date / time (FDT) for the file.

        "This field shall contain the time (UTC) (Zulu) of the file’s origination."

        @param fileDateTime the date and time for the file
    */
    @Override
    public final void setFileDateTime(final NitfDateTime fileDateTime) {
        nitfFileDateTime = fileDateTime;
    }

    /**
        Return the date / time (FDT) for the file.

        "This field shall contain the time (UTC) (Zulu) of the file’s origination."

        @return date time for the file
    */
    @Override
    public final NitfDateTime getFileDateTime() {
        return nitfFileDateTime;
    }

    /**
        Set the title (FTITLE) for the file.

        The file title is 80 characters maximum.

        @param fileTitle the file title
    */
    @Override
    public final void setFileTitle(final String fileTitle) {
        nitfFileTitle = fileTitle;
    }

    /**
        Return the title (FTITLE) for the file.

        @return the title of the file, or an empty string if not set
    */
    @Override
    public final String getFileTitle() {
        return nitfFileTitle;
    }

    /**
        Set the file security metadata elements for the file.

        See FileSecurityMetadata and SecurityMetadata for the various elements, which
        differ slightly between NITF 2.0 and NITF 2.1/NSIF 1.0.

        @param nitfFileSecurityMetadata the security metadata values to set.
    */
    @Override
    public final void setFileSecurityMetadata(final FileSecurityMetadata nitfFileSecurityMetadata) {
        fileSecurityMetadata = nitfFileSecurityMetadata;
    }

    /**
        Return the file security metadata for the file.

        @return file security metadata.
    */
    @Override
    public final FileSecurityMetadata getFileSecurityMetadata() {
        return fileSecurityMetadata;
    }

    /**
     * Set the background colour (FBKGC) for the file.
     *
     * This is only guaranteed to be valid for NITF 2.1 and NSIF 1.0 files. There are special rules (IPON Section 3.6)
     * that describe handling in NITF 2.0 files, which may result in unexpected background colours.
     *
     * @param backgroundColour colour to set
     */
    @Override
    public final void setFileBackgroundColour(final RGBColour backgroundColour) {
        nitfFileBackgroundColour = backgroundColour;
    }

    /**
     * Return the background colour (FBKGC) for the file.
     *
     * This is only guaranteed to be valid for NITF 2.1 and NSIF 1.0 files. There are special rules (IPON Section 3.6)
     * that describe handling in NITF 2.0 files, which may result in unexpected background colours.
     *
     * @return background colour
     */
    @Override
    public final RGBColour getFileBackgroundColour() {
        return nitfFileBackgroundColour;
    }

    /**
        Set the originator's name (ONAME) for the file.

        The originator's name is 24 characters maximum.

        @param originatorsName the originator's name.
    */
    @Override
    public final void setOriginatorsName(final String originatorsName) {
        nitfOriginatorsName = originatorsName;
    }

    /**
        Return the originator's name (ONAME) for the file.

        @return originator's name
    */
    @Override
    public final String getOriginatorsName() {
        return nitfOriginatorsName;
    }

    /**
        Set the originator's phone number (OPHONE) for the file.

        The originator's phone is 18 characters maximum.

        @param originatorsPhoneNumber the originator's phone number.
    */
    @Override
    public final void setOriginatorsPhoneNumber(final String originatorsPhoneNumber) {
        nitfOriginatorsPhoneNumber = originatorsPhoneNumber;
    }

    /**
        Return the originator's phone number (OPHONE) for the file.

        @return the originator's phone number
    */
    @Override
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
    @Override
    public final int getUserDefinedHeaderOverflow() {
        return nitfUserDefinedHeaderOverflow;
    }

    /**
     * Return the image segment subheader lengths.
     *
     * @return the list of image segment subheader lengths
     */
    @Override
    public final List<Integer> getImageSegmentSubHeaderLengths() {
        return lish;
    }

    /**
     * Return the image segment data lengths.
     *
     * @return the list of image segment data lengths
     */
    @Override
    public final List<Long> getImageSegmentDataLengths() {
        return li;
    }

    /**
     * Return the graphic (or symbol) segment subheader lengths.
     *
     * @return the list of graphic segment subheader lengths
     */
    @Override
    public final List<Integer> getGraphicSegmentSubHeaderLengths() {
        return lssh;
    }

    /**
     * Return the graphic (or symbol) segment data lengths.
     *
     * @return the list of graphic segment data lengths
     */
    @Override
    public final List<Integer> getGraphicSegmentDataLengths() {
        return ls;
    }

    /**
     * Return the symbol (or graphic) segment subheader lengths.
     *
     * @return the list of symbol segment subheader lengths
     */
    @Override
    public final List<Integer> getSymbolSegmentSubHeaderLengths() {
        return lssh;
    }

    /**
     * Return the symbol (or graphic) segment data lengths.
     *
     * @return the list of symbol segment data lengths
     */
    @Override
    public final List<Integer> getSymbolSegmentDataLengths() {
        return ls;
    }

    /**
     * Return the label segment subheader lengths.
     *
     * This will always be an empty list for NITF 2.1 / NSIF 1.0 file, which do not have label segments.
     *
     * @return the list of label segment subheader lengths
     */
    @Override
    public final List<Integer> getLabelSegmentSubHeaderLengths() {
        return llsh;
    }

    /**
     * Return the label segment data lengths.
     *
     * This will always be an empty list for NITF 2.1 / NSIF 1.0 file, which do not have label segments.
     *
     * @return the list of label segment data lengths
     */
    @Override
    public final List<Integer> getLabelSegmentDataLengths() {
        return ll;
    }

    /**
     * Return the text segment subheader lengths.
     *
     * @return the list of text segment subheader lengths
     */
    @Override
    public final List<Integer> getTextSegmentSubHeaderLengths() {
        return ltsh;
    }

    /**
     * Return the text segment data lengths.
     *
     * @return the list of text segment data lengths
     */
    @Override
    public final List<Integer> getTextSegmentDataLengths() {
        return lt;
    }

    /**
     * Return the DES subheader lengths.
     *
     * @return the list of data extension segment subheader lengths
     */
    @Override
    public final List<Integer> getDataExtensionSegmentSubHeaderLengths() {
        return ldsh;
    }

    /**
     * Return the DES data lengths.
     *
     * @return the list of data extension segment data lengths
     */
    @Override
    public final List<Integer> getDataExtensionSegmentDataLengths() {
        return ld;
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
    @Override
    public final int getExtendedHeaderDataOverflow() {
        return nitfExtendedHeaderDataOverflow;
    }

    /**
     * Get the file-level security metadata for the file.
     *
     * See FileSecurityMetadata for the various elements, which differ slightly between NITF 2.0 and NITF 2.1/NSIF 1.0.
     *
     * @return file security metadata
     */
    public final FileSecurityMetadata getSecurityMetadata() {
        return this.fileSecurityMetadata;
    }

    /**
     * Set the file-level security metadata for the file.
     *
     * See FileSecurityMetadata for the various elements, which differ slightly between NITF 2.0 and NITF 2.1/NSIF 1.0.
     *
     * @param fsmeta the security metadata to set.
     */
    @Override
    public final void setSecurityMetadata(final FileSecurityMetadata fsmeta) {
        this.fileSecurityMetadata = fsmeta;
    }

}
