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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: make public.
 *
 * @author bradh
 */
abstract class SlottedNitfParseStrategy implements NitfParseStrategy {
    protected Nitf nitfFileLevelHeader;
    protected final List<NitfImageSegment> imageSegments = new ArrayList<>();
    protected final List<NitfGraphicSegment> graphicSegments = new ArrayList<>();
    protected final List<NitfSymbolSegment> symbolSegments = new ArrayList<>();
    protected final List<NitfLabelSegment> labelSegments = new ArrayList<>();
    protected final List<NitfTextSegment> textSegments = new ArrayList<>();
    protected final List<NitfDataExtensionSegment> dataExtensionSegments = new ArrayList<>();

    public SlottedNitfParseStrategy() {
    }

    @Override
    public final void setFileHeader(final Nitf nitf) {
        nitfFileLevelHeader = nitf;
    }

    @Override
    public final Nitf getNitfHeader() {
        return nitfFileLevelHeader;
    }

    @Override
    public final List<NitfImageSegment> getImageSegments() {
        return imageSegments;
    }

    @Override
    public final List<NitfSymbolSegment> getSymbolSegments() {
        return symbolSegments;
    }

    @Override
    public final List<NitfLabelSegment> getLabelSegments() {
        return labelSegments;
    }

    @Override
    public final List<NitfGraphicSegment> getGraphicSegments() {
        return graphicSegments;
    }

    @Override
    public final List<NitfTextSegment> getTextSegments() {
        return textSegments;
    }

    @Override
    public final List<NitfDataExtensionSegment> getDataExtensionSegments() {
        return dataExtensionSegments;
    }

    @Override
    public abstract void baseHeadersRead(final NitfReader reader);

    protected void readImageSegmentHeadersOnly(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfImageSegments(); ++i) {
            NitfImageSegment imageSegment = new NitfImageSegment();
            new NitfImageSegmentParser(reader, imageSegment);
            reader.skip(nitfFileLevelHeader.getLengthOfImageSegment(i));
            imageSegments.add(imageSegment);
        }
    }

    protected void readImageSegmentHeadersAndData(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfImageSegments(); ++i) {
            NitfImageSegment imageSegment = new NitfImageSegment();
            new NitfImageSegmentParser(reader, imageSegment);
            // TODO: partition properly
            long lengthOfImageSegmentData = nitfFileLevelHeader.getLengthOfImageSegment(i);
            if (lengthOfImageSegmentData > 0) {
                imageSegment.setImageData(reader.readBytesRaw((int) lengthOfImageSegmentData));
            }
            imageSegments.add(imageSegment);
        }
    }

    protected void readGraphicSegmentHeadersOnly(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfGraphicSegments(); ++i) {
            NitfGraphicSegment graphicSegment = new NitfGraphicSegment();
            new NitfGraphicSegmentParser(reader, graphicSegment);
            reader.skip(nitfFileLevelHeader.getLengthOfGraphicSegment(i));
            graphicSegments.add(graphicSegment);
        }
    }

    protected void readGraphicSegmentHeadersAndData(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfGraphicSegments(); ++i) {
            NitfGraphicSegment graphicSegment = new NitfGraphicSegment();
            new NitfGraphicSegmentParser(reader, graphicSegment);
            // TODO: partion this better
            int lengthOfGraphicSegmentData = nitfFileLevelHeader.getLengthOfGraphicSegment(i);
            if (lengthOfGraphicSegmentData > 0) {
                graphicSegment.setGraphicData(reader.readBytesRaw(lengthOfGraphicSegmentData));
            }
            graphicSegments.add(graphicSegment);
        }
    }

    // We reuse the Graphic Segment length values here, but generate different type
    protected void readSymbolSegmentHeadersOnly(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfGraphicSegments(); ++i) {
            NitfSymbolSegment symbolSegment = new NitfSymbolSegment();
            new NitfSymbolSegmentParser(reader, symbolSegment);
            reader.skip(nitfFileLevelHeader.getLengthOfGraphicSegment(i));
            symbolSegments.add(symbolSegment);
        }
    }

    // We reuse the Graphic Segment length values here, but generate different type
    protected void readSymbolSegmentHeadersAndData(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfGraphicSegments(); ++i) {
            NitfSymbolSegment symbolSegment = new NitfSymbolSegment();
            new NitfSymbolSegmentParser(reader, symbolSegment);
            // TODO: partion this better
            int lengthOfSymbolSegmentData = nitfFileLevelHeader.getLengthOfGraphicSegment(i);
            if (lengthOfSymbolSegmentData > 0) {
                symbolSegment.setSymbolData(reader.readBytesRaw(lengthOfSymbolSegmentData));
            }
            symbolSegments.add(symbolSegment);
        }
    }

    protected void readLabelSegmentHeadersOnly(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfLabelSegments(); ++i) {
            NitfLabelSegment labelSegment = new NitfLabelSegment();
            new NitfLabelSegmentParser(reader, labelSegment);
            labelSegments.add(labelSegment);
        }
    }

    protected void readLabelSegmentHeadersAndData(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfLabelSegments(); ++i) {
            NitfLabelSegment labelSegment = new NitfLabelSegment();
            new NitfLabelSegmentParser(reader, labelSegment);
            // TODO: partion this better
            int lengthOfLabelSegmentData = nitfFileLevelHeader.getLengthOfLabelSegment(i);
            if (lengthOfLabelSegmentData > 0) {
                labelSegment.setLabelData(reader.readBytes(lengthOfLabelSegmentData));
            }
            labelSegments.add(labelSegment);
        }
    }

    protected void readTextSegmentHeadersOnly(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfTextSegments(); ++i) {
            NitfTextSegment textSegment = new NitfTextSegment();
            new NitfTextSegmentParser(reader, textSegment);
            textSegments.add(textSegment);
            reader.skip(nitfFileLevelHeader.getLengthOfTextSegment(i));
        }
    }

    protected void readTextSegmentHeadersAndData(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfTextSegments(); ++i) {
            NitfTextSegment textSegment = new NitfTextSegment();
            new NitfTextSegmentParser(reader, textSegment);
            // TODO: partion this better
            int lengthOfTextSegmentData = nitfFileLevelHeader.getLengthOfTextSegment(i);
            if (lengthOfTextSegmentData > 0) {
                textSegment.setTextData(reader.readBytes(lengthOfTextSegmentData));
            }
            textSegments.add(textSegment);
        }
    }

    protected void readDataExtensionSegments(final NitfReader reader) throws ParseException {
        for (int i = 0; i < nitfFileLevelHeader.getNumberOfDataExtensionSegments(); ++i) {
            NitfDataExtensionSegment segment = new NitfDataExtensionSegment();
            new NitfDataExtensionSegmentParser(reader, nitfFileLevelHeader.getLengthOfDataExtensionSegment(i), segment);
            dataExtensionSegments.add(segment);
        }
    }

}
