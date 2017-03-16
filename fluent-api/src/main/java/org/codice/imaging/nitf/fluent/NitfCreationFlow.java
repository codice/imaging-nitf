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

import org.codice.imaging.nitf.core.DataSource;
import org.codice.imaging.nitf.core.common.CommonBasicSegment;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.impl.SlottedStorage;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.text.TextSegment;

/**
 * The NitfCreationFlow provides methods for creating a NITF file from scratch.
 */
public class NitfCreationFlow {
    private final DataSource dataSource = new SlottedStorage();

    /**
     * Sets the header for this NITF.
     *
     * Subsequent calls to this method will overwrite the previous file header.
     * This method must be called before build().
     *
     * @param nitfHeaderSupplier the supplier that will supply the NitfHeader.
     * @return this NitfCreationFlow.
     */
    public final NitfCreationFlow fileHeader(final Supplier<NitfHeader> nitfHeaderSupplier) {
        dataSource.setNitfHeader(nitfHeaderSupplier.get());
        return this;
    }

    /**
     * Adds an ImageSegment to the NITF.
     *
     * @param imageSegmentSupplier the supplier that will supply the ImageSegment.
     * @return this NitfCreationFlow.
     */
    public final NitfCreationFlow imageSegment(final Supplier<ImageSegment> imageSegmentSupplier) {
        return addSegment(dataSource.getImageSegments(), imageSegmentSupplier);
    }

    /**
     * Adds a DataExtensionSegment to the NITF.
     *
     * @param dataExtensionSegmentSupplier the supplier that will supply the DataExtenstionSegment.
     * @return this NitfCreationFlow.
     */
    public final NitfCreationFlow dataExtensionSegment(
            final Supplier<DataExtensionSegment> dataExtensionSegmentSupplier) {
        if (dataExtensionSegmentSupplier == null) {
            return this;
        }
        DataExtensionSegment des = dataExtensionSegmentSupplier.get();
        if (des == null) {
            return this;
        }
        FileType desFileType = des.getFileType();
        FileType headerFileType = dataSource.getNitfHeader().getFileType();
        if (!desFileType.equals(headerFileType) || desFileType.equals(FileType.UNKNOWN)) {
            throw new IllegalStateException("NitfCreationFlow.addSegment(): segment FileType must match header FileType.");
        }
        this.dataSource.getDataExtensionSegments().add(des);
        if (des.isTreOverflow()) {
            int index = dataSource.getDataExtensionSegments().size();
            switch (des.getOverflowedHeaderType()) {
                case "UDHD":
                    dataSource.getNitfHeader().setUserDefinedHeaderOverflow(index);
                    break;
                case "XHD":
                    dataSource.getNitfHeader().setExtendedHeaderDataOverflow(index);
                    break;
                case "UDID":
                    dataSource.getImageSegments().get(des.getItemOverflowed() - 1).setUserDefinedHeaderOverflow(index);
                    break;
                case "IXSHD":
                    dataSource.getImageSegments().get(des.getItemOverflowed() - 1).setExtendedHeaderDataOverflow(index);
                    break;
                case "SXSHD":
                    dataSource.getGraphicSegments().get(des.getItemOverflowed() - 1).setExtendedHeaderDataOverflow(index);
                    break;
                case "LXSHD":
                    dataSource.getLabelSegments().get(des.getItemOverflowed() - 1).setExtendedHeaderDataOverflow(index);
                    break;
                case "TXSHD":
                    dataSource.getTextSegments().get(des.getItemOverflowed() - 1).setExtendedHeaderDataOverflow(index);
                    break;
                default:
                    throw new UnsupportedOperationException("Cannot set TRE overflow for: " + des.getOverflowedHeaderType());
            }
        }
        return this;
    }

    /**
     * Adds a TextSegment to the NITF.
     *
     * @param textSegmentSupplier the supplier that will supply the TextSegment.
     * @return this NitfCreationFlow.
     */
    public final NitfCreationFlow textSegment(final Supplier<TextSegment> textSegmentSupplier) {
        return addSegment(dataSource.getTextSegments(), textSegmentSupplier);
    }

    /**
     * Adds a GraphicSegment to the NITF.
     *
     * @param graphicSegmentSupplier the supplier that will supply the GraphicSegment.
     * @return this NitfCreationFlow.
     */
    public final NitfCreationFlow graphicSegment(final Supplier<GraphicSegment> graphicSegmentSupplier) {
        return addSegment(dataSource.getGraphicSegments(), graphicSegmentSupplier);
    }

    /**
     * Adds a SymbolSegment to the NITF.
     *
     * @param symbolSegmentSupplier the supplier that will supply the SymbolSegment.
     * @return this NitfCreationFlow
     */
    public final NitfCreationFlow symbolSegment(final Supplier<SymbolSegment> symbolSegmentSupplier) {
        return addSegment(dataSource.getSymbolSegments(), symbolSegmentSupplier);
    }

    /**
     * Adds a LabelSegment to the NITF.
     *
     * @param labelSegmentSupplier the supplier that will supply the LabelSegment.
     * @return this NitfCreationFlow
     */
    public final NitfCreationFlow labelSegment(final Supplier<LabelSegment> labelSegmentSupplier) {
        return addSegment(dataSource.getLabelSegments(), labelSegmentSupplier);
    }

    /**
     *
     * @return the DataSource that represents the NITF built by this NitfCreationFlow.
     * @throws IllegalStateException when called before fileHeader().
     */
    public final DataSource build() {
        if (dataSource.getNitfHeader() == null) {
            throw new IllegalStateException(
                    "NitfCreationFlow.build(): method cannot be called before fileHeader().");
        }

        return this.dataSource;
    }

    private <T> NitfCreationFlow addSegment(final List<T> segments, final Supplier<T> supplier) {
        if (supplier == null) {
            return this;
        }

        T segment = supplier.get();
        if (segment == null) {
            return this;
        }

        FileType fileType = ((CommonBasicSegment) segment).getFileType();
        if ((fileType != dataSource.getNitfHeader().getFileType()) || (fileType == FileType.UNKNOWN)) {
            throw new IllegalStateException("NitfCreationFlow.addSegment(): segment FileType must match header FileType.");
        }
        segments.add(segment);
        return this;
    }
}
