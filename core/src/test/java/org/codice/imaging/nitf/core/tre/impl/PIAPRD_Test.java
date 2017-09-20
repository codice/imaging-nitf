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
package org.codice.imaging.nitf.core.tre.impl;

import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.TreSource;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * Unit tests for PIAPRD TRE.
 *
 * See STDI-0002 Vol 1 Appendix C "PIAE"
 */
public class PIAPRD_Test {

    public PIAPRD_Test() {
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void checkPIAPRDLocation() throws NitfFormatException {
        TreBuilder piaprd = TreFactory.getDefault("PIAPRD", TreSource.ImageExtendedSubheaderData);
        fillTRE(piaprd);

        TreParser parser = new TreParser();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("TRE is only permitted in a file-level header, or in an overflow DES");
        parser.serializeTRE(piaprd.getTre());
    }

    @Test
    public void checkPIAPRDBuild() throws NitfFormatException {
        TreBuilder piaprd = TreFactory.getDefault("PIAPRD", TreSource.ExtendedHeaderData);
        fillTRE(piaprd);

        TreParser parser = new TreParser();
        byte[] serialisedTre = parser.serializeTRE(piaprd.getTre());
        String t = new String(serialisedTre);
        assertArrayEquals("979358f8-8173-11e7-89b3-1b1e7b6aa52a                            Nope                            EZ1DetA  Gold 34             Codice    CD201708150435  OpenStreetMap                           0000000000".getBytes(), serialisedTre);
    }

    private void fillTRE(TreBuilder treBuilder) {
        treBuilder.add(new TreSimpleEntryImpl("ACCESSID", "979358f8-8173-11e7-89b3-1b1e7b6aa52a", "string"));
        treBuilder.add(new TreSimpleEntryImpl("FMCONTROL", "Nope", "string"));
        treBuilder.add(new TreSimpleEntryImpl("SUBDET", "E", "string"));
        treBuilder.add(new TreSimpleEntryImpl("PRODCODE", "Z1", "string"));
        treBuilder.add(new TreSimpleEntryImpl("PRODUCERSE", "DetA", "string"));
        treBuilder.add(new TreSimpleEntryImpl("PRODIDNO", "Gold 34", "string"));
        treBuilder.add(new TreSimpleEntryImpl("PRODSNME", "Codice", "string"));
        treBuilder.add(new TreSimpleEntryImpl("PRODUCERCD", "CD", "string"));
        treBuilder.add(new TreSimpleEntryImpl("PRODCRTIME", "201708150435", "string"));
        treBuilder.add(new TreSimpleEntryImpl("MAPID", "OpenStreetMap", "string"));
        treBuilder.add(new TreSimpleEntryImpl("SECTITLEREP", "0", "integer"));
        treBuilder.add(new TreGroupListEntryImpl("SECTITLE"));
        treBuilder.add(new TreSimpleEntryImpl("REQORGREP", "0", "integer"));
        treBuilder.add(new TreGroupListEntryImpl("REQORG"));
        treBuilder.add(new TreSimpleEntryImpl("KEYWORDREP", "0", "integer"));
        treBuilder.add(new TreGroupListEntryImpl("KEYWORD"));
        treBuilder.add(new TreSimpleEntryImpl("ASSRPTREP", "0", "integer"));
        treBuilder.add(new TreGroupListEntryImpl("ASSRPT"));
        treBuilder.add(new TreSimpleEntryImpl("ATEXTREP", "0", "integer"));
        treBuilder.add(new TreGroupListEntryImpl("ATEXT"));
    }

}
