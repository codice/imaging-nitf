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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
class InteriorStyleElement extends ElementHelpers implements AbstractElement {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteriorStyleElement.class);
    private Mode mode = Mode.HOLLOW;

    private static final int STYLE_HOLLOW = 0;
    private static final int STYLE_SOLID = 1;
    private static final int STYLE_PATTERN = 2;
    private static final int STYLE_HATCH = 3;
    private static final int STYLE_EMPTY = 4;
    private static final int STYLE_GEOMETRIC_PATTERN = 5;
    private static final int STYLE_INTERPOLATED = 6;

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

    @Override
    public void readParameters(final CgmInputReader inputReader, final int parameterListLength) throws IOException {
        int data = inputReader.readEnumValue();
        switch (data) {
            case STYLE_HOLLOW:
                mode = Mode.HOLLOW;
                break;
            case STYLE_SOLID:
                mode = Mode.SOLID;
                break;
            case STYLE_PATTERN:
                mode = Mode.PATTERN;
                break;
            case STYLE_HATCH:
                mode = Mode.HATCH;
                break;
            case STYLE_EMPTY:
                mode = Mode.EMPTY;
                break;
            case STYLE_GEOMETRIC_PATTERN:
                mode = Mode.GEOMETRIC_PATTERN;
                break;
            case STYLE_INTERPOLATED:
                mode = Mode.INTERPOLATED;
                break;
            default:
                LOGGER.info("Unknown Interior Style value: " + data);
                break;
        }
    }

    @Override
    public void addStringDescription(final StringBuilder builder) {
        builder.append("\tInterior Style: ");
        builder.append(mode);
        builder.append(System.lineSeparator());
    }

    @Override
    public void render(final Graphics2D g2, final CgmGraphicState graphicState) {
        LOGGER.debug("TODO: render for " + getFriendlyName());
        StringBuilder builder = new StringBuilder();
        addStringDescription(builder);
        LOGGER.debug(builder.toString());
    }

}
