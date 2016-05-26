/*
 * The MIT License
 *
 * Copyright Codice Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codice.imaging.nitf.registryparser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Handler for simple field to description lookups.
 */
public class SimpleFieldLookupHandler extends LookupHandler {
    private Map<String, String> dataMap = new HashMap<>();

    public SimpleFieldLookupHandler(String outputFileName, String treName, String fieldName) {
        super(outputFileName, treName, fieldName);
    }

    public void handleRow(String fieldValue, String description, int rowNumber) {
        if (fieldValue.equals("None")) {
            // This is a hack until we get the register cleaned up
            return;
        }
        if (dataMap.containsKey(fieldValue)) {
            System.out.println("Parsing " + mTreName + ":" + mFieldName + "(around row " + (rowNumber + 1) + ") a duplicate VALUE was found: " + fieldValue);
        }
        dataMap.put(fieldValue, description);
    }

    public void serialise() throws IOException, XMLStreamException {
        if (dataMap.isEmpty()) {
            // This can happen if there are only "None" entries
            return;
        }
        String outputPath = "target/" + mOutputFileName; 
        try (OutputStream outStream = new FileOutputStream(outputPath)) {
            XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream);
            xmlWriter.writeStartDocument("utf-8", "1.0");
            xmlWriter.writeCharacters("\n");
            xmlWriter.writeStartElement("NitfTreValueLookup");
            xmlWriter.writeAttribute("tre", mTreName);
            xmlWriter.writeAttribute("field", mFieldName);
            xmlWriter.writeCharacters("\n");
            for (String key : new TreeSet<>(dataMap.keySet())) {
                xmlWriter.writeCharacters("\t");
                xmlWriter.writeEmptyElement("Value");
                xmlWriter.writeAttribute("value", key);
                xmlWriter.writeAttribute("description", dataMap.get(key));
                xmlWriter.writeCharacters("\n");
            }
            xmlWriter.writeEndElement();
            xmlWriter.writeCharacters("\n");
            xmlWriter.writeEndDocument();
            xmlWriter.flush();
            xmlWriter.close();
        }
    }

}
