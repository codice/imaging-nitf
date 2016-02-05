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
package org.codice.imaging.nitf.core.text;

final class TextConstants {

    private TextConstants() {
    }

    /**
     * Marker string for NITF text segment.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final String TE = "TE";

    /**
     * Length of the "Text Identifier" field in the NITF 2.1 text segment.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TEXTID_LENGTH = 7;

    /**
     * Length of the "Text Attachment Level" field in the NITF text segment.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TXTALVL_LENGTH = 3;

    /**
     * Length of the "Text Identifier" field in the NITF 2.0 text segment.
     * <p>
     * See MIL-STD-2500A.
     */
    protected static final int TEXTID20_LENGTH = 10;

    /**
     * Length of the "Text Title" field in the NITF text segment.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TXTITL_LENGTH = 80;

    /**
     * Length of the "Text Format" field in the NITF text segment.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TXTFMT_LENGTH = 3;

    /**
     * Length of the "Text Extended Subheader Data Length" field in the NITF text segment.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TXSHDL_LENGTH = 5;

    /**
     * Length of the "Text Extended Subheader Overflow" field in the NITF text segment.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TXSOFL_LENGTH = 3;
}
