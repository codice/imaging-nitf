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
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.List;


class PolylineElement extends ElementHelpers implements AbstractElement {

    private List<Point> points;
    public PolylineElement() {
        super(CgmIdentifier.POLYLINE);
    }

    @Override
    public void readParameters(final CgmInputReader dataReader, final int parameterListLength) throws IOException {
        points = dataReader.readPoints(parameterListLength);
    }

    @Override
    public void dumpParameters() {
        for (Point point : points) {
            System.out.println("\tPoint: " + point);
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    @Override
    public void render(final Graphics2D g2, final CgmGraphicState graphicState) {
        g2.setColor(graphicState.getLineColour());
        g2.setStroke(graphicState.getLineStroke());
        GeneralPath line = new GeneralPath(Path2D.WIND_EVEN_ODD, points.size());
        line.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 1; i < points.size(); ++i) {
            line.lineTo(points.get(i).x, points.get(i).y);
        }
        g2.draw(line);
    }
}
