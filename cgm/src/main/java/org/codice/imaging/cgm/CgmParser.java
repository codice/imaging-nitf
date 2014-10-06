/*
 * Copyright (c) 2014, Codice
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.codice.imaging.cgm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.codice.imaging.nitf.core.NitfGraphicSegment;

public class CgmParser {

    private NitfGraphicSegment mGraphicSegment = null;
    private DataInputStream dataStream = null;

    private final static int CLASS_DELIMITER = 0;
    private final static int CLASS_METAFILE_DESCRIPTOR = 1;
    private final static int CLASS_PICTURE_DESCRIPTOR = 2;
    private final static int CLASS_CONTROL = 3;
    private final static int CLASS_GRAPHICAL_PRIMITIVE = 4;
    private final static int CLASS_ATTRIBUTE = 5;
    private final static int CLASS_ESCAPE = 6;
    private final static int CLASS_EXTERNAL = 7;
    private final static int CLASS_SEGMENT = 8;
    private final static int CLASS_APPLICATION_STRUCTURE_DESCRIPTOR = 9;
    
    private final static int ID_NO_OP = 0;
    private final static int ID_BEGIN_METAFILE = 1;
    private final static int ID_END_METAFILE = 2;
    private final static int ID_BEGIN_PICTURE = 3;
    private final static int ID_BEGIN_PICTURE_BODY = 4;
    private final static int ID_END_PICTURE = 5;
    private final static int ID_BEGIN_SEGMENT = 6;
    private final static int ID_END_SEGMENT = 7;
    private final static int ID_BEGIN_FIGURE = 8;
    private final static int ID_END_FIGURE = 9;
    private final static int ID_BEGIN_PROTECTION_REGION = 13;
    private final static int ID_END_PROTECTION_REGION = 14;
    private final static int ID_BEGIN_COMPOUND_LINE = 15;
    private final static int ID_END_COMPOUND_LINE = 16;
    private final static int ID_BEGIN_COMPOUND_TEXT_PATH = 17;
    private final static int ID_END_COMPOUND_TEXT_PATH = 18;
    private final static int ID_BEGIN_TILE_ARRAY = 19;
    private final static int ID_END_TILE_ARRAY = 20;
    private final static int ID_BEGIN_APPLICATION_STRUCTURE = 21;
    private final static int ID_BEGIN_APPLICATION_STRUCTURE_BODY = 22;
    private final static int ID_END_APPLICATION_STRUCTURE = 23;
    
    private final static int ID_METAFILE_VERSION = 1;
    private final static int ID_METAFILE_DESCRIPTION = 2;
    private final static int ID_VDC_TYPE = 3;
    private final static int ID_INTEGER_PRECISION = 4;
    private final static int ID_REAL_PRECISION = 5; 
    private final static int ID_INDEX_PRECISION = 6;
    private final static int ID_COLOUR_PRECISION = 7;
    private final static int ID_COLOUR_INDEX_PRECISION = 8;
    private final static int ID_MAXIMUM_COLOUR_INDEX = 9;
    private final static int ID_COLOUR_VALUE_EXTENT = 10;
    private final static int ID_METAFILE_ELEMENT_LIST = 11;
    private final static int ID_METAFILE_DEFAULTS_REPLACEMENT = 12;
    private final static int ID_FONT_LIST = 13;
    private final static int ID_CHARACTER_SET_LIST = 14;
    private final static int ID_CHARACTER_CODING_ANNOUNCER = 15;
    private final static int ID_NAME_PRECISION = 16;
    private final static int ID_MAXIMUM_VDC_EXTENT = 17;
    private final static int ID_SEGMENT_PRIORITY_EXTENT = 18;
    private final static int ID_COLOUR_MODEL = 19;
    private final static int ID_COLOUR_CALIBRATION = 20;
    private final static int ID_FONT_PROPERTIES = 21;
    private final static int ID_GLYPH_MAPPING = 22;
    private final static int ID_SYMBOL_LIBRARY_LIST = 23;
    private final static int ID_PICTURE_DIRECTORY = 24;
    
    // Picture Descriptor Elements: Class 2
    private final static int ID_SCALING_MODE = 1;
    private final static int ID_COLOUR_SELECTION_MODE = 2;
    private final static int ID_LINE_WIDTH_SPECIFICATION_MODE = 3;
    private final static int ID_MARKER_SIZE_SPECIFICATION_MODE = 4;
    private final static int ID_EDGE_WIDTH_SPECIFICATION_MODE = 5;
    private final static int ID_VDC_EXTENT = 6;
    private final static int ID_BACKGROUND_COLOUR = 7;
    private final static int ID_DEVICE_VIEWPORT = 8;
    private final static int ID_DEVICE_VIEWPORT_SPECIFICATION_MODE = 9;
    private final static int ID_DEVICE_VIEWPORT_MAPPING = 10;
    private final static int ID_LINE_REPRESENTATION = 11;
    private final static int ID_MARKER_REPRESENTATION = 12;
    private final static int ID_TEXT_REPRESENTATION = 13;
    private final static int ID_FILL_REPRESENTATION = 14;
    private final static int ID_EDGE_REPRESENTATION = 15;
    private final static int ID_INTERIOR_STYLE_REPRESENTATION = 16;
    private final static int ID_LINE_AND_EDGE_TYPE_DEFINITION = 17;
    private final static int ID_HATCH_STYLE_DEFINITION = 18;
    private final static int ID_GEOMETRIC_PATTERN_DEFINITION = 19;
    private final static int ID_APPLICATION_STRUCTURE_DIRECTORY = 20;
    
    // Control Elements: Class 3
    private final static int ID_VDC_INTEGER_PRECISION = 1;
    
    // Graphical Primitive Elements: Class 4
    private final static int ID_POLYLINE = 1;
    private final static int ID_DISJOINT_POLYLINE = 2;
    private final static int ID_POLYMARKER = 3;
    private final static int ID_TEXT = 4;
    private final static int ID_RESTRICTED_TEXT = 5;
    
    // Graphical Primitive Elements: Class 5
    private final static int ID_LINE_BUNDLE_INDEX = 1;
    private final static int ID_LINE_TYPE = 2;
    private final static int ID_LINE_WIDTH = 3;
    private final static int ID_LINE_COLOUR = 4;
    private final static int ID_MARKER_BUNDLE_INDEX = 5;
    private final static int ID_MARKER_TYPE = 6;
    private final static int ID_MARKER_SIZE = 7;
    private final static int ID_MARKER_COLOUR = 8;
    private final static int ID_TEXT_BUNDLE_INDEX = 9;
    private final static int ID_TEXT_FONT_INDEX = 10;
    private final static int ID_TEXT_PRECISION = 11;
    private final static int ID_CHARACTER_EXPANSION_FACTOR = 12;
    private final static int ID_CHARACTER_SPACING = 13;
    private final static int ID_TEXT_COLOUR = 14;
    private final static int ID_CHARACTER_HEIGHT = 15;
    private final static int ID_CHARACTER_ORIENTATION = 16;
    private final static int ID_TEXT_PATH = 17;
    private final static int ID_TEXT_ALIGNMENT = 18;

    private static String getElementName(int elementClass, int elementId) {
        AbstractElement elementCode = getElementCode(elementClass, elementId);
        if (elementCode != null) {
            return elementCode.getName();
        } else {
            return String.format("missing element name for %d, %d", elementClass, elementId);
        }
    }

    private static AbstractElement getElementCode(int elementClass, int elementId) {
        for (int i = 0; i < elementCodes.length; ++i) {
            if (elementCodes[i].matches(elementClass, elementId)) {
                return elementCodes[i];
            }
        }
        return null;
    }

    private static final AbstractElement [] elementCodes = { 
        new NoArgumentsElement(CLASS_DELIMITER, ID_NO_OP, "no-op"),
        new StringFixedArgumentElement(CLASS_DELIMITER, ID_BEGIN_METAFILE, "BEGIN METAFILE"),
        new NoArgumentsElement(CLASS_DELIMITER, ID_END_METAFILE, "END METAFILE"),
        new StringFixedArgumentElement(CLASS_DELIMITER, ID_BEGIN_PICTURE, "BEGIN PICTURE"),
        new NoArgumentsElement(CLASS_DELIMITER, ID_BEGIN_PICTURE_BODY, "BEGIN PICTURE BODY"),
        new NoArgumentsElement(CLASS_DELIMITER, ID_END_PICTURE, "END PICTURE"),
        new NoArgumentsElement(CLASS_DELIMITER, ID_BEGIN_SEGMENT, "BEGIN SEGMENT"),
        
        new NoArgumentsElement(CLASS_METAFILE_DESCRIPTOR, ID_METAFILE_VERSION, "METAFILE VERSION"),
        new StringFixedArgumentElement(CLASS_METAFILE_DESCRIPTOR, ID_METAFILE_DESCRIPTION, "METAFILE DESCRIPTION"),
        new NoArgumentsElement(CLASS_METAFILE_DESCRIPTOR, ID_METAFILE_ELEMENT_LIST, "METAFILE ELEMENT LIST"),
        new NoArgumentsElement(CLASS_METAFILE_DESCRIPTOR, ID_FONT_LIST, "FONT LIST"),
        
        new NoArgumentsElement(CLASS_PICTURE_DESCRIPTOR, ID_SCALING_MODE, "SCALING MODE"),
        new NoArgumentsElement(CLASS_PICTURE_DESCRIPTOR, ID_COLOUR_SELECTION_MODE, "COLOUR SELECTION MODE"),
        new LineWidthSpecificationElement(CLASS_PICTURE_DESCRIPTOR, ID_LINE_WIDTH_SPECIFICATION_MODE, "LINE WIDTH SPECIFICATION MODE"),
        new NoArgumentsElement(CLASS_PICTURE_DESCRIPTOR, ID_MARKER_SIZE_SPECIFICATION_MODE, "MARKER SIZE SPECIFICATION MODE"),
        new NoArgumentsElement(CLASS_PICTURE_DESCRIPTOR, ID_EDGE_WIDTH_SPECIFICATION_MODE, "EDGE WIDTH SPECIFICATION MODE"),
        new NoArgumentsElement(CLASS_PICTURE_DESCRIPTOR, ID_VDC_EXTENT, "VDC EXTENT"),
        new NoArgumentsElement(CLASS_PICTURE_DESCRIPTOR, ID_BACKGROUND_COLOUR, "BACKGROUND COLOUR"),
        new NoArgumentsElement(CLASS_PICTURE_DESCRIPTOR, ID_DEVICE_VIEWPORT, "DEVICE VIEWPORT"),
        
        new NoArgumentsElement(CLASS_GRAPHICAL_PRIMITIVE, ID_POLYLINE, "POLYLINE"),
        new NoArgumentsElement(CLASS_GRAPHICAL_PRIMITIVE, ID_DISJOINT_POLYLINE, "DISJOINT POLYLINE"),
        new NoArgumentsElement(CLASS_GRAPHICAL_PRIMITIVE, ID_POLYMARKER, "POLYMARKER"),
        new NoArgumentsElement(CLASS_GRAPHICAL_PRIMITIVE, ID_TEXT, "TEXT"),
        new NoArgumentsElement(CLASS_GRAPHICAL_PRIMITIVE, ID_RESTRICTED_TEXT, "RESTRICTED TEXT"),
        
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_LINE_BUNDLE_INDEX, "LINE BUNDLE INDEX"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_LINE_TYPE, "LINE TYPE"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_LINE_WIDTH, "LINE WIDTH"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_LINE_COLOUR, "LINE COLOUR"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_MARKER_BUNDLE_INDEX, "MARKER BUNDLE INDEX"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_MARKER_TYPE, "MARKER TYPE"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_MARKER_SIZE, "MARKER SIZE"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_MARKER_COLOUR, "MARKER COLOUR"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_TEXT_BUNDLE_INDEX, "TEXT BUNDLE INDEX"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_TEXT_FONT_INDEX, "TEXT FONT INDEX"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_TEXT_PRECISION, "TEXT PRECISION"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_CHARACTER_EXPANSION_FACTOR, "CHARACTER EXPANSION FACTOR"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_CHARACTER_SPACING, "CHARACTER SPACING"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_TEXT_COLOUR, "TEXT COLOUR"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_CHARACTER_HEIGHT, "CHARACTER HEIGHT"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_CHARACTER_ORIENTATION, "CHARACTER ORIENTATION"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_TEXT_PATH, "TEXT PATH"),
        new NoArgumentsElement(CLASS_ATTRIBUTE, ID_TEXT_ALIGNMENT, "TEXT ALIGNMENT"),
    };

    CgmParser(NitfGraphicSegment graphicSegment) {
        mGraphicSegment = graphicSegment;
    }

    void dump() throws IOException {
        byte[] data = mGraphicSegment.getGraphicData();
        dataStream = new DataInputStream(new ByteArrayInputStream(data));
        int elementClass;
        int elementId;
        do {
            int commandHeader = dataStream.readUnsignedShort();
            elementClass = (commandHeader & 0xF000) >> 12;
            elementId = (commandHeader & 0x0FE0) >> 5;
            int parameterListLength = (commandHeader & 0x001F);
            System.out.println(String.format("Raw commandHeader: 0x%04x", commandHeader));
            System.out.println(String.format("\tElement: %s", getElementName(elementClass, elementId)));
            if (parameterListLength == 31) {
                int longFormWord2 = dataStream.readUnsignedShort();
                if ((longFormWord2 & 0x8000) == 0x8000) {
                    System.out.println("Not last partition");
                }
                parameterListLength = longFormWord2 & 0x7FFF;
            }
            AbstractElement elementCode = getElementCode(elementClass, elementId);
            elementCode.readParameters(dataStream, parameterListLength);
            if (parameterListLength % 2 == 1) {
                // Skip over the undeclared pad octet
                dataStream.skipBytes(1);
            }
        } while (!isEndMetaFile(elementClass, elementId));
    }

    private boolean isEndMetaFile(int elementClass, int elementId) {
        return ((elementClass == 0) && (elementId == 2));
    }

}
