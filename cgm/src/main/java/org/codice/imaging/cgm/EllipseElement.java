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

class EllipseElement extends ElementHelpers implements AbstractElement {

    private Point centre;
    private Point endpointOfFirstConjugateDiameter;
    private Point endpointOfSecondConjugateDiameter;

    public EllipseElement() {
        super(CgmIdentifier.ELLIPSE);
    }

    @Override
    public void readParameters(final CgmInputReader dataReader, final int parameterListLength) throws IOException {
        centre = dataReader.readPoint();
        endpointOfFirstConjugateDiameter = dataReader.readPoint();
        endpointOfSecondConjugateDiameter = dataReader.readPoint();
    }

    @Override
    public void addStringDescription(final StringBuilder builder) {
        builder.append("\tCentre: ");
        builder.append(centre);
        builder.append(System.lineSeparator());
        builder.append("\tEndpoint of first conjugate diameter: ");
        builder.append(endpointOfFirstConjugateDiameter);
        builder.append(System.lineSeparator());
        builder.append("\tEndpoint of second conjugate diameter: ");
        builder.append(endpointOfSecondConjugateDiameter);
        builder.append(System.lineSeparator());
    }

    @Override
    public void render(final Graphics2D g2, final CgmGraphicState graphicState) {
        applyFilledPrimitiveAttributes(g2, graphicState);
        float xOffset = Math.max(Math.abs(centre.x - endpointOfFirstConjugateDiameter.x), Math.abs(centre.x - endpointOfSecondConjugateDiameter.x));
        float yOffset = Math.max(Math.abs(centre.y - endpointOfFirstConjugateDiameter.y), Math.abs(centre.y - endpointOfSecondConjugateDiameter.y));
        System.out.println(String.format("Offsets: %f, %f", xOffset, yOffset));
        Ellipse2D ellipse = new Ellipse2D.Float(centre.x - xOffset, centre.y - yOffset, xOffset * 2, yOffset * 2);
        g2.draw(ellipse);
    }

}
