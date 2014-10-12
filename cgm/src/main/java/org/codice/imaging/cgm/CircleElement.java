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
import java.awt.geom.Ellipse2D;
import java.io.IOException;


class CircleElement extends ElementHelpers implements AbstractElement {

    Point centre;
    int radius;
    
    public CircleElement() {
        super(CgmIdentifier.CIRCLE);
    }

    @Override
    public void readParameters(CgmInputReader dataReader, int parameterListLength) throws IOException {
        centre = dataReader.readPoint();
        radius = dataReader.readSignedIntegerAtVdcIntegerPrecision();
    }
    
    @Override
    public void dumpParameters() {
        System.out.println("\tCentre: " + centre);
        System.out.println("\tRadius: " + radius);
    }

    @Override
    public void render(Graphics2D g2, CgmGraphicState graphicState) {
        applyFilledPrimitiveAttributes(g2, graphicState);
        Ellipse2D circle = new Ellipse2D.Float(centre.x - radius, centre.y - radius, radius * 2, radius * 2);
        g2.draw(circle);
    }

}
