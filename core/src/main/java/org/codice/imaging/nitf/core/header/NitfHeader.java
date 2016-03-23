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

import java.util.List;
import org.codice.imaging.nitf.core.RGBColour;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfDateTime;
import org.codice.imaging.nitf.core.common.TaggedRecordExtensionHandler;
import org.codice.imaging.nitf.core.security.FileSecurityMetadata;

/**
 * Interface for getting properties of a NITF file-level header.
 *
 * This corresponds to the structures in MIL-STD-2500C Table A-1.
 */
public interface NitfHeader extends TaggedRecordExtensionHandler {

    /**
    Return the complexity level (CLEVEL) for the file.
    Complexity level is provided in MIL-STD-2500C Table A-10 for NITF 2.1 and NSIF 1.0 files.
    @return complexity level
     */
    int getComplexityLevel();

    /**
     * Return the DES data lengths.
     *
     * @return the list of data extension segment data lengths
     */
    List<Integer> getDataExtensionSegmentDataLengths();

    /**
     * Return the DES subheader lengths.
     *
     * @return the list of data extension segment subheader lengths
     */
    List<Integer> getDataExtensionSegmentSubHeaderLengths();

    /**
     *
     * Return the extended header overflow (XHDOFL) for the file.
     *
     * This is the (1-base) index of the TRE into which extended header data
     * overflows.
     *
     * @return the extended header data overflow index
     */
    int getExtendedHeaderDataOverflow();

    /**
     * Return the background colour (FBKGC) for the file.
     *
     * This is only guaranteed to be valid for NITF 2.1 and NSIF 1.0 files. There are special rules (IPON Section 3.6)
     * that describe handling in NITF 2.0 files, which may result in unexpected background colours.
     *
     * @return background colour
     */
    RGBColour getFileBackgroundColour();

    /**
     * Return the date / time (FDT) for the file.
     *
     * "This field shall contain the time (UTC) (Zulu) of the file’s
     * origination."
     *
     * @return date time for the file
     */
    NitfDateTime getFileDateTime();

    /**
     * Return the file security metadata for the file.
     *
     * @return file security metadata.
     */
    FileSecurityMetadata getFileSecurityMetadata();

    /**
     * Return the title (FTITLE) for the file.
     *
     * @return the title of the file, or an empty string if not set
     */
    String getFileTitle();

    /**
     * Return the file type (NITF/NSIF version) for the file.
     *
     * @see FileType FileType enumeration for valid value of the type
     * @return the file type
     */
    FileType getFileType();

    /**
     * Return the graphic (or symbol) segment data lengths.
     *
     * @return the list of graphic segment data lengths
     */
    List<Integer> getGraphicSegmentDataLengths();

    /**
     * Return the graphic (or symbol) segment subheader lengths.
     *
     * @return the list of graphic segment subheader lengths
     */
    List<Integer> getGraphicSegmentSubHeaderLengths();

    /**
     * Return the image segment data lengths.
     *
     * @return the list of image segment data lengths
     */
    List<Long> getImageSegmentDataLengths();

    /**
     * Return the image segment subheader lengths.
     *
     * @return the list of image segment subheader lengths
     */
    List<Integer> getImageSegmentSubHeaderLengths();

    /**
     * Return the label segment data lengths.
     *
     * This will always be an empty list for NITF 2.1 / NSIF 1.0 file, which do not have label segments.
     *
     * @return the list of label segment data lengths
     */
    List<Integer> getLabelSegmentDataLengths();

    /**
     * Return the label segment subheader lengths.
     *
     * This will always be an empty list for NITF 2.1 / NSIF 1.0 file, which do not have label segments.
     *
     * @return the list of label segment subheader lengths
     */
    List<Integer> getLabelSegmentSubHeaderLengths();

    /**
     * Return the originating station identifier (OSTAID) for the file.
     *
     * "This field shall contain the identification code or name of the
     * originating organization, system, station, or product. It shall not be
     * filled with BCS spaces (0x20)."
     *
     * @return the originating station identifier
     */
    String getOriginatingStationId();

    /**
     * Return the originator's name (ONAME) for the file.
     *
     * @return originator's name
     */
    String getOriginatorsName();

    /**
     * Return the originator's phone number (OPHONE) for the file.
     *
     * @return the originator's phone number
     */
    String getOriginatorsPhoneNumber();

    /**
     * Return the standard type (STYPE) for the file.
     *
     * For NITF 2.1 / NSIF 1.0: "Standard type or capability. A BCS-A character
     * string BF01 which indicates that this file is formatted using ISO/IEC IS
     * 12087-5. NITF02.10 is intended to be registered as a profile of ISO/IEC
     * IS 12087-5." [MIL-STD-2500C]
     *
     * For NITF 2.0: "System type or capability. This field is reserved for
     * future use and shall be filled with spaces (ASCII 32, decimal)."
     *
     * That is, the only valid value for NITF 2.1 / NSIF 1.0 is "BF01", and the
     * only valid value for NITF 2.0 is " " (four spaces).
     *
     * @return the standard type.
     */
    String getStandardType();

    /**
     * Return the symbol (or graphic) segment data lengths.
     *
     * @return the list of symbol segment data lengths
     */
    List<Integer> getSymbolSegmentDataLengths();

    /**
     * Return the symbol (or graphic) segment subheader lengths.
     *
     * @return the list of symbol segment subheader lengths
     */
    List<Integer> getSymbolSegmentSubHeaderLengths();

    /**
     * Return the text segment data lengths.
     *
     * @return the list of text segment data lengths
     */
    List<Integer> getTextSegmentDataLengths();

    /**
     * Return the text segment subheader lengths.
     *
     * @return the list of text segment subheader lengths
     */
    List<Integer> getTextSegmentSubHeaderLengths();

    /**
     * Return the user defined header overflow (UDHOFL) for the file.
     * <p>
     * This is the (1-base) index of the TRE into which user defined header data
     * overflows.
     *
     * @return the user defined header overflow index
     */
    int getUserDefinedHeaderOverflow();

    /**
     * Set the title (FTITLE) for the file.
     *
     * The file title is 80 characters maximum.
     *
     * @param fileTitle the file title
     */
    void setFileTitle(final String fileTitle);

    /**
     * Set the originating station identifier (OSTAID) for the file.
     *
     * "This field shall contain the identification code or name of the
     * originating organization, system, station, or product. It shall not be
     * filled with BCS spaces (0x20)."
     *
     * This field should not exceed 10 characters in length.
     *
     * @param originatingStationId the originating station identifier to set
     */
    void setOriginatingStationId(final String originatingStationId);

    /**
     * Set the background colour (FBKGC) for the file.
     *
     * This is only guaranteed to be valid for NITF 2.1 and NSIF 1.0 files.
     * There are special rules (IPON Section 3.6) that describe handling in NITF
     * 2.0 files, which may result in unexpected background colours.
     *
     * @param backgroundColour colour to set
     */
    void setFileBackgroundColour(final RGBColour backgroundColour);

    /**
     * Set the date / time (FDT) for the file.
     *
     * "This field shall contain the time (UTC) (Zulu) of the file’s
     * origination."
     *
     * @param fileDateTime the date and time for the file
     */
    void setFileDateTime(final NitfDateTime fileDateTime);

    /**
     * Set the file security metadata elements for the file.
     *
     * See FileSecurityMetadata and SecurityMetadata for the various elements,
     * which differ slightly between NITF 2.0 and NITF 2.1/NSIF 1.0.
     *
     * @param nitfFileSecurityMetadata the security metadata values to set.
     */
    void setFileSecurityMetadata(final FileSecurityMetadata nitfFileSecurityMetadata);

    /**
     * Sets the file type (NITF/NSIF version) for the file.
     *
     * @see FileType FileType enumeration for valid values of the type
     *
     * @param type the file type to set
     */
    void setFileType(final FileType type);

    /**
     * Set the originator's name (ONAME) for the file.
     *
     * The originator's name is 24 characters maximum.
     *
     * @param originatorsName the originator's name.
     */
    void setOriginatorsName(final String originatorsName);

    /**
     * Set the originator's phone number (OPHONE) for the file.
     *
     * The originator's phone is 18 characters maximum.
     *
     * @param originatorsPhoneNumber the originator's phone number.
     */
    void setOriginatorsPhoneNumber(final String originatorsPhoneNumber);

    /**
     * Set the file-level security metadata for the file.
     *
     * See FileSecurityMetadata for the various elements, which differ slightly
     * between NITF 2.0 and NITF 2.1/NSIF 1.0.
     *
     * @param fsmeta the security metadata to set.
     */
    void setSecurityMetadata(final FileSecurityMetadata fsmeta);

    /**
     * Set the standard type (STYPE) for the file.
     *
     * For NITF 2.1 / NSIF 1.0: "Standard type or capability. A BCS-A character
     * string BF01 which indicates that this file is formatted using ISO/IEC IS
     * 12087-5. NITF02.10 is intended to be registered as a profile of ISO/IEC
     * IS 12087-5." [MIL-STD-2500C] For NITF 2.0: "System type or capability.
     * This field is reserved for future use and shall be filled with spaces
     * (ASCII 32, decimal)."
     *
     * That is, the only valid value for NITF 2.1 / NSIF 1.0 is "BF01", and the
     * only valid value for NITF 2.0 is " " (four spaces).
     *
     * @param standardType the standard type (four characters maximum).
     */
    void setStandardType(final String standardType);

}
