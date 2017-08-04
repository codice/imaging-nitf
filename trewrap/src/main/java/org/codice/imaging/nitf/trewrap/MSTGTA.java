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
 * Wrapper for the Mission Target Information (MSTGTA) TRE.
 */
public class MSTGTA extends FlatTreWrapper {

    private static final int VERTICAL_COLLECTION_TECHNIQUE = 0;
    private static final int FORWARD_OBLIQUE_COLLECTION_TECHNIQUE = 1;
    private static final int RIGHT_OBLIQUE_COLLECTION_TECHNIQUE = 2;
    private static final int LEFT_OBLIQUE_COLLECTION_TECHNIQUE = 3;
    private static final int BEST_POSSIBLE_COLLECTION_TECHNIQUE = 4;

    private static final int LAT_OFFSET = 0;
    private static final int LAT_DEGREES_LENGTH = 2;
    private static final int LAT_MINUTES_OFFSET = LAT_OFFSET + LAT_DEGREES_LENGTH;
    private static final int MINUTES_LENGTH = 2;
    private static final int LAT_SECONDS_OFFSET = LAT_MINUTES_OFFSET + MINUTES_LENGTH;
    private static final int SECONDS_LENGTH = 5;
    private static final int HEMISPHERE_MARKER_LENGTH = 1;
    private static final int LONGITUDE_OFFSET = LAT_SECONDS_OFFSET + SECONDS_LENGTH + HEMISPHERE_MARKER_LENGTH;
    private static final int LON_DEGREES_LENGTH = 3;
    private static final int LON_MINUTES_OFFSET = LON_DEGREES_LENGTH;
    private static final int LON_SECONDS_OFFSET = LON_MINUTES_OFFSET + MINUTES_LENGTH;
    private static final double MINUTES_IN_ONE_DEGREE = 60.0;
    private static final double SECONDS_IN_ONE_MINUTE = 60.0;

    private static final String TAG_NAME = "MSTGTA";

    /**
     * Create a MSTGTA TRE wrapper from an existing TRE.
     *
     * @param tre the TRE to wrap. Must match the MSTGTA tag.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public MSTGTA(final Tre tre) throws NitfFormatException {
        super(tre, TAG_NAME);
    }

    /**
     * Pre-Planned Target Number (TGT_NUM).
     *
     * A number assigned to each preplanned target, initialized at 00001.
     * Recorded in the mission target support data block and the mission catalog
     * support data block to associate the two groups of information. The same
     * number may be assigned to multiple mission catalogs support blocks. Each
     * mission target block shall have a unique number. 00000 = TRE is empty.
     * "Empty" indicates the TRE is a placeholder, not containing any actionable
     * data, and should be ignored by interpreting applications.
     *
     * Note that an empty TRE will be shown as not valid.
     *
     * @return target number, or 0 to indicate the whole TRE is not valid.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final int getTargetNumber() throws NitfFormatException {
        return getValueAsInteger("TGT_NUM");
    }

    /**
     * Get Designator of Target (TGT_ID).
     *
     * @return target identifier, or an empty string
     * @throws NitfFormatException if there was a problem during parsing
     *
     */
    public final String getTargetIdentifier() throws NitfFormatException {
        return getValueAsTrimmedString("TGT_ID");
    }

    /**
     * Get Basic Encyclopedia ID / OSUFFIX (target designator) of target
     * (TGT_BE).
     *
     * @return basic encyclopedia number (with O-suffix), or an empty string.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getTargetBE() throws NitfFormatException {
        return getValueAsTrimmedString("TGT_BE");
    }

    /**
     * Get Pre-Planned Target Priority (TGT_PRI).
     *
     * 1 = top priority 2 = second, etc.
     *
     * @return target priority, or an empty string.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getTargetPriority() throws NitfFormatException {
        return getValueAsTrimmedString("TGT_PRI");
    }

    /**
     * Get Target Requester (TGT_REQ).
     *
     * Identification of authority requesting targets image.
     *
     * @return target requester, or an empty string.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getTargetRequester() throws NitfFormatException {
        return getValueAsTrimmedString("TGT_REQ");
    }

    /**
     * Get Latest Time Information of Value (TGT_LTIOV).
     *
     * This field shall contain the date and time, referenced to UTC, at which
     * the information contained in the file, loses all value and should be
     * discarded. The date and time is in the format CCYYMMDDhhmm in which CCYY
     * is the year, MM is the month (01–12), DD is the day of the month (01 to
     * 31), hh is the hour (00 to 23), mm is the minute (00 to 59).
     *
     * @return UTC date time, or null
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final ZonedDateTime getLatestTimeInformationOfValue() throws NitfFormatException {
        String ltiov = getValueAsTrimmedString("TGT_LTIOV");
        if (ltiov.isEmpty()) {
            return null;
        }
        return parseAsZonedDateTime(CENTURY_DATE_TIME_MINUTES_FORMATTER, ltiov);
    }

    /**
     * Get Pre-Planned Target Type (TGT_TYPE) as an encoded field.
     *
     * <p>
     * <ul>
     * <li>0 = point
     * <li>1 = strip
     * <li>2 = area
     * </ul><p>
     *
     * @return target type, or an empty string if no information is available
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getPrePlannedTargetTypeEncoded() throws NitfFormatException {
        return getValueAsTrimmedString("TGT_TYPE");
    }

    /**
     * Get decoded Pre-Planned Target Type (TGT_TYPE) as an encoded field.
     *
     * <p>
     * <ul>
     * <li>0 = point
     * <li>1 = strip
     * <li>2 = area
     * </ul><p>
     *
     * @return target type as a decoded string, or "unknown" if not known.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getPrePlannedTargetType() throws NitfFormatException {
        switch (getPrePlannedTargetTypeEncoded()) {
            case "0":
                return "point";
            case "1":
                return "strip";
            case "2":
                return "area";
            default:
                return "unknown";
        }
    }

    /**
     * Get the Pre-Planned Collection Technique (TGT_COLL) encoded.
     *
     * <p>
     * <ul>
     * <li>0 = vertical
     * <li>1 = forward oblique
     * <li>2 = right oblique
     * <li>3 = left oblique
     * <li>4 = best possible
     * </ul><p>
     *
     * @return the collection technique as an encoded value.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final int getPrePlannedCollectionTechniqueEncoded() throws NitfFormatException {
        return getValueAsInteger("TGT_COLL");
    }

    /**
     * Get the Pre-Planned Collection Technique (TGT_COLL) decoded.
     *
     * <p>
     * <ul>
     * <li>0 = vertical
     * <li>1 = forward oblique
     * <li>2 = right oblique
     * <li>3 = left oblique
     * <li>4 = best possible
     * </ul><p>
     *
     * @return the collection technique as a decoded string, or "unknown"
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getPrePlannedCollectionTechnique() throws NitfFormatException {
        switch (getPrePlannedCollectionTechniqueEncoded()) {
            case VERTICAL_COLLECTION_TECHNIQUE:
                return "vertical";
            case FORWARD_OBLIQUE_COLLECTION_TECHNIQUE:
                return "forward oblique";
            case RIGHT_OBLIQUE_COLLECTION_TECHNIQUE:
                return "right oblique";
            case LEFT_OBLIQUE_COLLECTION_TECHNIQUE:
                return "left oblique";
            case BEST_POSSIBLE_COLLECTION_TECHNIQUE:
                return "best possible";
            default:
                return "unknown";
        }
    }

    /**
     * Get Target Functional Category Code (TGT_CAT).
     *
     * This is the Target Functional Category Code from DIAM-65-3-1. The five
     * character numeric code classifies the function performed by a facility.
     * The data code is based on an initial breakdown of targets into nine major
     * groups, identified by the first digit:
     * <p>
     * <ul>
     * <li>1 Raw Materials
     * <li>2 Basic Processing
     * <li>3 Basic Equipment Production
     * <li>4 Basic Services, Research, Utilities
     * <li>5 End Products (civilian)
     * <li>6 End Products (military)
     * <li>7 Places, Population, Gov‘t
     * <li>8 Air &amp; Missile Facilities
     * <li>9 Military Troop Facilities
     * </ul><p>
     * Each successive numeric character, reading from left to right, extends or
     * delineates the definition further.
     *
     * @return target category code, or an empty string.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getTargetFunctionalCategoryCode() throws NitfFormatException {
        return getValueAsTrimmedString("TGT_CAT");
    }

    /**
     * Get the Planned Time at Target (TGT_UTC).
     *
     * Format is hhmmssZ: hh = Hours, mm = Minutes, ss = Seconds, Z = UTC time
     * zone.
     *
     * @return planned time at target, or an empty string.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getPlannedTimeAtTarget() throws NitfFormatException {
        return getValueAsTrimmedString("TGT_UTC");
    }

    /**
     * Get the Target Elevation (TGT_ELEV) relative to MSL.
     *
     * Planned elevation of point target. For strip and area targets, this
     * corresponds to the average elevation of the target area. Measured in feet
     * or meters, as specified by TGT_ELEV_UNIT.
     *
     * @return target elevation, or an empty string.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getTargetElevation() throws NitfFormatException {
        return getValueAsTrimmedString("TGT_ELEV");
    }

    /**
     * Get the Unit of Target Elevation (TGT_ELEV_UNIT).
     *
     * f = feet, m = meters.
     *
     * @return target elevation units, or an empty string.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getTargetElevationUnits() throws NitfFormatException {
        return getValueAsTrimmedString("TGT_ELEV_UNIT");
    }

    /**
     * Get the target location (TGT_LOC).
     *
     * Planned latitude/ longitude of corresponding portion of target. Location
     * may be expressed in either degrees-minutes-seconds or in decimal degrees.
     * The format ddmmss.ssX represents degrees (00 to 89), minutes (00 to 59),
     * seconds (00 to 59), and hundredths of seconds (00 to 99) of latitude,
     * with X = N for north or S for south, and dddmmss.ssY represents degrees
     * (000 to 179), minutes (00 to 59), seconds (00 to 59), and hundredths of
     * seconds (00 to 99) of longitude, with Y = E for east or W for west. The
     * format ±dd.dddddd indicates degrees of latitude (north is positive), and
     * ±ddd.dddddd represents degrees of longitude (east is positive).
     *
     * @return target location in source string format.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final String getTargetLocationRaw() throws NitfFormatException {
        return getFieldValue("TGT_LOC");
    }

    /**
     * Get the latitude part of the target location (TGT_LOC).
     *
     * Planned latitude/ longitude of corresponding portion of target. Location
     * may be expressed in either degrees-minutes-seconds or in decimal degrees.
     * The format ddmmss.ssX represents degrees (00 to 89), minutes (00 to 59),
     * seconds (00 to 59), and hundredths of seconds (00 to 99) of latitude,
     * with X = N for north or S for south, and dddmmss.ssY represents degrees
     * (000 to 179), minutes (00 to 59), seconds (00 to 59), and hundredths of
     * seconds (00 to 99) of longitude, with Y = E for east or W for west. The
     * format ±dd.dddddd indicates degrees of latitude (north is positive), and
     * ±ddd.dddddd represents degrees of longitude (east is positive).
     *
     * @return latitude part of target location, converted from DMS if needed.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final double getTargetLocationLatitude() throws NitfFormatException {
        String targetLocation = getFieldValue("TGT_LOC");
        return parseLatitudeFromLocation(targetLocation);
    }

    /**
     * Get the longitude part of the target location (TGT_LOC).
     *
     * Planned latitude/ longitude of corresponding portion of target. Location
     * may be expressed in either degrees-minutes-seconds or in decimal degrees.
     * The format ddmmss.ssX represents degrees (00 to 89), minutes (00 to 59),
     * seconds (00 to 59), and hundredths of seconds (00 to 99) of latitude,
     * with X = N for north or S for south, and dddmmss.ssY represents degrees
     * (000 to 179), minutes (00 to 59), seconds (00 to 59), and hundredths of
     * seconds (00 to 99) of longitude, with Y = E for east or W for west. The
     * format ±dd.dddddd indicates degrees of latitude (north is positive), and
     * ±ddd.dddddd represents degrees of longitude (east is positive).
     *
     * @return longitude part of target location, converted from DMS if needed.
     * @throws NitfFormatException if there was a problem during parsing.
     */
    public final double getTargetLocationLongitude() throws NitfFormatException {
        String targetLocation = getFieldValue("TGT_LOC");
        return parseLongitudeFromLocation(targetLocation);
    }

    private double parseLatitudeFromLocation(final String location) throws NumberFormatException {
        String latitudePart = location.substring(0, LONGITUDE_OFFSET);
        if (latitudePart.startsWith("+") || latitudePart.startsWith("-")) {
            Double latitude = Double.parseDouble(latitudePart);
            return latitude;
        }
        if (latitudePart.endsWith("N") || latitudePart.endsWith("S")) {
            int degrees = Integer.parseInt(latitudePart.substring(LAT_OFFSET, LAT_MINUTES_OFFSET));
            int minutes = Integer.parseInt(latitudePart.substring(LAT_MINUTES_OFFSET, LAT_SECONDS_OFFSET));
            double seconds = Double.parseDouble(latitudePart.substring(LAT_SECONDS_OFFSET, LAT_SECONDS_OFFSET + SECONDS_LENGTH));
            Double latitude = (double) degrees + ((double) minutes + seconds / SECONDS_IN_ONE_MINUTE) / MINUTES_IN_ONE_DEGREE;
            if (latitudePart.endsWith("S")) {
                latitude *= -1.0;
            }
            return latitude;
        }
        return 0.0;
    }

    private double parseLongitudeFromLocation(final String location) throws NumberFormatException {
        String longitudePart = location.substring(LONGITUDE_OFFSET);
        if (longitudePart.startsWith("+") || longitudePart.startsWith("-")) {
            Double longitude = Double.parseDouble(longitudePart);
            return longitude;
        }
        if (longitudePart.endsWith("E") || longitudePart.endsWith("W")) {
            int degrees = Integer.parseInt(longitudePart.substring(0, LON_MINUTES_OFFSET));
            int minutes = Integer.parseInt(longitudePart.substring(LON_MINUTES_OFFSET, LON_SECONDS_OFFSET));
            double seconds = Double.parseDouble(longitudePart.substring(LON_SECONDS_OFFSET, LON_SECONDS_OFFSET + SECONDS_LENGTH));
            Double longitude = (double) degrees + ((double) minutes + seconds / SECONDS_IN_ONE_MINUTE) / MINUTES_IN_ONE_DEGREE;
            if (longitudePart.endsWith("W")) {
                longitude *= -1.0;
            }
            return longitude;
        }
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ValidityResult getValidity() throws NitfFormatException {
        ValidityResult validity = new ValidityResult();
        if (getValueAsInteger("TGT_NUM") == 0) {
            validity.setValidityStatus(ValidityResult.ValidityStatus.NOT_VALID);
            validity.setValidityResultDescription("TRE is Empty, and has no actionable data.");
        }
        return validity;
    }
}
