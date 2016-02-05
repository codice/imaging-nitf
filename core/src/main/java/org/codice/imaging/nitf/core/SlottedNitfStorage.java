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
package org.codice.imaging.nitf.core;

import java.util.ArrayList;
import java.util.List;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.text.TextSegment;

/**
 * A data structure collection that holds the various parts of a NITF file.
 */
public class SlottedNitfStorage implements NitfDataSource {

    /**
     * The file level header.
     */
    protected NitfFileHeader nitfFileLevelHeader;
    /**
     * The list of image segment.
     */
    protected final List<ImageSegment> imageSegments = new ArrayList<>();
    /**
     * The list of graphic segments.
     */
    protected final List<GraphicSegment> graphicSegments = new ArrayList<>();
    /**
     * The list of symbol segments.
     */
    protected final List<SymbolSegment> symbolSegments = new ArrayList<>();
    /**
     * The list of label segments.
     */
    protected final List<LabelSegment> labelSegments = new ArrayList<>();
    /**
     * The list of text segments.
     */
    protected final ArrayList<TextSegment> textSegments = new ArrayList<>();
    /**
     * The list of DES.
     */
    protected final List<DataExtensionSegment> dataExtensionSegments = new ArrayList<>();

    @Override
    public final NitfFileHeader getNitfHeader() {
        return nitfFileLevelHeader;
    }

    @Override
    public final List<ImageSegment> getImageSegments() {
        return imageSegments;
    }

    @Override
    public final List<GraphicSegment> getGraphicSegments() {
        return graphicSegments;
    }

    @Override
    public final List<SymbolSegment> getSymbolSegments() {
        return symbolSegments;
    }

    @Override
    public final List<LabelSegment> getLabelSegments() {
        return labelSegments;
    }

    @Override
    public final List<TextSegment> getTextSegments() {
        return textSegments;
    }

    @Override
    public final List<DataExtensionSegment> getDataExtensionSegments() {
        return dataExtensionSegments;
    }
}
