/*
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 */
package org.codice.imaging.nitf.fluent;

import java.util.function.Supplier;

import org.codice.imaging.nitf.core.DataSource;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.text.TextSegment;

/**
 * The NitfCreationFlow provides methods for creating a NITF file from scratch.
 */
public interface NitfCreationFlow {

    /**
     * Sets the header for this NITF.
     *
     * Subsequent calls to this method will overwrite the previous file header.
     * This method must be called before build().
     *
     * @param nitfHeaderSupplier the supplier that will supply the NitfHeader.
     * @return this NitfCreationFlow.
     */
    NitfCreationFlow fileHeader(Supplier<NitfHeader> nitfHeaderSupplier);

    /**
     * Adds an ImageSegment to the NITF.
     *
     * @param imageSegmentSupplier the supplier that will supply the ImageSegment.
     * @return this NitfCreationFlow.
     */
    NitfCreationFlow imageSegment(Supplier<ImageSegment> imageSegmentSupplier);

    /**
     * Adds a DataExtensionSegment to the NITF.
     *
     * @param dataExtensionSegmentSupplier the supplier that will supply the DataExtenstionSegment.
     * @return this NitfCreationFlow.
     */
    NitfCreationFlow dataExtensionSegment(
            Supplier<DataExtensionSegment> dataExtensionSegmentSupplier);

    /**
     * Adds a TextSegment to the NITF.
     *
     * @param textSegmentSupplier the supplier that will supply the TextSegment.
     * @return this NitfCreationFlow.
     */
    NitfCreationFlow textSegment(Supplier<TextSegment> textSegmentSupplier);

    /**
     * Adds a GraphicSegment to the NITF.
     *
     * @param graphicSegmentSupplier the supplier that will supply the GraphicSegment.
     * @return this NitfCreationFlow.
     */
    NitfCreationFlow graphicSegment(Supplier<GraphicSegment> graphicSegmentSupplier);

    /**
     * Adds a SymbolSegment to the NITF.
     *
     * @param symbolSegmentSupplier the supplier that will supply the SymbolSegment.
     * @return this NitfCreationFlow
     */
    NitfCreationFlow symbolSegment(Supplier<SymbolSegment> symbolSegmentSupplier);

    /**
     * Adds a LabelSegment to the NITF.
     *
     * @param labelSegmentSupplier the supplier that will supply the LabelSegment.
     * @return this NitfCreationFlow
     */
    NitfCreationFlow labelSegment(Supplier<LabelSegment> labelSegmentSupplier);

    /**
     *
     * @return the DataSource that represents the NITF built by this NitfCreationFlow.
     * @throws IllegalStateException when called before fileHeader().
     */
    DataSource build();

    /**
     * Write out the created file.
     *
     * @param filename the name of the file to write to
     */

    void write(final String filename);
}
