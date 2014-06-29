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
package org.codice.nitf.filereader;

public enum ImageCategory
{
// TODO: the rest
    UNKNOWN (""),
    VISUAL ("VIS"),
    SIDELOOKINGRADAR ("SL"),
    THERMALINFRARED ("TI"),
    FLIR ("FL"),
    RADAR ("RD"),
    ELECTROOPTICAL ("RO"),
    OPTICAL ("OP"),
    HIGRESRADAR ("HR"),
    HYPERSPECTRAL ("HS"),
    COLOURFRAMEPHOTO ("CP"),
    BLACKWHITEFRAMEPHOTO ("BP"),
    SYNTHETICAPERTURERADAR ("SAR"),
    SARRADIOHOLOGRAM ("SARIQ"),
    INFRARED ("IR"),
    MULTISPECTRAL ("MS"),
    FINGERPRINTS ("FP"),
    MAGNETICRESONANCEIMAGERY ("MRI"),
    XRAY ("XRAY"),
    CATSCAN ("CAT"),
    VIDEO ("VD"),
    BAROMETRICPRESSURE ("BARO"),
    WATERCURRENT ("CURRENT"),
    WATERDEPTH ("DEPTH"),
    AIRWINDCHART ("WIND"),
    RASTERMAP ("MAP"),
    COLOURPATCH ("PAT"),
    LEGEND ("LEG"),
    ELEVATIONMODEL ("DTEM"),
    MATRIXDATA ("MATR"),
    LOCATIONGRID ("LOCG");

    private final String textEquivalent;
    ImageCategory(String abbreviation) {
        this.textEquivalent = abbreviation;
    }
    static public ImageCategory getEnumValue(String textEquivalent) {
        for (ImageCategory icat : values()) {
            if (textEquivalent.equals(icat.textEquivalent)) {
                return icat;
            }
        }
        return UNKNOWN;
    }
};

