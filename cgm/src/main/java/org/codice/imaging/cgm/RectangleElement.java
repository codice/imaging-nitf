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


class RectangleElement extends ElementHelpers implements AbstractElement {

    private Point firstCorner;
    private Point secondCorner;

    public RectangleElement() {
        super(CgmIdentifier.RECTANGLE);
    }

    @Override
    public void readParameters(final CgmInputReader dataReader, final int parameterListLength) throws IOException {
        firstCorner = dataReader.readPoint();
        secondCorner = dataReader.readPoint();
    }

    @Override
    public void addStringDescription(final StringBuilder builder) {
        builder.append("\tFirst Point: ");
        builder.append(firstCorner);
        builder.append(System.lineSeparator());
        builder.append("\tSecond Point: ");
        builder.append(secondCorner);
        builder.append(System.lineSeparator());
    }

    @Override
    public void render(final Graphics2D g2, final CgmGraphicState graphicState) {
        applyFilledPrimitiveAttributes(g2, graphicState);
        int width = Math.abs(firstCorner.x - secondCorner.x);
        int height = Math.abs(firstCorner.y - secondCorner.y);
        int smallestX = Math.min(firstCorner.x, secondCorner.x);
        int smallestY = Math.min(firstCorner.y, secondCorner.y);
        g2.drawRect(smallestX, smallestY, width, height);
    }
}
