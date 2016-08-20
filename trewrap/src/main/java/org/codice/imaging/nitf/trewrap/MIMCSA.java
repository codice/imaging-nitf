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

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.tre.Tre;

/**
 * Wrapper for the Motion Imagery Collection Summary TRE (MIMCSA).
 *
 * From MIE4NITF Section 5.9.3.2: The Motion Imagery Collection Summary TRE
 * contains metadata describing the collection as a whole for a particular
 * phenomenological layer. It is placed in the NITF File Header of every file in
 * the collection including the Manifest File. It is possible that some of the
 * MIMCSA values may be unknown at the start of a collection. For example, the
 * maximum and minimum frame rates may not be known before the collection has
 * been completed. These metadata fields may be set to a distinguished value
 * (NaN) that indicates the value is not known. Once any unknown values are
 * determined, all subsequent instances of the MIMCSA TRE(s) shall be populated
 * with the correct values. When a Manifest File is created, the MIMCSA TRE(s)
 * shall be placed in the NITF File Header of the Manifest File.
 */
public class MIMCSA extends FlatTreWrapper {

    private static final String TAG_NAME = "MIMCSA";

    /**
     * Create a MIMCSA TRE wrapper from an existing TRE.
     *
     * @param tre the TRE to wrap. Must match the MIMCSA tag.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public MIMCSA(final Tre tre) throws NitfFormatException {
        super(tre, TAG_NAME);
    }

    /**
     * Get the phenomenology layer identifier.
     *
     * From MIE4NITF Section 5.9.3.2: A 36 character string identifying the
     * phenomenological layer of the collection that is summarized by this
     * instance of this TRE. The 36 character field is long enough for a UUID.
     * It is recommended that UUIDs be used but this is not mandatory. The field
     * may not be all spaces and shall be left justified and padded on the right
     * with BCS-A spaces if needed.
     *
     * @return layer identifier
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getLayerId() throws NitfFormatException {
        return getFieldValue("LAYER_ID");
    }

    /**
     * Get the nominal frame rate in frames per second.
     *
     * From MIE4NITF Section 5.9.3.2: The nominal frame rate within the
     * collection for this phenomenological layer in frames per second. If the
     * nominal frame rate is not known when the data producer completes a NITF
     * file, the value of this field shall be “NaN ” (“NaN” followed by ten
     * BCS-A spaces). If the collection contains multiple intervals where the
     * nominal frame rate differs (e.g., 2 days of 1 frame/hour, 1 minute of
     * 29.9 Hz, and then another day at 1 frame/hour) the data producer may
     * choose one of the frame rates and set the minimum and maximum frame rates
     * accordingly. If there is no nominal frame rate, this value may be set to
     * “NaN”. If the collection system is intending to collect at a specific
     * frame rate (e.g., 29.9Hz) but there are slight variations in the
     * collection rate, the value of this field should be the intended nominal
     * frame rate. Once the correct value of this field is known, the data
     * producer shall populate the copies of this TRE in NITF files of
     * subsequent time intervals and in the Manifest File with the correct
     * value. If the correct value of this field is not known until the
     * collection has been completed, the MIMCSA TRE in the Manifest File shall
     * be updated to the correct value before the file is made available to data
     * consumers.
     *
     * @return the frame rate in frames per second, or NaN.
     * @throws NitfFormatException if there is a parsing issue
     */
    public final double getNominalFrameRate() throws NitfFormatException {
        return getValueAsDouble("NOMINAL_FRAME_RATE");
    }

    /**
     * Get the minimum frame rate in frames per second.
     *
     * From MIE4NITF Section 5.9.3.2: The minimum frame rate within the
     * collection for this phenomenological layer in frames per second. This
     * field is intended to provide assistance to an application cataloging the
     * collection and is not intended for scientific analysis. If the minimum
     * frame rate is not known when the data producer completes a NITF file, the
     * value of this field shall be “NaN ” (“NaN” followed by ten BCS-A spaces).
     * Once the correct value of this field is known, the data producer shall
     * populate the copies of this TRE in NITF files of subsequent time
     * intervals and in the Manifest File with the correct value. If the correct
     * value of this field is not known until the collection has been completed,
     * the MIMCSA TRE in the Manifest File shall be updated to contain the
     * correct value before the file is made available to data consumers.
     *
     * @return minimum frame rate in frames per second, or NaN.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final double getMinimumFrameRate() throws NitfFormatException {
        return getValueAsDouble("MIN_FRAME_RATE");
    }

    /**
     * Get the maximum frame rate in frames per second.
     *
     * From MIE4NITF Section 5.9.3.2: The maximum frame rate within the
     * collection for this phenomenological layer in frames per second. This
     * field is intended to provide assistance to an application cataloging the
     * collection and is not intended for scientific analysis. If the minimum
     * frame rate is not known when the data producer completes a NITF file, the
     * value of this field shall be “NaN ” (“NaN” followed by ten BCS-A spaces).
     * Once the correct value of this field is known, the data producer shall
     * populate the copies of this TRE in NITF files of subsequent time
     * intervals and in the Manifest File with the correct value. If the correct
     * value of this field is not known until the collection has been completed,
     * the MIMCSA TRE in the Manifest File shall be updated to contain the
     * correct value before the file is made available to data consumers.
     *
     * @return maximum frame rate in frames per second, or NaN.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final double getMaximumFrameRate() throws NitfFormatException {
        return getValueAsDouble("MAX_FRAME_RATE");
    }

    /**
     * Get the temporal RSET.
     *
     * From MIE4NITF Section 5.9.3.2: The temporal position of the data for this
     * phenomenological layer in the temporal reduced resolution set, where a
     * value of 0 indicates the original collected temporal resolution, and
     * values greater than 0 indicate some reduced frame rate, where the frame
     * rate of T_RSET tr is less than the frame rate of T_RSET tr’ if tr &gt; tr’.
     * There is no implied temporal subsampling relationship between T_RSET
     * values. A common practice might be to relate them by powers of two, where
     * the frame rate of the MI data associated with T_RSET = 1 is twice that of
     * the same MI dataset with T_RSET = 2.
     *
     * @return the temporal position in the RSET.
     * @throws NitfFormatException if there is a parsing issue
     */
    public final int getTemporalRSET() throws NitfFormatException {
        return this.getValueAsInteger("T_RSET");
    }

    /**
     * Get the required motion imagery decoder.
     *
     * From MIE4NITF Section 5.9.3.2: The value of the NITF Image Subheader IC
     * value for NITF Image Segments in this phenomenological layer. Note this
     * restricts the data from all cameras in a single phenomenological layer to
     * be compressed using the same method. Thus all of the relevant NITF Image
     * Segments share the same IC value in those NITF Image Subheaders.
     *
     * @return the required decoder
     * @throws NitfFormatException if there is a parsing issue
     */
    public final String getRequiredDecoder() throws NitfFormatException {
        return getFieldValue("MI_REQ_DECODER");
    }

    /**
     * Get the required motion imagery decoder.
     *
     * From MIE4NITF Section 5.9.3.2: The value of the NITF Image Subheader IC
     * value for NITF Image Segments in this phenomenological layer. Note this
     * restricts the data from all cameras in a single phenomenological layer to
     * be compressed using the same method. Thus all of the relevant NITF Image
     * Segments share the same IC value in those NITF Image Subheaders.
     *
     * @return the required decoder as an enum
     * @throws NitfFormatException if there is a parsing issue
     */
    public final ImageCompression getRequiredDecoderValue() throws NitfFormatException {
        return ImageCompression.getEnumValue(getFieldValue("MI_REQ_DECODER"));
    }

    /**
     * Get the required motion imagery compression profile.
     *
     * From MIE4NITF Section 5.9.3.2: The name of the profile of the motion
     * imagery (or still) compression standard (specified in MI_REQ_DECODER)
     * required to decode the data from this phenomenological layer. The values
     * of this field are the formal names of the profiles as defined by the
     * standard in which the compression method is defined (e.g., “High profile”
     * from Recommendation ITU-T H.264, Annex A.2.4). For JPEG 2000 compression,
     * list the part of the standard used (e.g.
     * “ISO/IEC 15444-1                     ”). If the MI data is uncompressed
     * or there is no profile defined for the MI compression algorithm, the
     * field shall be set to “Not Applicable                      ”. This field
     * shall not be all spaces.
     *
     * @return the required motion imagery profile.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getRequiredProfile() throws NitfFormatException {
        return getFieldValue("MI_REQ_PROFILE");
    }

    /**
     * Get the required motion imagery compression profile level.
     *
     * From MIE4NITF Section 5.9.3.2: The value or name of the level for the
     * profile of the compression method required to decode the data from this
     * phenomenological layer, as defined in the standard in which the
     * compression method is defined (e.g. “4.2” from Recommendation ITU-T
     * H.264, Table A-1). For JPEG 2000, list the compliance class needed from
     * Table A-1 of ISO/IEC 15444-4 (e.g. “class2”). If the MI data is
     * uncompressed or there is no level defined for the MI compression
     * algorithm, the field shall be set to “N/A ”. This field shall not be all
     * spaces.
     *
     * @return the level of the required motion imagery profile.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getRequiredLevel() throws NitfFormatException {
        return getFieldValue("MI_REQ_LEVEL");
    }

    @Override
    public final ValidityResult getValidity() throws NitfFormatException {
        return new ValidityResult();
    }

}
