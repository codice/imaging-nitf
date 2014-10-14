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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codice.imaging.nitf.core.NitfGraphicSegment;

public class CgmParser {

    private CgmInputReader dataReader = null;
    private final List<AbstractElement> commands = new ArrayList<>();

    private static AbstractElement getElement(CgmIdentifier elementId){
        if (elements.containsKey(elementId)) {
            // System.out.println("About to instantiate:" + elementId.getFriendlyName());
            return instantiateElement(elementId);
        }
        return new NoArgumentsElement(CgmIdentifier.UNKNOWN);
    }

    private static AbstractElement instantiateElement(CgmIdentifier elementId) {
        Class elementClass = elements.get(elementId);
        AbstractElement element = instantiateElementWithNoArguments(elementClass);
        if (element == null) {
            element = instantiateElementWithElementId(elementClass, elementId);
        }
        return element;
    }

    private static AbstractElement instantiateElementWithNoArguments(Class elementClass){
        try {
            Constructor<AbstractElement> defaultConstructor = elementClass.getDeclaredConstructor();
            return defaultConstructor.newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(CgmParser.class.getName()).log(Level.SEVERE, elementClass.getSimpleName(), ex);
        }
        return null;
    }

    private static AbstractElement instantiateElementWithElementId(Class elementClass, CgmIdentifier elementId) {
        try {
            Constructor<AbstractElement> constructor = elementClass.getDeclaredConstructor(CgmIdentifier.class);
            return constructor.newInstance(elementId);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(CgmParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static final Map<CgmIdentifier, Class> elements = new HashMap<>();
    static {
        elements.put(CgmIdentifier.NO_OP, NoArgumentsElement.class);
        elements.put(CgmIdentifier.BEGIN_METAFILE, BeginMetafileElement.class);
        elements.put(CgmIdentifier.END_METAFILE, EndMetafileElement.class);
        elements.put(CgmIdentifier.BEGIN_PICTURE, BeginPictureElement.class);
        elements.put(CgmIdentifier.BEGIN_PICTURE_BODY, BeginPictureBodyElement.class);
        elements.put(CgmIdentifier.END_PICTURE, EndPictureElement.class);
        // BEGIN_SEGMENT prohibited in BIIF Profile BPCGM01.00
        // END_SEGMENT prohibited in BIIF Profile BPCGM01.00
        // BEGIN_FIGURE prohibited in BIIF Profile BPCGM01.00
        // END_FIGURE prohibited in BIIF Profile BPCGM01.00
        // BEGIN_PROTECTION_REGION prohibited in BIIF Profile BPCGM01.00
        // END_PROTECTION_REGION prohibited in BIIF Profile BPCGM01.00
        // BEGIN_COMPOUND_LINE prohibited in BIIF Profile BPCGM01.00
        // END_COMPOUND_LINE prohibited in BIIF Profile BPCGM01.00
        // BEGIN_COMPOUND_TEXT_PATH prohibited in BIIF Profile BPCGM01.00
        // END_COMPOUND_TEXT_PATH prohibited in BIIF Profile BPCGM01.00
        // BEGIN_TILE_ARRAY prohibited in BIIF Profile BPCGM01.00
        // END_TILE_ARRAY prohibited in BIIF Profile BPCGM01.00

        elements.put(CgmIdentifier.METAFILE_VERSION, MetafileVersionElement.class);
        elements.put(CgmIdentifier.METAFILE_DESCRIPTION, MetafileDescriptionElement.class);
        // TODO: VDC TYPE (must be Integer)
        // TODO: Integer precision (must be 16 bits)
        // REAL_PRECISION prohibited in BIIF Profile BPCGM01.00
        // TODO: Index precision (must be 16 bits)
        // TODO: Colour precision (must be 8 bits)
        // TODO: Colour index precision (colour selection mode must be 1)
        // MAXIMUM_COLOUR_INDEX prohibited in BIIF Profile BPCGM01.00
        // TODO: Colour value extent (must be 0, 0, 0 through 255, 255, 255)
        elements.put(CgmIdentifier.METAFILE_ELEMENT_LIST, MetafileElementsListElement.class);
        // METAFILE_DEFAULTS_REPLACEMENT prohibited in BIIF Profile BPCGM01.00
        elements.put(CgmIdentifier.FONT_LIST, FontListElement.class);
        // TODO: CHARACTER_SET_LIST - must be ISO 10646-1 Character Set Basic Latin
        // CHARACTER_CODING_ANNOUNCER prohibited in BIIF Profile BPCGM01.00
        // TODO: NAME_PRECISION - (Note: must be version 1)
        // MAXIMUM_VDC_EXTENT prohibited in BIIF Profile BPCGM01.00
        // SEGMENT_PRIORITY_EXTENT prohibited in BIIF Profile BPCGM01.00
        // COLOUR_MODEL prohibited in BIIF Profile BPCGM01.00
        // COLOUR_CALIBRATION prohibited in BIIF Profile BPCGM01.00
        // FONT_PROPERTIES prohibited in BIIF Profile BPCGM01.00
        // GLYPH_MAPPING prohibited in BIIF Profile BPCGM01.00
        // SYMBOL_LIBRARY_LIST prohibited in BIIF Profile BPCGM01.00
        // PICTURE_DIRECTORY?

        // SCALING_MODE prohibited in BIIF Profile BPCGM01.00
        elements.put(CgmIdentifier.COLOUR_SELECTION_MODE, ColourSelectionModeElement.class);
        elements.put(CgmIdentifier.LINE_WIDTH_SPECIFICATION_MODE, LineWidthSpecificationModeElement.class);
        elements.put(CgmIdentifier.MARKER_SIZE_SPECIFICATION_MODE, MarkerSizeSpecificationModeElement.class);
        elements.put(CgmIdentifier.EDGE_WIDTH_SPECIFICATION_MODE, EdgeWidthSpecificationModeElement.class);
        elements.put(CgmIdentifier.VDC_EXTENT, VdcExtentElement.class);
        elements.put(CgmIdentifier.BACKGROUND_COLOUR, NoArgumentsElement.class);
        // DEVICE_VIEWPORT prohibited in BIIF Profile BPCGM01.10
        // DEVICE_VIEWPORT_SPECIFICATION_MODE prohibited in BIIF Profile BPCGM01.10
        // DEVICE_VIEWPORT_MAPPING prohibited in BIIF Profile BPCGM01.10
        // LINE_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // MARKER_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // TEXT_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // FILL_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // EDGE_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // INTERIOR_STYLE_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // TODO: LINE_AND_EDGE_TYPE_DEFINITION
        // TODO: HATCH_STYLE_DEFINITION
        // GEOMETRIC_PATTERN_DEFINITION prohibited in BIIF Profile BPCGM01.10

        // TODO: VDC_INTEGER_PRECISION - must be 16 bits
        // VDC_REAL_PRECISION prohibited in BIIF Profile BPCGM01.10
        // TODO: AUXILLARY_COLOUR
        // TODO: TRANSPARENCY
        // CLIP_RECTANGLE prohibited in BIIF Profile BPCGM01.10
        // CLIP_INDICATOR prohibited in BIIF Profile BPCGM01.10
        // LINE_CLIPPING_MODE prohibited in BIIF Profile BPCGM01.10
        // MARKER_CLIPPING_MODE prohibited in BIIF Profile BPCGM01.10
        // EDGE_CLIPPING_MODE prohibited in BIIF Profile BPCGM01.10
        // NEW_REGION prohibited in BIIF Profile BPCGM01.10
        // SAVE_PRIMITIVE_CONTEXT prohibited in BIIF Profile BPCGM01.10
        // RESTORE_PRIMITIVE_CONTEXT prohibited in BIIF Profile BPCGM01.10
        // PROTECTION_REGION_INDICATOR prohibited in BIIF Profile BPCGM01.10
        // GENERALISED_TEXT_PATH_MODE prohibited in BIIF Profile BPCGM01.10
        // MITRE_LIMIT prohibited in BIIF Profile BPCGM01.10
        // TRANSPARENT_CELL_COLOUR prohibited in BIIF Profile BPCGM01.10

        elements.put(CgmIdentifier.POLYLINE, PolylineElement.class);
        elements.put(CgmIdentifier.DISJOINT_POLYLINE, NoArgumentsElement.class);
        elements.put(CgmIdentifier.POLYMARKER, NoArgumentsElement.class);
        elements.put(CgmIdentifier.TEXT, TextElement.class);
        // RESTRICTED_TEXT prohibited in BIIF Profile BPCGM01.10
        // APPEND_TEXT prohibited in BIIF Profile BPCGM01.10
        // TODO: POLYGON
        elements.put(CgmIdentifier.POLYGON_SET, PolygonSetElement.class);
        // CELL_ARRAY prohibited in BIIF Profile BPCGM01.10
        // GENERALISED_DRAWING_PRIMITIVE prohibited in BIIF Profile BPCGM01.10
        // TODO: RECTANGLE
        elements.put(CgmIdentifier.CIRCLE, CircleElement.class);
        // CIRCULAR_ARC_3_POINT prohibited in BIIF Profile BPCGM01.10
        // CIRCULAR_ARC_3_POINT_CLOSE prohibited in BIIF Profile BPCGM01.10
        // TODO: CIRCUALR_ARC_CENTRE
        // TODO: CIRCUALR_ARC_CENTRE
        // TODO: ELLIPSE
        // TODO: ELLIPTICAL_ARC
        // TODO: ELLIPICAL_ARC_CLOSE
        // CIRCULAR_ARC_CENTRE_REVERSED prohibited in BIIF Profile BPCGM01.10
        // CONNECTING_EDGE prohibited in BIIF Profile BPCGM01.10
        // HYPERBOLIC_ARC prohibited in BIIF Profile BPCGM01.10
        // PARABOLIC_ARC prohibited in BIIF Profile BPCGM01.10
        // NON_UNIFORM_B_SPLINE prohibited in BIIF Profile BPCGM01.10
        // NON_UNIFORM_RATIONAL_B_SPLINE prohibited in BIIF Profile BPCGM01.10
        // POLYBEZIER prohibited in BIIF Profile BPCGM01.10
        // POLYSYMBOL prohibited in BIIF Profile BPCGM01.10
        // BITONAL_TILE prohibited in BIIF Profile BPCGM01.10
        // TILE prohibited in BIIF Profile BPCGM01.10

        // LINE_BUNDLE_INDEX prohibited in BIIF Profile BPCGM01.10
        elements.put(CgmIdentifier.LINE_TYPE, LineTypeElement.class);
        elements.put(CgmIdentifier.LINE_WIDTH, LineWidthElement.class);
        elements.put(CgmIdentifier.LINE_COLOUR, LineColourElement.class);
        // MARKER_BUNDLE_INDEX prohibited in BIIF Profile BPCGM01.10
        // MARKER_TYPE prohibited in BIIF Profile BPCGM01.10
        // MARKER_SIZE prohibited in BIIF Profile BPCGM01.10
        // MARKER_COLOUR prohibited in BIIF Profile BPCGM01.10
        // TEXT_BUNDLE_INDEX prohibited in BIIF Profile BPCGM01.10
        elements.put(CgmIdentifier.TEXT_FONT_INDEX, TextFontIndexElement.class);
        elements.put(CgmIdentifier.TEXT_PRECISION, NoArgumentsElement.class);
        elements.put(CgmIdentifier.CHARACTER_EXPANSION_FACTOR, NoArgumentsElement.class);
        elements.put(CgmIdentifier.CHARACTER_SPACING, NoArgumentsElement.class);
        elements.put(CgmIdentifier.TEXT_COLOUR, TextColourElement.class);
        elements.put(CgmIdentifier.CHARACTER_HEIGHT, CharacterHeightElement.class);
        elements.put(CgmIdentifier.CHARACTER_ORIENTATION, CharacterOrientationElement.class);
        elements.put(CgmIdentifier.TEXT_PATH, NoArgumentsElement.class);
        elements.put(CgmIdentifier.TEXT_ALIGNMENT, NoArgumentsElement.class);
        // CHARACTER_SET_INDEX prohibited in BIIF Profile BPCGM01.10
        // ALTERNATE_CHARACTER_SET_INDEX prohibited in BIIF Profile BPCGM01.10
        // FILL_BUNDLE_INDEX prohibited in BIIF Profile BPCGM01.10
        elements.put(CgmIdentifier.INTERIOR_STYLE, InteriorStyleElement.class);
        elements.put(CgmIdentifier.FILL_COLOUR, FillColourElement.class);
        elements.put(CgmIdentifier.HATCH_INDEX, HatchIndexElement.class);
        // PATTERN_INDEX　prohibited in BIIF Profile BPCGM01.10
        // EDGE_BUNDLE_INDEX prohibited in BIIF Profile BPCGM01.10
        elements.put(CgmIdentifier.EDGE_TYPE, EdgeTypeElement.class);
        elements.put(CgmIdentifier.EDGE_WIDTH, EdgeWidthElement.class);
        elements.put(CgmIdentifier.EDGE_COLOUR, EdgeColourElement.class);
        elements.put(CgmIdentifier.EDGE_VISIBILITY, EdgeVisibilityElement.class);
        // FILL_REFERENCE_POINT prohibited in BIIF Profile BPCGM01.10
        // PATTERN_TABLE prohibited in BIIF Profile BPCGM01.10
        // PATTERN_SIZE prohibited in BIIF Profile BPCGM01.10
        // COLOUR_TABLE prohibited in BIIF Profile BPCGM01.10
        // ASPECT_SOURCE_FLAGS prohibited in BIIF Profile BPCGM01.10
        // PICK_IDENTIFIER prohibited in BIIF Profile BPCGM01.10
        // LINE_CAP prohibited in BIIF Profile BPCGM01.10
        // LINE_JOIN prohibited in BIIF Profile BPCGM01.10
        // LINE_TYPE_CONTINUATION prohibited in BIIF Profile BPCGM01.10
        // LINE_TYPE_INITIAL_OFFSET prohibited in BIIF Profile BPCGM01.10
        // TEXT_SOURCE_TYPE prohibited in BIIF Profile BPCGM01.10
        // RESTRICTED_TEXT_TYPE prohibited in BIIF Profile BPCGM01.10
        // INTERPOLATED_INTERIOR prohibited in BIIF Profile BPCGM01.10
        // EDGE_CAP prohibited in BIIF Profile BPCGM01.10
        // EDGE_JOIN prohibited in BIIF Profile BPCGM01.10
        // EDGE_TYPE_CONTINUATION prohibited in BIIF Profile BPCGM01.10
        // EDGE_TYPE_INITIAL_OFFSET prohibited in BIIF Profile BPCGM01.10
        // SYMBOL_LIBRARY_INDEX prohibited in BIIF Profile BPCGM01.10
        // SYMBOL_COLOUR prohibited in BIIF Profile BPCGM01.10
        // SYMBOL_SIZE prohibited in BIIF Profile BPCGM01.10
        // SYMBOL_ORIENTATION prohibited in BIIF Profile BPCGM01.10

        // ESCAPE prohibited in BIIF Profile BPCGM01.10

        // MESSAGE prohibited in BIIF Profile BPCGM01.10
        // APPLICATION_DATA prohibited in BIIF Profile BPCGM01.10

        // COPY_SEGMENT prohibited in BIIF Profile BPCGM01.10
        // INHERITANCE_FILTER prohibited in BIIF Profile BPCGM01.10
        // CLIP_INHERITANCE prohibited in BIIF Profile BPCGM01.10
        // SEGMENT_TRANSFORMATION prohibited in BIIF Profile BPCGM01.10
        // SEGMENT_HIGHLIGHTING prohibited in BIIF Profile BPCGM01.10
        // SEGMENT_DISPLAY_PRIORITY prohibited in BIIF Profile BPCGM01.10
        // SEGMENT_PICK_PRIORITY prohibited in BIIF Profile BPCGM01.10
    };

    public CgmParser(NitfGraphicSegment graphicSegment) {
        dataReader = new CgmInputReader(graphicSegment);
    }

    void buildCommandList() throws IOException {
        
        AbstractElement element;
        do {
            int commandHeader = dataReader.readUnsignedShort();
            int elementClass = getElementClass(commandHeader);
            int elementId = getElementId(commandHeader);
            int parameterListLength = getParameterListLength(commandHeader);

            CgmIdentifier elementIdentifier = CgmIdentifier.findIdentifier(elementClass, elementId);
            element = getElement(elementIdentifier);
            element.readParameters(dataReader, parameterListLength);
            commands.add(element);

            skipOverPadOctetIfNecessary(parameterListLength);
        } while (!element.matches(CgmIdentifier.END_METAFILE));
    }

    void dump() {
        for (AbstractElement command : commands) {
            System.out.println("Command: " + command.getFriendlyName());
            command.dumpParameters();
        }
    }

    private void skipOverPadOctetIfNecessary(int parameterListLength) throws IOException {
        if (parameterListLength % 2 != 0) {
            // Skip over the undeclared pad octet
            dataReader.skipBytes(1);
        }
    }

    private int getParameterListLength(int commandHeader) throws IOException {
        int parameterListLength = commandHeader & 0x001F;
        if (parameterListLength == 31) {
            int longFormWord2 = dataReader.readUnsignedShort();
            if ((longFormWord2 & 0x8000) == 0x8000) {
                // Don't know how to handle this case yet
                System.out.println("Not last partition");
            }
            parameterListLength = longFormWord2 & 0x7FFF;
        }
        return parameterListLength;
    }

    private static int getElementId(int commandHeader) {
        return (commandHeader & 0x0FE0) >> 5;
    }

    private static int getElementClass(int commandHeader) {
        return (commandHeader & 0xF000) >> 12;
    }

    List<AbstractElement> getCommandList() {
        return commands;
    }
}
