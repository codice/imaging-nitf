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

/**
 * Marker codes found in JPEG files.
 *
 * Some of these are JPEG 2000 compatible.
 */
enum JpegMarkerCode {

    BASELINE_DCT((short) 0xFFC0),
    EXTENDED_SEQUENTIAL_DCT((short) 0xFFC1),
    DEFINE_HUFFMAN_TABLES((short) 0xFFC4),
    RESTART_WITH_MODULO_8_COUNT_0((short) 0xFFD0),
    RESTART_WITH_MODULO_8_COUNT_1((short) 0xFFD1),
    RESTART_WITH_MODULO_8_COUNT_2((short) 0xFFD2),
    RESTART_WITH_MODULO_8_COUNT_3((short) 0xFFD3),
    RESTART_WITH_MODULO_8_COUNT_4((short) 0xFFD4),
    RESTART_WITH_MODULO_8_COUNT_5((short) 0xFFD5),
    RESTART_WITH_MODULO_8_COUNT_6((short) 0xFFD6),
    RESTART_WITH_MODULO_8_COUNT_7((short) 0xFFD7),
    START_OF_IMAGE((short) 0xFFD8),
    END_OF_IMAGE((short) 0xFFD9),
    START_OF_SCAN((short) 0xFFDA),
    DEFINE_QUANTIZATION_TABLES((short) 0xFFDB),
    DEFINE_RESTART_INTERVAL((short) 0xFFDD),
    NITF_APPLICATION_SEGMENT((short) 0xFFE6),
    COMMENT((short) 0xFFFE);

    private final short value;

    private JpegMarkerCode(short value) {
        this.value = value;
    }

    /**
     * Value of the marker code.
     *
     * @return integer value of the marker code
     */
    public int getValue() {
        return this.value;
    }
}
