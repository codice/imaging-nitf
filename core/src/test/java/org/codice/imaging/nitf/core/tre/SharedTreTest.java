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
package org.codice.imaging.nitf.core.tre;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Shared test code for TRE tests.
 */
class SharedTreTest {
    
    protected Tre parseTRE(String testData, int expectedLength, String treTag) throws NitfFormatException, IOException {
        byte bytes[] = testData.getBytes();
        return parseTRE(bytes, expectedLength, treTag);
    }

    protected Tre parseTRE(byte[] bytes, int expectedLength, String treTag) throws NitfFormatException, IOException {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, expectedLength, TreSource.TreOverflowDES);
        assertEquals(1, parseResult.getTREs().size());
        Tre tre = parseResult.getTREsWithName(treTag).get(0);
        assertNotNull(tre);
        TreParser treParser = new TreParser();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(treTag.getBytes(StandardCharsets.ISO_8859_1));
        baos.write(String.format("%05d", expectedLength - 11).getBytes(StandardCharsets.ISO_8859_1));
        baos.write(treParser.serializeTRE(tre));
        byte[] actualSerialisedResults = baos.toByteArray();
        assertArrayEquals(bytes, actualSerialisedResults);
        return tre;
    }

}
