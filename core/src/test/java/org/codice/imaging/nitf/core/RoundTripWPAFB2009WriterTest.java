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
 * Tests from the AFRL WPAFB 2009 data set.
 */
public class RoundTripWPAFB2009WriterTest extends AbstractWriterTest {

    public RoundTripWPAFB2009WriterTest() {
    }

    @Test
    public void roundTripWPAFBr0() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/WPAFB-21Oct2009/20091021203850-01001116-VIS.ntf.r0");
    }

    @Test
    public void roundTripWPAFBr1() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/WPAFB-21Oct2009/20091021203850-01001116-VIS.ntf.r1");
    }

    @Test
    public void roundTripWPAFBr2() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/WPAFB-21Oct2009/20091021203850-01001116-VIS.ntf.r2");
    }

    @Test
    public void roundTripWPAFBr3() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/WPAFB-21Oct2009/20091021203850-01001116-VIS.ntf.r3");
    }

    @Test
    public void roundTripWPAFBr4() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/WPAFB-21Oct2009/20091021203850-01001116-VIS.ntf.r4");
    }

    @Test
    public void roundTripWPAFBr5() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/WPAFB-21Oct2009/20091021203850-01001116-VIS.ntf.r5");
    }
}
