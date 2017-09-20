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

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreGroup;

/**
 * Wrapper for the Motion Imagery Segment (MTIMSA) TRE.
 */
public class MTIMSA extends TreWrapper {

    private static final String TAG_NAME = "MTIMSA";
    private static final BigInteger NANOSECONDS_IN_ONE_SECOND = BigInteger.valueOf(1000000000);

    /**
     * Create a MTIMSA TRE wrapper from an existing TRE.
     *
     * @param tre the TRE to wrap. Must match the MTIMSA tag.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public MTIMSA(final Tre tre) throws NitfFormatException {
        super(tre, TAG_NAME);
    }

    /**
     * Get the image segment index.
     *
     * From MIE4NITF Version 1.1: IMAGE_SEG_INDEX – The index of this NITF Image
     * Segment within the NITF File. This field is provided as protection
     * against a legacy tool that modifies the NITF File by adding or removing
     * NITF Image Segments without updating the mapping of cameras and temporal
     * blocks to NITF Image Segments specified in the MTIMFA TRE (defined in
     * Section 5.9.3.5). Tools that understand MIE4NITF shall properly update
     * both this TRE and the MTIMFA TRE whenever NITF Image Segments are added
     * or removed from the NITF File.
     *
     * @return image segment index of the segment containing this camera and
     * temporal block.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final int getImageSegmentIndex() throws NitfFormatException {
        return getValueAsInteger("IMAGE_SEG_INDEX");
    }

    /**
     * Whether the geo-coordinates for this image segment are static.
     *
     * From MIE4NITF Version 1.1: GEOCOORDS_STATIC – A flag indicating if the
     * geo-coordinates associated with this NITF Image Segment may be considered
     * static or unchanging across the MI frames contained in this Image
     * Segment. A value of 00 indicates the geo-coordinates are not static and a
     * value of 99 indicates they are. All other values are reserved for future
     * use. Note the definition of “static” is at the discretion of the data
     * producer. Small amounts of scene jitter may be ignored and the scene
     * considered static in some use cases. In others, the same amount of jitter
     * may not be considered static.
     *
     * @return true if coordinates are static ("99"), otherwise false.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final boolean geoCoordinatesAreStatic() throws NitfFormatException {
        return getFieldValue("GEOCOORDS_STATIC").equals("99");
    }

    /**
     * Get the phenomenological layer for this image segment.
     *
     * From MIE4NITF Version 1.1: LAYER_ID – The ID token of the
     * phenomenological layer associated with the camera for which this NITF
     * Image Segment contains MI data.
     *
     * @return the layer identifier (usually a GUID), or all spaces for a
     * quicklook image
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final String getLayerId() throws NitfFormatException {
        return getFieldValue("LAYER_ID");
    }

    /**
     * Get the camera set index for this image segment.
     *
     * From MIE4NITF Version 1.1: CAMERA_SET_INDEX – The index of the camera set
     * associated with the camera for which this NITF Image Segment contains MI
     * data.
     *
     * @return the camera set index for this image segment, or 0 for quicklook
     * image
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final int getCameraSetIndex() throws NitfFormatException {
        return getValueAsInteger("CAMERA_SET_INDEX");
    }

    /**
     * Get the camera identifier.
     *
     * From MIE4NITF Version 1.1: CAMERA_ID – The UUID of the camera for which
     * this NITF Image Segment contains MI data.
     *
     * @return camera UUID, or all spaces for a quicklook image.
     * @throws NitfFormatException if there was an issue during parsing
     */
    public final String getCameraIdentifier() throws NitfFormatException {
        return getFieldValue("CAMERA_ID");
    }

    /**
     * Get the time interval index for this image segment.
     *
     * From MIE4NITF Version 1.1: TIME_INTERVAL_INDEX – The index of the time
     * interval in which the temporal block corresponding to this NITF Image
     * Segment is contained.
     *
     * @return time interval index, or 0 for a quicklook image.
     * @throws NitfFormatException if there was an issue during parsing
     */
    public final int getTimeIntervalIndex() throws NitfFormatException {
        return getValueAsInteger("TIME_INTERVAL_INDEX");
    }

    /**
     * Get the temporal block index for this image segment.
     *
     * From MIE4NITF Version 1.1: TEMP_BLOCK_INDEX – The index of the temporal
     * block corresponding to this NITF Image Segment.
     *
     * @return the temporal block index, or 0 for a quicklook image.
     * @throws NitfFormatException if there was an issue during parsing
     */
    public final int getTemporalBlockIndex() throws NitfFormatException {
        return getValueAsInteger("TEMP_BLOCK_INDEX");
    }

    /**
     * Get the nominal frame rate for this image segment.
     *
     * From MIE4NITF Version 1.1: NOMINAL_FRAME_RATE – The nominal frame rate of
     * the data contained in this NITF Image Segment, specified in frames per
     * second. If the nominal frame rate is not known or the collection is
     * driven by asynchronous external events, the value of the field may be
     * “NaN” followed by BCS-A spaces, see Table 10.
     *
     * @return the nominal frame rate, or -1.0 if there is no valid frame rate.
     * @throws NitfFormatException if there was an issue during parsing
     */
    public final double getNominalFrameRate() throws NitfFormatException {
        if (getFieldValue("NOMINAL_FRAME_RATE").trim().equals("NaN")) {
            return -1.0;
        }
        return getValueAsDouble("NOMINAL_FRAME_RATE");
    }

    /**
     * Get the reference frame number.
     *
     * From MIE4NITF Version 1.1: REFERENCE_FRAME_NUM – The absolute reference
     * frame number of the first frame in this temporal block for this camera,
     * as specified by the collection system. If the collection system does not
     * use absolute frame numbers, then this field is BCS-A spaces filled. The
     * absolute frame number for any frame in this NITF Image Segment, if a
     * reference frame number is specified, is equal to REFERENCE_FRAME_NUM + n
     * – 1, where n is the one-based index of that frame in this NITF Image
     * Segment.
     *
     * @return the reference frame number, or -1 if there are no absolute frame
     * numbers.
     * @throws NitfFormatException if there was an issue during parsing
     */
    public final int getReferenceFrameNumber() throws NitfFormatException {
        if (getValueAsTrimmedString("REFERENCE_FRAME_NUM").isEmpty()) {
            return -1;
        }
        return getValueAsInteger("REFERENCE_FRAME_NUM");
    }

    /**
     * Get the base timestamp for this temporal block.
     *
     * From MIE4NITF Version 1.1: BASE_TIMESTAMP – A base timestamp from which
     * the timestamps for each of the individual frames in this temporal block
     * for this camera are calculated (see equation (1)). Timestamps are always
     * correspond to the start of collection of a frame, as defined by the
     * collection system. Only the proper level of meaningful precision should
     * be used in the timestamps, see Table 9.
     *
     * @return the base timestamp.
     * @throws NitfFormatException if there was an issue during parsing
     */
    public final ZonedDateTime getBaseTimestamp() throws NitfFormatException {
        return getValueAsZonedDateTime("BASE_TIMESTAMP", TIMESTAMP_NANO_FORMATTER);
    }

    /**
     * Get the delta time multiplier.
     *
     * From MIE4NITF Version 1.1: DT_MULTIPLIER – The number of nanoseconds
     * equal to 1 delta time unit.
     *
     * The number of nanoseconds equal to one time unit, or minimum “delta time”
     * that can be expressed between frames.
     *
     * Note that this can potentially overflow a long integer when converting.
     *
     * @return delta time in nanoseconds per unit
     * @throws NitfFormatException if there was an issue during parsing
     */
    public final BigInteger getDeltaTimeMultiplier() throws NitfFormatException {
        return getValueAsBigInteger("DT_MULTIPLIER");
    }

    /**
     * Get the number of frames in this image segment.
     *
     * From MIE4NITF Version 1.1: NUM_FRAMES – The number of frames in this NITF
     * Image Segment for this camera and temporal block.
     *
     * @return the number of frames
     * @throws NitfFormatException if there was an issue during parsing
     */
    public final long getNumberOfFrames() throws NitfFormatException {
        return getValueAsLongInteger("NUMBER_FRAMES");
    }

    /**
     * Get the number of delta time entries.
     *
     * From MIE4NITF Version 1.1: NUMBER_DT – The number of delta time unit
     * values contained in this NITF Image Segment for this camera and temporal
     * block. The value of this field must be either equal to NUM_FRAMES or must
     * be 1. If the value is equal to NUM_FRAMES, then a separate DTn value is
     * specified for each frame. If the value is 1, then only a single DTn value
     * is specified, which applies to every frame. This indicates that the
     * frames for this camera in this temporal block are evenly spaced
     * temporally.
     *
     * @return the number of delta times
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final long getNumberOfDeltaTimes() throws NitfFormatException {
        return getValueAsLongInteger("NUMBER_DT");
    }

    /**
     * Get a specific delta time.
     *
     * From MIE4NITF Version 1.1: DTn – The number of delta time units between
     * this frame and the previous frame. The number of DTn values must be equal
     * to the value of the NUMBER_DT field. The values are expressed as fixed
     * length unsigned integers ranging from one to eight bytes in length.
     * DT_SIZE determines the number of bytes used for the DTn values. For a
     * given MTIMSA TRE, the number of bytes for each DTn is fixed, but the
     * number of bytes used for DTn can change between different MTIMSA TREs.
     *
     * @param deltaItemIndex the zero-based index of the delta time to retrieve.
     * @return the delta time.
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final BigInteger getDeltaTime(final long deltaItemIndex) throws NitfFormatException {
        List<TreGroup> deltaTimes = getTRE().getGroupListEntry("DELTA_TIME").getGroups();
        TreGroup deltaTimeGroup = deltaTimes.get((int) deltaItemIndex);
        return deltaTimeGroup.getBigIntegerValue("DT");
    }

    /**
     * Get the timestamp for a specified frame.
     *
     * @param frameIndex the frame index (one-based) to get the timestamp for.
     * @return UTC date-time for the frame
     * @throws NitfFormatException if there was an issue during parsing.
     */
    public final ZonedDateTime getFrameTime(final long frameIndex) throws NitfFormatException {
        if (getNumberOfDeltaTimes() == 1L) {
            // Equal time between frames
            ZonedDateTime timeStamp = getBaseTimestamp();
            BigInteger nanoseconds = getDeltaTime(0).multiply(getDeltaTimeMultiplier()).multiply(BigInteger.valueOf(frameIndex));
            return addNanoSeconds(nanoseconds, timeStamp);
        } else {
            ZonedDateTime timeStamp = getBaseTimestamp();
            for (long i = 0; i < frameIndex; ++i) {
                BigInteger nanoseconds = getDeltaTime(i).multiply(getDeltaTimeMultiplier());
                timeStamp = addNanoSeconds(nanoseconds, timeStamp);
            }
            return timeStamp;
        }
    }

    private ZonedDateTime addNanoSeconds(final BigInteger nanoseconds, final ZonedDateTime timeStamp) {
        BigInteger seconds = nanoseconds.divide(NANOSECONDS_IN_ONE_SECOND);
        BigInteger nanoRemainder = nanoseconds.mod(NANOSECONDS_IN_ONE_SECOND);
        return timeStamp.plusSeconds(seconds.longValueExact()).plusNanos(nanoRemainder.longValueExact());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ValidityResult getValidity() throws NitfFormatException {
        ValidityResult validity = new ValidityResult();
        if ((getNumberOfDeltaTimes() != 1L) && (getNumberOfDeltaTimes() != getNumberOfFrames())) {
            validity.setValidityStatus(ValidityResult.ValidityStatus.NOT_VALID);
            validity.setValidityResultDescription("Number of delta times must equal number of frames, or be 1.");
        }
        return validity;
    }

}
