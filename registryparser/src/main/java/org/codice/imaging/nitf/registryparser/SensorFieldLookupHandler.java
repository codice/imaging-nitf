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
 * Lookup for fields that depend on the associated sensor.
 */
class SensorFieldLookupHandler extends LookupHandler {
    private Map<String, Map<String, String> > sensorMap = new HashMap<>();

    SensorFieldLookupHandler(String outputFileName, String treName, String fieldName) {
        super(outputFileName, treName, fieldName);
    }

    public void handleRow(String fieldValue, String description, String sensorIdent, int rowNumber) {
        if (!sensorMap.containsKey(sensorIdent)) {
            Map<String, String> fieldValues = new HashMap<>();
            sensorMap.put(sensorIdent, fieldValues);
        }
        Map<String, String> valuesForThisSensor = sensorMap.get(sensorIdent);
        if (valuesForThisSensor.containsKey(fieldValue)) {
            System.out.println("Sensor: " + sensorIdent + ", parsing " + mTreName + ":" + mFieldName + "(around row " + (rowNumber + 1) + ") a duplicate VALUE was found: " + fieldValue);
        }
        valuesForThisSensor.put(fieldValue, description);
    }

    @Override
    public void serialise() throws IOException, XMLStreamException {
        String outputPath = "target/" + mOutputFileName;
        try (OutputStream outStream = new FileOutputStream(outputPath)) {
            XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(outStream);
            xmlWriter.writeStartDocument("utf-8", "1.0");
            xmlWriter.writeCharacters("\n");
            xmlWriter.writeStartElement("NitfTreValueLookup");
            xmlWriter.writeAttribute("tre", mTreName);
            xmlWriter.writeAttribute("field", mFieldName);
            xmlWriter.writeCharacters("\n");
            for (String sensorIdent : new TreeSet<>(sensorMap.keySet())) {
                xmlWriter.writeCharacters("\t");
                xmlWriter.writeStartElement("Sensor");
                xmlWriter.writeAttribute("ident", sensorIdent);
                xmlWriter.writeCharacters("\n");
                Map<String, String> valuesForThisSensor = sensorMap.get(sensorIdent);
                for (String key : new TreeSet<>(valuesForThisSensor.keySet())) {
                    xmlWriter.writeCharacters("\t\t");
                    xmlWriter.writeEmptyElement("Value");
                    xmlWriter.writeAttribute("value", key);
                    xmlWriter.writeAttribute("description", valuesForThisSensor.get(key));
                    xmlWriter.writeCharacters("\n");
                }
                xmlWriter.writeCharacters("\t");
                xmlWriter.writeEndElement();
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
