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

import java.util.List;
import java.util.function.Supplier;

import org.codice.imaging.nitf.core.NitfDataSource;
import org.codice.imaging.nitf.core.NitfFileHeader;
import org.codice.imaging.nitf.core.SlottedMemoryNitfStorage;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.text.TextSegment;

/**
 * The NitfCreationFlow provides methods for creating a NITF file from scratch.
 */
public class NitfCreationFlow {
    private final SlottedMemoryNitfStorage dataSource = new SlottedMemoryNitfStorage();

    /**
     * Sets the file header for this NITF.  Subsequent calls to this method will overwrite
     * the previous file header.  This method must be called before build().
     *
     * @param nitfFileHeaderSupplier the supplier that will supply the
     * NitfFileHeader.
     * @return this NitfCreationFlow.
     */
    public NitfCreationFlow fileHeader(Supplier<NitfFileHeader> nitfFileHeaderSupplier) {
        dataSource.setFileHeader(nitfFileHeaderSupplier.get());
        return this;
    }

    /**
     * Adds an ImageSegment to the NITF.
     *
     * @param imageSegmentSupplier the supplier that will supply the ImageSegment.
     * @return this NitfCreationFlow.
     */
    public NitfCreationFlow imageSegment(Supplier<ImageSegment> imageSegmentSupplier) {
        return addSegment(dataSource.getImageSegments(), imageSegmentSupplier);
    }

    /**
     * Adds a DataExtensionSegment to the NITF.
     *
     * @param dataExtensionSegmentSupplier the supplier that will supply the DataExtenstionSegment.
     * @return this NitfCreationFlow.
     */
    public NitfCreationFlow dataExtensionSegment(
            Supplier<DataExtensionSegment> dataExtensionSegmentSupplier) {
        return addSegment(dataSource.getDataExtensionSegments(), dataExtensionSegmentSupplier);
    }

    /**
     * Adds a TextSegment to the NITF.
     *
     * @param textSegmentSupplier the supplier that will supply the TextSegment.
     * @return this NitfCreationFlow.
     */
    public NitfCreationFlow textSegment(Supplier<TextSegment> textSegmentSupplier) {
        return addSegment(dataSource.getTextSegments(), textSegmentSupplier);
    }

    /**
     * Adds a GraphicSegment to the NITF.
     *
     * @param graphicSegmentSupplier the supplier that will supply the GraphicSegment.
     * @return this NitfCreationFlow.
     */
    public NitfCreationFlow graphicSegment(Supplier<GraphicSegment> graphicSegmentSupplier) {
        return addSegment(dataSource.getGraphicSegments(), graphicSegmentSupplier);
    }

    /**
     * Adds a SymbolSegment to the NITF.
     *
     * @param symbolSegmentSupplier the supplier that will supply the SymbolSegment.
     * @return this NitfCreationFlow
     */
    public NitfCreationFlow symbolSegment(Supplier<SymbolSegment> symbolSegmentSupplier) {
        return addSegment(dataSource.getSymbolSegments(), symbolSegmentSupplier);
    }

    /**
     * Adds a LabelSegment to the NITF.
     *
     * @param labelSegmentSupplier the supplier that will supply the LabelSegment.
     * @return this NitfCreationFlow
     */
    public NitfCreationFlow labelSegment(Supplier<LabelSegment> labelSegmentSupplier) {
        return addSegment(dataSource.getLabelSegments(), labelSegmentSupplier);
    }

    /**
     *
     * @return the NitfDataSource that represents the NITF built by this NitfCreationFlow.
     * @throws IllegalStateException when called before fileHeader().
     */
    public NitfDataSource build() {
        if (dataSource.getNitfHeader() == null) {
            throw new IllegalStateException(
                    "NitfCreationFlow.build(): method cannot be called before fileHeader().");
        }

        return this.dataSource;
    }

    private <T> NitfCreationFlow addSegment(List<T> segments, Supplier<T> supplier) {
        if (supplier != null) {
            segments.add(supplier.get());
            return this;
        }

        return null;
    }
}
