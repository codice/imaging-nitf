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
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.header.impl.NitfParser;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.TargetId;
import org.codice.imaging.nitf.core.image.impl.TargetIdImpl;
import org.codice.imaging.nitf.core.impl.ConfigurableHeapStrategy;
import org.codice.imaging.nitf.core.impl.HeapStrategyConfiguration;
import org.codice.imaging.nitf.core.impl.NitfFileWriter;
import org.codice.imaging.nitf.core.impl.NitfOutputStreamWriter;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.junit.Before;
import org.junit.Test;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Tests for writing NITF file - basic cases.
 */
public class BasicWriterTest extends AbstractWriterTest {

    TestLogger LOGGER = TestLoggerFactory.getTestLogger(NitfFileWriter.class);

    @Test
    public void roundTripSimpleFile() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/WithBE.ntf");
    }

    @Test
    public void roundTripDESFile() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/autzen-utm10.ntf");
    }

    @Test
    public void roundTripGraphicSegmentExt() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/gdal3453.ntf");
    }

    @Test
    public void roundTripMultiImageFile() throws NitfFormatException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3361c.nsf");
    }

    @Test
    public void roundTripGraphicFile() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3051e.ntf");
    }

    @Test
    public void roundTripGraphic2File() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/ns3051v.nsf");
    }

    @Test
    public void roundTripTREs() throws IOException, NitfFormatException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3128b.ntf");
    }

    @Before
    public void clearLoggers()
    {
        TestLoggerFactory.clearAll();
    }

    @Test
    public void checkBadWriter() throws NitfFormatException, IOException {
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream("/WithBE.ntf")));
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.ALL_SEGMENT_DATA);
        HeapStrategyConfiguration heapStrategyConfiguration = new HeapStrategyConfiguration(length -> length > ABOUT_100K);
        HeapStrategy<ImageInputStream> imageDataStrategy = new ConfigurableHeapStrategy<>(heapStrategyConfiguration,
                FileImageInputStream::new, MemoryCacheImageInputStream::new);
        parseStrategy.setImageHeapStrategy(imageDataStrategy);
        NitfParser.parse(reader, parseStrategy);

        // Introduce deliberate issue
        // Introduce deliberate issue
        ImageSegment imageSegment = parseStrategy.getDataSource().getImageSegments().get(0);
        TargetId originalTargetId = imageSegment.getImageTargetId();
        TargetIdImpl newTargetId = new TargetIdImpl();
        newTargetId.setBasicEncyclopediaNumber(originalTargetId.getBasicEncyclopediaNumber());
        newTargetId.setOSuffix(originalTargetId.getOSuffix());
        newTargetId.setCountryCode(null);
        imageSegment.setImageTargetId(newTargetId);

        File outputFile = File.createTempFile("checkBadWriter",".ntf");
        NitfWriter writer = new NitfFileWriter(parseStrategy.getDataSource(), outputFile.getAbsolutePath());
        assertEquals(0, LOGGER.getLoggingEvents().size());
        writer.write();
        assertThat(LOGGER.getLoggingEvents(), is(Arrays.asList(LoggingEvent.error("Could not write {}", "Cannot generate string target identifier with null country code"))));
    }

    @Test
    public void checkBadStreamWriter() throws NitfFormatException, IOException {
        OutputStream outputStream = new FileOutputStream(File.createTempFile("checkBadStreamWriter", ".ntf"));
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream("/WithBE.ntf")));
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.ALL_SEGMENT_DATA);
        HeapStrategyConfiguration heapStrategyConfiguration = new HeapStrategyConfiguration(length -> length > ABOUT_100K);
        HeapStrategy<ImageInputStream> imageDataStrategy = new ConfigurableHeapStrategy<>(heapStrategyConfiguration,
                FileImageInputStream::new, MemoryCacheImageInputStream::new);
        parseStrategy.setImageHeapStrategy(imageDataStrategy);
        NitfParser.parse(reader, parseStrategy);

        // Introduce deliberate issue
        ImageSegment imageSegment = parseStrategy.getDataSource().getImageSegments().get(0);
        TargetId originalTargetId = imageSegment.getImageTargetId();
        TargetIdImpl newTargetId = new TargetIdImpl();
        newTargetId.setBasicEncyclopediaNumber(originalTargetId.getBasicEncyclopediaNumber());
        newTargetId.setOSuffix(originalTargetId.getOSuffix());
        newTargetId.setCountryCode(null);
        imageSegment.setImageTargetId(newTargetId);

        NitfWriter writer = new NitfOutputStreamWriter(parseStrategy.getDataSource(), outputStream);
        assertEquals(0, LOGGER.getLoggingEvents().size());
        writer.write();
        assertThat(LOGGER.getLoggingEvents(), is(Arrays.asList(LoggingEvent.error("Could not write {}", "Cannot generate string target identifier with null country code"))));
    }
}
