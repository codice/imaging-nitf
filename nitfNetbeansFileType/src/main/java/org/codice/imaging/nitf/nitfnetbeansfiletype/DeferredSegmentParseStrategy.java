/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.codice.imaging.nitf.core.FileReader;
import org.codice.imaging.nitf.core.FileType;
import org.codice.imaging.nitf.core.NitfDataExtensionSegmentHeader;
import org.codice.imaging.nitf.core.NitfGraphicSegmentHeader;
import org.codice.imaging.nitf.core.NitfImageSegmentHeader;
import org.codice.imaging.nitf.core.NitfLabelSegmentHeader;
import org.codice.imaging.nitf.core.NitfReader;
import org.codice.imaging.nitf.core.NitfSymbolSegmentHeader;
import org.codice.imaging.nitf.core.NitfTextSegmentHeader;
import org.codice.imaging.nitf.core.SlottedNitfParseStrategy;

class DeferredSegmentParseStrategy extends SlottedNitfParseStrategy {

    private List<Long> imageSegmentHeaderOffsets = new ArrayList<>();
    private List<Long> imageSegmentDataOffsets = new ArrayList<>();
    private List<Long> graphicSegmentHeaderOffsets = new ArrayList<>();
    private List<Long> graphicSegmentDataOffsets = new ArrayList<>();
    private List<Long> symbolSegmentHeaderOffsets = new ArrayList<>();
    private List<Long> symbolSegmentDataOffsets = new ArrayList<>();
    private List<Long> labelSegmentHeaderOffsets = new ArrayList<>();
    private List<Long> labelSegmentDataOffsets = new ArrayList<>();
    private List<Long> textSegmentHeaderOffsets = new ArrayList<>();
    private List<Long> textSegmentDataOffsets = new ArrayList<>();
    private List<Long> dataExtensionSegmentHeaderOffsets = new ArrayList<>();
    private List<Long> dataExtensionSegmentDataOffsets = new ArrayList<>();
    private FileReader fileReader;

    public DeferredSegmentParseStrategy() {
    }

    @Override
    public void baseHeadersRead(final NitfReader reader) {
        fileReader = (FileReader) reader;
        long offset = reader.getCurrentOffset();
        for (int i = 0; i < nitfFileLevelHeader.getImageSegmentSubHeaderLengths().size(); ++i) {
            long headerOffset = offset;
            getImageSegmentHeaderOffsets().add(headerOffset);
            long dataOffset = headerOffset + nitfFileLevelHeader.getImageSegmentSubHeaderLengths().get(i);
            getImageSegmentDataOffsets().add(dataOffset);
            offset = dataOffset + nitfFileLevelHeader.getImageSegmentDataLengths().get(i);
        }
        if (nitfFileLevelHeader.getFileType() == FileType.NITF_TWO_ZERO) {
            for (int i = 0; i < nitfFileLevelHeader.getSymbolSegmentSubHeaderLengths().size(); ++i) {
                long headerOffset = offset;
                getSymbolSegmentHeaderOffsets().add(headerOffset);
                long dataOffset = headerOffset + nitfFileLevelHeader.getSymbolSegmentSubHeaderLengths().get(i);
                getSymbolSegmentDataOffsets().add(dataOffset);
                offset = dataOffset + nitfFileLevelHeader.getSymbolSegmentDataLengths().get(i);
            }
            for (int i = 0; i < nitfFileLevelHeader.getLabelSegmentSubHeaderLengths().size(); ++i) {
                long headerOffset = offset;
                getLabelSegmentHeaderOffsets().add(headerOffset);
                long dataOffset = headerOffset + nitfFileLevelHeader.getLabelSegmentSubHeaderLengths().get(i);
                getLabelSegmentDataOffsets().add(dataOffset);
                offset = dataOffset + nitfFileLevelHeader.getLabelSegmentDataLengths().get(i);
            }
        } else {
            for (int i = 0; i < nitfFileLevelHeader.getGraphicSegmentSubHeaderLengths().size(); ++i) {
                long headerOffset = offset;
                getGraphicSegmentHeaderOffsets().add(headerOffset);
                long dataOffset = headerOffset + nitfFileLevelHeader.getGraphicSegmentSubHeaderLengths().get(i);
                getGraphicSegmentDataOffsets().add(dataOffset);
                offset = dataOffset + nitfFileLevelHeader.getGraphicSegmentDataLengths().get(i);
            }
        }
        for (int i = 0; i < nitfFileLevelHeader.getTextSegmentSubHeaderLengths().size(); ++i) {
            long headerOffset = offset;
            getTextSegmentHeaderOffsets().add(headerOffset);
            long dataOffset = headerOffset + nitfFileLevelHeader.getTextSegmentSubHeaderLengths().get(i);
            getTextSegmentDataOffsets().add(dataOffset);
            offset = dataOffset + nitfFileLevelHeader.getTextSegmentDataLengths().get(i);
        }
        for (int i = 0; i < nitfFileLevelHeader.getDataExtensionSegmentSubHeaderLengths().size(); ++i) {
            long headerOffset = offset;
            getDataExtensionSegmentHeaderOffsets().add(headerOffset);
            long dataOffset = headerOffset + nitfFileLevelHeader.getDataExtensionSegmentSubHeaderLengths().get(i);
            getDataExtensionSegmentDataOffsets().add(dataOffset);
            offset = dataOffset + nitfFileLevelHeader.getDataExtensionSegmentDataLengths().get(i);
        }
    }

    /**
     * @return the imageSegmentHeadersOffsets
     */
    public List<Long> getImageSegmentHeaderOffsets() {
        return imageSegmentHeaderOffsets;
    }

    /**
     * @param offsets the imageSegmentHeadersOffsets to set
     */
    public final void setImageSegmentHeaderOffsets(final List<Long> offsets) {
        imageSegmentHeaderOffsets = offsets;
    }

    /**
     * @return the imageSegmentDataOffsets
     */
    public List<Long> getImageSegmentDataOffsets() {
        return imageSegmentDataOffsets;
    }

    /**
     * @param offsets the imageSegmentDataOffsets to set
     */
    public void setImageSegmentDataOffsets(final List<Long> offsets) {
        imageSegmentDataOffsets = offsets;
    }

    /**
     * @return the graphicSegmentHeadersOffsets
     */
    public List<Long> getGraphicSegmentHeaderOffsets() {
        return graphicSegmentHeaderOffsets;
    }

    /**
     * @param offsets the graphicSegmentHeadersOffsets to set
     */
    public void setGraphicSegmentHeadersOffsets(final List<Long> offsets) {
        graphicSegmentHeaderOffsets = offsets;
    }

    /**
     * @return the graphicSegmentDataOffsets
     */
    public List<Long> getGraphicSegmentDataOffsets() {
        return graphicSegmentDataOffsets;
    }

    /**
     * @param offsets the graphicSegmentDataOffsets to set
     */
    public void setGraphicSegmentDataOffsets(final List<Long> offsets) {
        graphicSegmentDataOffsets = offsets;
    }

    /**
     * @return the symbolSegmentHeadersOffsets
     */
    public List<Long> getSymbolSegmentHeaderOffsets() {
        return symbolSegmentHeaderOffsets;
    }

    /**
     * @param offsets the symbolSegmentHeadersOffsets to set
     */
    public void setSymbolSegmentHeaderOffsets(final List<Long> offsets) {
        symbolSegmentHeaderOffsets = offsets;
    }

    /**
     * @return the symbolSegmentDataOffsets
     */
    public List<Long> getSymbolSegmentDataOffsets() {
        return symbolSegmentDataOffsets;
    }

    /**
     * @param offsets the symbolSegmentDataOffsets to set
     */
    public void setSymbolSegmentDataOffsets(final List<Long> offsets) {
        this.symbolSegmentDataOffsets = offsets;
    }

    /**
     * @return the labelSegmentHeadersOffsets
     */
    public List<Long> getLabelSegmentHeaderOffsets() {
        return labelSegmentHeaderOffsets;
    }

    /**
     * @param offsets the labelSegmentHeadersOffsets to set
     */
    public void setLabelSegmentHeaderOffsets(final List<Long> offsets) {
        labelSegmentHeaderOffsets = offsets;
    }

    /**
     * @return the labelSegmentDataOffsets
     */
    public List<Long> getLabelSegmentDataOffsets() {
        return labelSegmentDataOffsets;
    }

    /**
     * @param offsets the labelSegmentDataOffsets to set
     */
    public void setLabelSegmentDataOffsets(final List<Long> offsets) {
        labelSegmentDataOffsets = offsets;
    }

    /**
     * @return the textSegmentHeaderOffsets
     */
    public List<Long> getTextSegmentHeaderOffsets() {
        return textSegmentHeaderOffsets;
    }

    /**
     * @param offsets the textSegmentHeaderOffsets to set
     */
    public void setTextSegmentHeaderOffsets(final List<Long> offsets) {
        textSegmentHeaderOffsets = offsets;
    }

    /**
     * @return the textSegmentDataOffsets
     */
    public List<Long> getTextSegmentDataOffsets() {
        return textSegmentDataOffsets;
    }

    /**
     * @param offsets the textSegmentDataOffsets to set
     */
    public void setTextSegmentDataOffsets(final List<Long> offsets) {
        textSegmentDataOffsets = offsets;
    }

    /**
     * @return the dataExtensionSegmentHeaderOffsets
     */
    public List<Long> getDataExtensionSegmentHeaderOffsets() {
        return dataExtensionSegmentHeaderOffsets;
    }

    /**
     * @param offsets the dataExtensionSegmentHeaderOffsets to set
     */
    public void setDataExtensionSegmentHeaderOffsets(final List<Long> offsets) {
        dataExtensionSegmentHeaderOffsets = offsets;
    }

    /**
     * @return the dataExtensionSegmentDataOffsets
     */
    public List<Long> getDataExtensionSegmentDataOffsets() {
        return dataExtensionSegmentDataOffsets;
    }

    /**
     * @param offsets the dataExtensionSegmentDataOffsets to set
     */
    public final void setDataExtensionSegmentDataOffsets(final List<Long> offsets) {
        dataExtensionSegmentDataOffsets = offsets;
    }

    NitfImageSegmentHeader getImageSegmentHeader(final int index) throws ParseException {
        long segmentHeaderOffset = imageSegmentHeaderOffsets.get(index);
        fileReader.seekToAbsoluteOffset(segmentHeaderOffset);
        return readImageSegmentHeader(fileReader, index);
    }

    NitfGraphicSegmentHeader getGraphicSegmentHeader(final int index) throws ParseException {
        long segmentHeaderOffset = graphicSegmentHeaderOffsets.get(index);
        fileReader.seekToAbsoluteOffset(segmentHeaderOffset);
        return readGraphicSegmentHeader(fileReader, index);
    }

    NitfSymbolSegmentHeader getSymbolSegmentHeader(final int index) throws ParseException {
        long segmentHeaderOffset = symbolSegmentHeaderOffsets.get(index);
        fileReader.seekToAbsoluteOffset(segmentHeaderOffset);
        return readSymbolSegmentHeader(fileReader, index);
    }

    NitfLabelSegmentHeader getLabelSegmentHeader(final int index) throws ParseException {
        long segmentHeaderOffset = labelSegmentHeaderOffsets.get(index);
        fileReader.seekToAbsoluteOffset(segmentHeaderOffset);
        return readLabelSegmentHeader(fileReader, index);
    }

    String getLabelSegmentData(final NitfLabelSegmentHeader header, final int index) throws ParseException {
        long segmentDataOffset = labelSegmentDataOffsets.get(index);
        fileReader.seekToAbsoluteOffset(segmentDataOffset);
        return fileReader.readBytes(header.getLabelDataLength());
    }

    NitfTextSegmentHeader getTextSegmentHeader(final int index) throws ParseException {
        long segmentHeaderOffset = textSegmentHeaderOffsets.get(index);
        fileReader.seekToAbsoluteOffset(segmentHeaderOffset);
        return readTextSegmentHeader(fileReader, index);
    }

    String getTextSegmentData(final NitfTextSegmentHeader header, final int index) throws ParseException {
        long segmentDataOffset = textSegmentDataOffsets.get(index);
        fileReader.seekToAbsoluteOffset(segmentDataOffset);
        return fileReader.readBytes(header.getTextDataLength());
    }

    NitfDataExtensionSegmentHeader getDataExtensionSegmentHeader(final int index) throws ParseException {
        long segmentHeaderOffset = dataExtensionSegmentHeaderOffsets.get(index);
        fileReader.seekToAbsoluteOffset(segmentHeaderOffset);
        return readDataExtensionSegmentHeader(fileReader, index);
    }

    void parseDataExtensionSegmentData(final NitfDataExtensionSegmentHeader header, final int index) throws ParseException {
        long segmentDataOffset = dataExtensionSegmentDataOffsets.get(index);
        fileReader.seekToAbsoluteOffset(segmentDataOffset);
        readDataExtensionSegmentData(header, fileReader);
    }

    byte[] getDataExtensionSegmentData(final int index) {
        return this.dataExtensionSegmentData.get(index);
    }
}
