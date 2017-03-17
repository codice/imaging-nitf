/*
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 */
package org.codice.imaging.nitf.fluent.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests that the flow API for writing NITFs works as intended.
 */
public class WritingFlowTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WritingFlowTest.class);

    private static final String INPUT_FILE_NAME = "ns3038a.nsf";

    private static final String DIRECTORY_NAME = "JitcNitf21Samples";

    private static final String TEST_FILE_TITLE = "test file title";

    private static final String TEST_ORIGINATOR_NAME = "test originator";

    private static final String TEST_STATIION_ID = "_OSTAID";

    private File outputFile;

    @Before
    public void setUp() {
        String outputFileName = "target" + File.separator + "output-" + INPUT_FILE_NAME;
        this.outputFile = new File(outputFileName);
    }

    @Test
    public void testNS3038A() throws IOException, NitfFormatException {
        String inputFileName = "/" + DIRECTORY_NAME + "/" + INPUT_FILE_NAME;
        LOGGER.info("================================== Testing :" + inputFileName);
        Assert.assertNotNull("Test file missing: " + inputFileName,
                getClass().getResource(inputFileName));
        modifyNitf(inputFileName);
        verifyResult();
    }

    public void modifyNitf(final String inputFileName) throws IOException, NitfFormatException {

        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            new NitfParserInputFlowImpl().inputStream(getClass().getResourceAsStream(inputFileName))
                    .allData()
                    .fileHeader(header -> {
                        header.setFileTitle(TEST_FILE_TITLE);
                        header.setOriginatorsName(TEST_ORIGINATOR_NAME);
                        header.setOriginatingStationId(TEST_STATIION_ID);
                    })
                    .dataSource(datasource -> new NitfWriterFlowImpl().outputStream(() -> outputStream)
                            .write(datasource));
        }
    }

    private void verifyResult() throws NitfFormatException, FileNotFoundException {
        new NitfParserInputFlowImpl().file(outputFile)
                .headerOnly()
                .fileHeader(header -> {
                    Assert.assertThat(header.getFileTitle(), CoreMatchers.is(TEST_FILE_TITLE));
                    Assert.assertThat(header.getOriginatorsName(),
                            CoreMatchers.is(TEST_ORIGINATOR_NAME));
                    Assert.assertThat(header.getOriginatingStationId(),
                            CoreMatchers.is(TEST_STATIION_ID));
                });
    }

}
