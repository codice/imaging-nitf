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
package org.codice.imaging.nitf.core;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class RGBColourTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testBadRGBColourConstructorArgumentLength() throws NitfFormatException {
        byte[] rgb = new byte[3];
        RGBColour goodColour = new RGBColour(rgb);
        assertNotNull(goodColour);
        byte[] shortArray = new byte[2];
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect number of bytes in RGB constructor array");
        RGBColour failColour = new RGBColour(shortArray);
    }

    @Test
    public void testConstructor3Arg() {
        RGBColour powderpuffBlue = new RGBColour((byte) 114, (byte) 136, (byte) 159);
        assertEquals((byte) 114, powderpuffBlue.getRed());
        assertEquals((byte) 136, powderpuffBlue.getGreen());
        assertEquals((byte) 159, powderpuffBlue.getBlue());
    }
}
