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

import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TargetIdTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testTargetIdDefaultConstructor() {
        TargetId tgtid = new TargetId();
        assertNotNull(tgtid);
    }

    @Test
    public void testTargetIdAccessors() {
        TargetId tgtid = new TargetId();
        assertNotNull(tgtid);

        tgtid.setBasicEncyclopediaNumber("ABCDEFGHIJ");
        assertEquals("ABCDEFGHIJ", tgtid.getBasicEncyclopediaNumber());

        tgtid.setOSuffix("UVWXY");
        assertEquals("UVWXY", tgtid.getOSuffix());

        tgtid.setCountryCode("AU");
        assertEquals("AU", tgtid.getCountryCode());

        assertEquals("ABCDEFGHIJUVWXYAU", tgtid.toString());
    }

    @Test
    public void testGoodTargetIdConstructorArgumentLength() throws NitfFormatException {
        String targetIdArgument = "ABCDEFGHIJUVWXYAU";
        TargetId tgtid = new TargetId(targetIdArgument);
        assertNotNull(tgtid);
        assertEquals("ABCDEFGHIJ", tgtid.getBasicEncyclopediaNumber());
        assertEquals("UVWXY", tgtid.getOSuffix());
        assertEquals("AU", tgtid.getCountryCode());
    }

    @Test
    public void testNullTargetIdConstructorArgument() throws NitfFormatException {
        String targetIdArgument = null;
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Null argument for TargetId");
        TargetId tgtid = new TargetId(targetIdArgument);
    }

    @Test
    public void testBadTargetIdConstructorArgumentLength() throws NitfFormatException {
        String targetIdArgument = "ABCDEFGHIJUVWXYA";
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length for TargetId:16");
        TargetId tgtid = new TargetId(targetIdArgument);
    }
}