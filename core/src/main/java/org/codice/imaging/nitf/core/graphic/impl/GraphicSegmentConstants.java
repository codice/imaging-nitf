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
package org.codice.imaging.nitf.core.graphic.impl;

/**
 * Shared graphic segment values.
 */
public final class GraphicSegmentConstants {
    /**
     * Marker string for NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    public static final String SY = "SY";

    /**
     * Length of the Graphic Identifier in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    public static final int SID_LENGTH = 10;

    /**
     * Length of the "Graphic name" field in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    public static final int SNAME_LENGTH = 20;

    /**
     * Expected content of the "Graphic Type" field in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    public static final String SFMT_CGM = "C";

    /**
     * Default content for the the "Reserved for Future Use" SSTRUCT field in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    public static final String SSTRUCT = "0000000000000";

    /**
     * Length of the "Graphic Display Level" field in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    public static final int SDLVL_LENGTH = 3;

    /**
     * Length of the "Graphic Attachment Level" field in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    public static final int SALVL_LENGTH = 3;

    /**
     * Length of the half of the "Graphic Location" field in the NITF graphic segment
     * <p>
     * See MIL-STD-2500C Table A-5.
     * <p>
     * The actual length is twice this, since the format is RRRRRCCCCC, but we parse rows then columns.
     */
    public static final int SLOC_HALF_LENGTH = 5;

    /**
     * Length of half of the "First Graphic Bound Location" field in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     * <p>
     * The actual length is twice this, since the format is rrrrrccccc, but we parse rows then columns.
     */
    public static final int SBND1_HALF_LENGTH = 5;

    /**
     * Length of the "Graphic Colour" field in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    public static final int SCOLOR_LENGTH = 1;

    /**
     * Length of half of the "Second Graphic Bound Location" field in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     * <p>
     * The actual length is twice this, since the format is rrrrrccccc, but we parse rows then columns.
     */
    public static final int SBND2_HALF_LENGTH = 5;

    /**
     * Default value of the "Reserved for Figure Use" SRES2 field in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    public static final String SRES = "00";

    /**
     * Length of the "Graphic Extended Subheader Data Length" field in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    public static final int SXSHDL_LENGTH = 5;

    /**
     * Length of the "Graphic Extended Subheader Data" field in the NITF graphic segment.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    public static final int SXSOFL_LENGTH = 3;

    private GraphicSegmentConstants() {
    }
}
