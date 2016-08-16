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
package org.codice.imaging.nitf.core.dataextension;

import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.CommonSegment;
import org.codice.imaging.nitf.core.tre.TreCollection;

/**
 Common data extension segment parsing functionality.
 */
public interface DataExtensionSegment extends CommonSegment {

    /**
     * Set the DES version (DESVER).
     * <p>
     * "This field shall contain the alphanumeric version number of the use of
     * the tag. The version number is assigned as part of the registration
     * process."
     *
     * @param version the version (valid range 1 to 99).
     */
    void setDESVersion(final int version);


    /**
     Return the DES version (DESVER).
     <p>
     "This field shall contain
     the alphanumeric version number of the use of the tag.
     The version number is assigned as part of the
     registration process."

     @return the DES version.
     */
    int getDESVersion();

    /**
     Return the overflowed header type.
     <p>
     For NITF 2.0: <i>This field shall be present if DESTAG = "Registered Extensions" or "Controlled
     Extensions." Its presence indicates that the DES contains a tagged record extension
     that would not fit in the file header or component header where it would ordinarily
     be located. Its value indicates the data type to which the enclosed tagged record is
     relevant. If the value of DESTAG is "Controlled Extensions," the valid values for
     DESOFLOW are XHD, IXSHD, SXSHD, LSXHD or TXSHD. If the value of
     DESTAG is "Registered Extensions," the valid values for DESOFLW are UDHD
     and UDID.</i> [From MIL-STD-2500A TABLE XVIII. Data extension segment subheader field definitions]
     <p>
     Note that DESTAG is the identifier (getIdentifier()) for NITF 2.0.
     <p>
     For NITF 2.1 / NSIF 1.0: <i>This field shall be
     present if DESID = TRE_OVERFLOW. Its presence
     indicates that the DES contains a TRE that would not fit
     in the file header or segment subheader where it would
     ordinarily be located. Its value indicates the segment
     type to which the enclosed TRE is relevant.</i>
     [From MIL-STD-2500C TABLE A-8. NITF Data Extension Segment (DES) subheader].
     <p>
     The valid values for NITF 2.1 / NSIF 1.0 are XHD, IXSHD, SXSHD, TXSHD, UDHD and UDID.

     @return the overflowed header type.
     */
    String getOverflowedHeaderType();

    /**
     Set the item that was overflowed.
     <p>
     For NITF 2.0: <i>This field shall be present if DESOFLW is present. It shall contain the number of
     the data item in the file, of the type indicated in DESOFLW to which the tagged
     record extensions in the segment apply. For example, if DESOFLW = UDID and
     DESITEM = 3, then the tagged record extensions in the segment applies to the third
     image in the file. If the value of DESOFLW = UDHD, the value of DESITEM
     shall be 0."</i> [From MIL-STD-2500A TABLE XVIII. Data extension segment subheader field definitions]
     <p>
     For NITF 2.1 / NSIF 1.0: <i>type to which the enclosed TRE is relevant.
     DES Data Item Overflowed. This field shall be present
     if DESOFLW is present. It shall contain the number of
     the data item in the file, of the type indicated in
     DESOFLW to which the TRE in the segment apply. For
     example, if DESOFLW = UDID and DESITEM = 003,
     then the TRE in the segment apply to the third image in
     the file. If the value of DESOFLW = UDHD, the value
     of DESITEM shall be BCS zeros (0x30).</i>
     [From MIL-STD-2500C TABLE A-8. NITF Data Extension Segment (DES) subheader].

     @return the overflowed item
     */
    int getItemOverflowed();

    /**
     * Set the user defined subheader data (DESSHF).
     * <p>
     * This can only be non-null if the identifier is not "Registered
     * Extensions" or "Controlled Extensions" (NITF 2.0) or "TRE_OVERFLOW" (NITF
     * 2.1 / NSIF 1.0).
     *
     * @param data the user defined subheader data
     */
    void setUserDefinedSubheaderField(final String data);

    /**
     Set the user defined subheader data (DESSHF).
     <p>
     This will be null if the identifier is "Registered Extensions" or "Controlled
     Extensions" (NITF 2.0) or "TRE_OVERFLOW" (NITF 2.1 / NSIF 1.0).

     @return the user defined subheader data
     */
    String getUserDefinedSubheaderField();

    /**
     * Check if this DES is a NITF 2.1 TRE overflow DES.
     *
     * @return true for NITF 2.1 TRE overflow, otherwise false
     */
    boolean isTreOverflowNitf21();

    /**
     * Check if this DES is a NITF 2.0 TRE overflow DES.
     *
     * @return true for NITF 2.0 TRE overflow, otherwise false
     */
    boolean isTreOverflowNitf20();

    /**
     * Check if this DES is a TRE overflow DES.
     * <p>
     * This handles both NITF 2.0 and NITF 2.1 / NSIF 1.0 files
     *
     * @return true for TRE overflow, otherwise false
     */
    boolean isTreOverflow();

    /**
     * Merges overflow Tres into this data extension header.
     *
     * @param overflowTres - the Tres to be merged in.
     */
    void mergeTREs(TreCollection overflowTres);

    /**
     * Check if this DES is streaming mode.
     *
     * @return true if DES is streaming mode updates, otherwise false.
     */
    boolean isStreamingMode();

    /**
     * Set the DES data, if any.
     *
     * @param stream stream containing the data for this DES.
     */
    void setData(final ImageInputStream stream);

    /**
     * Get the data for this DES.
     *
     * @return data for this DES.
     */
    ImageInputStream getData();

    /**
     * Get the length of the data for this segment.
     *
     * @return the number of bytes of data for this segment.
     */
    long getDataLength();

    /**
     * Set the length of data for this segment.
     *
     * @param length the number of bytes of data in this segment.
     */
    void setDataLength(final long length);


}
