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
package org.codice.imaging.nitf.deswrap;

/**
 * Shared constants for CSSHPA creation and parsing.
 */
final class CSSHPAConstants {

    private CSSHPAConstants() {
    }

    static final String CLOUD_SHAPES_USE = "CLOUD_SHAPES";

    static final int SHAPE_CLASS_MULTIPOINT = 8;
    static final int SHAPE_CLASS_NULL = 0;
    static final int SHAPE_CLASS_POINTM = 21;
    static final int SHAPE_CLASS_POLYLINEZ = 13;
    static final int SHAPE_CLASS_MULTIPOINTZ = 18;
    static final int SHAPE_CLASS_POINTZ = 11;
    static final int SHAPE_CLASS_POLYLINEM = 23;
    static final int SHAPE_CLASS_MULTIPOINTM = 28;
    static final int SHAPE_CLASS_POINT = 1;
    static final int SHAPE_CLASS_POLYGONM = 25;
    static final int SHAPE_CLASS_POLYGONZ = 15;
    static final int SHAPE_CLASS_POLYLINE = 3;
    static final int SHAPE_CLASS_POLYGON = 5;
    static final int SHAPE_CLASS_MULTIPATCH = 31;
    static final int SHAPE_CLASS_LENGTH = 10;
    static final int SHAPE_FILENAME_LENGTH = 3;
    static final int CC_SOURCE_LENGTH = 18;
    static final int FILEOFFSET_LENGTH = 6;
    static final int SHAPE_TYPE_OFFSET = 32;
    static final int SHAPE_USE_LENGTH = 25;
}
