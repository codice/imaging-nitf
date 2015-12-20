package org.codice.imaging.nitf.core.common;

/**
 * Constants used by classes within the 'common' package.
 */
final class CommonConstants {

    /**
     * Length of the "ENCRYP" field used in multiple headers.
     * <p>
     * See, for example, MIL-STD-2500C Table A-1.
     */
    static final int ENCRYP_LENGTH = 1;

    /**
     * Length of an RGB colour field.
     * <p>
     * See, for example, FBKGC in MIL-STD-2500C Table A-1.
     */
    static final int RGB_COLOUR_LENGTH = 3;

    // Dates
    /**
     * The length of a "proper" formatted date.
     */
    static final int STANDARD_DATE_TIME_LENGTH = 14;

    /**
     * The date format used by NITF 2.1.
     */
    static final String NITF21_DATE_FORMAT = "yyyyMMddHHmmss";

    /**
     * The date format used by NITF 2.0.
     */
    static final String NITF20_DATE_FORMAT = "ddHHmmss'Z'MMMyy";


    private CommonConstants() {
    }
}
