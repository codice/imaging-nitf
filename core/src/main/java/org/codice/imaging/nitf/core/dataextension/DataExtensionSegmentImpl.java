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

import java.io.IOException;
import java.text.ParseException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.CommonSegmentImpl;
import org.codice.imaging.nitf.core.common.FileType;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.CONTROLLED_EXTENSIONS;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESITEM_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESOFLW_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESSHL_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.REGISTERED_EXTENSIONS;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.STREAMING_FILE_HEADER;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.TRE_OVERFLOW;

/**
    Data Extension Segment (DES) subheader and associated data.
*/
class DataExtensionSegmentImpl extends CommonSegmentImpl implements DataExtensionSegment {

    private int desVersion = -1;
    private String overflowedHeaderType = null;
    private int desItemOverflowed = 0;
    private String userDefinedSubheaderField = null;
    private ImageInputStream desData = null;
    private long dataLength = 0;

    /**
        Default constructor.
    */
    DataExtensionSegmentImpl() {
    }

    /**
        Set the DES version (DESVER).
        <p>
        "This field shall contain
        the alphanumeric version number of the use of the tag.
        The version number is assigned as part of the
        registration process."

        @param version the version (valid range 1 to 99).
    */
    public final void setDESVersion(final int version) {
        desVersion = version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getDESVersion() {
        return desVersion;
    }

    /**
        Set the overflowed header type.
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
        For NITF 2.1 / NSIF 1.0: <i>DES Overflowed Header Type. This field shall be
        present if DESID = TRE_OVERFLOW. Its presence
        indicates that the DES contains a TRE that would not fit
        in the file header or segment subheader where it would
        ordinarily be located. Its value indicates the segment
        type to which the enclosed TRE is relevant.</i> [From MIL-STD-2500C TABLE A-8. NITF Data Extension
        Segment (DES) subheader].
        <p>
        The valid values for NITF 2.1 / NSIF 1.0 are XHD, IXSHD, SXSHD, TXSHD, UDHD and UDID.

        @param headerType the overflowed header type.
    */
    public final void setOverflowedHeaderType(final String headerType) {
        overflowedHeaderType = headerType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getOverflowedHeaderType() {
        return overflowedHeaderType;
    }

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
        For NITF 2.1 / NSIF 1.0: <i>This field shall be present
        if DESOFLW is present. It shall contain the number of
        the data item in the file, of the type indicated in
        DESOFLW to which the TRE in the segment apply. For
        example, if DESOFLW = UDID and DESITEM = 003,
        then the TRE in the segment apply to the third image in
        the file. If the value of DESOFLW = UDHD, the value
        of DESITEM shall be BCS zeros (0x30).</i>
        [From MIL-STD-2500C TABLE A-8. NITF Data Extension Segment (DES) subheader].

        @param itemOverflowed the overflowed item
    */
    public final void setItemOverflowed(final int itemOverflowed) {
        desItemOverflowed = itemOverflowed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getItemOverflowed() {
        return desItemOverflowed;
    }

    /**
        Set the user defined subheader data (DESSHF).
        <p>
        This can only be non-null if the identifier is not "Registered Extensions" or "Controlled
        Extensions" (NITF 2.0) or "TRE_OVERFLOW" (NITF 2.1 / NSIF 1.0).

        @param data the user defined subheader data
    */
    public final void setUserDefinedSubheaderField(final String data) {
        userDefinedSubheaderField = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getUserDefinedSubheaderField() {
        return userDefinedSubheaderField;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isTreOverflowNitf21() {
        return getIdentifier().trim().equals(TRE_OVERFLOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isTreOverflowNitf20() {
        return getIdentifier().trim().equals(REGISTERED_EXTENSIONS)
                || getIdentifier().trim().equals(CONTROLLED_EXTENSIONS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isTreOverflow(final FileType fileType) {
        if (fileType == FileType.NITF_TWO_ZERO) {
            return isTreOverflowNitf20();
        } else {
            return isTreOverflowNitf21();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStreamingMode() {
        return STREAMING_FILE_HEADER.equals(getIdentifier().trim());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(final ImageInputStream dataStream) {
        desData = dataStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageInputStream getData() {
        return desData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getHeaderLength() throws ParseException, IOException {
        long headerLength = DataExtensionConstants.DE.length()
                + DataExtensionConstants.DESID_LENGTH
                + DataExtensionConstants.DESVER_LENGTH
                + getSecurityMetadata().getSerialisedLength();
        if (isTreOverflowNitf20() || isTreOverflowNitf21()) {
            headerLength += DESOFLW_LENGTH;
            headerLength += DESITEM_LENGTH;
        }
        headerLength += DESSHL_LENGTH;
        headerLength += getUserDefinedSubheaderField().length();
        return headerLength;
    }

    @Override
    public final long getDataLength() {
        return dataLength;
    }

    @Override
    public void setDataLength(final long length) {
        dataLength = length;
    }
}
