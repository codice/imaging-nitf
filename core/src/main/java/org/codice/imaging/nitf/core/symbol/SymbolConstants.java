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
package org.codice.imaging.nitf.core.symbol;

/**
 * Constants used in symbol parsing.
 */

public final class SymbolConstants {
    // Symbol (Graphic) Segment
    /**
     * Length of the "Symbol Type" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int SYTYPE_LENGTH = 1;

    /**
     * Length of the "Number of Lines Per Symbol" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int NLIPS_LENGTH = 4;

    /**
     * Length of the "Number of Pixels Per Line" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int NPIXPL_LENGTH = 4;

    /**
     * Length of the "Line Width" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int NWDTH_LENGTH = 4;

    /**
     * Length of the "Number of Bits Per Pixel" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int SYNBPP_LENGTH = 1;

    /**
     * Length of the "Symbol Number" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int SNUM_LENGTH = 6;

    /**
     * Length of the "Symbol Rotation" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int SROT_LENGTH = 3;

    /**
     * Length of the "Number of LUT Entries" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int SYNELUT_LENGTH = 3;

    private SymbolConstants() {
    }
}
