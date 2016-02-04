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

import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import org.codice.imaging.nitf.core.common.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.common.FileType;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DE;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESID_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESITEM_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESOFLW_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESSHL_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESVER_LENGTH;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Writer for Data Extension Segments (DESs).
 */
public class DataExtensionSegmentWriter extends AbstractSegmentWriter {

    /**
     * Constructor.
     *
     * @param output the target to write the data extension segment to.
     * @param treParser TreParser to use to serialise out the TREs.
     */
    public DataExtensionSegmentWriter(final DataOutput output, final TreParser treParser) {
        super(output, treParser);
    }

    /**
     * Write out the subheader information for this data extension segment.
     *
     * @param header the header to write
     * @param fileType the type (NITF version) of file to write out this segment header for.
     * @throws IOException on write failure
     * @throws ParseException on TRE parse problems
     */
    public final void writeDESHeader(final NitfDataExtensionSegmentHeader header, final FileType fileType) throws IOException, ParseException {
        writeFixedLengthString(DE, DE.length());
        writeFixedLengthString(header.getIdentifier(), DESID_LENGTH);
        writeFixedLengthNumber(header.getDESVersion(), DESVER_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata(), fileType);
        if (header.isTreOverflow(fileType)) {
            writeFixedLengthString(header.getOverflowedHeaderType(), DESOFLW_LENGTH);
            writeFixedLengthNumber(header.getItemOverflowed(), DESITEM_LENGTH);
        }
        writeFixedLengthNumber(header.getUserDefinedSubheaderField().length(), DESSHL_LENGTH);
        if (header.getUserDefinedSubheaderField().length() > 0) {
            mOutput.writeBytes(header.getUserDefinedSubheaderField());
        } else {
            byte[] treData = mTreParser.getTREs(header, TreSource.TreOverflowDES);
            mOutput.write(treData);
        }
    }

    /**
     * Write out the data associated with this Data Extension Segment.
     *
     * @param desData the data to write
     * @throws IOException on write failure.
     */
    public final void writeDESData(final byte[] desData) throws IOException {
        if (desData != null) {
            mOutput.write(desData);
        }
    }
}
