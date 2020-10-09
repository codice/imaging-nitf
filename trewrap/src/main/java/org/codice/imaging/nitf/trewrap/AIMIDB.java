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
package org.codice.imaging.nitf.trewrap;

import java.time.ZonedDateTime;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;

/**
 * Wrapper for the Additional Image ID Extension (AIMIDB) TRE.
 */
public class AIMIDB extends FlatTreWrapper {

    private static final String TAG_NAME = "AIMIDB";

    /**
     * Create a AIMIDB TRE wrapper from an existing TRE.
     *
     * @param tre the TRE to wrap. Must match the AIMIDB tag.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public AIMIDB(final Tre tre) throws NitfFormatException {
        super(tre, TAG_NAME);
    }

    /**
     * Get the acquisition date and time.
     *
     * From STDI-0002 E.3.1.2 "This field shall contain the date and time,
     * referenced to UTC, of the collection in the format CCYYMMDDhhmmss, in
     * which CCYY is the year, MM is the month (01–12), DD is the day of the
     * month (01 to 31), hh is the hour (00 to 23), mm is the minute (00 to 59),
     * and ss is the second (00 to 59). This field is equivalent to the IDATIM
     * field in the image subheader.
     *
     * @return UTC referenced date time
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final ZonedDateTime getAcquisitionDate() throws NitfFormatException {
        return getValueAsZonedDateTime("ACQUISITION_DATE", CENTURY_DATE_TIME_SECONDS_FORMATTER);
    }

    /**
     * Get the mission number.
     *
     * From STDI-0002 E.3.1.2: Four character descriptor of the mission, which
     * has the form PPNN, where PP is the DIA Project Code (range is AA to ZZ)
     * or U0 if the Project Code is unknown, and "NN" is an assigned two-digit
     * identifier, for example, the last digits of FLIGHT_NO. "UNKN" (without
     * quotes) shall be used if no specific descriptor is known.
     *
     * @return mission number
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getMissionNumber() throws NitfFormatException {
        return getFieldValue("MISSION_NO");
    }

    /**
     * Get the name of the mission (mission identification).
     *
     * From STDI-0002 E.3.1.2: The Air Tasking Order Mission Number should be
     * used, if available, followed by spaces. "NOT AVAIL." (two words separated
     * by a single space and a trailing period, but without quotes) shall be
     * used if the Mission name is unavailable.
     *
     * @return mission identification, or "NOT AVAIL."
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getMissionIdentification() throws NitfFormatException {
        return getFieldValue("MISSION_IDENTIFICATION");
    }

    /**
     * Get the flight number.
     *
     * From STDI-0002 E.3.1.2: Each flight shall be identified by a flight
     * number in the range 01 to 09. Flight 01 shall be the first flight of the
     * day, flight 02 the second, etc. In order to ensure uniqueness in the
     * image id, if the aircraft mission extends across midnight UTC, the flight
     * number shall be 0x (where x is in the range 0 to 9) on images acquired
     * before midnight UTC and Ax on images acquired after midnight UTC; for
     * extended missions Bx, … Zx shall designate images acquired on subsequent
     * days. The value 00 indicates the flight number is unavailable.
     *
     * @return flight number or 00.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getFlightNumber() throws NitfFormatException {
        return getFieldValue("FLIGHT_NO");
    }

    /**
     * Get the image operation number.
     *
     * From STDI-0002 E.3.1.2: Reset to 001 at the start of each flight and
     * incremented by 1 for each distinct imaging operation. Reset to 001 for
     * the imaging operation following 999. A value of 000 indicates the
     * airborne system does not number imaging operations. For imagery derived
     * from video systems this field contains the frame number within the
     * ACQUISITION_DATE time.
     *
     * @return image operation number or 0 if imaging operations are not
     * numbered.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final int getImageOperationNumber() throws NitfFormatException {
        return getValueAsInteger("OP_NUM");
    }

    /**
     * Get the current segment identifier.
     *
     * From STDI-0002 E.3.1.2: Identifies which segment (piece) of an imaging
     * operation contains this image. AA is the first segment; AB is the second
     * segment, etc. This field shall contain AA if the image is not segmented
     * (i.e., consists of a single segment).
     *
     * @return segment identifier or AA if imaging operation is not segmented.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getCurrentSegmentID() throws NitfFormatException {
        return getFieldValue("CURRENT_SEGMENT");
    }

    /**
     * Get the reprocess number (SAR).
     *
     * From STDI-0002 E.3.1.2: For SAR imagery this field indicates whether the
     * data was reprocessed to overcome initial processing failures, or has been
     * enhanced. A 00 in this field indicates that the data is an originally
     * processed image, a 01 indicates the first reprocess/enhancement, etc. For
     * visible and infrared imagery this field shall contain 00 to indicate no
     * reprocessing or enhancement.
     *
     * @return reprocess number, or 0 for original images (including vis/IR).
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final int getReprocessNumber() throws NitfFormatException {
        return getValueAsInteger("REPRO_NUM");
    }

    /**
     * Get replay.
     *
     * From STDI-0002 E.3.1.2: Indicates whether the data was reprocessed to
     * overcome initial processing failures, or retransmitted to overcome
     * transmission errors. A 000 in this field indicates that the data is an
     * originally processed and transmitted image, a value in the ranges of G01
     * to G99 or P01 to P99 indicates the data is reprocessed, and a value in
     * the range of T01 to T99 indicates it was retransmitted.
     *
     * This field can be space filled.
     *
     * @return replay identifier, or 000 if the image is an original, or spaces.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getReplay() throws NitfFormatException {
        return getFieldValue("REPLAY");
    }

    /**
     * Get the starting tile column number.
     *
     * From STDI-0002 E.3.1.2: For tiled (blocked) sub-images, the number of the
     * first tile within the CURRENT_SEGMENT, relative to tiling at the start of
     * the imaging operation. Tiles are rectangular arrays of pixels
     * (dimensionally defined by the NITF image subheader NPPBH and NPPBV
     * fields) that subdivide an image. For untiled (single block) images this
     * field shall contain 001.
     *
     * @return starting tile column number or 1 for untiled images.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final int getStartingTileColumnNumber() throws NitfFormatException {
        return getValueAsInteger("START_TILE_COLUMN");
    }

    /**
     * Get the starting tile row number.
     *
     * From STDI-0002 E.3.1.2: For tiled (blocked) sub-images, the number of the
     * first tile within the CURRENT_SEGMENT, relative to tiling at the start of
     * the imaging operation. For untiled (single block) images this field shall
     * contain 00001.
     *
     * @return starting tile row number.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final int getStartingTileRowNumber() throws NitfFormatException {
        return getValueAsInteger("START_TILE_ROW");
    }

    /**
     * Get the ending segment.
     *
     * From STDI-0002 E.3.1.2: Ending segment ID of the imaging operation. This
     * field shall contain AA if the image is not segmented (i.e., consists of a
     * single segment). During an extended imaging operation the end segment may
     * not be known or predictable before it is collected; the value 00 (numeric
     * zeros) shall indicate that the ending segment of the operation is
     * unknown.
     *
     * @return the end segment, or 00 if ending segment is unknown.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getEndSegment() throws NitfFormatException {
        return getFieldValue("END_SEGMENT");
    }

    /**
     * Get the ending tile column number.
     *
     * From STDI-0002 E.3.1.2: For tiled (blocked) sub-images, the number of the
     * last tile within the END_SEGMENT, relative to tiling at the start of the
     * imaging operation. For untiled (single block) images this field shall
     * contain 001.
     *
     * @return ending tile column number, or 1 if the image is not tiled.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final int getEndTileColumn() throws NitfFormatException {
        return getValueAsInteger("END_TILE_COLUMN");
    }

    /**
     * Get the ending tile row number.
     *
     * From STDI-0002 E.3.1.2: For tiled (blocked) sub-images, the number of the
     * last tile within the END_SEGMENT, relative to tiling at the start of the
     * imaging operation. For untiled (single block) images this field shall
     * contain 00001.
     *
     * @return ending tile row number, or 1 if the image is not tiled.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final int getEndTileRow() throws NitfFormatException {
        return getValueAsInteger("END_TILE_ROW");
    }

    /**
     * Get the country code.
     *
     * From STDI-0002 E.3.1.2: Two letter code defining the country for the
     * reference point of the image. Standard codes may be found in FIPS PUB
     * 10-4.
     *
     * @return the country code, or spaces.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getCountryCode() throws NitfFormatException {
        return getFieldValue("COUNTRY");
    }

    /**
     * Get the location.
     *
     * From STDI-0002 E.3.1.2: Location of the natural reference point of the
     * sensor provides a rough indication of geographic coverage. The format
     * ddmmX represents degrees (00 to 89) and minutes (00 to 59) of latitude,
     * with X = N or S for north or south, and dddmmY represents degrees (000 to
     * 179) and minutes (00 to 59) of longitude, with Y = E or W for east or
     * west, respectively. For SAR imagery the reference point is normally the
     * center of the first image block. For EO-IR imagery the reference point
     * for framing sensors is the center of the frame; for continuous sensors,
     * it is the center of the first row of the image.
     *
     * Note: because the location is only reported to one arc-minute, it may be
     * more than ½ mile in error, and not actually represent any point within
     * the boundary of the image. Spaces indicate the location is unavailable.
     *
     * @return the location (in ddmmXdddmmY form), or spaces.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getLocation() throws NitfFormatException {
        return getFieldValue("LOCATION");
    }

    @Override
    public final ValidityResult getValidity() throws NitfFormatException {
        // TODO: build checks
        return new ValidityResult();
    }

}
