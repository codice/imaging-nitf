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
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.dataextension.NitfDataExtensionSegmentHeader;
import org.codice.imaging.nitf.core.graphic.GraphicSegmentHeader;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;
import org.codice.imaging.nitf.core.label.LabelSegmentHeader;
import org.codice.imaging.nitf.core.symbol.SymbolSegmentHeader;
import org.codice.imaging.nitf.core.text.TextSegmentHeader;

/**
 * A data structure collection that holds the various parts of a NITF file.
 */
public class SlottedNitfStorage implements NitfDataSource {

    /**
     * The file level header.
     */
    protected NitfFileHeader nitfFileLevelHeader;
    /**
     * The list of image segment headers.
     */
    protected final List<NitfImageSegmentHeader> imageSegmentHeaders = new ArrayList<>();
    /**
     * The list of image segment data.
     */
    protected final List<ImageInputStream> imageSegmentStreams = new ArrayList<>();
    /**
     * The list of graphic segment headers.
     */
    protected final List<GraphicSegmentHeader> graphicSegmentHeaders = new ArrayList<>();
    /**
     * The list of graphic segment data.
     */
    protected final List<ImageInputStream> graphicSegmentData = new ArrayList<>();
    /**
     * The list of symbol segment headers.
     */
    protected final List<SymbolSegmentHeader> symbolSegmentHeaders = new ArrayList<>();
    /**
     * The list of symbol segment data.
     */
    protected final List<ImageInputStream> symbolSegmentData = new ArrayList<>();
    /**
     * The list of label segment headers.
     */
    protected final List<LabelSegmentHeader> labelSegmentHeaders = new ArrayList<>();
    /**
     * The list of label segment data.
     */
    protected final List<String> labelSegmentData = new ArrayList<>();
    /**
     * The list of text segment headers.
     */
    protected final ArrayList<TextSegmentHeader> textSegmentHeaders = new ArrayList<>();
    /**
     * The list of text segment data.
     */
    protected final ArrayList<String> textSegmentData = new ArrayList<>();
    /**
     * The list of DES headers.
     */
    protected final List<NitfDataExtensionSegmentHeader> dataExtensionSegmentHeaders = new ArrayList<>();
    /**
     * The list of DES data.
     */
    protected final List<byte[]> dataExtensionSegmentData = new ArrayList<>();

    @Override
    public final NitfFileHeader getNitfHeader() {
        return nitfFileLevelHeader;
    }

    @Override
    public final List<NitfImageSegmentHeader> getImageSegmentHeaders() {
        return imageSegmentHeaders;
    }

    @Override
    public final List<ImageInputStream> getImageSegmentData() {
        return imageSegmentStreams;
    }

    @Override
    public final List<GraphicSegmentHeader> getGraphicSegmentHeaders() {
        return graphicSegmentHeaders;
    }

    @Override
    public final List<ImageInputStream> getGraphicSegmentData() {
        return graphicSegmentData;
    }

    @Override
    public final List<SymbolSegmentHeader> getSymbolSegmentHeaders() {
        return symbolSegmentHeaders;
    }

    @Override
    public final List<ImageInputStream> getSymbolSegmentData() {
        return symbolSegmentData;
    }

    @Override
    public final List<LabelSegmentHeader> getLabelSegmentHeaders() {
        return labelSegmentHeaders;
    }

    @Override
    public final List<String> getLabelSegmentData() {
        return labelSegmentData;
    }

    @Override
    public final List<TextSegmentHeader> getTextSegmentHeaders() {
        return textSegmentHeaders;
    }

    @Override
    public final List<String> getTextSegmentData() {
        return textSegmentData;
    }

    @Override
    public final List<NitfDataExtensionSegmentHeader> getDataExtensionSegmentHeaders() {
        return dataExtensionSegmentHeaders;
    }

    @Override
    public final List<byte[]> getDataExtensionSegmentData() {
        return dataExtensionSegmentData;
    }
}
