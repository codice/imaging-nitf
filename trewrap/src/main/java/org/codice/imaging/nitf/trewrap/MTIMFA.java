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
import org.codice.imaging.nitf.core.tre.TreGroup;

/**
 * Wrapper for the Motion Imagery File TRE (MTIMFA).
 *
 * From MIE4NITF Section 5.9.3.5: The motion imaging file TRE specifies how the
 * data for all of the cameras in a phenomenological layer for a single camera
 * set and a single time interval is subdivided into NITF Image Segments in the
 * NITF file containing the MTIMFA TRE. This TRE is placed in the NITF File
 * Header of each file and the contents of the TRE are specific to that file.
 * For each phenomenological layer, there shall be one instance of this TRE in
 * the NITF File Header for that layer if the number of cameras in that
 * phenomenological layer is greater than one; if the number of temporal blocks
 * for any camera is greater than one; or if the collection contains multiple
 * phenomenological layers. However, data producers are strongly encouraged to
 * populate this TRE whenever possible to allow data consumers to locate the
 * data for the collection within the NITF File. The MTIMFA TRE is not placed in
 * the Manifest File.
 */
public class MTIMFA extends TreWrapper {

    private static final String TAG_NAME = "MTIMFA";

    /**
     * Create a MTIMFA TRE wrapper from an existing TRE.
     *
     * @param tre the TRE to wrap. Must match the MTIMFA tag.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public MTIMFA(final Tre tre) throws NitfFormatException {
        super(tre, TAG_NAME);
    }

    /**
     * Get the layer identifier.
     *
     * From MIE4NITF Section 5.9.3.5: LAYER_ID – A 36 character string
     * identifying the phenomenological layer in the collection for which the
     * mapping between cameras, temporal blocks and NITF Image Segments are
     * specified by this TRE. The 36 character field is long enough for a UUID.
     * It is recommended that UUIDs be used but this is not mandatory. The field
     * may not be all spaces and shall be left justified and padded on the right
     * with BCS-A spaces if needed.
     *
     * @return layer identifier, with trailing whitespace (if any) removed.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getLayerId() throws NitfFormatException {
        return getFieldValue("LAYER_ID").trim();
    }

    /**
     * Get the camera set index for the cameras in this NITF file.
     *
     * From MIE4NITF Section 5.9.3.5: CAMERA_SET_INDEX – The index of the camera
     * set containing the cameras in this phenomenological layer in the NITF
     * file containing this TRE.
     *
     * @return camera set index
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getCameraSetIndex() throws NitfFormatException {
        return getValueAsInteger("CAMERA_SET_INDEX");
    }

    /**
     * Get the time interval index corresponding to this NITF file.
     *
     * From MIE4NITF Section 5.9.3.5: TIME_INTERVAL_INDEX – The index of the
     * time interval corresponding to the NITF file containing this TRE and the
     * temporal blocks of MI data.
     *
     * @return time interval index.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getTimeIntervalIndex() throws NitfFormatException {
        return getValueAsInteger("TIME_INTERVAL_INDEX");
    }

    /**
     * Get the number of cameras.
     *
     * From MIE4NITF Section 5.9.3.5: NUM_CAMERAS_DEFINED – The number of
     * cameras in the camera set in the specified phenomenological layer for
     * which this particular NITF File contains temporal blocks and MI data. If
     * a particular camera does not have MI data for the time interval
     * represented by this NITF file, then that camera is not counted by this
     * field.
     *
     * This specifies the valid camera indexes (0 ... NUM_CAMERAS_DEFINED - 1).
     *
     * @return the number of cameras in the camera set.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getNumberOfCameras() throws NitfFormatException {
        return getValueAsInteger("NUM_CAMERAS_DEFINED");
    }

    /**
     * Get the camera ID (UUID) for the specified camera index.
     *
     * From MIE4NITF Section 5.9.3.5: CAMERA_IDn – The identifier for nth camera
     * in this phenomenological layer in this camera set. The value is a UUID
     * and is specific to the particular camera, not just the type, mission or
     * model associated with the camera.
     *
     * @param cameraIndex the camera index (zero based)
     * @return the camera identification.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getCameraID(final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCamera(cameraIndex);
        return camera.getFieldValue("CAMERA_ID");
    }

    /**
     * Get the number of temporal blocks for the specified camera index.
     *
     * From MIE4NITF Section 5.9.3.5: NUM_TEMP_BLOCKSn – The number of temporal
     * blocks in this NITF File for the nth camera. This value might be greater
     * than the actual number of temporal blocks for the camera. For example,
     * the collection system may have allocated space for a certain number of
     * temporal blocks, only to encounter a malfunction midway through the
     * temporal block. Allowing this number to be greater than the actual number
     * of blocks allows the data producer to finish production of the file
     * without relocating collected data. Data producers must space fill all
     * fields in this TRE for an unused temporal block. Data consumers that
     * discover a single space filled field for a temporal block may consider
     * that temporal block and any subsequent temporal blocks for this camera
     * and time interval as unused. All unused temporal blocks must be in a
     * contiguous group at the end of the list.
     *
     * This specifies the valid temporal block indexes (0 ... NUM_TEMP_BLOCKS -
     * 1).
     *
     * @param cameraIndex the camera index (zero based)
     * @return the number of temporal blocks
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getNumTemporalBlocks(final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCamera(cameraIndex);
        return camera.getIntValue("NUM_TEMP_BLOCKS");
    }

    /**
     * Get start timestamp for temporal block.
     *
     * From MIE4NITF Section 5.9.3.5: START_TIMESTAMPnm – The start time of mth
     * temporal block for nth camera, or BCS-A spaces if this temporal block
     * definition is unused.
     *
     * @param cameraIndex the camera index (zero based)
     * @param temporalBlockIndex the temporal block index (zero based)
     * @return the start timestamp
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final ZonedDateTime getStartTimestamp(final int cameraIndex, final int temporalBlockIndex) throws NitfFormatException {
        TreGroup tempBlock = getTemporalBlock(cameraIndex, temporalBlockIndex);
        return parseAsZonedDateTime(TIMESTAMP_NANO_FORMATTER, tempBlock.getFieldValue("START_TIMESTAMP"));
    }

    /**
     * Get end timestamp for temporal block.
     *
     * From MIE4NITF Section 5.9.3.5: END_TIMESTAMPnm – The start time of mth
     * temporal block for nth camera, or BCS-A spaces if this temporal block
     * definition is unused. The end timestamp is an exclusive limit to the
     * range; for any temporal block (n,m), the end timestamp must be greater
     * than the start timestamp of the last frame for camera n in temporal block
     * m and not greater than the start time of the first frame for Camera n in
     * temporal block m+1.
     *
     * @param cameraIndex the camera index (zero based)
     * @param temporalBlockIndex the temporal block index (zero based)
     * @return the end timestamp
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final ZonedDateTime getEndTimestamp(final int cameraIndex, final int temporalBlockIndex) throws NitfFormatException {
        TreGroup tempBlock = getTemporalBlock(cameraIndex, temporalBlockIndex);
        return parseAsZonedDateTime(TIMESTAMP_NANO_FORMATTER, tempBlock.getFieldValue("END_TIMESTAMP"));
    }

    /**
     * Get image segment for temporal block.
     *
     * From MIE4NITF Section 5.9.3.5: IMAGE_SEG_INDEXnm – The index of the NITF
     * Image Segment in this NITF file that contains the mth temporal block of
     * MI data for the nth camera, or BCS-A spaces if this temporal block
     * definition is unused.
     *
     * @param cameraIndex the camera index (zero based)
     * @param temporalBlockIndex the temporal block index (zero based)
     * @return the image segment index
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getImageSegmentIndex(final int cameraIndex, final int temporalBlockIndex) throws NitfFormatException {
        TreGroup tempBlock = getTemporalBlock(cameraIndex, temporalBlockIndex);
        return tempBlock.getIntValue("IMAGE_SEG_INDEX");
    }

    /**
     * Test if the temporal block data is filled or not.
     *
     * @param cameraIndex the camera index (zero based)
     * @param temporalBlockIndex the temporal block index (zero based)
     * @return true if all of the temporal block fields are not space-filled, otherwise false.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final boolean hasValidTimestamps(final int cameraIndex, final int temporalBlockIndex) throws NitfFormatException {
        TreGroup temporalBlock = getTemporalBlock(cameraIndex, temporalBlockIndex);
        return ((!temporalBlock.getFieldValue("START_TIMESTAMP").trim().isEmpty())
                && (!temporalBlock.getFieldValue("END_TIMESTAMP").trim().isEmpty())
                && (!temporalBlock.getFieldValue("IMAGE_SEG_INDEX").trim().isEmpty()));
    }

    private TreGroup getCamera(final int cameraIndex) throws NitfFormatException {
        TreGroup camera = mTre.getGroupListEntry("CAMERAS").getGroups().get(cameraIndex);
        return camera;
    }

    private TreGroup getTemporalBlock(final int cameraIndex, final int temporalBlockIndex) throws NitfFormatException {
        TreGroup camera = getCamera(cameraIndex);
        TreGroup tempBlock = camera.getGroupListEntry("TEMPORAL_BLOCKS").getGroups().get(temporalBlockIndex);
        return tempBlock;
    }

    @Override
    public final ValidityResult getValidity() throws NitfFormatException {
        ValidityResult validity = new ValidityResult();
        return validity;
    }
}
