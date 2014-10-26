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


class TextElement extends ElementHelpers implements AbstractElement {

    private Point textPosition;
    private int isFinal;
    private String text;

    public TextElement() {
        super(CgmIdentifier.TEXT);
    }

    @Override
    public void readParameters(final CgmInputReader dataReader, final int parameterListLength) throws IOException {
        textPosition = dataReader.readPoint();
        isFinal = dataReader.readEnumValue();
        text = dataReader.getStringFixed();
    }

    @Override
    public void addStringDescription(final StringBuilder builder) {
        builder.append("\tText: ");
        builder.append(textPosition);
        builder.append("|");
        builder.append(isFinal);
        builder.append(" : ");
        builder.append(text);
        builder.append(System.lineSeparator());
    }

    public String getText() {
        return text;
    }

    public Point getPosition() {
        return textPosition;
    }

    @Override
    public void render(final Graphics2D g2, final CgmGraphicState graphicState) {
        Graphics2D localGraphics = (Graphics2D) g2.create();
        localGraphics.setColor(graphicState.getTextColour());
        localGraphics.setFont(graphicState.getFont());
        if (graphicState.characterOrientationHasInvertedY()) {
            localGraphics.translate(0, graphicState.getSizeY());
            localGraphics.scale(1.0, -1.0);
        }
        localGraphics.drawString(text, textPosition.x, textPosition.y);
    }
}
