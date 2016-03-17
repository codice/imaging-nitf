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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.codice.imaging.nitf.core.common.FileReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.junit.Test;

/**
 * Test for Streaming mode support.
 *
 * Streaming mode is no longer supported in NITF (as of RFC-NTB-065). It isn't that hard to support, and while no-one is
 * supposed to have produced it, there is possibly some small user group who may have generated such data.
 */
public class StreamingModeTest {

    public StreamingModeTest() {
    }

    @Test
    public void checkStreamingModeSourceRewrite() throws ParseException, URISyntaxException, IOException {
        final String testfile = "/JitcNitf21Samples/ns3321a.nsf";
        String outputFile = FilenameUtils.getName(testfile);
        assertNotNull("Test file missing", getClass().getResource(testfile));

        File resourceFile = new File(getClass().getResource(testfile).getFile());
        SlottedNitfParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        NitfReader reader = new FileReader(resourceFile);
        NitfFileParser.parse(reader, parseStrategy);
        NitfWriter writer = new NitfFileWriter(parseStrategy.getNitfDataSource(), outputFile);
        writer.write();
        assertTrue(FileUtils.contentEquals(new File(getClass().getResource("/ns3321a.nsf.reference").toURI()), new File(outputFile)));
        assertTrue(new File(outputFile).delete());
    }
}
