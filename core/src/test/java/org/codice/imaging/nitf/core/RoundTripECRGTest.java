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

import java.io.IOException;
import java.net.URISyntaxException;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.junit.Test;

/**
 * Tests for ECRG profile of NITF.
 */
public class RoundTripECRGTest extends AbstractWriterTest {

    public RoundTripECRGTest() {
    }

    @Test
    public void roundTripECRG9s() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/ECRG/000000009s0013.lf2");
    }

    @Test
    public void roundTripECRG9t() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/ECRG/000000009t0013.lf2");
    }

    @Test
    public void roundTripECRGhh() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/ECRG/00000000hh0013.lf2");
    }

    @Test
    public void roundTripECRGhj() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/ECRG/00000000hj0013.lf2");
    }

    @Test
    public void roundTripECRGr8() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/ECRG/00000000r80013.lf2");
    }

    @Test
    public void roundTripECRGr9() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/ECRG/00000000r90013.lf2");
    }
}
