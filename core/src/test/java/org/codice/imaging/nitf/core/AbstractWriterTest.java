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
package org.codice.imaging.nitf.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.NitfFileParser;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Shared writer test code
 */
class AbstractWriterTest {

    private static final int ABOUT_100K = 100000;

    protected InputStream getInputStream(String testfile) {
        assertNotNull("Test file missing", getClass().getResource(testfile));
        return getClass().getResourceAsStream(testfile);
    }

    protected void roundTripFile(String sourceFileName) throws URISyntaxException, NitfFormatException, IOException {
        String outputFile = FilenameUtils.getName(sourceFileName);
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream(sourceFileName)));
        SlottedNitfParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        HeapStrategyConfiguration heapStrategyConfiguration = new HeapStrategyConfiguration(length -> length > ABOUT_100K);
        HeapStrategy<ImageInputStream> imageDataStrategy = new ConfigurableHeapStrategy<>(heapStrategyConfiguration,
                file -> new FileImageInputStream(file), is -> new MemoryCacheImageInputStream(is));
        parseStrategy.setImageHeapStrategy(imageDataStrategy);

        NitfFileParser.parse(reader, parseStrategy);
        NitfWriter writer = new NitfFileWriter(parseStrategy.getNitfDataSource(), outputFile);
        writer.write();
        assertTrue(FileUtils.contentEquals(new File(getClass().getResource(sourceFileName).toURI()), new File(outputFile)));
        assertTrue(new File(outputFile).delete());

        // Do the same again, but with stream writing
        try (
            OutputStream outputStream = new FileOutputStream(outputFile)) {
            writer = new NitfOutputStreamWriter(parseStrategy.getNitfDataSource(), outputStream);
            writer.write();
            assertTrue(FileUtils.contentEquals(new File(getClass().getResource(sourceFileName).toURI()), new File(outputFile)));
        }
        assertTrue(new File(outputFile).delete());
    }
}
