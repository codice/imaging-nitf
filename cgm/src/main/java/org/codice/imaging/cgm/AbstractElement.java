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
 * Representation of a CGM element.
 *
 * This could be a graphical primitive (e.g. circle, line) or some attribute (colour, line thickness). It can also be a state or marker element,
 * such as "begin the file".
 */
public interface AbstractElement {
    /**
     * Read the parameters for this element.
     *
     * @param dataReader source of the parameter information
     * @param parameterListLength the length of the parameter data (bytes)
     * @throws IOException on parsing / reading error
     */
    void readParameters(final CgmInputReader dataReader, final int parameterListLength) throws IOException;

    /**
     * Check if this element matches the specified identifier.
     *
     * @param cgmIdentifier the identifier to check
     *
     * @return true if the class and ident matches, otherwise false
     */
    boolean matches(final CgmIdentifier cgmIdentifier);

    /**
     * Return a user-displayable name for this element.
     *
     * @return a user-displayable name
     */
    String getFriendlyName();

    /**
     * Append a text description of this element and its parameters to the specified builder.
     *
     * @param builder the builder to append to.
     */
    void addStringDescription(final StringBuilder builder);

    /**
     * Render this element to the specified AWT graphics.
     *
     * @param g2 the graphics to render to
     * @param graphicState the current state / context of the renderer
     */
    void render(final Graphics2D g2, final CgmGraphicState graphicState);
}
