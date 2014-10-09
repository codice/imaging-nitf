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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codice.imaging.nitf.core.NitfGraphicSegment;

public class CgmParser {

    private CgmInputReader dataReader = null;
    private final List<AbstractElement> commands = new ArrayList<>();

    private static AbstractElement getElement(CgmIdentifier elementId){
        if (elements.containsKey(elementId)) {
            return instantiateElement(elementId);
        }
        System.out.println("Returning null for elementId:" + elementId);
        return null;
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
            Logger.getLogger(CgmParser.class.getName()).log(Level.SEVERE, null, ex);
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

    private static final HashMap<CgmIdentifier, Class> elements = new HashMap<>();
    static {
        elements.put(CgmIdentifier.NO_OP, NoArgumentsElement.class);
        elements.put(CgmIdentifier.BEGIN_METAFILE, BeginMetafileElement.class);
        elements.put(CgmIdentifier.END_METAFILE, NoArgumentsElement.class);
        elements.put(CgmIdentifier.BEGIN_PICTURE, StringFixedArgumentElement.class);
        elements.put(CgmIdentifier.BEGIN_PICTURE_BODY, NoArgumentsElement.class);
        elements.put(CgmIdentifier.END_PICTURE, NoArgumentsElement.class);
        
        elements.put(CgmIdentifier.METAFILE_VERSION, IntegerArgumentElement.class);
        elements.put(CgmIdentifier.METAFILE_DESCRIPTION, StringFixedArgumentElement.class);
        elements.put(CgmIdentifier.METAFILE_ELEMENT_LIST, MetafileElementsListElement.class);
        elements.put(CgmIdentifier.FONT_LIST, FontListElement.class);
        
        elements.put(CgmIdentifier.SCALING_MODE, NoArgumentsElement.class);
        elements.put(CgmIdentifier.COLOUR_SELECTION_MODE, ColourSelectionModeElement.class);
        elements.put(CgmIdentifier.LINE_WIDTH_SPECIFICATION_MODE, LineWidthSpecificationModeElement.class);
        elements.put(CgmIdentifier.MARKER_SIZE_SPECIFICATION_MODE, MarkerSizeSpecificationModeElement.class);
        elements.put(CgmIdentifier.EDGE_WIDTH_SPECIFICATION_MODE, EdgeWidthSpecificationModeElement.class);
        elements.put(CgmIdentifier.VDC_EXTENT, VdcExtentElement.class);
        elements.put(CgmIdentifier.BACKGROUND_COLOUR, NoArgumentsElement.class);
        elements.put(CgmIdentifier.DEVICE_VIEWPORT, NoArgumentsElement.class);
        
        elements.put(CgmIdentifier.POLYLINE, PolylineElement.class);
        elements.put(CgmIdentifier.DISJOINT_POLYLINE, NoArgumentsElement.class);
        elements.put(CgmIdentifier.POLYMARKER, NoArgumentsElement.class);
        elements.put(CgmIdentifier.TEXT, TextElement.class);
        elements.put(CgmIdentifier.RESTRICTED_TEXT, NoArgumentsElement.class);
        
        elements.put(CgmIdentifier.LINE_BUNDLE_INDEX, NoArgumentsElement.class);
        elements.put(CgmIdentifier.LINE_TYPE, LineTypeElement.class);
        elements.put(CgmIdentifier.LINE_WIDTH, LineWidthElement.class);
        elements.put(CgmIdentifier.LINE_COLOUR, LineColourElement.class);
        elements.put(CgmIdentifier.MARKER_BUNDLE_INDEX, NoArgumentsElement.class);
        elements.put(CgmIdentifier.MARKER_TYPE, NoArgumentsElement.class);
        elements.put(CgmIdentifier.MARKER_SIZE, NoArgumentsElement.class);
        elements.put(CgmIdentifier.MARKER_COLOUR, NoArgumentsElement.class);
        elements.put(CgmIdentifier.TEXT_BUNDLE_INDEX, NoArgumentsElement.class);
        elements.put(CgmIdentifier.TEXT_FONT_INDEX, TextFontIndexElement.class);
        elements.put(CgmIdentifier.TEXT_PRECISION, NoArgumentsElement.class);
        elements.put(CgmIdentifier.CHARACTER_EXPANSION_FACTOR, NoArgumentsElement.class);
        elements.put(CgmIdentifier.CHARACTER_SPACING, NoArgumentsElement.class);
        elements.put(CgmIdentifier.TEXT_COLOUR, TextColourElement.class);
        elements.put(CgmIdentifier.CHARACTER_HEIGHT, CharacterHeightElement.class);
        elements.put(CgmIdentifier.CHARACTER_ORIENTATION, CharacterOrientationElement.class);
        elements.put(CgmIdentifier.TEXT_PATH, NoArgumentsElement.class);
        elements.put(CgmIdentifier.TEXT_ALIGNMENT, NoArgumentsElement.class);
    };

    public CgmParser(NitfGraphicSegment graphicSegment) {
        dataReader = new CgmInputReader(graphicSegment);
    }

    void buildCommandList() throws IOException, InstantiationException, IllegalAccessException {
        
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
        int parameterListLength = (commandHeader & 0x001F);
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
