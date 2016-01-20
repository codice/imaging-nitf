package org.codice.imaging.nitf.core.tre;

final class TreConstants {
    private TreConstants() {

    }

    /**
     * Length of the "Unique Extension Type Identifier" field in the Registered and controlled TRE format.
     * <p>
     * See MIL-STD-2500C Table A-7.
     */
    static final int TAG_LENGTH = 6;

    /**
     * Length of the "Length of REDATA Field" field in the Registered and controlled TRE format.
     * <p>
     * See MIL-STD-2500C Table A-7.
     */
    static final int TAGLEN_LENGTH = 5;

    static final String AND_CONDITION = " AND ";
    static final String UNSUPPORTED_IFTYPE_FORMAT_MESSAGE = "Unsupported format for iftype:";
}
