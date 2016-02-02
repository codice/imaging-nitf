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

import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegmentWriter;
import org.codice.imaging.nitf.core.graphic.GraphicSegment;
import org.codice.imaging.nitf.core.graphic.GraphicSegmentWriter;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.ImageSegmentWriter;
import org.codice.imaging.nitf.core.label.LabelSegment;
import org.codice.imaging.nitf.core.label.LabelSegmentWriter;
import org.codice.imaging.nitf.core.symbol.SymbolSegment;
import org.codice.imaging.nitf.core.symbol.SymbolSegmentWriter;
import org.codice.imaging.nitf.core.text.TextSegment;
import org.codice.imaging.nitf.core.text.TextSegmentWriter;
import org.codice.imaging.nitf.core.tre.TreParser;

/**
 * Output independent parts of a NitfWriter implementation.
 */
public abstract class SharedNitfWriter implements NitfWriter {

    private TreParser mTreParser = null;
    private NitfDataSource mDataSource = null;

    /**
     * The target to write the data to.
     */
    protected DataOutput mOutput = null;

    /**
     * Initialise shared state.
     *
     * @param dataSource the parseStrategy that provides the data to be written out.
     */
    protected SharedNitfWriter(final NitfDataSource dataSource) {
        mDataSource = dataSource;
    }

    /**
     * Write out the data to the specified target.
     *
     * @throws ParseException if there is a problem reading data
     * @throws IOException if there is a problem writing data
     */
    protected final void writeData() throws ParseException, IOException {
        mTreParser = new TreParser();
        NitfFileHeaderWriter fileHeaderWriter = new NitfFileHeaderWriter(mOutput, mTreParser);
        fileHeaderWriter.writeFileHeader(mDataSource);
        writeImageSegments();
        writeGraphicSegments();
        writeSymbolSegments();
        writeLabelSegments();
        writeTextSegments();
        writeDataExtensionSegments();
    }

    private void writeImageSegments() throws ParseException, IOException {
        ImageSegmentWriter imageSegmentWriter = new ImageSegmentWriter(mOutput, mTreParser);
        for (ImageSegment imageSegment : mDataSource.getImageSegments()) {
            imageSegmentWriter.writeImageSegment(imageSegment, mDataSource.getNitfHeader().getFileType());
        }
    }

    private void writeGraphicSegments() throws IOException, ParseException {
        GraphicSegmentWriter graphicSegmentWriter = new GraphicSegmentWriter(mOutput, mTreParser);
        for (GraphicSegment graphicSegment : mDataSource.getGraphicSegments()) {
            graphicSegmentWriter.writeGraphicSegment(graphicSegment);
        }
    }

    private void writeSymbolSegments() throws ParseException, IOException {
        SymbolSegmentWriter symbolSegmentWriter = new SymbolSegmentWriter(mOutput, mTreParser);
        for (SymbolSegment symbolSegment : mDataSource.getSymbolSegments()) {
            symbolSegmentWriter.writeSymbolSegment(symbolSegment);
        }
    }

    private void writeLabelSegments() throws IOException, ParseException {
        LabelSegmentWriter labelSegmentWriter = new LabelSegmentWriter(mOutput, mTreParser);
        for (LabelSegment labelSegment : mDataSource.getLabelSegments()) {
            labelSegmentWriter.writeLabel(labelSegment);
        }
    }

    private void writeTextSegments() throws ParseException, IOException {
        TextSegmentWriter textSegmentWriter = new TextSegmentWriter(mOutput, mTreParser);
        for (TextSegment textSegment : mDataSource.getTextSegments()) {
            textSegmentWriter.writeTextSegment(textSegment, mDataSource.getNitfHeader().getFileType());
        }
    }

    private void writeDataExtensionSegments() throws ParseException, IOException {
        DataExtensionSegmentWriter dataExtensionSegmentWriter = new DataExtensionSegmentWriter(mOutput, mTreParser);
        for (DataExtensionSegment des : mDataSource.getDataExtensionSegments()) {
            if (!des.isStreamingMode()) {
                dataExtensionSegmentWriter.writeDESHeader(des, mDataSource.getNitfHeader().getFileType());
            }
        }
    }
}
