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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.text.ParseException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ImageUtmCoordinateTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testImageCoordinatePairDefaultConstructor() {
        ImageUtmCoordinate coord = new ImageUtmCoordinate(ImageCoordinatesRepresentation.UTMUPSNORTH);
        assertNotNull(coord);
    }

    @Test
    public void testNullArgumentDMS() throws ParseException {
        ImageUtmCoordinate coord = new ImageUtmCoordinate(ImageCoordinatesRepresentation.UTMUPSNORTH);
        exception.expect(ParseException.class);
        exception.expectMessage("Null argument for UTM parsing");
        coord.setFromUtmUps(null);
    }

    @Test
    public void testValidArgumentDMS() throws ParseException {
        ImageUtmCoordinate coord = new ImageUtmCoordinate(ImageCoordinatesRepresentation.UTMUPSNORTH);
        coord.setFromUtmUps("331234561234567");
        assertNotNull(coord);
        assertThat(coord.getZone(), is(33));
        assertThat(coord.getEasting(), is(123456));
        assertThat(coord.getNorthing(), is(1234567));
        assertEquals("331234561234567", coord.getSourceFormat());
    }

    @Test
    public void testBadArgumentLengthDMS() throws ParseException {
        ImageUtmCoordinate coord = new ImageUtmCoordinate(ImageCoordinatesRepresentation.UTMUPSNORTH);
        exception.expect(ParseException.class);
        exception.expectMessage("Incorrect length for UTM / UPS North String");
        coord.setFromUtmUps("33BJK12345123452");
    }

    @Test
    public void testNumberFormatArgumentDMS() throws ParseException {
        ImageUtmCoordinate coord = new ImageUtmCoordinate(ImageCoordinatesRepresentation.UTMUPSNORTH);
        exception.expect(NumberFormatException.class);
        exception.expectMessage("For input string: \"AA\"");
        coord.setFromUtmUps("AA1234561234567");
    }

}
