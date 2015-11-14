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
import java.awt.geom.Arc2D;
import java.io.IOException;

class CircularArcCentreElement extends ElementHelpers implements AbstractElement {

    private Point centre;
    private int deltaXforStartVector;
    private int deltaYforStartVector;
    private int deltaXforEndVector;
    private int deltaYforEndVector;
    private int radius;

    private static final double FULL_CIRCLE = 360.0;

    public CircularArcCentreElement() {
        super(CgmIdentifier.CIRCULAR_ARC_CENTRE);
    }

    @Override
    public void readParameters(final CgmInputReader dataReader, final int parameterListLength) throws IOException {
        centre = dataReader.readPoint();
        deltaXforStartVector = dataReader.readSignedIntegerAtVdcIntegerPrecision();
        deltaYforStartVector = dataReader.readSignedIntegerAtVdcIntegerPrecision();
        deltaXforEndVector = dataReader.readSignedIntegerAtVdcIntegerPrecision();
        deltaYforEndVector = dataReader.readSignedIntegerAtVdcIntegerPrecision();
        radius = dataReader.readSignedIntegerAtVdcIntegerPrecision();
    }

    @Override
    public void addStringDescription(final StringBuilder builder) {
        builder.append("\tCentre: ");
        builder.append(centre);
        builder.append(System.lineSeparator());
        builder.append("\tRadius: ");
        builder.append(radius);
        builder.append(System.lineSeparator());
    }

    @Override
    public void render(final Graphics2D g2, final CgmGraphicState graphicState) {
        g2.setColor(graphicState.getLineColour());
        g2.setStroke(graphicState.getLineStroke());
        double startAngle = -1.0 * Math.toDegrees(Math.atan2(deltaYforStartVector, deltaXforStartVector));
        double endAngle = -1.0 * Math.toDegrees(Math.atan2(deltaYforEndVector, deltaXforEndVector));
        double extent = endAngle - startAngle;
        if (extent > 0.0) {
            extent -= FULL_CIRCLE;
        }
        Arc2D arc = new Arc2D.Double(centre.x - radius, centre.y - radius, radius * 2, radius * 2, startAngle, extent, Arc2D.OPEN);
        g2.draw(arc);
    }

}
