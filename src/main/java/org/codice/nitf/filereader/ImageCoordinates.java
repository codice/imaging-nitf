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

public class ImageCoordinates {

    private ImageCoordinatePair coordinate00;
    private ImageCoordinatePair coordinate0MaxCol;
    private ImageCoordinatePair coordinateMaxRowMaxCol;
    private ImageCoordinatePair coordinateMaxRow0;

    public ImageCoordinates(ImageCoordinatePair coord00, ImageCoordinatePair coord0MaxCol,
                            ImageCoordinatePair coordMaxRowMaxCol, ImageCoordinatePair coordMaxRow0) {
        coordinate00 = coord00;
        coordinate0MaxCol = coord0MaxCol;
        coordinateMaxRowMaxCol = coordMaxRowMaxCol;
        coordinateMaxRow0 = coordMaxRow0;
    }

    public ImageCoordinatePair getCoordinate00() {
        return coordinate00;
    }

    public ImageCoordinatePair getCoordinate0MaxCol() {
        return coordinate0MaxCol;
    }

    public ImageCoordinatePair getCoordinateMaxRowMaxCol() {
        return coordinateMaxRowMaxCol;
    }

    public ImageCoordinatePair getCoordinateMaxRow0() {
        return coordinateMaxRow0;
    }
}