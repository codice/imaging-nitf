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
package org.codice.imaging.nitf.core;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
   Raster Product Format (RPF) attribute parser.
*/
public class RasterProductFormatAttributeParser {

    private static final Logger LOG = LoggerFactory.getLogger(RasterProductFormatAttributeParser.class);

    private static final int ATTRIBUTE_SECTION_SUBHEADER_LENGTH = 10;

    private static final int CURRENCY_DATE_ATTR_ID = 1;
    private static final int PRODUCTION_DATE_ATTR_ID = 2;
    private static final int SIGNIFICANT_DATE_ATTR_ID = 3;
    private static final int MAP_CHART_SOURCE_ATTR_ID = 4;
    private static final int PROJECTION_SYSTEM_ATTR_ID = 5;
    private static final int VERTICAL_DATUM_ATTR_ID = 6;
    private static final int HORIZONTAL_DATUM_ATTR_ID = 7;
    private static final int VERTICAL_ABSOLUTE_ACCURACY_ATTR_ID = 8;
    private static final int HORIZONTAL_ABSOLUTE_ACCURACY_ATTR_ID = 9;
    private static final int VERTICAL_RELATIVE_ACCURACY_ATTR_ID = 10;
    private static final int HORIZONTAL_RELATIVE_ACCURACY_ATTR_ID = 11;
    private static final int ELLIPSOID_ATTR_ID = 12;
    private static final int SOUNDING_DATUM_ATTR_ID = 13;
    private static final int NAVIGATION_SYSTEM_ATTR_ID = 14;
    private static final int GRID_ATTR_ID = 15;
    private static final int EASTERLY_ANNUAL_MAGNETIC_CHANGE_ATTR_ID = 16;
    private static final int WESTERLY_ANNUAL_MAGNETIC_CHANGE_ATTR_ID = 17;
    private static final int GRID_NORTH_MAGNETIC_NORTH_ANGLE_ATTR_ID = 18;
    private static final int GRID_CONVERGENCE_ANGLE_ATTR_ID = 19;
    private static final int HIGHEST_KNOWN_ELEVATION_ATTR_ID = 20;
    private static final int MULTIPLE_LEGEND_ATTR_ID = 21;
    private static final int IMAGE_SOURCE_ATTR_ID = 22;
    private static final int DATA_LEVEL_ATTR_ID = 23;
    private static final int CHART_UPDATE_INFORMATION_ATTR_ID = 24;
    private static final int CONTOUR_INTERVAL_ATTR_ID = 25;

    private static final int DATE_LENGTH = 8;

    /**
    Raster Product Format (RPF) offset record.
    */
    private static class OffsetRecord {
        private int attributeId = 0;
        private int parameterId = 0;
        private int arealCoverageSequenceNumber = 0;
        private int attributeRecordOffset = 0;
    }

    /**
        Parse the content out of RPF DES.

        @param desData byte array of DES data.
        @return attributes for the RPF DES.
        @throws ParseException if the data format is not as expected.
    */
    public final RasterProductFormatAttributes parseRpfDes(final byte[] desData) throws ParseException {
        RasterProductFormatAttributes attributes = new RasterProductFormatAttributes();

        ByteBuffer bytes = ByteBuffer.wrap(desData);

        int numberOfAttributeOffsetRecords = bytes.getShort();
//        int numberOfExplicitArealCoverageRecords = bytes.getShort();
//        int attributeOffsetTableOffset = bytes.getInt();
//        int attributeOffsetRecordLength = bytes.getShort();
//         System.out.println("RPFDES: numberOfAttributeOffsetRecords:" + numberOfAttributeOffsetRecords);
//         System.out.println("RPFDES: numberOfExplicitArealCoverageRecords:" + numberOfExplicitArealCoverageRecords);
//         System.out.println("RPFDES: attributeOffsetTableOffset:" + attributeOffsetTableOffset);
//         System.out.println("RPFDES: attributeOffsetRecordLength:" + attributeOffsetRecordLength);

        // System.out.println("Attribute offset table");
        List<OffsetRecord> offsetRecords = new ArrayList<OffsetRecord>();
        for (int i = 0; i < numberOfAttributeOffsetRecords; ++i) {
            OffsetRecord offsetRecord = new OffsetRecord();
            offsetRecord.attributeId = bytes.getShort();
            offsetRecord.parameterId = bytes.get();
            offsetRecord.arealCoverageSequenceNumber = bytes.get();
            offsetRecord.attributeRecordOffset = bytes.getInt();
            offsetRecords.add(offsetRecord);
        }

        for (int i = 0; i < offsetRecords.size(); ++i) {
            OffsetRecord offsetRecord = offsetRecords.get(i);
//             System.out.println("\tAttributeId:" + offsetRecord.attributeId);
//             System.out.println("\tParameterId:" + offsetRecord.parameterId);
//             System.out.println("\tAreal coverage sequence number:" + offsetRecord.arealCoverageSequenceNumber);
//             System.out.println("\tattribute record offset:" + offsetRecord.attributeRecordOffset);
            bytes.position(ATTRIBUTE_SECTION_SUBHEADER_LENGTH + offsetRecord.attributeRecordOffset);
            switch(offsetRecord.attributeId) {
                case CURRENCY_DATE_ATTR_ID:
                    attributes.addCurrencyDate(offsetRecord.arealCoverageSequenceNumber, parseRpfCurrencyDate(bytes, offsetRecord));
                    break;
                case PRODUCTION_DATE_ATTR_ID:
                    attributes.addProductionDate(offsetRecord.arealCoverageSequenceNumber, parseRpfProductionDate(bytes, offsetRecord));
                    break;
                case SIGNIFICANT_DATE_ATTR_ID:
                    attributes.addSignificantDate(offsetRecord.arealCoverageSequenceNumber, parseRpfSignificantDate(bytes, offsetRecord));
                    break;
                // TODO: the rest of the records
                case MAP_CHART_SOURCE_ATTR_ID:
                    break;
                case PROJECTION_SYSTEM_ATTR_ID:
                    break;
                case VERTICAL_DATUM_ATTR_ID:
                    break;
                case HORIZONTAL_DATUM_ATTR_ID:
                    break;
                case VERTICAL_ABSOLUTE_ACCURACY_ATTR_ID:
                    break;
                case HORIZONTAL_ABSOLUTE_ACCURACY_ATTR_ID:
                    break;
                case VERTICAL_RELATIVE_ACCURACY_ATTR_ID:
                    break;
                case HORIZONTAL_RELATIVE_ACCURACY_ATTR_ID:
                    break;
                case ELLIPSOID_ATTR_ID:
                    break;
                case SOUNDING_DATUM_ATTR_ID:
                    break;
                case NAVIGATION_SYSTEM_ATTR_ID:
                    break;
                case GRID_ATTR_ID:
                    break;
                case EASTERLY_ANNUAL_MAGNETIC_CHANGE_ATTR_ID:
                    break;
                case WESTERLY_ANNUAL_MAGNETIC_CHANGE_ATTR_ID:
                    break;
                case GRID_NORTH_MAGNETIC_NORTH_ANGLE_ATTR_ID:
                    break;
                case GRID_CONVERGENCE_ANGLE_ATTR_ID:
                    break;
                case HIGHEST_KNOWN_ELEVATION_ATTR_ID:
                    break;
                case MULTIPLE_LEGEND_ATTR_ID:
                    break;
                case IMAGE_SOURCE_ATTR_ID:
                    break;
                case DATA_LEVEL_ATTR_ID:
                    break;
                case CHART_UPDATE_INFORMATION_ATTR_ID:
                    break;
                case CONTOUR_INTERVAL_ATTR_ID:
                    break;
                default:
                    System.out.println(String.format("Unhandled offset record: %d/%d - %d", offsetRecord.attributeId, offsetRecord.parameterId,
                    offsetRecord.arealCoverageSequenceNumber));
                    break;
            }
        }
        // TODO: Areal records.
        return attributes;
    }

    private String parseRpfCurrencyDate(final ByteBuffer bytes, final OffsetRecord offsetRecord) throws ParseException {
        if (offsetRecord.parameterId != 1) {
            throw new ParseException("Unexpected parameter id when parsing currency date:" + offsetRecord.parameterId, bytes.position());
        }
        return readAsci(bytes, DATE_LENGTH);
    }

    private String parseRpfProductionDate(final ByteBuffer bytes, final OffsetRecord offsetRecord) throws ParseException {
        if (offsetRecord.parameterId != 1) {
            throw new ParseException("Unexpected parameter id when parsing production date:" + offsetRecord.parameterId, bytes.position());
        }
        return readAsci(bytes, DATE_LENGTH);
    }

    private String parseRpfSignificantDate(final ByteBuffer bytes, final OffsetRecord offsetRecord) throws ParseException {
        if (offsetRecord.parameterId != 1) {
            throw new ParseException("Unexpected parameter id when parsing significant date:" + offsetRecord.parameterId, bytes.position());
        }
        return readAsci(bytes, DATE_LENGTH);
    }

    private String readAsci(final ByteBuffer bytes, final int numBytes) {
        byte[] data = new byte[numBytes];
        bytes.get(data, 0, numBytes);
        try {
            return new String(data, "US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            LOG.warn("UnsupportedEncodingException while trying to convert to ASCII:", ex);
        }
        return "";
    }
}
