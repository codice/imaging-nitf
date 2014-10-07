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
import org.codice.imaging.nitf.core.NitfGraphicSegment;

public class CgmParser {

    private CgmInputReader dataReader = null;
    
//    private static String getElementName(int elementClass, int elementId) {
//        CgmClass cgmClass = CgmClass.lookup(elementClass);
//        return getElementName(cgmClass, elementId);
//    }
//
//    private static String getElementName(CgmClass cgmClass, int elementId) {
//        AbstractElement elementCode = getElementCode(cgmClass, elementId);
//        if (elementCode != null) {
//            return elementCode.getName();
//        } else {
//            return String.format("missing element name for %d, %d", cgmClass.getClassIdentifier(), elementId);
//        }
//    }

//    private static AbstractElement getElementCode(int elementClass, int elementId) {
//        CgmClass cgmClass = CgmClass.lookup(elementClass);
//        return getElementCode(cgmClass, elementId);
//    }

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
        new NoArgumentsElement(CgmIdentifier.METAFILE_ELEMENT_LIST),
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
        new NoArgumentsElement(CgmIdentifier.TEXT),
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
        new NoArgumentsElement(CgmIdentifier.TEXT_FONT_INDEX),
        new NoArgumentsElement(CgmIdentifier.TEXT_PRECISION),
        new NoArgumentsElement(CgmIdentifier.CHARACTER_EXPANSION_FACTOR),
        new NoArgumentsElement(CgmIdentifier.CHARACTER_SPACING),
        new TextColourElement(),
        new NoArgumentsElement(CgmIdentifier.CHARACTER_HEIGHT),
        new NoArgumentsElement(CgmIdentifier.CHARACTER_ORIENTATION),
        new NoArgumentsElement(CgmIdentifier.TEXT_PATH),
        new NoArgumentsElement(CgmIdentifier.TEXT_ALIGNMENT),
    };

    CgmParser(NitfGraphicSegment graphicSegment) {
        dataReader = new CgmInputReader(graphicSegment);
    }

    void dump() throws IOException {
        
        int elementClass;
        int elementId;
        do {
            int commandHeader = dataReader.readUnsignedShort();
            elementClass = (commandHeader & 0xF000) >> 12;
            elementId = (commandHeader & 0x0FE0) >> 5;
            int parameterListLength = (commandHeader & 0x001F);
            if (parameterListLength == 31) {
                int longFormWord2 = dataReader.readUnsignedShort();
                if ((longFormWord2 & 0x8000) == 0x8000) {
                    System.out.println("Not last partition");
                }
                parameterListLength = longFormWord2 & 0x7FFF;
            }
            CgmIdentifier elementIdentifier = CgmIdentifier.findIdentifier(elementClass, elementId);
            AbstractElement elementCode = getElement(elementIdentifier);
            System.out.println("Element: " + elementCode.getFriendlyName());
            elementCode.readParameters(dataReader, parameterListLength);
            if (parameterListLength % 2 == 1) {
                // Skip over the undeclared pad octet
                dataReader.skipBytes(1);
            }
        } while (!isEndMetaFile(elementClass, elementId));
    }

    private boolean isEndMetaFile(int elementClass, int elementId) {
        return ((elementClass == 0) && (elementId == 2));
    }

}
