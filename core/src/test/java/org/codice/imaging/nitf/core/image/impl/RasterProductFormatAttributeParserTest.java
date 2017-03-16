/**
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
 **/
package org.codice.imaging.nitf.core.image.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import java.nio.ByteBuffer;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.image.RasterProductFormatAttributes;
import org.junit.Before;
import org.junit.Test;

public class RasterProductFormatAttributeParserTest {
    private static final short RECORD_COUNT = 3;
    private static final short[] ATTRIBUTE_IDS = {1, 2, 3};
    private static final byte[] PARAMETER_IDS = {1, 1, 1};
    private static final byte[] AREAL_COVERAGE_SEQUENCE_NUMBERS = {1, 2, 3};
    private static final int[] ATTRIBUTE_RECORD_OFFSETS = {16, 24, 32};
    private static final String[] dates = {"01/01/15", "02/01/15", "03/01/15"};

    private ByteBuffer byteBuffer = ByteBuffer.allocate(77);

    @Before
    public void setup() {
        byteBuffer.putShort(RECORD_COUNT);

        for (int i = 0; i < PARAMETER_IDS.length; i++) {
            byteBuffer.putShort(ATTRIBUTE_IDS[i]);
            byteBuffer.put(PARAMETER_IDS[i]);
            byteBuffer.put(AREAL_COVERAGE_SEQUENCE_NUMBERS[i]);
            byteBuffer.putInt(ATTRIBUTE_RECORD_OFFSETS[i]);
        }

        for (int i = 0; i < dates.length; i++) {
            pushDateAsBytes(dates[i]);
        }

        byteBuffer.rewind();
    }

    private void pushDateAsBytes(String date) {
        byte[] chars = date.getBytes();

        for (byte value : chars) {
            byteBuffer.put(value);
        }
    }

    @Test
    public void testParseRpfDes() throws NitfFormatException {
        RasterProductFormatAttributeParser parser = new RasterProductFormatAttributeParser();
        RasterProductFormatAttributes rasterProductFormatAttributes = parser.parseRpfDes(byteBuffer);

        assertThat(rasterProductFormatAttributes.getCurrencyDate(AREAL_COVERAGE_SEQUENCE_NUMBERS[0]), is(dates[0]));
        assertThat(rasterProductFormatAttributes.getProductionDate(AREAL_COVERAGE_SEQUENCE_NUMBERS[1]), is(dates[1]));
        assertThat(rasterProductFormatAttributes.getSignificantDate(AREAL_COVERAGE_SEQUENCE_NUMBERS[2]), is(dates[2]));
    }
}
