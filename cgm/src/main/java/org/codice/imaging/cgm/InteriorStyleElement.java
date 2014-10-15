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
import java.io.IOException;

/**
 *
 */
class InteriorStyleElement extends ElementHelpers implements AbstractElement {
    InteriorStyleElement() {
        super(CgmIdentifier.INTERIOR_STYLE);
    }

    enum Mode {
        HOLLOW,
        SOLID,
        PATTERN,
        HATCH,
        EMPTY,
        GEOMETRIC_PATTERN,
        INTERPOLATED
    }
    Mode mode = Mode.HOLLOW;
    
    @Override
    public void readParameters(CgmInputReader inputReader, int parameterListLength) throws IOException {
        int data = inputReader.readEnumValue();
        switch (data) {
            case 0:
                mode = Mode.HOLLOW;
                break;
            case 1:
                mode = Mode.SOLID;
                break;
            case 2:
                mode = Mode.PATTERN;
                break;
            case 3:
                mode = Mode.HATCH;
                break;
            case 4:
                mode = Mode.EMPTY;
                break;
            case 5:
                mode = Mode.GEOMETRIC_PATTERN;
                break;
            case 6:
                mode = Mode.INTERPOLATED;
                break;
            default:
                System.out.println("Unknown Interior Style value: " + data);
                break;
        }
    }

    @Override
    public void dumpParameters() {
        System.out.println("\tInterior Style: " + mode);
    }
    
    @Override
    public void render(Graphics2D g2, CgmGraphicState graphicState) {
        System.out.println("TODO: render for " + getFriendlyName());
    }

}
