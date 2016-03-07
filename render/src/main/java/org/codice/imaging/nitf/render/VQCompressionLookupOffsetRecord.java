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
package org.codice.imaging.nitf.render;

class VQCompressionLookupOffsetRecord {

    private int compressionLookupTableId = 0;
    private int numberOfCompressionLookupRecords = 0;
    private int numberOfValuesPerCompressionLookupRecord = 0;
    private int compressionLookupValueBitLength = 0;
    private int compressionLookupTableOffset = 0;

    int getCompressionLookupTableId() {
        return compressionLookupTableId;
    }

    void setCompressionLookupTableId(final int tableId) {
        this.compressionLookupTableId = tableId;
    }

    int getNumberOfCompressionLookupRecords() {
        return numberOfCompressionLookupRecords;
    }

    void setNumberOfCompressionLookupRecords(final int numRecords) {
        this.numberOfCompressionLookupRecords = numRecords;
    }

    int getNumberOfValuesPerCompressionLookupRecord() {
        return numberOfValuesPerCompressionLookupRecord;
    }

    void setNumberOfValuesPerCompressionLookupRecord(final int numValuesPerRecord) {
        this.numberOfValuesPerCompressionLookupRecord = numValuesPerRecord;
    }

    int getCompressionLookupValueBitLength() {
        return compressionLookupValueBitLength;
    }

    void setCompressionLookupValueBitLength(final int bitLength) {
        this.compressionLookupValueBitLength = bitLength;
    }

    int getCompressionLookupTableOffset() {
        return compressionLookupTableOffset;
    }

    void setCompressionLookupTableOffset(final int tableOffset) {
        this.compressionLookupTableOffset = tableOffset;
    }
}
