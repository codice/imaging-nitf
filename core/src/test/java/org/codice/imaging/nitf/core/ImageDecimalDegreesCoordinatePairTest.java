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
package org.codice.imaging.nitf.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ImageDecimalDegreesCoordinatePairTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testImageCoordinatePairDefaultConstructor() {
        ImageDecimalDegreesCoordinatePair coord = new ImageDecimalDegreesCoordinatePair();
        assertNotNull(coord);
    }

    @Test
    public void testImageCoordinatePairAccessors() {
        ImageDecimalDegreesCoordinatePair coord = new ImageDecimalDegreesCoordinatePair(-35.3761, 149.1018);
        assertNotNull(coord);
        assertEquals(-35.3761, coord.getLatitude(), 0.00001);
        assertEquals(149.1018, coord.getLongitude(), 0.00001);
    }

    @Test
    public void testNullArgumentDecimalDegrees() throws ParseException {
        ImageDecimalDegreesCoordinatePair coord = new ImageDecimalDegreesCoordinatePair();
        exception.expect(ParseException.class);
        exception.expectMessage("Null argument for decimal degrees parsing");
        coord.setFromDecimalDegrees(null);
    }

    @Test
    public void testValidArgumentDecimalDegrees() throws ParseException {
        ImageDecimalDegreesCoordinatePair coord = new ImageDecimalDegreesCoordinatePair();
        coord.setFromDecimalDegrees("-33.302+150.220");
        assertNotNull(coord);
        assertEquals(-33.302, coord.getLatitude(), 0.0001);
        assertEquals(150.22, coord.getLongitude(), 0.0001);
        assertEquals("-33.302+150.220", coord.getSourceFormat());
    }

    @Test
    public void testBadArgumentLengthDecimalDegrees() throws ParseException {
        ImageDecimalDegreesCoordinatePair coord = new ImageDecimalDegreesCoordinatePair();
        exception.expect(ParseException.class);
        exception.expectMessage("Incorrect length for decimal degrees parsing");
        coord.setFromDecimalDegrees("333019S1502203");
    }

    @Test
    public void testBadFirstHemisphereArgumentDecimalDegrees() throws ParseException {
        ImageDecimalDegreesCoordinatePair coord = new ImageDecimalDegreesCoordinatePair();
        exception.expect(ParseException.class);
        exception.expectMessage("Incorrect decimal degrees format: 333019X1502203E");
        coord.setFromDecimalDegrees("333019X1502203E");
    }

    @Test
    public void testBadSecondHemisphereArgumentDecimalDegrees() throws ParseException {
        ImageDecimalDegreesCoordinatePair coord = new ImageDecimalDegreesCoordinatePair();
        exception.expect(ParseException.class);
        exception.expectMessage("Incorrect decimal degrees format: 333019S1502203Y");
        coord.setFromDecimalDegrees("333019S1502203Y");
    }

    @Test
    public void testNumberFormatArgumentDecimalDegrees() throws ParseException {
        ImageDecimalDegreesCoordinatePair coord = new ImageDecimalDegreesCoordinatePair();
        exception.expect(ParseException.class);
        exception.expectMessage("Incorrect decimal degrees format: 333019S1502x03E");
        coord.setFromDecimalDegrees("333019S1502x03E");
    }
}
