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
package org.codice.imaging.nitf.core.label;

final class LabelConstants {
    // label segment
    /**
     * Marker string for NITF Label subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final String LA = "LA";

    /**
     * Length of the "Label Identifier" field in the NITF 2.0 Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LID_LENGTH = 10;

    /**
     * Length of the "Label Font Style" field in the NITF Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LFS_LENGTH = 1;

    /**
     * Length of the "Label Cell Width" field in the NITF Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LCW_LENGTH = 2;

    /**
     * Length of the "Label Cell Height" field in the NITF Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LCH_LENGTH = 2;

    /**
     * Length of the "Label Display Level" field in the NITF 2.0 label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LDLVL_LENGTH = 3;

    /**
     * Length of the "Label Attachment Level" field in the NITF label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LALVL_LENGTH = 3;

    /**
     * Length of half of the "Label Location" field in the NITF label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     * <p>
     * The actual length is twice this, since the format is rrrrrccccc, but we parse rows then columns.
     */
    protected static final int LLOC_HALF_LENGTH = 5;

    /**
     * Length of the "Extended Subheader Data Length" field in the NITF label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LXSHDL_LENGTH = 5;

    /**
     * Length of the "Extended Subheader Overflow" field in the NITF label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LXSOFL_LENGTH = 3;


    private LabelConstants() {
    }
}
