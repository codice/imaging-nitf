/**
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
 **/
package org.codice.nitf.filereader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

public class Nitf21HeaderTest {

    @Test
    public void testCompliantHeaderRead() throws IOException, ParseException {
        final String simpleNitf21File = "/i_3034c.ntf";

        assertNotNull("Test file missing", getClass().getResource(simpleNitf21File));

        InputStream is = getClass().getResourceAsStream(simpleNitf21File);
        NitfHeaderReader reader = new NitfHeaderReader(is);
        assertTrue(reader.isNitf());
        assertEquals(NitfVersion.TWO_ONE, reader.getVersion());
        assertEquals(3, reader.getComplexityLevel());
        assertEquals("BF01", reader.getStandardType());
        assertEquals("I_3034C", reader.getOriginatingStationId());
        assertEquals("1997-12-18 12:15:39", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(reader.getFileDateTime()));
        is.close();
    }

}