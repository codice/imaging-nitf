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
package org.codice.imaging.nitf.render;

import java.io.IOException;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.junit.Test;

/**
 * Additional tests for MULTI rendering.
 *
 * There are some tests for multiband in various modes across the JITC samples. These supplement those tests for
 * additional cases.
 */
public class MultibandRenderTest extends RenderTestSupport {

    public MultibandRenderTest(String testname) {
        super(testname);
    }

    @Test
    public void testThreeBandMono() throws IOException, NitfFormatException {
        testOneFile("merlionM.ntf", "Codice");
    }

    @Test
    public void testTwoBandLUT() throws IOException, NitfFormatException {
        testOneFile("LUinBand2.ntf", "Codice");
    }

    @Test
    public void testSpaceFilledIREPBANDs() throws IOException, NitfFormatException {
        testOneFile("blank_irepbands.ntf", "Codice");
    }

}
