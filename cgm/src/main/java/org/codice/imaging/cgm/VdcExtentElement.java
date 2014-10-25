/*
 * Copyright (c) 2014, Codice
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.codice.imaging.cgm;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;

/**
 *
 * @author bradh
 */
class VdcExtentElement extends ElementHelpers implements AbstractElement {

    private VdcExtent vdcExtent;

    public VdcExtentElement() {
        super(CgmIdentifier.VDC_EXTENT);
    }

    @Override
    public void readParameters(final CgmInputReader dataReader, final int parameterListLength) throws IOException {
        Point lowerLeft = dataReader.readPoint();
        Point upperRight = dataReader.readPoint();
        vdcExtent = new VdcExtent(lowerLeft, upperRight);
    }

    @Override
    public void dumpParameters() {
        System.out.println("\tVDC Extent: " + vdcExtent.toString());
    }

    @Override
    public void render(final Graphics2D g2, final CgmGraphicState graphicState) {
        graphicState.setVdcExtent(vdcExtent);
        double scaleX = 1.0;
        double translateX = 0.0;
        if (vdcExtent.isIncreasingLeft()) {
            scaleX = -1.0;
            translateX = graphicState.getSizeX();
        }
        double scaleY = 1.0;
        double translateY = 0.0;
        if (vdcExtent.isIncreasingUp()) {
            scaleY = -1.0;
            translateY = (graphicState.getSizeY());
        }
        if (scaleX < 0) {
            g2.translate(translateX, 0);
        }
        if (scaleY < 0) {
            g2.translate(0, translateY);
        }
        g2.scale(scaleX, scaleY);
    }

}
