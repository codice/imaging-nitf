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

import java.util.List;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreGroup;

/**
 * Wrapper for the Camera Set Definition TRE (CAMSDA) TRE.
 */
public class CAMSDA extends TreWrapper {

    private static final String TAG_NAME = "CAMSDA";

    /**
     * Create a CAMSDA TRE wrapper from an existing TRE.
     *
     * @param tre the TRE to wrap. Must match the CAMSDA tag.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public CAMSDA(final Tre tre) throws NitfFormatException {
        super(tre, TAG_NAME);
    }

    @Override
    public final ValidityResult getValidity() throws NitfFormatException {
        ValidityResult validity = new ValidityResult();
        return validity;
    }

    /**
     * Get the number of camera sets in the collection (NUM_CAMERA_SETS).
     *
     * From MIE4NITF Version 1.1: NUM_CAMERA_SETS – The number of camera sets in
     * the collection.
     *
     * Note that not all of those camera sets are necessarily in this TRE
     * instance.
     *
     * @return the number of camera sets in the collection.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getNumberOfCameraSetsInCollection() throws NitfFormatException {
        return getValueAsInteger("NUM_CAMERA_SETS");
    }

    /**
     * Get the number of camera sets in this TRE instance
     * (NUM_CAMERA_SETS_IN_TRE).
     *
     * From MIE4NITF Version 1.1: NUM_CAMERA_SETS_IN_TRE– The number of camera
     * sets defined in this instance of the CAMSDA TRE. Due to the length limits
     * of NITF TREs, only a portion of the camera set data may fit within a
     * single instance of the CAMSDA TRE. If the total size of the camera set
     * data exceeds the NITF TRE limit, then multiple CAMSDA TREs are used to
     * describe the collection.
     *
     * @return the number of camera sets in the TRE.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getNumberOfCameraSetsInTRE() throws NitfFormatException {
        return getValueAsInteger("NUM_CAMERA_SETS_IN_TRE");
    }

    /**
     * Get the index of the first camera set in this TRE
     * (FIRST_CAMERA_SET_IN_TRE).
     *
     * From MIE4NITF Version 1.1: FIRST_CAMERA_SET_IN_TRE– The index of the
     * first camera set defined in this instance of the CAMSDA TRE.
     *
     * @return the index of the first camera set in the TRE.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getIndexOfFirstCameraSetInTRE() throws NitfFormatException {
        return getValueAsInteger("FIRST_CAMERA_SET_IN_TRE");
    }

    /**
     * Get the number of camera in a camera set in this TRE.
     *
     * This provides the number of cameras in the camera set as a zero base
     * index for this TRE. So if the collection has 100 entries, and this TRE
     * has the last 20 (i.e. 79-99 in a zero base), the valid camera set index
     * range is 0 to 19. If this is the first (or only) CAMSDA TRE, this is the
     * same as getNumberOfCamerasInCameraSet().
     *
     * @param cameraSetIndex zero base camera set index for this TRE.
     * @return number of cameras in the specified camera set.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getNumberOfCamerasInCameraSetForTRE(final int cameraSetIndex) throws NitfFormatException {
        TreGroup cameraSet = getCameraSetTreBased(cameraSetIndex);
        return cameraSet.getIntValue("NUM_CAMERAS_IN_SET");
    }

    /**
     * Get the number of camera in a camera set in the collection.
     *
     * This provides the number of cameras in the camera set as a zero base
     * index for the collection. So if the collection has 100 entries, and this
     * TRE has the last 20 (i.e. 79-99 in a zero base), the valid camera set
     * index range is 79 to 99. If this is the first (or only) CAMSDA TRE, this
     * is the same as getNumberOfCamerasInCameraSetForTRE().
     *
     * @param cameraSetIndex zero base camera set index for the collection.
     * @return number of cameras in the specified camera set.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getNumberOfCamerasInCameraSet(final int cameraSetIndex) throws NitfFormatException {
        TreGroup cameraSet = getCameraSetCollectionBased(cameraSetIndex);
        return cameraSet.getIntValue("NUM_CAMERAS_IN_SET");
    }

    /**
     * Get the camera ID for a given camera in a given camera set in the
     * collection.
     *
     * From MIE4NITF Version 1.1: CAMERA_IDnm – The identifier for mth camera in
     * the nth camera set. The value is a UUID and is specific to the particular
     * camera, not just the type, mission or model associated with the camera.
     * UUIDs are to be given in their “8-4-4-4-12” or hexadecimal format (ITU-T
     * X.667).
     *
     * The camera set index is a zero base index for the collection. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 79 to 99. If this is the
     * first (or only) CAMSDA TRE, this is the same as
     * getCameraIDForCameraSetInTRE().
     *
     * @param cameraSetIndex zero base camera set index for the collection.
     * @param cameraIndex zero base camera index
     * @return the camera ID (UUID).
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getCameraID(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraCollectionBased(cameraSetIndex, cameraIndex);
        return camera.getFieldValue("CAMERA_ID");
    }

    /**
     * Get the camera ID for a given camera in a given camera set in the
     * collection.
     *
     * From MIE4NITF Version 1.1: CAMERA_IDnm – The identifier for mth camera in
     * the nth camera set. The value is a UUID and is specific to the particular
     * camera, not just the type, mission or model associated with the camera.
     * UUIDs are to be given in their “8-4-4-4-12” or hexadecimal format (ITU-T
     * X.667).
     *
     * The camera set index is a zero base index for the TRE. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 0 to 19. If this is the
     * first (or only) CAMSDA TRE, this is the same as getCameraID().
     *
     * @param cameraSetIndex zero base camera set index for the TRE.
     * @param cameraIndex zero base camera index
     * @return the camera ID (UUID).
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getCameraIDForCameraSetInTRE(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraTreBased(cameraSetIndex, cameraIndex);
        return camera.getFieldValue("CAMERA_ID");
    }

    /**
     * Get the camera description for a given camera in a given camera set in
     * the collection.
     *
     * From MIE4NITF Version 1.1: CAMERA_DESCnm – A human readable description
     * for the mth camera in the nth camera set. This field may be filled with
     * BCS-A spaces. The camera descriptions need not be unique across multiple
     * collection systems or within a collection. This description is not a
     * replacement for the CAMERA_IDnm field. If there is a distinct difference
     * between the data from different cameras, data providers are encouraged to
     * set the CAMERA_DESCnm field to allow a human user to differentiate
     * between the cameras in a meaningful way.
     *
     * The camera set index is a zero base index for the collection. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 79 to 99. If this is the
     * first (or only) CAMSDA TRE, this is the same as
     * getCameraDescriptionForCameraSetInTRE().
     *
     * @param cameraSetIndex zero base camera set index for the collection.
     * @param cameraIndex zero base camera index
     * @return the camera description, with trailing whitespace removed.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getCameraDescription(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraCollectionBased(cameraSetIndex, cameraIndex);
        return camera.getFieldValue("CAMERA_DESC").trim();
    }

    /**
     * Get the camera description for a given camera in a given camera set in
     * the TRE.
     *
     * From MIE4NITF Version 1.1: CAMERA_DESCnm – A human readable description
     * for the mth camera in the nth camera set. This field may be filled with
     * BCS-A spaces. The camera descriptions need not be unique across multiple
     * collection systems or within a collection. This description is not a
     * replacement for the CAMERA_IDnm field. If there is a distinct difference
     * between the data from different cameras, data providers are encouraged to
     * set the CAMERA_DESCnm field to allow a human user to differentiate
     * between the cameras in a meaningful way.
     *
     * The camera set index is a zero base index for the TRE. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 0 to 19. If this is the
     * first (or only) CAMSDA TRE, this is the same as getCameraDescription().
     *
     * @param cameraSetIndex zero base camera set index for the TRE.
     * @param cameraIndex zero base camera index
     * @return the camera description, with trailing whitespace removed.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getCameraDescriptionForCameraSetInTRE(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraTreBased(cameraSetIndex, cameraIndex);
        return camera.getFieldValue("CAMERA_DESC").trim();
    }

    /**
     * Get the phenomenology layer identifier for a given camera in a given
     * camera set in the collection.
     *
     * From MIE4NITF Version 1.1: LAYER_IDnm – A 36 character string identifying
     * the phenomenological layer for mth camera in the nth camera set. The 36
     * character field is long enough for a UUID. It is recommended that UUIDs
     * be used but this is not mandatory. The field may not be all spaces and
     * shall be left justified and padded on the right with BCS-A spaces if
     * needed.
     *
     * The camera set index is a zero base index for the collection. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 79 to 99. If this is the
     * first (or only) CAMSDA TRE, this is the same as
     * getLayerIDForCameraSetInTRE().
     *
     * @param cameraSetIndex zero base camera set index for the collection.
     * @param cameraIndex zero base camera index
     * @return the layer ID, with trailing whitespace (if any) removed.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getLayerID(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraCollectionBased(cameraSetIndex, cameraIndex);
        return camera.getFieldValue("LAYER_ID").trim();
    }

    /**
     * Get the phenomenology layer identifier for a given camera in a given
     * camera set in this TRE.
     *
     * From MIE4NITF Version 1.1: LAYER_IDnm – A 36 character string identifying
     * the phenomenological layer for mth camera in the nth camera set. The 36
     * character field is long enough for a UUID. It is recommended that UUIDs
     * be used but this is not mandatory. The field may not be all spaces and
     * shall be left justified and padded on the right with BCS-A spaces if
     * needed.
     *
     * The camera set index is a zero base index for the TRE. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 0 to 19. If this is the
     * first (or only) CAMSDA TRE, this is the same as getLayerID().
     *
     * @param cameraSetIndex zero base camera set index for the TRE.
     * @param cameraIndex zero base camera index
     * @return the layer ID, with trailing whitespace (if any) removed.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getLayerIDForCameraSetInTRE(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraTreBased(cameraSetIndex, cameraIndex);
        return camera.getFieldValue("LAYER_ID").trim();
    }

    /**
     * Get the image display level for a given camera in a given camera set in
     * the collection.
     *
     * From MIE4NITF Version 1.1: IDLVLnm – A 3 character BCS-N positive integer
     * containing the image display level value for the mth camera in the nth
     * cameras set. This value is copied from the IDLVL field in the NITF Image
     * Segment Subheader that contains the frames associated with this camera.
     *
     * The camera set index is a zero base index for the collection. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 79 to 99. If this is the
     * first (or only) CAMSDA TRE, this is the same as
     * getImageDisplayLevelForCameraSetInTRE().
     *
     * @param cameraSetIndex zero base camera set index for the collection.
     * @param cameraIndex zero base camera index
     * @return the image display level.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getImageDisplayLevel(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraCollectionBased(cameraSetIndex, cameraIndex);
        return camera.getIntValue("IDLVL");
    }

    /**
     * Get the image display level for a given camera in a given camera set in
     * this TRE.
     *
     * From MIE4NITF Version 1.1: IDLVLnm – A 3 character BCS-N positive integer
     * containing the image display level value for the mth camera in the nth
     * cameras set. This value is copied from the IDLVL field in the NITF Image
     * Segment Subheader that contains the frames associated with this camera.
     *
     * The camera set index is a zero base index for the TRE. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 0 to 19. If this is the
     * first (or only) CAMSDA TRE, this is the same as getImageDisplayLevel().
     *
     * @param cameraSetIndex zero base camera set index for the TRE.
     * @param cameraIndex zero base camera index
     * @return the image display level.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getImageDisplayLevelForCameraSetInTRE(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraTreBased(cameraSetIndex, cameraIndex);
        return camera.getIntValue("IDLVL");
    }

    /**
     * Get the image attachment level for a given camera in a given camera set
     * in the collection.
     *
     * From MIE4NITF Version 1.1: IALVLnm – A 3 character BCS-N positive integer
     * containing the image attachment level value for the mth camera in the nth
     * cameras set. This value is copied from the IALVL field in the NITF Image
     * Segment Subheader that contains the frames associated with this camera.
     *
     * The camera set index is a zero base index for the collection. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 79 to 99. If this is the
     * first (or only) CAMSDA TRE, this is the same as
     * getImageAttachmentLevelForCameraSetInTRE().
     *
     * @param cameraSetIndex zero base camera set index for the collection.
     * @param cameraIndex zero base camera index
     * @return the image attachment level.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getImageAttachmentLevel(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraCollectionBased(cameraSetIndex, cameraIndex);
        return camera.getIntValue("IALVL");
    }

    /**
     * Get the image attachment level for a given camera in a given camera set
     * in this TRE.
     *
     * From MIE4NITF Version 1.1: IALVLnm – A 3 character BCS-N positive integer
     * containing the image attachment level value for the mth camera in the nth
     * cameras set. This value is copied from the IALVL field in the NITF Image
     * Segment Subheader that contains the frames associated with this camera.
     *
     * The camera set index is a zero base index for the TRE. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 0 to 19. If this is the
     * first (or only) CAMSDA TRE, this is the same as
     * getImageAttachmentLevel().
     *
     * @param cameraSetIndex zero base camera set index for this TRE.
     * @param cameraIndex zero base camera index
     * @return the image attachment level.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getImageAttachmentLevelForCameraSetInTRE(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraTreBased(cameraSetIndex, cameraIndex);
        return camera.getIntValue("IALVL");
    }

    /**
     * Get the image location row for a given camera in a given camera set in
     * the collection.
     *
     * From MIE4NITF Version 1.1: ILOCnm – A 10 character BCS-N positive integer
     * containing the image location value for the mth camera in the nth cameras
     * set at the original spatial resolution of the data (Rset = 0, i.e. IMAG =
     * 1.0). This value may differ from the ILOC field in the NITF Image Segment
     * Subheader that contains the frames associated with this camera, if the
     * camera data has been magnified or reduced relative to the original data
     * (i.e. IMAG ≠ 1.0).
     *
     * The camera set index is a zero base index for the collection. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 79 to 99.
     *
     * @param cameraSetIndex zero base camera set index for the collection.
     * @param cameraIndex zero base camera index
     * @return the row part of the image location.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getImageLocationRow(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraCollectionBased(cameraSetIndex, cameraIndex);
        String iloc = camera.getFieldValue("ILOC");
        return Integer.parseInt(iloc.substring(0, "RRRRR".length()));
    }
    /**
     * Get the image location row for a given camera in a given camera set in
     * this TRE.
     *
     * From MIE4NITF Version 1.1: ILOCnm – A 10 character BCS-N positive integer
     * containing the image location value for the mth camera in the nth cameras
     * set at the original spatial resolution of the data (Rset = 0, i.e. IMAG =
     * 1.0). This value may differ from the ILOC field in the NITF Image Segment
     * Subheader that contains the frames associated with this camera, if the
     * camera data has been magnified or reduced relative to the original data
     * (i.e. IMAG ≠ 1.0).
     *
     * The camera set index is a zero base index for the TRE. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 0 to 19. If this is the
     * first (or only) CAMSDA TRE, this is the same as getImageLocationRow().
     *
     * @param cameraSetIndex zero base camera set index for this TRE.
     * @param cameraIndex zero base camera index
     * @return the row part of the image location.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getImageLocationRowForCameraSetInTRE(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraTreBased(cameraSetIndex, cameraIndex);
        String iloc = camera.getFieldValue("ILOC");
        return Integer.parseInt(iloc.substring(0, "RRRRR".length()));
    }

    /**
     * Get the image location column for a given camera in a given camera set in
     * the collection.
     *
     * From MIE4NITF Version 1.1: ILOCnm – A 10 character BCS-N positive integer
     * containing the image location value for the mth camera in the nth cameras
     * set at the original spatial resolution of the data (Rset = 0, i.e. IMAG =
     * 1.0). This value may differ from the ILOC field in the NITF Image Segment
     * Subheader that contains the frames associated with this camera, if the
     * camera data has been magnified or reduced relative to the original data
     * (i.e. IMAG ≠ 1.0).
     *
     * The camera set index is a zero base index for the collection. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 79 to 99.
     *
     * @param cameraSetIndex zero base camera set index for the collection.
     * @param cameraIndex zero base camera index
     * @return the column part of the image location.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getImageLocationColumn(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraCollectionBased(cameraSetIndex, cameraIndex);
        String iloc = camera.getFieldValue("ILOC");
        return Integer.parseInt(iloc.substring("RRRRR".length()));
    }

    /**
     * Get the image location column for a given camera in a given camera set in
     * this TRE.
     *
     * From MIE4NITF Version 1.1: ILOCnm – A 10 character BCS-N positive integer
     * containing the image location value for the mth camera in the nth cameras
     * set at the original spatial resolution of the data (Rset = 0, i.e. IMAG =
     * 1.0). This value may differ from the ILOC field in the NITF Image Segment
     * Subheader that contains the frames associated with this camera, if the
     * camera data has been magnified or reduced relative to the original data
     * (i.e. IMAG ≠ 1.0).
     *
     * The camera set index is a zero base index for the TRE. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 0 to 19. If this is the
     * first (or only) CAMSDA TRE, this is the same as getImageLocationColumn().
     *
     * @param cameraSetIndex zero base camera set index for this TRE.
     * @param cameraIndex zero base camera index
     * @return the column part of the image location.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getImageLocationColumnForCameraSetInTRE(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraTreBased(cameraSetIndex, cameraIndex);
        String iloc = camera.getFieldValue("ILOC");
        return Integer.parseInt(iloc.substring("RRRRR".length()));
    }

    /**
     * Get the number of rows of pixels for a given camera in a given camera set
     * in the collection.
     *
     * From MIE4NITF Version 1.1: NROWSnm – An 8 character BCS-N positive
     * integer containing the number of significant rows of pixels for the mth
     * camera in the nth camera set at the original spatial resolution of the
     * data (Rset = 0, i.e. IMAG = 1.0). This value may differ from the NROWS
     * field in the NITF Image Segment Subheader that contains the frames
     * associated with this camera, if the camera data has been magnified or
     * reduced relative to the original data (i.e. IMAG ≠ 1.0).
     *
     * The camera set index is a zero base index for the collection. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 79 to 99. If this is the
     * first (or only) CAMSDA TRE, this is the same as
     * getNumRowsForCameraSetInTRE().
     *
     * @param cameraSetIndex zero base camera set index for the collection.
     * @param cameraIndex zero base camera index
     * @return the number of significant rows of pixels.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getNumRows(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraCollectionBased(cameraSetIndex, cameraIndex);
        return camera.getIntValue("NROWS");
    }

    /**
     * Get the number of rows of pixels for a given camera in a given camera set
     * in this TRE.
     *
     * From MIE4NITF Version 1.1: NROWSnm – An 8 character BCS-N positive
     * integer containing the number of significant rows of pixels for the mth
     * camera in the nth camera set at the original spatial resolution of the
     * data (Rset = 0, i.e. IMAG = 1.0). This value may differ from the NROWS
     * field in the NITF Image Segment Subheader that contains the frames
     * associated with this camera, if the camera data has been magnified or
     * reduced relative to the original data (i.e. IMAG ≠ 1.0).
     *
     * The camera set index is a zero base index for the TRE. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 0 to 19. If this is the
     * first (or only) CAMSDA TRE, this is the same as getNumRows().
     *
     * @param cameraSetIndex zero base camera set index for this TRE.
     * @param cameraIndex zero base camera index
     * @return the number of significant rows of pixels.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getNumRowsForCameraSetInTRE(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraTreBased(cameraSetIndex, cameraIndex);
        return camera.getIntValue("NROWS");
    }

    /**
     * Get the number of columns of pixels for a given camera in a given camera
     * set in the collection.
     *
     * From MIE4NITF Version 1.1: NCOLSnm – An 8 character BCS-N positive
     * integer containing the number of significant columns of pixels for the
     * mth camera in the nth camera set at the original spatial resolution of
     * the data (Rset = 0, i.e. IMAG = 1.0). This value may differ from the
     * NCOLS field in the NITF Image Segment Subheader that contains the frames
     * associated with this camera, if the camera data has been magnified or
     * reduced relative to the original data (i.e. IMAG ≠ 1.0).
     *
     * The camera set index is a zero base index for the collection. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 79 to 99. If this is the
     * first (or only) CAMSDA TRE, this is the same as
     * getNumColumnsForCameraSetInTRE().
     *
     * @param cameraSetIndex zero base camera set index for the collection.
     * @param cameraIndex zero base camera index
     * @return the number of significant columns of pixels.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getNumColumns(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraCollectionBased(cameraSetIndex, cameraIndex);
        return camera.getIntValue("NCOLS");
    }

    /**
     * Get the number of columns of pixels for a given camera in a given camera
     * set in this TRE.
     *
     * From MIE4NITF Version 1.1: NCOLSnm – An 8 character BCS-N positive
     * integer containing the number of significant columns of pixels for the
     * mth camera in the nth camera set at the original spatial resolution of
     * the data (Rset = 0, i.e. IMAG = 1.0). This value may differ from the
     * NCOLS field in the NITF Image Segment Subheader that contains the frames
     * associated with this camera, if the camera data has been magnified or
     * reduced relative to the original data (i.e. IMAG ≠ 1.0).
     *
     * The camera set index is a zero base index for the TRE. So if the
     * collection has 100 entries, and this TRE has the last 20 (i.e. 79-99 in a
     * zero base), the valid camera set index range is 0 to 19. If this is the
     * first (or only) CAMSDA TRE, this is the same as getNumColumns().
     *
     * @param cameraSetIndex zero base camera set index for this TRE.
     * @param cameraIndex zero base camera index
     * @return the number of significant columns of pixels.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getNumColumnsForCameraSetInTRE(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup camera = getCameraTreBased(cameraSetIndex, cameraIndex);
        return camera.getIntValue("NCOLS");
    }

    private TreGroup getCameraSetTreBased(final int cameraSetIndex) throws NitfFormatException {
        List<TreGroup> cameraSets = mTre.getEntry("CAMERA_SETS").getGroups();
        TreGroup cameraSet = cameraSets.get(cameraSetIndex);
        return cameraSet;
    }

    private TreGroup getCameraSetCollectionBased(final int cameraSetIndex) throws NitfFormatException {
        return getCameraSetTreBased(cameraSetIndex - (getIndexOfFirstCameraSetInTRE() - 1));
    }

    private TreGroup getCameraTreBased(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup cameraSet = getCameraSetTreBased(cameraSetIndex);
        return cameraSet.getEntry("CAMERAS").getGroups().get(cameraIndex);
    }

    private TreGroup getCameraCollectionBased(final int cameraSetIndex, final int cameraIndex) throws NitfFormatException {
        TreGroup cameraSet = getCameraSetCollectionBased(cameraSetIndex);
        return cameraSet.getEntry("CAMERAS").getGroups().get(cameraIndex);
    }
}
