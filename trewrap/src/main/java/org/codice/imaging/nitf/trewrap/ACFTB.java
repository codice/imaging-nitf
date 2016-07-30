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

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.xml.stream.XMLStreamException;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.trewrap.fields.ACFTBMissionPlanMode;
import org.codice.imaging.nitf.trewrap.fields.ACFTBSceneSource;
import org.codice.imaging.nitf.trewrap.fields.ACFTBSensorId;

/**
 * Wrapper for the Aircraft Information (ACFTB) TRE.
 */
public class ACFTB extends FlatTreWrapper {

    /**
     * Flag value that indicates that the True Map Angle.
     */
    public static final double INVALID_TRUE_MAP_ANGLE = -360.0;

    private static final String TAG_NAME = "ACFTB ";

    private static final int LAT_OFFSET = 0;
    private static final int LAT_DEGREES_LENGTH = 2;
    private static final int LAT_MINUTES_OFFSET = LAT_OFFSET + LAT_DEGREES_LENGTH;
    private static final int MINUTES_LENGTH = 2;
    private static final int LAT_SECONDS_OFFSET = LAT_MINUTES_OFFSET + MINUTES_LENGTH;
    private static final int SECONDS_LENGTH = 7;
    private static final int HEMISPHERE_MARKER_LENGTH = 1;
    private static final int LONGITUDE_OFFSET = LAT_SECONDS_OFFSET + SECONDS_LENGTH + HEMISPHERE_MARKER_LENGTH;
    private static final int LON_DEGREES_LENGTH = 3;
    private static final int LON_MINUTES_OFFSET = LON_DEGREES_LENGTH;
    private static final int LON_SECONDS_OFFSET = LON_MINUTES_OFFSET + MINUTES_LENGTH;
    private static final double MINUTES_IN_ONE_DEGREE = 60.0;
    private static final double SECONDS_IN_ONE_MINUTE = 60.0;

    private static final double MINIMUM_VALID_LOC_ACCY = 0.01;

    /**
     * Create a ACFTB TRE wrapper from an existing TRE.
     *
     * @param tre the TRE to wrap. Must match the ACFTB tag.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public ACFTB(final Tre tre) throws NitfFormatException {
        super(tre, TAG_NAME);
    }

    /**
     * Get the Aircraft Mission Identification field value.
     *
     * If the mission id is unavailable, will contain "NOT AVAILABLE".
     *
     * @return the mission identification, with any trailing spaces removed.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getAircraftMissionIdentification() throws NitfFormatException {
        return getValueAsTrimmedString("AC_MSN_ID");
    }

    /**
     * Get the Aircraft Tail Number field value.
     *
     * This can contain alphanumeric characters, or may be spaced filled.
     *
     * @return the aircraft tail number, with any trailing spaces removed.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getAircraftTailNumber() throws NitfFormatException {
        return getValueAsTrimmedString("AC_TAIL_NO");
    }

    /**
     * Get the Aircraft Take-off field value.
     *
     * This is a UTC Date/time (without meaningful seconds). It can be null
     * if not specified.
     *
     * @return the aircraft take-off time, or null.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final ZonedDateTime getAircraftTakeoff() throws NitfFormatException {
        return getValueAsZonedDateTime("AC_TO", CENTURY_DATE_TIME_MINUTES_FORMATTER);
    }

    /**
     * Get the Aircraft Sensor ID Type field value.
     *
     * This is an encoded field that identifies the sensor type that produced the image.
     *
     * @return the sensor ID type.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getSensorIdentificationType() throws NitfFormatException {
        return getValueAsTrimmedString("SENSOR_ID_TYPE");
    }

    /**
     * Get the Aircraft Sensor ID field value.
     *
     * This is an encoded field that identifies the specific sensor that produced the image.
     *
     * @return the sensor ID
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getSensorIdentification() throws NitfFormatException {
        return getValueAsTrimmedString("SENSOR_ID");
    }

    /**
     * Get the decoded Aircraft Sensor ID field value.
     *
     * This is an descriptive text corresponding to the specific sensor that produced the image.
     *
     * @return description of the sensor ID
     * @throws NitfFormatException if there is a parsing issue on the NITF side
     * @throws XMLStreamException if there is a problem creating the lookup class.
     * @throws IOException if the resource could not be opened.
     */
    public final String getSensorIdentificationDecoded() throws NitfFormatException, XMLStreamException, IOException {
        String encodedValue = getValueAsTrimmedString("SENSOR_ID");
        ACFTBSensorId acftbSensorIdLookup = ACFTBSensorId.getInstance();
        return acftbSensorIdLookup.lookupDescription(encodedValue);
    }

    /**
     * Get the Scene Source field value.
     *
     * This is an encoded field that identifies the origin of the request for the
     * current scene. A scene is a single image or a collection of images providing
     * continguous coverage of an area of interest.
     *
     * @return the scene source
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getSceneSource() throws NitfFormatException {
        return getValueAsTrimmedString("SCENE_SOURCE");
    }

    /**
     * Get the decoded Scene Source field value.
     *
     * This is an string description (decode) of the encoded scene source field
     * that identifies the origin of the request for the
     * current scene. A scene is a single image or a collection of images providing
     * continguous coverage of an area of interest.
     *
     * @return text description of the scene source,or null if not known.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getSceneSourceDecoded() throws NitfFormatException {
        String sceneSource = getValueAsTrimmedString("SCENE_SOURCE");
        if (sceneSource.equals("")) {
            return "Not specified";
        } else if (sceneSource.equals("0")) {
            return "Pre-Planned";
        }
        ACFTBSceneSource acftbSceneSourceLookup = ACFTBSceneSource.getInstance();
        return acftbSceneSourceLookup.lookupDescription(getSensorIdentification(), sceneSource, null);
    }

    /**
     * Get the scene number.
     *
     * From STDI-0002 Appendix E: "Identifies the current scene, and is determined
     * from the mission plan; except for immediate scenes, where it may have the
     * value 000000, the scenes are numbered from 000001 to 999999. The scene
     * number is only useful to replay/regenerate a specific scene; there is no
     * relationship between the scene number and an exploitation requirement."
     *
     * @return the scene number (as an integer)
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getSceneNumber() throws NitfFormatException {
        return getValueAsInteger("SCNUM");
    }

    /**
     * Get the processing date.
     *
     * From STDI-0002 Appendix E: "SAR: when raw data is converted to imagery.
     * EO-IR: when image file is created. ... This date changes at midnight UTC."
     *
     * @return the processing date, or null if the date could not be parsed
     * @throws NitfFormatException if there is a parsing issue with the TRE.
     */
    public final LocalDate getProcessingDate() throws NitfFormatException {
        return getValueAsLocalDate("PDATE");
    }

    /**
     * Get the immediate scene host.
     *
     * From STDI-0002 Appendix E: "Together with Immediate Scene Request ID
     * below, denotes the scene that the immediate scene was initiated from and
     * can be used to renumber the scene, Example: If the immediate scene was
     * initiated from scene number 000123 and this is the third request from that
     * scene, then the scene number field will be 000000, the immediate scene
     * host field will contain 000123 and the immediate scene request id will
     * contain 000003. Only non-zero for immediate scenes."
     *
     * @return the scene number (as an integer)
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getImmediateSceneHost() throws NitfFormatException {
        return getValueAsInteger("IMHOSTNO");
    }

    /**
     * Get the immediate scene request ID.
     *
     * See getImmediateSceneHost() for more information.
     *
     * @return the scene request identifier (as an integer)
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getImmediateSceneRequestId() throws NitfFormatException {
        return getValueAsInteger("IMREQID");
    }

    /**
     * Get the mission plan mode.
     *
     * This is an encoded field value that defined the current sensor-specific
     * (SENSOR_TYPE/SENSOR_ID) collection mode.
     *
     * @return the mission plan mode identifier (as an integer)
     * @throws NitfFormatException if there is a parsing issue
     */
    public final int getMissionPlanMode() throws NitfFormatException {
        return getValueAsInteger("MPLAN");
    }

    /**
     * Get the decoded description of the mission plan mode.
     *
     * This is a (sensor specific) decode of the mission plan mode encoded value.
     *
     * @return text description of the mission plan mode identifier
     * @throws NitfFormatException if there is a parsing issue
     */
    public final String getMissionPlanModeDecoded() throws NitfFormatException {
        String sensorId = getSensorIdentification();
        String mplan = this.getFieldValue("MPLAN");
        ACFTBMissionPlanMode mplanLookup = ACFTBMissionPlanMode.getInstance();
        if (mplanLookup.getKnownSensors().contains(sensorId)) {
            return mplanLookup.lookupDescription(sensorId, mplan);
        } else {
            // TODO: add test for SENSOR_ID_TYPE if there is ever anything for SAR
            return mplanLookup.lookupDescription("General Purpose, EO, IR, and Spectral", mplan);
        }
    }

    /**
     * Get the latitude component of the entry location (ENTLOC) for the image.
     *
     * From STDI-0002 Appendix E: "Where the image extends along an extended
     * path, as with SAR Search modes and EO-IR Wide Area Search modes, the
     * entry and exit locations are the specified latitude, longitude and
     * elevation above mean sea level (MSL) of the planned entry and exit points
     * on the centerline of the area contained within the NITF Image Segment.
     * Where the image is confined to the area about a single reference point,
     * as with Spot modes and Point Target modes, the entry fields contain the
     * specified reference point latitude/longitude/elevation, and the exit
     * fields are filled with spaces. For any persistent surveillance platform,
     * the definition of the Entry Location field values is different from the
     * published value. A persistent surveillance platform may define the ENTLOC
     * field as holding the location of the Persistent Surveillance stare point,
     * which may or may not be within the bounds of the individual frame image
     * camera extent."
     *
     * @return latitude component of entry location, or 0.0 if there is no valid
     * data in the ENTLOC field
     * @throws NitfFormatException if there is a parsing issue
     */
    public final double getEntryLocationLatitude() throws NitfFormatException {
        String entloc = getFieldValue("ENTLOC");
        return parseLatitudeFromLocation(entloc);
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

    /**
     * Get the longitude component of the entry location for the image.
     *
     * From STDI-0002 Appendix E: "Where the image extends along an extended
     * path, as with SAR Search modes and EO-IR Wide Area Search modes, the
     * entry and exit locations are the specified latitude, longitude and
     * elevation above mean sea level (MSL) of the planned entry and exit points
     * on the centerline of the area contained within the NITF Image Segment.
     * Where the image is confined to the area about a single reference point,
     * as with Spot modes and Point Target modes, the entry fields contain the
     * specified reference point latitude/longitude/elevation, and the exit
     * fields are filled with spaces. For any persistent surveillance platform,
     * the definition of the Entry Location field values is different from the
     * published value. A persistent surveillance platform may define the ENTLOC
     * field as holding the location of the Persistent Surveillance stare point,
     * which may or may not be within the bounds of the individual frame image
     * camera extent."
     *
     * @return longitude component of entry location, or 0.0 if there is no
     * valid data in the ENTLOC field.
     * @throws NitfFormatException if there is a parsing issue
     */
    public final double getEntryLocationLongitude() throws NitfFormatException {
        String entloc = getFieldValue("ENTLOC");
        return parseLongitudeFromLocation(entloc);
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
     * Check if the entry point location has (potentially) valid data.
     *
     * ENTLOC can be space filled, so getEntryPointLatitude() and
     * getEntryPointLongitude() may not have meaningful data.
     *
     * @return true if the entry point data appears valid, otherwise false
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final boolean entryPointHasData() throws NitfFormatException {
        return locationHasData(getFieldValue("ENTLOC"));
    }

    /**
     * Check whether the location accuracy field is valid.
     *
     * Location accuracy applies to entry point and exit point locations.
     *
     * @return true if the location accuracy has valid data, otherwise false.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final boolean locationAccuracyIsKnown() throws NitfFormatException {
        if (getLocationAccuracy() < MINIMUM_VALID_LOC_ACCY) {
            return false;
        }
        return true;
    }

    /**
     * Get the location accuracy field value.
     *
     * Location accuracy applies to entry point and exit point locations. Values
     * of 0 indicate unknown.
     *
     * @return 90% probable circular error in feet, or unknown.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final double getLocationAccuracy() throws NitfFormatException {
        String locaccy = getFieldValue("LOC_ACCY");
        return Double.parseDouble(locaccy);
    }

    /**
     * Get the entry elevation.
     *
     * This is the imaging operation entry point ground elevation. For
     * persistent surveillance platforms, this si the elevation of the stare
     * point, which may not fall within the field of view of the frame.
     *
     * The units are determined by ELEV_UNIT (see getUnitOfElevation().
     *
     * @return entry elevation in feet or metres, or null if not specified.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final Integer getEntryElevation() throws NitfFormatException {
        String entryElevation = getFieldValue("ENTELV");
        if (entryElevation.trim().length() == 0) {
            return null;
        }
        return Integer.parseInt(entryElevation);
    }

    /**
     * Get the elevation unit (entry and exit elevations).
     *
     * @return m for metres, f for feet, or space for not specified.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final String getUnitOfElevation() throws NitfFormatException {
        return getFieldValue("ELV_UNIT");
    }

    /**
     * Check if the exit point location has (potentially) valid data.
     *
     * EXITLOC can be space filled, so getExitPointLatitude() and
     * getExitPointLongitude() may not have meaningful data.
     *
     * @return true if the exit point data appears valid, otherwise false
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final boolean exitPointHasData() throws NitfFormatException {
        return locationHasData(getFieldValue("EXITLOC"));
    }

    /**
     * Get the latitude component of the exit location (EXITLOC) for the image.
     *
     * From STDI-0002 Appendix E: "Where the image extends along an extended
     * path, as with SAR Search modes and EO-IR Wide Area Search modes, the
     * entry and exit locations are the specified latitude, longitude and
     * elevation above mean sea level (MSL) of the planned entry and exit points
     * on the centerline of the area contained within the NITF Image Segment.
     * Where the image is confined to the area about a single reference point,
     * as with Spot modes and Point Target modes, the entry fields contain the
     * specified reference point latitude/longitude/elevation, and the exit
     * fields are filled with spaces. For any persistent surveillance platform,
     * the definition of the Entry Location field values is different from the
     * published value. A persistent surveillance platform may define the ENTLOC
     * field as holding the location of the Persistent Surveillance stare point,
     * which may or may not be within the bounds of the individual frame image
     * camera extent."
     *
     * @return latitude component of exit location, or 0.0 if there is no valid
     * data in the EXITLOC field
     * @throws NitfFormatException if there is a parsing issue
     */
    public final double getExitLocationLatitude() throws NitfFormatException {
        String exitloc = getFieldValue("EXITLOC");
        return parseLatitudeFromLocation(exitloc);
    }

    /**
     * Get the longitude component of the exit location for the image.
     *
     * From STDI-0002 Appendix E: "Where the image extends along an extended
     * path, as with SAR Search modes and EO-IR Wide Area Search modes, the
     * entry and exit locations are the specified latitude, longitude and
     * elevation above mean sea level (MSL) of the planned entry and exit points
     * on the centerline of the area contained within the NITF Image Segment.
     * Where the image is confined to the area about a single reference point,
     * as with Spot modes and Point Target modes, the entry fields contain the
     * specified reference point latitude/longitude/elevation, and the exit
     * fields are filled with spaces. For any persistent surveillance platform,
     * the definition of the Entry Location field values is different from the
     * published value. A persistent surveillance platform may define the ENTLOC
     * field as holding the location of the Persistent Surveillance stare point,
     * which may or may not be within the bounds of the individual frame image
     * camera extent."
     *
     * @return longitude component of exit location, or 0.0 if there is no
     * valid data in the EXITLOC field.
     * @throws NitfFormatException if there is a parsing issue
     */
    public final double getExitLocationLongitude() throws NitfFormatException {
        String exitloc = getFieldValue("EXITLOC");
        return parseLongitudeFromLocation(exitloc);
    }

    private boolean locationHasData(final String location) {
        // TODO: consider additional checks.
        return (!location.trim().equals(""));
    }

    /**
     * Get the exit elevation.
     *
     * This is the imaging operation exit point ground elevation.
     *
     * The units are determined by ELEV_UNIT (see getUnitOfElevation().
     *
     * @return exit elevation in feet or metres, or null if not specified.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final Integer getExitElevation() throws NitfFormatException {
        String exitElevation = getFieldValue("EXITELV");
        if (exitElevation.trim().length() == 0) {
            return null;
        }
        return Integer.parseInt(exitElevation);
    }

    /**
     * Get the True Map Angle.
     *
     * From STDI-0002 Appendix E: "SAR: In Search modes, the true map angle is
     * the angle between the ground projection of the line of sight from the
     * aircraft and the scene centerline. In Spot modes, the true map angle is
     * the angle, measured at the central reference point, between the ground
     * projection of the line of sight from the aircraft and a line parallel to
     * the aircraft desired track heading.  EO-IR: The true map angle is defined
     * in the NED coordinate system with origin at the aircraft (aircraft local
     * NED), as the angle between the scene entry line of sight and the
     * instantaneous aircraft track heading vector. The aircraft track-heading
     * vector is obtained by rotating the north unit vector of the aircraft
     * local NED coordinate system in the aircraft local NE plane through the
     * aircraft track-heading angle. The true map angle is measured in the
     * slanted plane containing the scene entry line of sight and the aircraft
     * track-heading vector. This angle is always positive."
     *
     * @return true map angle, or -360 (INVALID_TRUE_MAP_ANGLE constant) if not
     * specified.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final double getTrueMapAngle() throws NitfFormatException {
        String tmap = getFieldValue("TMAP");
        if (tmap.trim().equals("")) {
            return INVALID_TRUE_MAP_ANGLE;
        }
        return Double.parseDouble(tmap);
    }

    /**
     * Check whether this TRE has a valid True Map Angle.
     *
     * This can be space filled, so it may not be provided.
     * @return true if there is True map angle information, otherwise false.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final boolean hasTrueMapAngle() throws NitfFormatException {
        String tmap = getFieldValue("TMAP");
        return (!tmap.trim().equals(""));
    }

    /**
     * Test whether the Row Spacing is known.
     *
     * This can be measured in adjacent row pixels distance (feet or metres), or
     * in centre-to-centre distance in micro-radians. See getRowSpacingUnits()
     * for the units.
     *
     * @return true if the row spacing is known, otherwise false.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final boolean rowSpacingIsKnown() throws NitfFormatException {
        String rowSpacing = getFieldValue("ROW_SPACING");
        return (!rowSpacing.equals("0000000"));
    }

    /**
     * Get the row spacing.
     *
     * This can be measured in adjacent row pixels distance (feet or metres), or
     * in centre-to-centre distance in micro-radians. See getRowSpacingUnits()
     * for the units.
     *
     * @return row spacing, or 0 if not known.
     * @throws NitfFormatException if there as an issue during parsing.
     */
    public final double getRowSpacing() throws NitfFormatException {
        String rowSpacing = getFieldValue("ROW_SPACING");
        return Double.parseDouble(rowSpacing);
    }

    /**
     * Get the row spacing units.
     *
     * @return f for feet, m for metres, r for micro-radians, or u for unknown.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final String getRowSpacingUnits() throws NitfFormatException {
        return getFieldValue("ROW_SPACING_UNITS");
    }

    /**
     * Test whether the Column Spacing is known.
     *
     * This can be measured in adjacent column pixels distance (feet or metres),
     * or in centre-to-centre distance in micro-radians. See
     * getColumnSpacingUnits() for the units.
     *
     * @return true if the column spacing is known, otherwise false.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final boolean columnSpacingIsKnown() throws NitfFormatException {
        String columnSpacing = getFieldValue("COL_SPACING");
        return (!columnSpacing.equals("0000000"));
    }

    /**
     * Get the column spacing.
     *
     * This can be measured in adjacent column pixels distance (feet or metres),
     * or in centre-to-centre distance in micro-radians. See
     * getColumnSpacingUnits() for the units.
     *
     * @return column spacing, or 0 if not known.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final double getColumnSpacing() throws NitfFormatException {
        String columnSpacing = getFieldValue("COL_SPACING");
        return Double.parseDouble(columnSpacing);
    }

    /**
     * Get the column spacing units.
     *
     * @return f for feet, m for metres, r for micro-radians, or u for unknown.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final String getColumnSpacingUnits() throws NitfFormatException {
        return getFieldValue("COL_SPACING_UNITS");
    }

    /**
     * Check if focal length is available.
     *
     * The focal length may not be available or not applicable to the sensor.
     *
     * @return true if focal length is available, otherwise false.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final boolean focalLengthIsAvailable() throws NitfFormatException {
        String focalLength = getFieldValue("FOCAL_LENGTH");
        return (!"999.99".equals(focalLength));
    }

    /**
     * Get the focal length.
     *
     * This is the effective distance from the optical lens to the sensor
     * elements.
     *
     * @return focal length in centimetres, or 999.99 if not applicable or not
     * available.
     *
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final double getFocalLength() throws NitfFormatException {
        String focalLength = getFieldValue("FOCAL_LENGTH");
        return Double.parseDouble(focalLength);
    }

    /**
     * Check if the sensor serial number field is valid.
     *
     * This field can be space filled, so it may not be valid.
     *
     * @return true if the sensor serial number field is valid, or false if it
     * was space filled.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final boolean sensorSerialNumberIsValid() throws NitfFormatException {
        String sensorSerialNumber = getValueAsTrimmedString("SENSERIAL");
        return (sensorSerialNumber.length() > 0);
    }

    /**
     * Get the sensor serial number.
     *
     * @return sensor serial number, or 0 if not available.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final int getSensorSerialNumber() throws NitfFormatException {
        String sensorSerialNumber = getFieldValue("SENSERIAL");
        if (!sensorSerialNumberIsValid()) {
            return 0;
        }
        return Integer.parseInt(sensorSerialNumber);
    }

    /**
     * Get the airborne software version.
     *
     * @return version (vvvv.rr, vvvv = version, rr = revision) or space filled.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final String getAirborneSoftwareVersion() throws NitfFormatException {
        return getValueAsTrimmedString("ABSWVER");
    }

    /**
     * Get the calibration date for the sensor.
     *
     * Date that the sensor was last calibrated.
     *
     * @return the calibration date, or null if the date could not be parsed
     * @throws NitfFormatException if there is a parsing issue with the TRE.
     */
    public final LocalDate getCalibrationDate() throws NitfFormatException {
        return getValueAsLocalDate("CAL_DATE");
    }

    /**
     * Get the patch total.
     *
     * From STDI-0002 Appendix E: "Total Number of Patches contained in the
     * imaging operation. Generally this will also be consistent with the number
     * of PATCH and/or CMETAA extensions contained in an imaging operation. 0000
     * for EO-IR imagery. (0000 indicates no PATCH extension present.)
     *
     * For SAR Spot, this will be 0 or 1. For SAR Search, this will be 0 to
     * 9999. For EO-IR, this will be 0.
     *
     * @return patch total (0 means no PATCH extension).
     *
     * @throws NitfFormatException if there is a parsing issue with the TRE.
     */
    public final int getPatchTotal() throws NitfFormatException {
        return getValueAsInteger("PATCH_TOT");
    }

    /**
     * Get the Total number of MTIRP extensions contained in the file.
     *
     * Each extension (TRE) identified 1 to 999 moving targets. This field
     * identies the number of extensions (not the number of targets). This will
     * always be 0 for EO-IR imagery.
     *
     * @return the total number of moving target extension TREs.
     * @throws NitfFormatException if there is a parsing issue with the TRE.
     */
    public final int getMTITotal() throws NitfFormatException {
        return getValueAsInteger("MTI_TOT");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ValidityResult getValidity() throws NitfFormatException {
        // TODO: add more validity checks
        return new ValidityResult();
    }
}
