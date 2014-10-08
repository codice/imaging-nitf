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
import java.util.ArrayList;
import java.util.List;

import org.codice.imaging.nitf.core.NitfGraphicSegment;

public class CgmParser {

    private CgmInputReader dataReader = null;
    private List<AbstractElement> commands = new ArrayList<>();

    private static AbstractElement getElement(CgmIdentifier elementId) {
        for (int i = 0; i < elementCodes.length; ++i) {
            if (elementCodes[i].matches(elementId)) {
                return elementCodes[i];
            }
        }
        System.out.println("Returning null for elementId:" + elementId);
        return null;
    }

    private static final AbstractElement [] elementCodes = { 
        new NoArgumentsElement(CgmIdentifier.NO_OP),
        new BeginMetafileElement(),
        new NoArgumentsElement(CgmIdentifier.END_METAFILE),
        new StringFixedArgumentElement(CgmIdentifier.BEGIN_PICTURE),
        new NoArgumentsElement(CgmIdentifier.BEGIN_PICTURE_BODY),
        new NoArgumentsElement(CgmIdentifier.END_PICTURE),
        new NoArgumentsElement(CgmIdentifier.BEGIN_SEGMENT),
        
        new IntegerArgumentElement(CgmIdentifier.METAFILE_VERSION),
        new StringFixedArgumentElement(CgmIdentifier.METAFILE_DESCRIPTION),
        new MetafileElementsListElement(),
        new FontListElement(),
        
        new NoArgumentsElement(CgmIdentifier.SCALING_MODE),
        new ColourSelectionModeElement(),
        new LineWidthSpecificationModeElement(),
        new MarkerSizeSpecificationModeElement(),
        new EdgeWidthSpecificationModeElement(),
        new VdcExtentElement(),
        new NoArgumentsElement(CgmIdentifier.BACKGROUND_COLOUR),
        new NoArgumentsElement(CgmIdentifier.DEVICE_VIEWPORT),
        
        new PolylineElement(),
        new NoArgumentsElement(CgmIdentifier.DISJOINT_POLYLINE),
        new NoArgumentsElement(CgmIdentifier.POLYMARKER),
        new TextElement(),
        new NoArgumentsElement(CgmIdentifier.RESTRICTED_TEXT),
        
        new NoArgumentsElement(CgmIdentifier.LINE_BUNDLE_INDEX),
        new LineTypeElement(),
        new LineWidthElement(),
        new LineColourElement(),
        new NoArgumentsElement(CgmIdentifier.MARKER_BUNDLE_INDEX),
        new NoArgumentsElement(CgmIdentifier.MARKER_TYPE),
        new NoArgumentsElement(CgmIdentifier.MARKER_SIZE),
        new NoArgumentsElement(CgmIdentifier.MARKER_COLOUR),
        new NoArgumentsElement(CgmIdentifier.TEXT_BUNDLE_INDEX),
        new TextFontIndexElement(),
        new NoArgumentsElement(CgmIdentifier.TEXT_PRECISION),
        new NoArgumentsElement(CgmIdentifier.CHARACTER_EXPANSION_FACTOR),
        new NoArgumentsElement(CgmIdentifier.CHARACTER_SPACING),
        new TextColourElement(),
        new CharacterHeightElement(),
        new CharacterOrientationElement(),
        new NoArgumentsElement(CgmIdentifier.TEXT_PATH),
        new NoArgumentsElement(CgmIdentifier.TEXT_ALIGNMENT),
    };

    CgmParser(NitfGraphicSegment graphicSegment) {
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
}
