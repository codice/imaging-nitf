/**
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
 **/
package org.codice.nitf.filereader;

import java.text.ParseException;
import java.util.Set;

public class NitfGraphicSegment extends AbstractNitfSubSegment {

    private String graphicIdentifier = null;
    private String graphicName = null;
    private int graphicDisplayLevel = 0;
    private int graphicAttachmentLevel = 0;
    private int graphicLocationRow = 0;
    private int graphicLocationColumn = 0;
    private int boundingBox1Row = 0;
    private int boundingBox1Column = 0;
    private GraphicColour graphicColour = GraphicColour.UNKNOWN;
    private int boundingBox2Row = 0;
    private int boundingBox2Column = 0;
    private byte[] data = null;

    private NitfSecurityMetadata securityMetadata = null;

    public NitfGraphicSegment() {
    }

    public final void parse(final NitfReader nitfReader, final int graphicLength, final Set<ParseOption> parseOptions) throws ParseException {
        new NitfGraphicSegmentParser(nitfReader, graphicLength, parseOptions, this);
    }

    public final void setGraphicIdentifier(final String identifier) {
        graphicIdentifier = identifier;
    }

    public final String getGraphicIdentifier() {
        return graphicIdentifier;
    }

    public final void setGraphicName(final String name) {
        graphicName = name;
    }

    public final String getGraphicName() {
        return graphicName;
    }

    public final void setSecurityMetadata(final NitfSecurityMetadata nitfSecurityMetadata) {
        securityMetadata = nitfSecurityMetadata;
    }

    public final NitfSecurityMetadata getSecurityMetadata() {
        return securityMetadata;
    }

    public final void setGraphicDisplayLevel(final int displayLevel) {
        graphicDisplayLevel = displayLevel;
    }

    public final int getGraphicDisplayLevel() {
        return graphicDisplayLevel;
    }

    public final void setGraphicAttachmentLevel(final int attachmentLevel) {
        graphicAttachmentLevel = attachmentLevel;
    }

    public final int getGraphicAttachmentLevel() {
        return graphicAttachmentLevel;
    }

    public final void setGraphicLocationRow(final int rowNumber) {
        graphicLocationRow = rowNumber;
    }

    public final int getGraphicLocationRow() {
        return graphicLocationRow;
    }

    public final void setGraphicLocationColumn(final int columnNumber) {
        graphicLocationColumn = columnNumber;
    }

    public final int getGraphicLocationColumn() {
        return graphicLocationColumn;
    }

    public final void setBoundingBox1Row(final int rowNumber) {
        boundingBox1Row = rowNumber;
    }

    public final int getBoundingBox1Row() {
        return boundingBox1Row;
    }

    public final void setBoundingBox1Column(final int columnNumber) {
        boundingBox1Column = columnNumber;
    }

    public final int getBoundingBox1Column() {
        return boundingBox1Column;
    }

    public final void setGraphicColour(final GraphicColour colour) {
        graphicColour = colour;
    }

    public final GraphicColour getGraphicColour() {
        return graphicColour;
    }

    public final void setBoundingBox2Row(final int rowNumber) {
        boundingBox2Row = rowNumber;
    }

    public final int getBoundingBox2Row() {
        return boundingBox2Row;
    }

    public final void setBoundingBox2Column(final int columnNumber) {
        boundingBox2Column = columnNumber;
    }

    public final int getBoundingBox2Column() {
        return boundingBox2Column;
    }

    public final void setGraphicData(final byte[] graphicData) {
        data = graphicData;
    }

    public final byte[] getGraphicData() {
        return data;
    }
}
