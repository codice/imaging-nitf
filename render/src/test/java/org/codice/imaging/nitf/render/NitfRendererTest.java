/*
 * Copyright (C) 2016 Codice Foundation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.codice.imaging.nitf.render;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageSegment;

import org.codice.imaging.nitf.core.image.impl.ImageBandImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

/**
 * Unit tests for NitfRenderer class.
 */
public class NitfRendererTest {

    private NitfRenderer renderer = new NitfRenderer();

    private ImageSegment mockImageSegmentHeader;

    private ImageBandImpl imageBandR;

    private ImageBandImpl imageBandG;

    private ImageBandImpl imageBandB;

    private ImageBandImpl imageBandM;

    private ImageBandImpl imageBandLU;

    public NitfRendererTest() {
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        mockImageSegmentHeader = Mockito.mock(ImageSegment.class);

        imageBandR = new ImageBandImpl();
        imageBandR.setImageRepresentation("R");

        imageBandG = new ImageBandImpl();
        imageBandG.setImageRepresentation("G");

        imageBandB = new ImageBandImpl();
        imageBandB.setImageRepresentation("B");

        imageBandM = new ImageBandImpl();
        imageBandM.setImageRepresentation("M");

        imageBandLU = new ImageBandImpl();
        imageBandLU.setImageRepresentation("LU");
    }

    @Test
    public void checkUnhandledCompressionException() throws IOException {
        // Mock up a render source
        Mockito.when(mockImageSegmentHeader.getImageCompression()).thenReturn(ImageCompression.UNKNOWN);

        // Check the exception
        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage("Unhandled image compression format: UNKNOWN");
        renderer.render(mockImageSegmentHeader, null);
    }


    @Test
    public void testRGBbands() {
        Mockito.when(mockImageSegmentHeader.getNumBands()).thenReturn(5);
        Mockito.when(mockImageSegmentHeader.getImageBandZeroBase(0)).thenReturn(imageBandLU);
        Mockito.when(mockImageSegmentHeader.getImageBandZeroBase(1)).thenReturn(imageBandM);
        Mockito.when(mockImageSegmentHeader.getImageBandZeroBase(2)).thenReturn(imageBandR);
        Mockito.when(mockImageSegmentHeader.getImageBandZeroBase(3)).thenReturn(imageBandG);
        Mockito.when(mockImageSegmentHeader.getImageBandZeroBase(4)).thenReturn(imageBandB);

        int[] sourceBands = renderer.getSourceBands(mockImageSegmentHeader);
        assertThat(sourceBands.length, is(3));
        assertThat(sourceBands[0], is(2));
        assertThat(sourceBands[1], is(3));
        assertThat(sourceBands[2], is(4));
    }

    @Test
    public void testLUband() {
        Mockito.when(mockImageSegmentHeader.getNumBands()).thenReturn(2);
        Mockito.when(mockImageSegmentHeader.getImageBandZeroBase(0)).thenReturn(imageBandM);
        Mockito.when(mockImageSegmentHeader.getImageBandZeroBase(1)).thenReturn(imageBandLU);

        int[] sourceBands = renderer.getSourceBands(mockImageSegmentHeader);
        assertThat(sourceBands.length, is(1));
        assertThat(sourceBands[0], is(1));
    }

    @Test
    public void testMband() {
        Mockito.when(mockImageSegmentHeader.getNumBands()).thenReturn(1);
        Mockito.when(mockImageSegmentHeader.getImageBandZeroBase(0)).thenReturn(imageBandM);

        int[] sourceBands = renderer.getSourceBands(mockImageSegmentHeader);
        assertThat(sourceBands.length, is(1));
        assertThat(sourceBands[0], is(0));
    }
}
