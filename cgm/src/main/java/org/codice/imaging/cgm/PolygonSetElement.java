/*
 * Copyright (c) 2014, 2016, Codice
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
import java.awt.Polygon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PolygonSetElement extends ElementHelpers {
    private static final Logger LOGGER = LoggerFactory.getLogger(PolygonSetElement.class);

    private final List<Point> points = new ArrayList<>();
    private final List<Integer> edgeOutFlags = new ArrayList<>();

    PolygonSetElement() {
        super(CgmIdentifier.POLYGON_SET);
    }

    @Override
    public void readParameters(final CgmInputReader dataReader, final int parameterListLength) throws IOException {
        int bytesRead = 0;
        while (bytesRead < parameterListLength) {
            Point point = dataReader.readPoint();
            points.add(point);
            bytesRead += dataReader.getNumberOfBytesInPoint();
            int edgeOutFlag = dataReader.readEnumValue();
            bytesRead += dataReader.getNumberOfBytesInEnumValue();
            edgeOutFlags.add(edgeOutFlag);
        }
    }

    @Override
    public void addStringDescription(final StringBuilder builder) {
        for (int pointIndex = 0; pointIndex < points.size(); ++pointIndex) {
            builder.append("\tPoint: ");
            builder.append(points.get(pointIndex));
            builder.append(System.lineSeparator());
            builder.append("\tEdge out flag: ");
            builder.append(edgeOutFlags.get(pointIndex));
            builder.append(System.lineSeparator());
        }
    }

    @Override
    public void render(final Graphics2D g2, final CgmGraphicState graphicState) {
        LOGGER.debug("figure out how to render edge out flags");
        applyFilledPrimitiveAttributes(g2, graphicState);
        Polygon polygon = new Polygon();
        for (int pointIndex = 0; pointIndex < points.size(); ++pointIndex) {
            Point point = points.get(pointIndex);
            polygon.addPoint(point.x, point.y);
        }
        g2.draw(polygon);
    }

}
