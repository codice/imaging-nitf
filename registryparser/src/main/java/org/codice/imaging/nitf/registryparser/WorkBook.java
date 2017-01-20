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
import javax.xml.stream.XMLStreamException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Basic Excel parser for the NITF Metadata Registry.
 */
public class WorkBook {

    private static final String WORKBOOK_RESOURCE_NAME = "/NITF Metadata Register 04082016 (Pre-Coordination Draft).xlsx";

    public static void main(String[] args) throws IOException, XMLStreamException {
        try (Workbook wb = new XSSFWorkbook(WorkBook.class.getResourceAsStream(
                WORKBOOK_RESOURCE_NAME));) {
            Sheet metadataSheet = wb.getSheet("TRE Metadata");
            TreMetadataSheetHandler treMetadataSheetHandler = new TreMetadataSheetHandler(
                    metadataSheet);
            treMetadataSheetHandler.process();
        }
    }
}
