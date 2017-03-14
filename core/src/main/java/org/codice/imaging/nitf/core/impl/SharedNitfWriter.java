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
package org.codice.imaging.nitf.core.impl;

import java.io.DataOutput;
import java.io.IOException;

import org.codice.imaging.nitf.core.DataSource;
import org.codice.imaging.nitf.core.NitfWriter;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.dataextension.impl.DataExtensionSegmentWriter;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.graphic.impl.GraphicSegmentWriter;
import org.codice.imaging.nitf.core.header.impl.NitfHeaderWriter;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.impl.ImageSegmentWriter;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.label.impl.LabelSegmentWriter;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.symbol.impl.SymbolSegmentWriter;
import org.codice.imaging.nitf.core.text.TextSegment;
import org.codice.imaging.nitf.core.text.impl.TextSegmentWriter;
import org.codice.imaging.nitf.core.tre.impl.TreParser;

/**
 * Output independent parts of a NitfWriter implementation.
 */
public abstract class SharedNitfWriter implements NitfWriter {

    private TreParser mTreParser = null;
    private DataSource mDataSource = null;

    /**
     * The target to write the data to.
     */
    protected DataOutput mOutput = null;

    /**
     * Initialise shared state.
     *
     * @param dataSource the parseStrategy that provides the data to be written out.
     */
    protected SharedNitfWriter(final DataSource dataSource) {
        mDataSource = dataSource;
    }

    /**
     * Write out the data to the specified target.
     *
     * @throws NitfFormatException if there is a problem reading data
     * @throws IOException if there is a problem writing data
     */
    protected final void writeData() throws NitfFormatException, IOException {
        mTreParser = new TreParser();
        NitfHeaderWriter fileHeaderWriter = new NitfHeaderWriter(mOutput, mTreParser);
        fileHeaderWriter.writeFileHeader(mDataSource);
        writeImageSegments();
        writeGraphicSegments();
        writeSymbolSegments();
        writeLabelSegments();
        writeTextSegments();
        writeDataExtensionSegments();
    }

    private void writeImageSegments() throws NitfFormatException, IOException {
        ImageSegmentWriter imageSegmentWriter = new ImageSegmentWriter(mOutput, mTreParser);
        for (ImageSegment imageSegment : mDataSource.getImageSegments()) {
            imageSegmentWriter.writeImageSegment(imageSegment, mDataSource.getNitfHeader().getFileType());
        }
    }

    private void writeGraphicSegments() throws IOException, NitfFormatException {
        GraphicSegmentWriter graphicSegmentWriter = new GraphicSegmentWriter(mOutput, mTreParser);
        for (GraphicSegment graphicSegment : mDataSource.getGraphicSegments()) {
            graphicSegmentWriter.writeGraphicSegment(graphicSegment);
        }
    }

    private void writeSymbolSegments() throws NitfFormatException, IOException {
        SymbolSegmentWriter symbolSegmentWriter = new SymbolSegmentWriter(mOutput, mTreParser);
        for (SymbolSegment symbolSegment : mDataSource.getSymbolSegments()) {
            symbolSegmentWriter.writeSymbolSegment(symbolSegment);
        }
    }

    private void writeLabelSegments() throws IOException, NitfFormatException {
        LabelSegmentWriter labelSegmentWriter = new LabelSegmentWriter(mOutput, mTreParser);
        for (LabelSegment labelSegment : mDataSource.getLabelSegments()) {
            labelSegmentWriter.writeLabel(labelSegment);
        }
    }

    private void writeTextSegments() throws NitfFormatException, IOException {
        TextSegmentWriter textSegmentWriter = new TextSegmentWriter(mOutput, mTreParser);
        for (TextSegment textSegment : mDataSource.getTextSegments()) {
            textSegmentWriter.writeTextSegment(textSegment, mDataSource.getNitfHeader().getFileType());
        }
    }

    private void writeDataExtensionSegments() throws NitfFormatException, IOException {
        DataExtensionSegmentWriter dataExtensionSegmentWriter = new DataExtensionSegmentWriter(mOutput, mTreParser);
        for (DataExtensionSegment des : mDataSource.getDataExtensionSegments()) {
            if (!des.isStreamingMode()) {
                dataExtensionSegmentWriter.writeDESHeader(des);
            }
        }
    }
}
