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
package org.codice.imaging.nitf.core.image.impl;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.codice.imaging.nitf.core.image.TargetId;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TargetIdTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testTargetIdDefaultConstructor() {
        TargetId tgtid = new TargetIdImpl();
        assertNotNull(tgtid);
    }

    @Test
    public void testTargetIdAccessors() throws NitfFormatException {
        TargetIdImpl tgtid = new TargetIdImpl();
        assertNotNull(tgtid);

        tgtid.setBasicEncyclopediaNumber("ABCDEFGHIJ");
        assertEquals("ABCDEFGHIJ", tgtid.getBasicEncyclopediaNumber());

        tgtid.setOSuffix("UVWXY");
        assertEquals("UVWXY", tgtid.getOSuffix());

        tgtid.setCountryCode("AU");
        assertEquals("AU", tgtid.getCountryCode());

        assertEquals("ABCDEFGHIJUVWXYAU", tgtid.textValue());
    }

    @Test
    public void testPartialFillCountryCode() throws NitfFormatException {
        TargetIdImpl targetId = new TargetIdImpl();
        targetId.setCountryCode("AS");
        assertEquals("               AS", targetId.textValue());
    }

    @Test
    public void testPartialFillOSuffix() throws NitfFormatException {
        TargetIdImpl targetId = new TargetIdImpl();
        // This example is a bit silly, but we're unit testing.
        targetId.setOSuffix("UVWXY");
        assertEquals("          UVWXY  ", targetId.textValue());
    }

    @Test
    public void testPartialFillBEandCountryCode() throws NitfFormatException {
        TargetIdImpl targetId = new TargetIdImpl();
        targetId.setBasicEncyclopediaNumber("ABCDEFGHIJ");
        targetId.setCountryCode("AS");
        assertEquals("ABCDEFGHIJ     AS", targetId.textValue());
    }

    @Test
    public void testGoodTargetIdConstructorArgumentLength() throws NitfFormatException {
        String targetIdArgument = "ABCDEFGHIJUVWXYAU";
        TargetId tgtid = new TargetIdImpl(targetIdArgument);
        assertNotNull(tgtid);
        assertEquals("ABCDEFGHIJ", tgtid.getBasicEncyclopediaNumber());
        assertEquals("UVWXY", tgtid.getOSuffix());
        assertEquals("AU", tgtid.getCountryCode());
    }

    @Test
    public void testNullTargetIdConstructorArgument() throws NitfFormatException {
        String targetIdArgument = null;
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Null argument for TargetIdImpl");
        TargetId tgtid = new TargetIdImpl(targetIdArgument);
    }

    @Test
    public void testBadTargetIdConstructorArgumentLength() throws NitfFormatException {
        String targetIdArgument = "ABCDEFGHIJUVWXYA";
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length for TargetIdImpl:16");
        TargetId tgtid = new TargetIdImpl(targetIdArgument);
    }

    @Test
    public void testToStringWithNullBE() throws NitfFormatException {
        TargetIdImpl targetId = new TargetIdImpl();
        targetId.setBasicEncyclopediaNumber(null);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Cannot generate string target identifier with null BE number");
        targetId.textValue();
    }

    @Test
    public void testToStringWithNullOSuffix() throws NitfFormatException {
        TargetIdImpl targetId = new TargetIdImpl();
        targetId.setOSuffix(null);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Cannot generate string target identifier with null O-suffix");
        targetId.textValue();
    }

    @Test
    public void testToStringWithNullCountryCode() throws NitfFormatException {
        TargetIdImpl targetId = new TargetIdImpl();
        targetId.setCountryCode(null);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Cannot generate string target identifier with null country code");
        targetId.textValue();
    }

    @Test
    public void testToStringWithTooLongBE() throws NitfFormatException {
        TargetIdImpl targetId = new TargetIdImpl();
        targetId.setBasicEncyclopediaNumber("ABCDEFGHIJK");
        exception.expect(NitfFormatException.class);
        exception.expectMessage("TargetIdImpl field value is too long");
        targetId.textValue();
    }
}
