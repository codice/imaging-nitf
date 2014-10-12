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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bradh
 */
class CgmGraphicState {
    // TODO: getters / setters
    Color lineColour;
    float lineWidth;
    int lineJoinStyle = BasicStroke.JOIN_MITER;
    int lineCapStyle = BasicStroke.CAP_SQUARE;
    BasicStroke lineStroke = new BasicStroke();

    private Color edgeColour;
    private float edgeWidth;
    private int edgeJoinStyle = BasicStroke.JOIN_MITER;
    private int edgeCapStyle = BasicStroke.CAP_SQUARE;
    private BasicStroke edgeStroke = new BasicStroke();

    private int hatchIndex = 1;

    Color textColour;
    Font font = Font.decode("Serif");
    List<String> fontList = new ArrayList<>();
    private int fontIndex = 0;
    private int textFontHeight = 12;

    void setLineWidth(float size) {
        lineWidth = size;
        updateStroke();
    }

    private void updateStroke() {
        lineStroke = new BasicStroke(lineWidth, lineCapStyle, lineJoinStyle);
    }

    void setLineColour(Color colour) {
        lineColour = colour;
    }
    
    void setTextColour(Color colour) {
        textColour = colour;
    }

    void setFontList(List<String> fonts) {
        // TODO: the font list needs to be translated to font names we have...
        fontList = fonts;
    }

    void setTextFontIndex(int indexedValue) {
        fontIndex = indexedValue - 1;
        updateFont();
    }

    void setCharacterHeight(int characterHeight) {
        textFontHeight = characterHeight;
        updateFont();
    }

    private void updateFont() {
        font = new Font(fontList.get(fontIndex), Font.PLAIN, textFontHeight);
    }

    void setEdgeColour(Color colour) {
        edgeColour = colour;
    }

    Color getEdgeColour() {
        return edgeColour;
    }

    void setEdgeWidth(int size) {
        edgeWidth = size;
        updateEdgeStroke();
    }

    Stroke getEdgeStroke() {
        return edgeStroke;
    }

    private void updateEdgeStroke() {
        edgeStroke = new BasicStroke(edgeWidth, edgeCapStyle, edgeJoinStyle);
    }

    void setHatchIndex(int indexedValue) {
        hatchIndex = indexedValue;
    }
    
    int getHatchIndex() {
        return hatchIndex;
    }
}
