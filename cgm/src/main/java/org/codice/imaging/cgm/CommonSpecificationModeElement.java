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

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class CommonSpecificationModeElement extends ElementHelpers implements AbstractElement {
    private static final Logger LOG = LoggerFactory.getLogger(CommonSpecificationModeElement.class);

    private static final int LINE_WIDTH_MODE_ABSOLUTE = 0;
    private static final int LINE_WIDTH_MODE_SCALED = 1;
    private static final int LINE_WIDTH_MODE_FRACTIONAL = 2;
    private static final int LINE_WIDTH_MODE_MM = 3;

    protected Mode mode = Mode.ABSOLUTE;

    enum Mode {
        ABSOLUTE,
        SCALED,
        FRACTIONAL,
        MILLIMETRES
    }

    protected CommonSpecificationModeElement(final CgmIdentifier cgmIdentifier) {
        super(cgmIdentifier);
    }

    @Override
    public void readParameters(final CgmInputReader inputReader, final int parameterListLength) throws IOException {
        int data = inputReader.readEnumValue();
        switch (data) {
            case LINE_WIDTH_MODE_ABSOLUTE:
                mode = CommonSpecificationModeElement.Mode.ABSOLUTE;
                break;
            case LINE_WIDTH_MODE_SCALED:
                mode = CommonSpecificationModeElement.Mode.SCALED;
                break;
            case LINE_WIDTH_MODE_FRACTIONAL:
                mode = CommonSpecificationModeElement.Mode.FRACTIONAL;
                break;
            case LINE_WIDTH_MODE_MM:
                mode = CommonSpecificationModeElement.Mode.MILLIMETRES;
                break;
            default:
                LOG.info(String.format("Unknown %s value: %d", getFriendlyName(), data));
                break;
        }
    }

    @Override
    public void addStringDescription(final StringBuilder builder) {
        builder.append("\t");
        builder.append(getFriendlyName());
        builder.append(": ");
        builder.append(mode);
        builder.append(System.lineSeparator());
    }
}
