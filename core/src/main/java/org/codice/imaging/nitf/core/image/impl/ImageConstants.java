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
package org.codice.imaging.nitf.core.image.impl;

/**
 * Constants used by classes within the 'image' package.
 */
public final class ImageConstants {
    // image segment
    /**
     * Marker string for NITF Image segment.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final String IM = "IM";

    /**
     * Length of the "Image Identifier 1" IID1 field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IID1_LENGTH = 10;

    /**
     * Length of the "Target Identifier" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int TGTID_LENGTH = 17;

    /**
     * Length of the "Image Identifier 2" IID2 field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IID2_LENGTH = 80;

    /**
     * Length of the "Image Source" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int ISORCE_LENGTH = 42;

    /**
     * Length of the "Number of Significant Rows" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int NROWS_LENGTH = 8;

    /**
     * Length of the "Number of Significant Columns" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int NCOLS_LENGTH = 8;

    /**
     * Length of the "Pixel Value Type" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int PVTYPE_LENGTH = 3;

    /**
     * Length of the "Image Representation" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IREP_LENGTH = 8;

    /**
     * Length of the "Image Category" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int ICAT_LENGTH = 8;

    /**
     * Length of the "Actual Bits-Per-Pixel per Band" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int ABPP_LENGTH = 2;

    /**
     * Length of the "Pixel Justification" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int PJUST_LENGTH = 1;

    /**
     * Length of the "Image Coordinate Representation" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int ICORDS_LENGTH = 1;

    /**
     * Length of the "Image Geographic Location" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IGEOLO_LENGTH = 60;

    /**
     * Length of the "Number of Image Comments" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int NICOM_LENGTH = 1;

    /**
     * Length of each of the "Image Comment n" fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int ICOM_LENGTH = 80;

    /**
     * Length of the "Image Compression" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IC_LENGTH = 2;

    /**
     * Length of the "Compression Rate Code" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int COMRAT_LENGTH = 4;

    /**
     * Length of the "Number of Bands" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int NBANDS_LENGTH = 1;

    /**
     * Length of the "Number of Multispectral Bands" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int XBANDS_LENGTH = 5;

    /**
     * Length of the "Image Sync code" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int ISYNC_LENGTH = 1;

    /**
     * Length of the "Image Mode" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IMODE_LENGTH = 1;

    /**
     * Length of the "Number of Blocks Per Row" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int NBPR_LENGTH = 4;

    /**
     * Length of the "Number of Blocks Per Column" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int NBPC_LENGTH = 4;

    /**
     * Length of the "Number of Pixels Per Block Horizontal" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int NPPBH_LENGTH = 4;

    /**
     * Length of the "Number of Pixels Per Block Vertical" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int NPPBV_LENGTH = 4;

    /**
     * Length of the "Number of Bits Per Pixel Per Band" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int NBPP_LENGTH = 2;

    /**
     * Length of the "Image Display Level" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IDLVL_LENGTH = 3;

    /**
     * Length of the "Image Attachment Level" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IALVL_LENGTH = 3;

    /**
     * Length of half the "Image Location" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     * <p>
     * The actual ILOC field is twice this length, in the format RRRRRCCCCC, but we parse half at a time.
     */
    public static final int ILOC_HALF_LENGTH = 5;

    /**
     * Length of the "Image Magnification" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IMAG_LENGTH = 4;

    /**
     * Length of the "User Defined Image Data Length" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int UDIDL_LENGTH = 5;

    /**
     * Length of the "User Defined Overflow" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int UDOFL_LENGTH = 3;

    /**
     * Length of the "User Defined Image Data Length" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IXSHDL_LENGTH = 5;

    /**
     * Length of the "Image Extended Subheader Overflow" field in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */

    public static final int IXSOFL_LENGTH = 3;

    // image band
    /**
     * Length of the "nth Band Representation" IREPBANDn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IREPBAND_LENGTH = 2;

    /**
     * Length of the "nth Band Subcategory" ISUBCATn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int ISUBCAT_LENGTH = 6;

    /**
     * Length of the "nth Band Image Filter Condition" IFCn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IFC_LENGTH = 1;

    /**
     * Length of the "nth Band Standard Image Filter Code" IMFLTn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int IMFLT_LENGTH = 3;

    /**
     * Length of the "Number of LUTS for the nth Image Band" NLUTSn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int NLUTS_LENGTH = 1;

    /**
     * Length of the "Number of LUT Entries for the nth Image Band" NELUTn fields in the NITF image header.
     * <p>
     * See MIL-STD-2500C Table A-3.
     */
    public static final int NELUT_LENGTH = 5;
    static final int MAX_NUM_BANDS_IN_NBANDS_FIELD = 9;

    private ImageConstants() {
    }

}
