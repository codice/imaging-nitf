package org.codice.imaging.nitf.core.image;

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

import java.text.ParseException;

import org.codice.usng4j.CoordinateSystemTranslator;
import org.codice.usng4j.DecimalDegreesCoordinate;
import org.codice.usng4j.UsngCoordinate;
import org.codice.usng4j.UtmCoordinate;
import org.codice.usng4j.impl.CoordinateSystemTranslatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class CoordinateUtility {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoordinateUtility.class);

    private CoordinateUtility() {
    }

    private static CoordinateSystemTranslator coordinateSystemTranslator =
            new CoordinateSystemTranslatorImpl();

    static DecimalDegreesCoordinate buildCoordinateFromMgrs(final String mgrsString) {
        try {
            UsngCoordinate usngCoordinate = coordinateSystemTranslator.parseMgrsString(mgrsString);
            DecimalDegreesCoordinate latLonCoordinate = coordinateSystemTranslator.toLatLon(usngCoordinate);
            return latLonCoordinate;
        } catch (ParseException pe) {
            String message = String.format("MGRS input value: '%s' could not be parsed.", mgrsString);
            LOGGER.warn(message, pe);
        }

        return null;
    }

    static DecimalDegreesCoordinate buildCoordinateFromUtm(final String utmString) {
        try {
            UtmCoordinate utmCoordinate = coordinateSystemTranslator.parseUtmString(utmString);
            DecimalDegreesCoordinate latLonCoordinate = coordinateSystemTranslator.toLatLon(utmCoordinate);
            return latLonCoordinate;
        } catch (ParseException pe) {
            String message = String.format("UTM input value: '%s' could not be parsed.", utmString);
            LOGGER.warn(message, pe);
        }

        return null;
    }
}

