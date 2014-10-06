/*
 * Copyright (c) 2014, Codice
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.codice.imaging.cgm;

import java.io.IOException;
import java.text.ParseException;
import java.util.EnumSet;
import org.codice.imaging.nitf.core.Nitf;
import org.codice.imaging.nitf.core.NitfFileFactory;
import org.codice.imaging.nitf.core.ParseOption;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class CgmParserTest {
    
    public CgmParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void I_3051E() throws IOException {
        String parentDirectory = "JitcNitf21Samples";
        String testfile = "i_3051e.ntf";
        String inputFileName = "/" + parentDirectory + "/" + testfile;
        System.out.println("================================== Testing :" + inputFileName);
        assertNotNull("Test file missing: " + inputFileName, getClass().getResource(inputFileName));
        try {
            System.out.println("loading from InputStream");
            Nitf nitf = NitfFileFactory.parseSelectedDataSegments(getClass().getResourceAsStream(inputFileName), EnumSet.allOf(ParseOption.class));

            if (nitf.getNumberOfGraphicSegments() == 0) {
                System.out.println("Loaded file, but found no graphic segments.");
                System.exit(0);
            }
            CgmParser parser = new CgmParser(nitf.getGraphicSegmentZeroBase(0));
            parser.dump();
        } catch (ParseException e) {
            System.out.println("Failed to load from InputStream " + e.getMessage());
            e.printStackTrace();
        }
    }
}
