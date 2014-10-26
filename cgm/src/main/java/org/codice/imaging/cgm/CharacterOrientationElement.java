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


class CharacterOrientationElement extends ElementHelpers implements AbstractElement {
    private static final Logger LOG = LoggerFactory.getLogger(CharacterOrientationElement.class);

    private int xCharacterUpComponent;
    private int yCharacterUpComponent;
    private int xCharacterBaseComponent;
    private int yCharacterBaseComponent;

    public CharacterOrientationElement() {
        super(CgmIdentifier.CHARACTER_ORIENTATION);
    }

    @Override
    public void readParameters(final CgmInputReader dataReader, final int parameterListLength) throws IOException {
        xCharacterUpComponent = dataReader.readSignedIntegerAtVdcIntegerPrecision();
        yCharacterUpComponent = dataReader.readSignedIntegerAtVdcIntegerPrecision();
        xCharacterBaseComponent = dataReader.readSignedIntegerAtVdcIntegerPrecision();
        yCharacterBaseComponent = dataReader.readSignedIntegerAtVdcIntegerPrecision();
    }

@Override
    public void addStringDescription(final StringBuilder builder) {
        builder.append(String.format("\tUp vector: %d, %d", xCharacterUpComponent, yCharacterUpComponent));
        builder.append(String.format("\tBaseline vector: %d, %d", xCharacterBaseComponent, yCharacterBaseComponent));
    }

    @Override
    public void render(final Graphics2D g2, final CgmGraphicState graphicState) {
        // This could be smarter when we need to handle anything more complex than BIFF Profile BPCGM01.10
        if (graphicState.getVdcExtent().isIncreasingUp()) {
            graphicState.setCharacterOrientationInvertY(true);
        } else {
            graphicState.setCharacterOrientationInvertY(false);
        }
    }

}
