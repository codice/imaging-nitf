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
import java.util.function.Consumer;

import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.CommonSegmentImpl;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
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
    private long dataLength = 0;
    private Consumer<Consumer<ImageInputStream>> dataConsumer;

    /**
        Default constructor.
    */
    DataExtensionSegmentImpl(final FileType fileType) {
        setFileType(fileType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
    public final boolean isTreOverflow() {
        if (getFileType() == FileType.NITF_TWO_ZERO) {
            return isTreOverflowNitf20();
        } else if ((getFileType().equals(FileType.NITF_TWO_ONE)) || (getFileType().equals(FileType.NSIF_ONE_ZERO))) {
            return isTreOverflowNitf21();
        } else {
            throw new UnsupportedOperationException("Unsupported NITF version for TRE overflow determination:" + getFileType().getTextEquivalent());
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
    public void consume(final Consumer<ImageInputStream> consumer) {
       if (dataConsumer != null) {
           dataConsumer.accept(consumer);
       }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDataConsumer(final Consumer<Consumer<ImageInputStream>> consumer) {
        dataConsumer = consumer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getHeaderLength() throws NitfFormatException, IOException {
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
