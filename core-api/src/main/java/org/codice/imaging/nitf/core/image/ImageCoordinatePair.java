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
package org.codice.imaging.nitf.core.image;

/**
 A coordinate pair (latitude / longitude or equivalent).
 */
public interface ImageCoordinatePair {
    /**
     Return the latitude value.
     <p>
     North is positive.

     @return the latitude value.
     */
    double getLatitude();

    /**
     Return the longitude value.
     <p>
     East is positive.

     @return the longitude value.
     */
    double getLongitude();

    /**
     Return the original source format.

     @return the original source format.
     */
    String getSourceFormat();
}
