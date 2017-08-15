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
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreSource;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
        Tre piaprd = TreFactory.getDefault("PIAPRD", TreSource.ImageExtendedSubheaderData);
        fillTRE(piaprd);

        TreParser parser = new TreParser();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("TRE is only permitted in a file-level header, or in an overflow DES");
        parser.serializeTRE(piaprd);
    }

    @Test
    public void checkPIAPRDBuild() throws NitfFormatException {
        Tre piaprd = TreFactory.getDefault("PIAPRD", TreSource.ExtendedHeaderData);
        fillTRE(piaprd);

        TreParser parser = new TreParser();
        byte[] serialisedTre = parser.serializeTRE(piaprd);
        String t = new String(serialisedTre);
        assertArrayEquals("979358f8-8173-11e7-89b3-1b1e7b6aa52a                            Nope                            EZ1DetA  Gold 34             Codice    CD201708150435  OpenStreetMap                           0000000000".getBytes(), serialisedTre);
    }

    private void fillTRE(Tre piaprd) {
        piaprd.add(new TreEntryImpl("ACCESSID", "979358f8-8173-11e7-89b3-1b1e7b6aa52a", "string"));
        piaprd.add(new TreEntryImpl("FMCONTROL", "Nope", "string"));
        piaprd.add(new TreEntryImpl("SUBDET", "E", "string"));
        piaprd.add(new TreEntryImpl("PRODCODE", "Z1", "string"));
        piaprd.add(new TreEntryImpl("PRODUCERSE", "DetA", "string"));
        piaprd.add(new TreEntryImpl("PRODIDNO", "Gold 34", "string"));
        piaprd.add(new TreEntryImpl("PRODSNME", "Codice", "string"));
        piaprd.add(new TreEntryImpl("PRODUCERCD", "CD", "string"));
        piaprd.add(new TreEntryImpl("PRODCRTIME", "201708150435", "string"));
        piaprd.add(new TreEntryImpl("MAPID", "OpenStreetMap", "string"));
        piaprd.add(new TreEntryImpl("SECTITLEREP", "0", "integer"));
        piaprd.add(new TreEntryImpl("SECTITLE"));
        piaprd.add(new TreEntryImpl("REQORGREP", "0", "integer"));
        piaprd.add(new TreEntryImpl("REQORG"));
        piaprd.add(new TreEntryImpl("KEYWORDREP", "0", "integer"));
        piaprd.add(new TreEntryImpl("KEYWORD"));
        piaprd.add(new TreEntryImpl("ASSRPTREP", "0", "integer"));
        piaprd.add(new TreEntryImpl("ASSRPT"));
        piaprd.add(new TreEntryImpl("ATEXTREP", "0", "integer"));
        piaprd.add(new TreEntryImpl("ATEXT"));
    }

}
