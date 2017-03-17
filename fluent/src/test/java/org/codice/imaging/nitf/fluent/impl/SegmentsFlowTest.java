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
package org.codice.imaging.nitf.fluent.impl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for NitfSegmentsFlow.
 */
public class SegmentsFlowTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public SegmentsFlowTest() {
    }

    @Test
    public void NullSource() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("ImageSegmentFlow(): constructor argument 'dataSource' may not be null.");
        NitfSegmentsFlowImpl flow = new NitfSegmentsFlowImpl(null, null);
    }
}
