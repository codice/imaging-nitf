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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Parsing code for the TRE Metadata sheet.
 */
public class TreMetadataSheetHandler {

    private static final int TRE_COLUMN = 1;
    private static final int FIELD_NAME_COLUMN = 2;
    private static final int VALUE_COLUMN = 3;
    private static final int SENSOR_COLUMN = 4;
    private static final int DESCRIPTION_COLUMN = 5;

    private final List<SimpleFieldLookupHandler> simpleHandlers = new ArrayList<>();
    private final List<SensorFieldLookupHandler> sensorHandlers = new ArrayList<>();

    private final Sheet metadataSheet;

    TreMetadataSheetHandler(Sheet sheet) {
        metadataSheet = sheet;
        sanityCheckHeader();
    }

    public final void process() throws IOException, XMLStreamException {
        for (Row row : metadataSheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            handleOneRow(row);
        }
        for (SimpleFieldLookupHandler handler : simpleHandlers) {
            handler.serialise();
        }
        for (SensorFieldLookupHandler handler: sensorHandlers) {
            handler.serialise();
        }
    }

    private void handleOneRow(Row row) {
        String treName = row.getCell(TRE_COLUMN).toString();
        String fieldName = row.getCell(FIELD_NAME_COLUMN).toString();
        String fieldValue = row.getCell(VALUE_COLUMN).toString();
        String sensor = row.getCell(SENSOR_COLUMN).toString();
        String description = row.getCell(DESCRIPTION_COLUMN).toString();
        if (sensor.equals("N/A")) {
            handleSimpleRow(treName, fieldName, fieldValue, description, row.getRowNum());
        } else {
            handleSensorRow(sensor, treName, fieldName, fieldValue, description, row.getRowNum());
        }
    }

    private void sanityCheckHeader() throws IllegalStateException {
        Row headerRow = metadataSheet.getRow(0);
        checkHeaderCell(headerRow, TRE_COLUMN, "TRE");
        checkHeaderCell(headerRow, FIELD_NAME_COLUMN, "FIELD");
        checkHeaderCell(headerRow, VALUE_COLUMN, "VALUE");
        checkHeaderCell(headerRow, SENSOR_COLUMN, "SENSOR");
        checkHeaderCell(headerRow, DESCRIPTION_COLUMN, "DESCRIPTION");
    }

    private void checkHeaderCell(Row headerRow, int zeroBasedIndex, String expectedContent) throws IllegalStateException {
        if (!headerRow.getCell(zeroBasedIndex).toString().equals(expectedContent)) {
            throw new IllegalStateException("Expected second column to be " + expectedContent + ", but was " + headerRow.getCell(zeroBasedIndex).toString() + ". Possible format change");
        }
    }

    private void handleSimpleRow(String treName, String fieldName, String fieldValue, String description, int rowNumber) {
        SimpleFieldLookupHandler handler = getHandler(treName, fieldName);
        if (handler != null) {
            handler.handleRow(fieldValue, description, rowNumber);
        } else {
            throw new IllegalStateException("No handler available");
        }
    }

    private void handleSensorRow(String sensor, String treName, String fieldName, String fieldValue, String description, int rowNumber) {
        SensorFieldLookupHandler handler = getSensorHandler(treName, fieldName);

        if (handler != null) {
            handler.handleRow(fieldValue, description, sensor, rowNumber);
        } else {
            throw new IllegalStateException("No handler available");
        }
    }

    private SimpleFieldLookupHandler getHandler(String treName, String fieldName) {
        for (SimpleFieldLookupHandler handler : simpleHandlers) {
            if (handler.getTreName().equals(treName) && handler.getFieldName().equals(fieldName)) {
                return handler;
            }
        }
        SimpleFieldLookupHandler handler = new SimpleFieldLookupHandler(treName + "_" + fieldName + ".xml", treName, fieldName);
        simpleHandlers.add(handler);
        return handler;        
    }

    private SensorFieldLookupHandler getSensorHandler(String treName, String fieldName) {
        for (SensorFieldLookupHandler handler : sensorHandlers) {
            if (handler.getTreName().equals(treName) && handler.getFieldName().equals(fieldName)) {
                return handler;
            }
        }
        SensorFieldLookupHandler handler = new SensorFieldLookupHandler(treName + "_" + fieldName + "_sensor.xml", treName, fieldName);
        sensorHandlers.add(handler);
        return handler;        
    }
}
