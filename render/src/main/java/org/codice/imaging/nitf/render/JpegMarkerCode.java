package org.codice.imaging.nitf.render;

public enum JpegMarkerCode {

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

    private short value;
    
    private JpegMarkerCode(short value) {
        this.value = value;    
    }

    public int getValue() {
        return this.value;
    }
}
