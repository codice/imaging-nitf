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
package org.codice.imaging.nitf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.codice.imaging.nitf.image.ImageCoordinatePair;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class ImageCoordinatePairTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testImageCoordinatePairDefaultConstructor() {
        ImageCoordinatePair coord = new ImageCoordinatePair();
        assertNotNull(coord);
    }

    @Test
    public void testImageCoordinatePairAccessors() {
        ImageCoordinatePair coord = new ImageCoordinatePair(-35.3761, 149.1018);
        assertNotNull(coord);
        assertEquals(-35.3761, coord.getLatitude(), 0.00001);
        assertEquals(149.1018, coord.getLongitude(), 0.00001);
    }

    @Test
    public void testNullArgumentDMS() throws ParseException {
        ImageCoordinatePair coord = new ImageCoordinatePair();
        exception.expect(ParseException.class);
        exception.expectMessage("Null argument for DMS parsing");
        coord.setFromDMS(null);
    }

    @Test
    public void testValidArgumentDMS() throws ParseException {
        ImageCoordinatePair coord = new ImageCoordinatePair();
        coord.setFromDMS("333019S1502203E");
        assertNotNull(coord);
        assertEquals(-33.5052, coord.getLatitude(), 0.0001);
        assertEquals(150.3675, coord.getLongitude(), 0.0001);
        assertEquals("333019S1502203E", coord.getSourceFormat());
    }

    @Test
    public void testBadArgumentLengthDMS() throws ParseException {
        ImageCoordinatePair coord = new ImageCoordinatePair();
        exception.expect(ParseException.class);
        exception.expectMessage("Incorrect length for DMS parsing:14");
        coord.setFromDMS("333019S1502203");
    }

    @Test
    public void testBadFirstHemisphereArgumentDMS() throws ParseException {
        ImageCoordinatePair coord = new ImageCoordinatePair();
        exception.expect(ParseException.class);
        exception.expectMessage("Incorrect format for N/S flag while DMS parsing: X(333019X1502203E)");
        coord.setFromDMS("333019X1502203E");
    }

    @Test
    public void testBadSecondHemisphereArgumentDMS() throws ParseException {
        ImageCoordinatePair coord = new ImageCoordinatePair();
        exception.expect(ParseException.class);
        exception.expectMessage("Incorrect format for E/W flag while DMS parsing: Y(333019S1502203Y)");
        coord.setFromDMS("333019S1502203Y");
    }

    @Test
    public void testNumberFormatArgumentDMS() throws ParseException {
        ImageCoordinatePair coord = new ImageCoordinatePair();
        exception.expect(ParseException.class);
        exception.expectMessage("Incorrect DMS format: 333019S1502x03E");
        coord.setFromDMS("333019S1502x03E");
    }
}
