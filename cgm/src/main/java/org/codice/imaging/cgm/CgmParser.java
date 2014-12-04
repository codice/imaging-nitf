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
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * CGM Parser.
 */
public class CgmParser {

    private static final int LONG_FORM_LENGTH_INDICATOR = 31;
    private static final int NOT_LAST_PARTITION_FLAG_BIT = 0x8000;
    private static final int LONG_FORM_WORD_LENGTH_BITMASK = 0x7FFF;
    private static final int PARAMETER_LIST_LENGTH_BITMASK = 0x001F;
    private static final int ELEMENT_ID_BITMASK = 0x0FE0;
    private static final int ELEMENT_ID_BIT_SHIFT = 5;
    private static final int ELEMENT_CLASS_BIT_MASK = 0xF000;
    private static final int ELEMENT_CLASS_BIT_SHIFT = 12;

    private static final Map<CgmIdentifier, Class> ELEMENTS = new HashMap<>();

    private CgmInputReader dataReader = null;
    private final List<AbstractElement> commands = new ArrayList<>();

    private static AbstractElement getElement(final CgmIdentifier elementId) {
        if (ELEMENTS.containsKey(elementId)) {
            // System.out.println("About to instantiate:" + elementId.getFriendlyName());
            return instantiateElement(elementId);
        }
        return new NoArgumentsElement(CgmIdentifier.UNKNOWN);
    }

    private static AbstractElement instantiateElement(final CgmIdentifier elementId) {
        Class elementClass = ELEMENTS.get(elementId);
        AbstractElement element = instantiateElementWithNoArguments(elementClass);
        if (element == null) {
            element = instantiateElementWithElementId(elementClass, elementId);
        }
        return element;
    }

    private static AbstractElement instantiateElementWithNoArguments(final Class elementClass) {
        try {
            Constructor<AbstractElement> defaultConstructor = elementClass.getDeclaredConstructor();
            return defaultConstructor.newInstance();
        } catch (NoSuchMethodException |
                 SecurityException |
                 InstantiationException |
                 IllegalAccessException |
                 IllegalArgumentException |
                 InvocationTargetException ex) {
            Logger.getLogger(CgmParser.class.getName()).log(Level.SEVERE, elementClass.getSimpleName(), ex);
        }
        return null;
    }

    private static AbstractElement instantiateElementWithElementId(final Class elementClass, final CgmIdentifier elementId) {
        try {
            Constructor<AbstractElement> constructor = elementClass.getDeclaredConstructor(CgmIdentifier.class);
            return constructor.newInstance(elementId);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(CgmParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    static {
        ELEMENTS.put(CgmIdentifier.NO_OP, NoArgumentsElement.class);
        ELEMENTS.put(CgmIdentifier.BEGIN_METAFILE, BeginMetafileElement.class);
        ELEMENTS.put(CgmIdentifier.END_METAFILE, EndMetafileElement.class);
        ELEMENTS.put(CgmIdentifier.BEGIN_PICTURE, BeginPictureElement.class);
        ELEMENTS.put(CgmIdentifier.BEGIN_PICTURE_BODY, BeginPictureBodyElement.class);
        ELEMENTS.put(CgmIdentifier.END_PICTURE, EndPictureElement.class);
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

        ELEMENTS.put(CgmIdentifier.METAFILE_VERSION, MetafileVersionElement.class);
        ELEMENTS.put(CgmIdentifier.METAFILE_DESCRIPTION, MetafileDescriptionElement.class);
        // TODO: [IMG-30]  VDC TYPE (must be Integer)
        // TODO: [IMG-31]  Integer precision (must be 16 bits)
        // REAL_PRECISION prohibited in BIIF Profile BPCGM01.00
        // TODO: [IMG-32]  Index precision (must be 16 bits)
        // TODO: [IMG-33]  Colour precision (must be 8 bits)
        // TODO: [IMG-34]  Colour index precision (colour selection mode must be 1)
        // MAXIMUM_COLOUR_INDEX prohibited in BIIF Profile BPCGM01.00
        // TODO: [IMG-35]  Colour value extent (must be 0, 0, 0 through 255, 255, 255)
        ELEMENTS.put(CgmIdentifier.METAFILE_ELEMENT_LIST, MetafileElementsListElement.class);
        // METAFILE_DEFAULTS_REPLACEMENT prohibited in BIIF Profile BPCGM01.00
        ELEMENTS.put(CgmIdentifier.FONT_LIST, FontListElement.class);
        // TODO: [IMG-36]  CHARACTER_SET_LIST - must be ISO 10646-1 Character Set Basic Latin
        // CHARACTER_CODING_ANNOUNCER prohibited in BIIF Profile BPCGM01.00
        // TODO: [IMG-37]  NAME_PRECISION - (Note: must be version 1)
        // MAXIMUM_VDC_EXTENT prohibited in BIIF Profile BPCGM01.00
        // SEGMENT_PRIORITY_EXTENT prohibited in BIIF Profile BPCGM01.00
        // COLOUR_MODEL prohibited in BIIF Profile BPCGM01.00
        // COLOUR_CALIBRATION prohibited in BIIF Profile BPCGM01.00
        // FONT_PROPERTIES prohibited in BIIF Profile BPCGM01.00
        // GLYPH_MAPPING prohibited in BIIF Profile BPCGM01.00
        // SYMBOL_LIBRARY_LIST prohibited in BIIF Profile BPCGM01.00
        // PICTURE_DIRECTORY?

        // SCALING_MODE prohibited in BIIF Profile BPCGM01.00
        ELEMENTS.put(CgmIdentifier.COLOUR_SELECTION_MODE, ColourSelectionModeElement.class);
        ELEMENTS.put(CgmIdentifier.LINE_WIDTH_SPECIFICATION_MODE, LineWidthSpecificationModeElement.class);
        ELEMENTS.put(CgmIdentifier.MARKER_SIZE_SPECIFICATION_MODE, MarkerSizeSpecificationModeElement.class);
        ELEMENTS.put(CgmIdentifier.EDGE_WIDTH_SPECIFICATION_MODE, EdgeWidthSpecificationModeElement.class);
        ELEMENTS.put(CgmIdentifier.VDC_EXTENT, VdcExtentElement.class);
        ELEMENTS.put(CgmIdentifier.BACKGROUND_COLOUR, NoArgumentsElement.class);
        // DEVICE_VIEWPORT prohibited in BIIF Profile BPCGM01.10
        // DEVICE_VIEWPORT_SPECIFICATION_MODE prohibited in BIIF Profile BPCGM01.10
        // DEVICE_VIEWPORT_MAPPING prohibited in BIIF Profile BPCGM01.10
        // LINE_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // MARKER_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // TEXT_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // FILL_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // EDGE_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // INTERIOR_STYLE_REPRESENTATION prohibited in BIIF Profile BPCGM01.10
        // TODO: [IMG-38]  LINE_AND_EDGE_TYPE_DEFINITION
        // TODO: [IMG-39]  HATCH_STYLE_DEFINITION
        // GEOMETRIC_PATTERN_DEFINITION prohibited in BIIF Profile BPCGM01.10

        // TODO: [IMG-40]  VDC_INTEGER_PRECISION - must be 16 bits
        // VDC_REAL_PRECISION prohibited in BIIF Profile BPCGM01.10
        // TODO: [IMG-41]  AUXILLARY_COLOUR
        // TODO: [IMG-42]  TRANSPARENCY
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

        ELEMENTS.put(CgmIdentifier.POLYLINE, PolylineElement.class);
        ELEMENTS.put(CgmIdentifier.DISJOINT_POLYLINE, NoArgumentsElement.class);
        ELEMENTS.put(CgmIdentifier.POLYMARKER, NoArgumentsElement.class);
        ELEMENTS.put(CgmIdentifier.TEXT, TextElement.class);
        // RESTRICTED_TEXT prohibited in BIIF Profile BPCGM01.10
        // APPEND_TEXT prohibited in BIIF Profile BPCGM01.10
        ELEMENTS.put(CgmIdentifier.POLYGON, PolygonElement.class);
        ELEMENTS.put(CgmIdentifier.POLYGON_SET, PolygonSetElement.class);
        // CELL_ARRAY prohibited in BIIF Profile BPCGM01.10
        // GENERALISED_DRAWING_PRIMITIVE prohibited in BIIF Profile BPCGM01.10
        ELEMENTS.put(CgmIdentifier.RECTANGLE, RectangleElement.class);
        ELEMENTS.put(CgmIdentifier.CIRCLE, CircleElement.class);
        // CIRCULAR_ARC_3_POINT prohibited in BIIF Profile BPCGM01.10
        // CIRCULAR_ARC_3_POINT_CLOSE prohibited in BIIF Profile BPCGM01.10
        ELEMENTS.put(CgmIdentifier.CIRCULAR_ARC_CENTRE, CircularArcCentreElement.class);
        // TODO: [IMG-43]  CIRCULAR_ARC_CENTRE_CLOSE
        ELEMENTS.put(CgmIdentifier.ELLIPSE, EllipseElement.class);
        // TODO: [IMG-44]  ELLIPTICAL_ARC
        // TODO: [IMG-45]  ELLIPICAL_ARC_CLOSE
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
        ELEMENTS.put(CgmIdentifier.LINE_TYPE, LineTypeElement.class);
        ELEMENTS.put(CgmIdentifier.LINE_WIDTH, LineWidthElement.class);
        ELEMENTS.put(CgmIdentifier.LINE_COLOUR, LineColourElement.class);
        // MARKER_BUNDLE_INDEX prohibited in BIIF Profile BPCGM01.10
        // MARKER_TYPE prohibited in BIIF Profile BPCGM01.10
        // MARKER_SIZE prohibited in BIIF Profile BPCGM01.10
        // MARKER_COLOUR prohibited in BIIF Profile BPCGM01.10
        // TEXT_BUNDLE_INDEX prohibited in BIIF Profile BPCGM01.10
        ELEMENTS.put(CgmIdentifier.TEXT_FONT_INDEX, TextFontIndexElement.class);
        ELEMENTS.put(CgmIdentifier.TEXT_PRECISION, NoArgumentsElement.class);
        ELEMENTS.put(CgmIdentifier.CHARACTER_EXPANSION_FACTOR, NoArgumentsElement.class);
        ELEMENTS.put(CgmIdentifier.CHARACTER_SPACING, NoArgumentsElement.class);
        ELEMENTS.put(CgmIdentifier.TEXT_COLOUR, TextColourElement.class);
        ELEMENTS.put(CgmIdentifier.CHARACTER_HEIGHT, CharacterHeightElement.class);
        ELEMENTS.put(CgmIdentifier.CHARACTER_ORIENTATION, CharacterOrientationElement.class);
        ELEMENTS.put(CgmIdentifier.TEXT_PATH, NoArgumentsElement.class);
        ELEMENTS.put(CgmIdentifier.TEXT_ALIGNMENT, NoArgumentsElement.class);
        // CHARACTER_SET_INDEX prohibited in BIIF Profile BPCGM01.10
        // ALTERNATE_CHARACTER_SET_INDEX prohibited in BIIF Profile BPCGM01.10
        // FILL_BUNDLE_INDEX prohibited in BIIF Profile BPCGM01.10
        ELEMENTS.put(CgmIdentifier.INTERIOR_STYLE, InteriorStyleElement.class);
        ELEMENTS.put(CgmIdentifier.FILL_COLOUR, FillColourElement.class);
        ELEMENTS.put(CgmIdentifier.HATCH_INDEX, HatchIndexElement.class);
        // PATTERN_INDEX　prohibited in BIIF Profile BPCGM01.10
        // EDGE_BUNDLE_INDEX prohibited in BIIF Profile BPCGM01.10
        ELEMENTS.put(CgmIdentifier.EDGE_TYPE, EdgeTypeElement.class);
        ELEMENTS.put(CgmIdentifier.EDGE_WIDTH, EdgeWidthElement.class);
        ELEMENTS.put(CgmIdentifier.EDGE_COLOUR, EdgeColourElement.class);
        ELEMENTS.put(CgmIdentifier.EDGE_VISIBILITY, EdgeVisibilityElement.class);
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
    }

    /**
     * Constructor.
     *
     * @param graphicSegmentData the CGM data to parse
     */
    public CgmParser(final byte[] graphicSegmentData) {
        dataReader = new CgmInputReader(graphicSegmentData);
    }

    /**
     * Constructor.
     *
     * @param stream stream containing the CGM data to parse
     */
    public CgmParser(final InputStream stream) {
        dataReader = new CgmInputReader(stream);
    }

    /**
     * Build the command list for the file.
     *
     * @throws IOException on parse failure
     */
    public final void buildCommandList() throws IOException {

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

    final void dump() {
        System.out.print(getCommandListAsString());
    }

    /**
     * Return the command list as lines of text.
     *
     * @return the command list in string form
     */
    public final String getCommandListAsString() {
        StringBuilder builder = new StringBuilder();
        for (AbstractElement command : commands) {
            builder.append("Command: ");
            builder.append(command.getFriendlyName());
            builder.append(System.lineSeparator());
            command.addStringDescription(builder);
        }
        return builder.toString();
    }

    private void skipOverPadOctetIfNecessary(final int parameterListLength) throws IOException {
        if (parameterListLength % 2 != 0) {
            // Skip over the undeclared pad octet
            dataReader.skipBytes(1);
        }
    }

    private int getParameterListLength(final int commandHeader) throws IOException {
        int parameterListLength = commandHeader & PARAMETER_LIST_LENGTH_BITMASK;
        if (parameterListLength == LONG_FORM_LENGTH_INDICATOR) {
            int longFormWord2 = dataReader.readUnsignedShort();
            if ((longFormWord2 & NOT_LAST_PARTITION_FLAG_BIT) == NOT_LAST_PARTITION_FLAG_BIT) {
                // Don't know how to handle this case yet
                System.out.println("Not last partition");
            }
            parameterListLength = longFormWord2 & LONG_FORM_WORD_LENGTH_BITMASK;
        }
        return parameterListLength;
    }

    private static int getElementId(final int commandHeader) {
        return (commandHeader & ELEMENT_ID_BITMASK) >> ELEMENT_ID_BIT_SHIFT;
    }

    private static int getElementClass(final int commandHeader) {
        return (commandHeader & ELEMENT_CLASS_BIT_MASK) >> ELEMENT_CLASS_BIT_SHIFT;
    }

    /**
     * Return the commands that have been parsed from the file.
     *
     * This won't make much sense unless the file has been parsed, using buildCommandList().
     * @return the commands
     */
    public final List<AbstractElement> getCommandList() {
        return commands;
    }
}
