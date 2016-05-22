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
import java.io.InputStream;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Shared test code for TRE tests.
 */
class SharedTreTest {
    
    protected Tre parseTRE(String testData, int expectedLength, String treTag) throws NitfFormatException {
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, expectedLength, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre tre = parseResult.getTREsWithName(treTag).get(0);
        assertNotNull(tre);
        return tre;
    }

}
