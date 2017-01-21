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
package org.codice.imaging.nitf.trewrap;

import java.time.LocalDate;
import java.util.Arrays;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreFactory;
import org.codice.imaging.nitf.core.tre.TreSource;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Tests for the PIAPEB wrapper.
 */
public class PIAPEB_WrapTest extends SharedTreTestSupport {

    TestLogger LOGGER = TestLoggerFactory.getTestLogger(TreWrapper.class);

    private final String mTestData = "PIAPEB00094Maxwell                     James                       Clerk                       18310613UK";
    private final String mAllSpaces = "PIAPEB00094                                                                                              ";
    
    public PIAPEB_WrapTest() {
    }

    @Test
    public void testGetters() throws NitfFormatException {
        Tre tre = parseTRE(mTestData, "PIAPEB");
        PIAPEB piapeb = new PIAPEB(tre);
        assertEquals("Maxwell", piapeb.getLastName());
        assertEquals("James", piapeb.getFirstName());
        assertEquals("Clerk", piapeb.getMiddleName());
        assertEquals(LocalDate.of(1831, 6, 13), piapeb.getBirthDate());
        assertEquals("UK", piapeb.getAssociatedCountryCode());
    }
    
    @Test
    public void testGettersAllSpaces() throws NitfFormatException {
        Tre tre = parseTRE(mAllSpaces, "PIAPEB");
        PIAPEB piapeb = new PIAPEB(tre);
        assertEquals("", piapeb.getLastName());
        assertEquals("", piapeb.getFirstName());
        assertEquals("", piapeb.getMiddleName());
        assertThat(LOGGER.getLoggingEvents().isEmpty(), is(true));
        assertEquals(null, piapeb.getBirthDate());
        assertThat(LOGGER.getLoggingEvents(), is(Arrays.asList(LoggingEvent.debug("Could not parse          as a local date: Text '        ' could not be parsed at index 0"))));
        assertEquals("  ", piapeb.getAssociatedCountryCode());
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void checkBadUsage() throws NitfFormatException {
        Tre tre = TreFactory.getDefault("PIAEVA", TreSource.ImageExtendedSubheaderData);
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Incorrect TRE name for PIAPEB wrapper");
        PIAPEB piapeb = new PIAPEB(tre);
    }

    @Test
    public void checkBuildOrder() throws NitfFormatException {
        PIAPEB piapeb = new PIAPEB(TreSource.ImageExtendedSubheaderData);
        piapeb.setLastName("AB");
        piapeb.setBirthDate(LocalDate.of(1987, 3, 15));
        piapeb.setMiddleName("CD");
        piapeb.setFirstName("EF");
        piapeb.setAssociatedCountry("US");
        assertEquals("AB", piapeb.getLastName());
        piapeb.setLastName("ZY");
        assertEquals("ZY", piapeb.getLastName());

        Tre tre = piapeb.getTRE();
        assertNotNull(tre);
        assertEquals("ZY", tre.getFieldValue("LASTNME"));

        byte[] serialisedTRE = piapeb.serialize();
        Assert.assertArrayEquals("ZY                          EF                          CD                          19870315US".getBytes(), serialisedTRE);
    }

    @Test
    public void checkBuildWithOnlyDefaults() throws NitfFormatException {
        PIAPEB piapeb = new PIAPEB(TreSource.UserDefinedImageData);
        byte[] serialisedTRE = piapeb.serialize();
        Assert.assertArrayEquals("                                                                                              ".getBytes(), serialisedTRE);
    }
    
    @Test
    public void checkBuildOrderPartialValues() throws NitfFormatException {
        PIAPEB piapeb = new PIAPEB(TreSource.ImageExtendedSubheaderData);
        piapeb.setBirthDate(LocalDate.of(1987, 3, 15));
        piapeb.setLastName("ZY");
        byte[] serialisedTRE = piapeb.serialize();
        Assert.assertArrayEquals("ZY                                                                                  19870315  ".getBytes(), serialisedTRE);
    }

    @Test
    public void checkBuild_IsValid() throws NitfFormatException {
        PIAPEB piapeb = new PIAPEB(TreSource.ExtendedHeaderData);
        assertTrue(piapeb.getValidity().isValid());
    }

    @Test
    public void checkBuild_IsValidNullLastNameValue() throws NitfFormatException {
        PIAPEB piapeb = new PIAPEB(TreSource.ImageExtendedSubheaderData);
        piapeb.setLastName(null);
        assertFalse(piapeb.getValidity().isValid());
        assertEquals("LASTNME may not be null", piapeb.getValidity().getValidityResultDescription());
    }

    @Test
    public void checkBuild_IsValidNullFirstNameValue() throws NitfFormatException {
        PIAPEB piapeb = new PIAPEB(TreSource.UserDefinedImageData);
        piapeb.setFirstName(null);
        assertFalse(piapeb.getValidity().isValid());
        assertEquals("FIRSTNME may not be null", piapeb.getValidity().getValidityResultDescription());
    }

    @Test
    public void checkBuild_IsValidNullMiddleNameValue() throws NitfFormatException {
        PIAPEB piapeb = new PIAPEB(TreSource.UserDefinedImageData);
        piapeb.setMiddleName(null);
        assertFalse(piapeb.getValidity().isValid());
        assertEquals("MIDNME may not be null", piapeb.getValidity().getValidityResultDescription());
    }

    @Test
    public void checkBuild_IsValidNullAssociatedCountryValue() throws NitfFormatException {
        PIAPEB piapeb = new PIAPEB(TreSource.ImageExtendedSubheaderData);
        piapeb.setAssociatedCountry(null);
        assertFalse(piapeb.getValidity().isValid());
        assertEquals("ASSOCTRY may not be null", piapeb.getValidity().getValidityResultDescription());
    }

    @Before
    public void clearLoggers() {
        TestLoggerFactory.clear();
    }
}
