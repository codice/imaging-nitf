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
 * Test files from the AFRL "Green 2007" data set
 */
public class RoundTripNitfGreen2007Test extends AbstractWriterTest {

    public RoundTripNitfGreen2007Test() {
    }

    @Test
    public void roundTripNitfFile103243R0() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/Green2007/TimeStep103243.ntf.r0");
    }

    @Test
    public void roundTripNitfFile103498R5() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/Green2007/TimeStep103498.ntf.r5");
    }
}
