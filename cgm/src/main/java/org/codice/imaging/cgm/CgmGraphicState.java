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
    
    private VdcExtent vdcExtent;
    private final int sizeX;
    private final int sizeY;
    
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    // Default from ISO/IEC 8632-1:1999(E), and MITRE_LIMIT attribute
    // element is prohibited in BIIF Profile BPCGM01.10
    private static final float MITRE_LIMIT = 32767.0f;
    private static final float[] DASH = {10.0f};
    private static final float[] DOT = {2.0f};
    private static final float[] DASH_DOT = {10.0f, 10.0f, 2.0f, 2.0f};
    private static final float[] DASH_DOT_DOT = {10.0f, 10.0f, 2.0f, 2.0f, 2.0f, 2.0f};
    
    // TODO: getters / setters
    Color lineColour;
    float lineWidth;
    private int lineType = 0;
    private static final int LINE_JOIN_STYLE = BasicStroke.JOIN_BEVEL;
    private static final int LINE_CAP_STYLE = BasicStroke.CAP_SQUARE;
    BasicStroke lineStroke = new BasicStroke();

    private Color edgeColour;
    private float edgeWidth;
    private static final int EDGE_JOIN_STYLE = BasicStroke.JOIN_BEVEL;
    private static final int EDGE_CAP_STYLE = BasicStroke.CAP_SQUARE;
    private EdgeVisibilityElement.Mode edgeVisibility;
    private BasicStroke edgeStroke = new BasicStroke();

    private int hatchIndex = 1;

    Color textColour;
    Font font = Font.decode("Serif");
    List<String> fontList = new ArrayList<>();
    private int fontIndex = 0;
    private int textFontHeight = 12;
    private boolean characterOrientationIsInvertedY = false;

    CgmGraphicState(int x, int y) {
        sizeX = x;
        sizeY = y;
        this.edgeVisibility = EdgeVisibilityElement.Mode.OFF;
    }

    void setLineWidth(float size) {
        lineWidth = size;
        updateLineStroke();
    }

    void setLineType(int indexedValue) {
        lineType = indexedValue;
        updateLineStroke();
    }

    private void updateLineStroke() {
        switch (lineType) {
            case 1:
                lineStroke = new BasicStroke(lineWidth, LINE_CAP_STYLE, LINE_JOIN_STYLE, MITRE_LIMIT);
                break;
            case 2:
                lineStroke = new BasicStroke(lineWidth, LINE_CAP_STYLE, LINE_JOIN_STYLE, MITRE_LIMIT, DASH, 0.0f);
                break;
            case 3:
                lineStroke = new BasicStroke(lineWidth, LINE_CAP_STYLE, LINE_JOIN_STYLE, MITRE_LIMIT, DOT, 0.0f);
                break;
            case 4:
                lineStroke = new BasicStroke(lineWidth, LINE_CAP_STYLE, LINE_JOIN_STYLE, MITRE_LIMIT, DASH_DOT, 0.0f);
                break;
            case 5:
                lineStroke = new BasicStroke(lineWidth, LINE_CAP_STYLE, LINE_JOIN_STYLE, MITRE_LIMIT, DASH_DOT_DOT, 0.0f);
                break;
            default:
                // TODO: log warning.
                lineStroke = new BasicStroke(lineWidth, LINE_CAP_STYLE, LINE_JOIN_STYLE, MITRE_LIMIT);
                break;
        }
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
        if (getEdgeVisibility() == EdgeVisibilityElement.Mode.OFF) {
            return TRANSPARENT;
        }
        return edgeColour;
    }

    void setEdgeWidth(int size) {
        edgeWidth = size;
        updateEdgeStroke();
    }

    Stroke getEdgeStroke() {
        return edgeStroke;
    }
    
    void setEdgeVisibility(EdgeVisibilityElement.Mode mode) {
        edgeVisibility = mode;
    }
    
    EdgeVisibilityElement.Mode getEdgeVisibility() {
        return edgeVisibility;
    }

    private void updateEdgeStroke() {
        edgeStroke = new BasicStroke(edgeWidth, EDGE_CAP_STYLE, EDGE_JOIN_STYLE);
    }

    void setHatchIndex(int indexedValue) {
        hatchIndex = indexedValue;
    }
    
    int getHatchIndex() {
        return hatchIndex;
    }

    double getSizeX() {
        return sizeX;
    }

    double getSizeY() {
        return sizeY;
    }

    void setVdcExtent(VdcExtent extent) {
        vdcExtent = extent;
    }

    VdcExtent getVdcExtent() {
        return vdcExtent;
    }

    void setCharacterOrientationInvertY(boolean isInverted) {
        characterOrientationIsInvertedY = isInverted;
    }
    
    boolean characterOrientationHasInvertedY() {
        return characterOrientationIsInvertedY;
    }
}
