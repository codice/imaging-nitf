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
package org.codice.imaging.nitf.fluent;

import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.stream.StreamSource;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for registering additional TRE parser.
 */
public class AdditionalTREsTest {
    
    public AdditionalTREsTest() {
    }

    @Test
    public void ParseWithoutAdditionalTRE() throws NitfFormatException {
        final String testfile = "/JitcNitf21Samples/ns3228d.nsf";
        List<Tre> tres = new ArrayList<>();
        new NitfParserInputFlow()
                .inputStream(getClass().getResourceAsStream(testfile))
                .allData()
                .fileHeader(header -> tres.addAll(header.getTREsRawStructure().getTREs()));
        assertThat(tres.size(), is(1));
        Tre jitcid = tres.get(0);
        assertThat(jitcid, not(nullValue()));
        assertThat(jitcid.getRawData().length, not(0L));
    }

    @Test
    public void ParseWithAdditionalTREinString() throws NitfFormatException {
        final String testfile = "/JitcNitf21Samples/ns3228d.nsf";
        List<Tre> tres = new ArrayList<>();
        new NitfParserInputFlow()
                .inputStream(getClass().getResourceAsStream(testfile))
                .treDescriptor("<?xml version=\"1.0\"?><tres><tre name=\"JITCID\" location=\"image\"><field name=\"Info\" length=\"200\"/></tre></tres>")
                .allData()
                .fileHeader(header -> tres.addAll(header.getTREsRawStructure().getTREs()));
        assertThat(tres.size(), is(1));
        Tre jitcid = tres.get(0);
        assertThat(jitcid, not(nullValue()));
        assertThat(jitcid.getRawData(), is(nullValue()));
        assertThat(jitcid.getEntries().size(), is(1));
        assertThat(jitcid.getFieldValue("Info").trim(), is("I_3228D, Checks multi spectral image of 6 bands, the image subheader tells the receiving system to display band 2 as red, band 4 as green, and band 6 as blue."));
    }
    
    @Test
    public void ParseWithAdditionalTREinXmlSource() throws NitfFormatException {
        final String testfile = "/JitcNitf21Samples/ns3228d.nsf";
        List<Tre> tres = new ArrayList<>();
        new NitfParserInputFlow()
                .inputStream(getClass().getResourceAsStream(testfile))
                .treDescriptor(new StreamSource(new StringReader("<?xml version=\"1.0\"?><tres><tre name=\"JITCID\" location=\"image\"><field name=\"Info\" length=\"200\"/></tre></tres>")))
                .allData()
                .fileHeader(header -> tres.addAll(header.getTREsRawStructure().getTREs()));
        assertThat(tres.size(), is(1));
        Tre jitcid = tres.get(0);
        assertThat(jitcid, not(nullValue()));
        assertThat(jitcid.getRawData(), is(nullValue()));
        assertThat(jitcid.getEntries().size(), is(1));
        assertThat(jitcid.getFieldValue("Info").trim(), is("I_3228D, Checks multi spectral image of 6 bands, the image subheader tells the receiving system to display band 2 as red, band 4 as green, and band 6 as blue."));
    }
    
    @Test
    public void ParseWithAdditionalTREinFile() throws NitfFormatException, URISyntaxException {
        final String testfile = "/JitcNitf21Samples/ns3228d.nsf";
        List<Tre> tres = new ArrayList<>();
        new NitfParserInputFlow()
                .inputStream(getClass().getResourceAsStream(testfile))
                .treDescriptor(getClass().getResource("/jitcid.xml").toURI())
                .allData()
                .fileHeader(header -> tres.addAll(header.getTREsRawStructure().getTREs()));
        assertThat(tres.size(), is(1));
        Tre jitcid = tres.get(0);
        assertThat(jitcid, not(nullValue()));
        assertThat(jitcid.getRawData(), is(nullValue()));
        assertThat(jitcid.getEntries().size(), is(1));
        assertThat(jitcid.getFieldValue("Info").trim(), is("I_3228D, Checks multi spectral image of 6 bands, the image subheader tells the receiving system to display band 2 as red, band 4 as green, and band 6 as blue."));
    }
}
