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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests that stress TRE serialisation / parsing
 */
public class TreTortureTest {

    private final Source badSourceXml = new StreamSource(new StringReader("<?xml version=\"1.0\"?>junk"));

    private final String xmlTREwithoutType
            = "<?xml version=\"1.0\"?>"
            + " <tres>"
            + "     <tre name=\"TST01A\">"
            + "         <field name=\"INFO\" length=\"20\"/>"
            + "     </tre>"
            + "</tres>";

    private final Source sourceTREwithoutType = new StreamSource(new StringReader(xmlTREwithoutType));

    private final String xmlTREs
            = "<?xml version=\"1.0\"?>"
            + " <tres>"
            + "     <tre name=\"TST01A\">"
            + "         <field name=\"INFO\" type=\"string\" length=\"20\"/>"
            + "     </tre>"
            + "     <tre name=\"TST02A\">"
            + "         <field name=\"NUM_INT\" type=\"integer\" minval=\"2\" maxval=\"104\" length=\"4\"/>"
            + "     </tre>"
            + "     <tre name=\"TST03A\">"
            + "         <field name=\"INFO_LEN\" type=\"integer\" minval=\"2\" maxval=\"80\" length=\"2\"/>"
            + "         <field name=\"INFO\" type=\"string\" length_var=\"INFO_LEN\"/>"
            + "     </tre>"
            + "     <tre name=\"TST04A\" location=\"file\">"
            + "         <field name=\"SCALE\" type=\"real\" minval=\"0.25\" maxval=\"4\" length=\"8\"/>"
            + "     </tre>"
            + "     <tre name=\"TST05A\">"
            + "         <field name=\"FLAG\" type=\"string\" length=\"1\"/>"
            + "         <if cond=\"FLAG=Y\">"
            + "             <field name=\"NUMB\" type=\"integer\" length=\"2\"/>"
            + "         </if>"
            + "         <field name=\"TEXT\" type=\"string\" length=\"20\"/>"
            + "     </tre>"
            + "     <tre name=\"TST06A\">"
            + "         <loop iterations=\"5\" md_prefix=\"COEFF_%d\" name=\"COEFFICIENTS\">"
            + "             <field name=\"CONCEPT\" type=\"string\" length=\"7\"/>"
            + "             <field name=\"VALUE\" type=\"integer\" length=\"4\"/>"
            + "         </loop>"
            + "     </tre>"
            + "     <tre name=\"TST07A\">"
            + "         <field name=\"UINT\" type=\"UINT\" length=\"1\"/>"
            + "     </tre>"
            + "     <tre name=\"TST08A\">"
            + "         <field name=\"UINT\" type=\"UINT\" length=\"2\"/>"
            + "     </tre>"
            + "     <tre name=\"TST09A\">"
            + "         <field name=\"UINT\" type=\"UINT\" length=\"3\"/>"
            + "     </tre>"
            + "     <tre name=\"TST10A\" location=\"file\">"
            + "         <field name=\"UINT4\" type=\"UINT\" length=\"4\"/>"
            + "         <field name=\"UINT5\" type=\"UINT\" length=\"5\"/>"
            + "         <field name=\"UINT6\" type=\"UINT\" length=\"6\"/>"
            + "         <field name=\"UINT7\" type=\"UINT\" length=\"7\"/>"
            + "         <field name=\"UINT8\" type=\"UINT\" length=\"8\"/>"
            + "     </tre>"
            + "     <tre name=\"TST11A\" location=\"image\">"
            + "         <field name=\"LENGTH\" type=\"UINT\" length=\"2\"/>"
            + "         <field name=\"VALUE\" type=\"string\" length_var=\"LENGTH\"/>"
            + "     </tre>"
            + "     <tre name=\"TST12A\" location=\"image\">"
            + "         <field name=\"REAL1\" type=\"real\" length=\"13\" format=\"UE\"/>"
            + "     </tre>"
            + "     <tre name=\"TST13A\" location=\"image\">"
            + "         <field name=\"VAL1\" type=\"IEEE754\" length=\"4\"/>"
            + "     </tre>"
            + "     <tre name=\"TST14A\" location=\"image\">"
            + "         <field name=\"VAL1\" type=\"IEEE754\" length=\"8\"/>"
            + "     </tre>"
            + "</tres>";

    private final Source sourceTREs = new StreamSource(new StringReader(xmlTREs));

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void checkMissingType() throws NitfFormatException {
        Tre tst01a = TreFactory.getDefault("TST01A", TreSource.UserDefinedHeaderData);
        tst01a.add(new TreEntryImpl("INFO", "Some information", "string"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREwithoutType);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Cannot pad unknown data type for INFO");
        parser.serializeTRE(tst01a);
    }

    @Test
    public void checkBadXml() throws NitfFormatException {
        TreParser parser = new TreParser();
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Exception while loading TRE XML");
        parser.registerAdditionalTREdescriptor(badSourceXml);
    }

    @Test
    public void checkBasic() throws NitfFormatException {
        Tre tst01a = TreFactory.getDefault("TST01A", TreSource.UserDefinedHeaderData);
        tst01a.add(new TreEntryImpl("INFO", "Some information", "string"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst01a);
        assertNotNull(serialisedTre);
        assertEquals("TST01A", tst01a.getName());
        assertArrayEquals("Some information    ".getBytes(), serialisedTre);
    }

    @Test
    public void checkTST01A_TooLongString() throws NitfFormatException {
        Tre tst01a = TreFactory.getDefault("TST01A", TreSource.UserDefinedHeaderData);
        tst01a.add(new TreEntryImpl("INFO", "Some more information", "string"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length serialising out: INFO");
        parser.serializeTRE(tst01a);
    }

    @Test
    public void checkTST01A_NullString() throws NitfFormatException {
        Tre tst01a = TreFactory.getDefault("TST01A", TreSource.UserDefinedHeaderData);
        tst01a.add(new TreEntryImpl("INFO", null, "string"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Cannot serialize null entry for: INFO");
        parser.serializeTRE(tst01a);
    }

    @Test
    public void checkTST02A_Integer() throws NitfFormatException {
        Tre tst02a = TreFactory.getDefault("TST02A", TreSource.UserDefinedHeaderData);
        tst02a.add(new TreEntryImpl("NUM_INT", "2", "integer"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst02a);
        assertNotNull(serialisedTre);
        assertEquals("TST02A", tst02a.getName());
        assertArrayEquals("0002".getBytes(), serialisedTre);
    }

    @Test
    public void checkTST02A_MinValue() throws NitfFormatException {
        Tre tst02a = TreFactory.getDefault("TST02A", TreSource.UserDefinedHeaderData);
        tst02a.add(new TreEntryImpl("NUM_INT", "1", "integer"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Minimum value for NUM_INT is 2, got 1");
        parser.serializeTRE(tst02a);
    }

    @Test
    public void checkTST02A_MaxValue() throws NitfFormatException {
        Tre tst02a = TreFactory.getDefault("TST02A", TreSource.UserDefinedHeaderData);
        tst02a.add(new TreEntryImpl("NUM_INT", "105", "integer"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Maximum value for NUM_INT is 104, got 105");
        parser.serializeTRE(tst02a);
    }

    @Test
    public void checkTST02A_BadFormat() throws NitfFormatException {
        Tre tst02a = TreFactory.getDefault("TST02A", TreSource.UserDefinedHeaderData);
        tst02a.add(new TreEntryImpl("NUM_INT", "z1", "integer"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Could not parse NUM_INT value z1 as a number.");
        parser.serializeTRE(tst02a);
    }

    @Test
    public void checkTST03A_Valid() throws NitfFormatException {
        Tre tst03a = TreFactory.getDefault("TST03A", TreSource.UserDefinedHeaderData);
        tst03a.add(new TreEntryImpl("INFO_LEN", String.valueOf("Some more information.".length()), "integer"));
        tst03a.add(new TreEntryImpl("INFO", "Some more information.", "string"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst03a);
        assertNotNull(serialisedTre);
        assertEquals("TST03A", tst03a.getName());
        assertArrayEquals("22Some more information.".getBytes(), serialisedTre);
    }

    @Test
    public void checkTST03A_VarLengthMismatch() throws NitfFormatException {
        Tre tst03a = TreFactory.getDefault("TST03A", TreSource.UserDefinedHeaderData);
        tst03a.add(new TreEntryImpl("INFO_LEN", "21", "integer"));
        tst03a.add(new TreEntryImpl("INFO", "Some more information.", "string"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Actual length for INFO did not match specified length of 21");
        parser.serializeTRE(tst03a);
    }

    @Test
    public void checkTST04A_Valid() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntryImpl("SCALE", "2.3", "real"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst04a);
        assertNotNull(serialisedTre);
        assertEquals("TST04A", tst04a.getName());
        assertArrayEquals("2.300000".getBytes(), serialisedTre);
    }

    @Test
    public void checkTST04A_BadFieldName() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntryImpl("SCALE", "2.3", "real"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Failed to look up NOSUCHFIELD as double value");
        tst04a.getDoubleValue("NOSUCHFIELD");
    }

    @Test
    public void checkTST04A_TooMuchPrecision() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntryImpl("SCALE", "2.3012343", "real"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst04a);
        assertNotNull(serialisedTre);
        assertEquals("TST04A", tst04a.getName());
        assertArrayEquals("2.301234".getBytes(), serialisedTre);
    }

    @Test
    public void checkTST04A_BadFormat() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntryImpl("SCALE", "z1", "real"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Could not parse SCALE value z1 as a floating point number.");
        parser.serializeTRE(tst04a);
    }

    @Test
    public void checkTST04A_MinValue() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntryImpl("SCALE", "0.24999", "real"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Minimum value for SCALE is 0.250000, got 0.249990");
        parser.serializeTRE(tst04a);
    }

    @Test
    public void checkTST04A_MaxValue() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntryImpl("SCALE", "4.0001", "real"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Maximum value for SCALE is 4.000000, got 4.000100");
        parser.serializeTRE(tst04a);
    }

    @Test
    public void checkTST04A_BadLocation() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.ImageExtendedSubheaderData);
        tst04a.add(new TreEntryImpl("SCALE", "3.0", "real"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("TRE is only permitted in a file-level header, or in an overflow DES");
        parser.serializeTRE(tst04a);
    }

    @Test
    public void checkTST05A_ValidNoFlag() throws NitfFormatException {
        Tre tst05a = TreFactory.getDefault("TST05A", TreSource.UserDefinedHeaderData);
        tst05a.add(new TreEntryImpl("FLAG", "N", "string"));
        tst05a.add(new TreEntryImpl("TEXT", "HELLO WORLD", "string"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst05a);
        assertNotNull(serialisedTre);
        assertEquals("TST05A", tst05a.getName());
        assertArrayEquals("NHELLO WORLD         ".getBytes(), serialisedTre);
    }

    @Test
    public void checkTST05A_ValidWithFlag() throws NitfFormatException {
        Tre tst05a = TreFactory.getDefault("TST05A", TreSource.UserDefinedHeaderData);
        tst05a.add(new TreEntryImpl("FLAG", "Y", "string"));
        tst05a.add(new TreEntryImpl("NUMB", "3", "integer"));
        tst05a.add(new TreEntryImpl("TEXT", "HELLO WORLD", "string"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst05a);
        assertNotNull(serialisedTre);
        assertEquals("TST05A", tst05a.getName());
        assertArrayEquals("Y03HELLO WORLD         ".getBytes(), serialisedTre);
    }

    @Test
    public void parseTST05A_ValidNoFlag() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream("TST05A00021NHELLO WORLD         ".getBytes()));
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        TreCollection parseResult = parser.parse(nitfReader, 32, TreSource.UserDefinedHeaderData);
        assertNotNull(parseResult);
        assertNotNull(parseResult.getTREs());
        assertEquals(1, parseResult.getTREs().size());
        Tre tst05a = parseResult.getTREs().get(0);
        assertNotNull(tst05a);
        assertNotNull(tst05a.getEntries());
        assertEquals(2, tst05a.getEntries().size());
        TreEntry flag = tst05a.getEntry("FLAG");
        assertNotNull(flag);
        assertEquals("N", flag.getFieldValue());
        TreEntry text = tst05a.getEntry("TEXT");
        assertNotNull(text);
        assertEquals("HELLO WORLD         ", text.getFieldValue());
    }

    @Test
    public void parseTST05A_ValidWithFlag() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream("TST05A00023Y03HELLO WORLD         ".getBytes()));
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        TreCollection parseResult = parser.parse(nitfReader, 32, TreSource.UserDefinedHeaderData);
        assertNotNull(parseResult);
        assertNotNull(parseResult.getTREs());
        assertEquals(1, parseResult.getTREs().size());
        Tre tst05a = parseResult.getTREs().get(0);
        assertNotNull(tst05a);
        assertNotNull(tst05a.getEntries());
        assertEquals(3, tst05a.getEntries().size());

        TreEntry flag = tst05a.getEntries().get(0);
        assertNotNull(flag);
        assertEquals("Y", flag.getFieldValue());

        TreEntry numb = tst05a.getEntries().get(1);
        assertNotNull(numb);
        assertEquals("03", numb.getFieldValue());
        assertEquals(3, tst05a.getIntValue("NUMB"));

        TreEntry text = tst05a.getEntries().get(2);
        assertNotNull(text);
        assertEquals("HELLO WORLD         ", text.getFieldValue());
    }

    @Test
    public void checkTST06A_Valid() throws NitfFormatException {
        Tre tst06a = TreFactory.getDefault("TST06A", TreSource.UserDefinedHeaderData);
        TreEntryImpl coefficients = new TreEntryImpl("COEFFICIENTS");
        tst06a.add(coefficients);
        for (int i = 0; i < 5; i++) {
            TreGroup coefficient = new TreGroupImpl();
            coefficient.add(new TreEntryImpl("CONCEPT", String.format("Key %d", i + 1), "string"));
            coefficient.add(new TreEntryImpl("VALUE", String.valueOf(i * 7 + 1), "integer"));
            coefficients.addGroup(coefficient);
        }

        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst06a);
        assertNotNull(serialisedTre);
        assertEquals("TST06A", tst06a.getName());
        assertArrayEquals("Key 1  0001Key 2  0008Key 3  0015Key 4  0022Key 5  0029".getBytes(), serialisedTre);
    }

    @Test
    public void parseTST06A_Valid() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream("TST06A00055Key 1  0001Key 2  0008Key 3  0015Key 4  0022Key 5  0029".getBytes()));
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        TreCollection parseResult = parser.parse(nitfReader, 66, TreSource.UserDefinedHeaderData);
        assertNotNull(parseResult);
        assertNotNull(parseResult.getTREs());
        assertEquals(1, parseResult.getTREs().size());
        Tre tst06a = parseResult.getTREs().get(0);
        assertNotNull(tst06a);
        assertNotNull(tst06a.getEntries());
        assertEquals(1, tst06a.getEntries().size());
        List<TreGroup> coefficients = tst06a.getEntries().get(0).getGroups();
        assertNotNull(coefficients);
        assertEquals(5, coefficients.size());
        TreGroup coef1 = coefficients.get(0);
        assertNotNull(coef1);
        assertEquals(2, coef1.getEntries().size());
        assertEquals("Key 1  ", coef1.getFieldValue("CONCEPT"));
        assertEquals("CONCEPT: Key 1  ", coef1.getEntry("CONCEPT").toString());
        assertEquals("0001", coef1.getFieldValue("VALUE"));

        TreGroup coef5 = coefficients.get(4);
        assertNotNull(coef5);
        assertEquals(2, coef5.getEntries().size());
        assertEquals("Key 5  ", coef5.getFieldValue("CONCEPT"));
        assertEquals("0029", coef5.getFieldValue("VALUE"));
    }

    // "TST07A00001"
    static final String TST07A_ONE = "5453543037413030303031";
    // "TST08A00002"
    static final String TST08A_TWO = "5453543038413030303032";
    // "TST09A00003"
    static final String TST09A_THREE = "5453543039413030303033";

    @Test
    public void parseTST07A_0x00() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST07A_ONE + "00")));
        Tre tst07a = parseTST07AsingleByte(bufferedStream);

        TreEntry uint = tst07a.getEntries().get(0);
        assertNotNull(uint);
        assertEquals(0x00, uint.getFieldValue().getBytes()[0]);
        assertEquals(0, tst07a.getIntValue("UINT"));
    }

    @Test
    public void parseTST07A_0x01() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST07A_ONE + "01")));
        Tre tst07a = parseTST07AsingleByte(bufferedStream);

        TreEntry uint = tst07a.getEntries().get(0);
        assertNotNull(uint);
        assertEquals(1, tst07a.getIntValue("UINT"));
    }

    @Test
    public void parseTST07A_0x7f() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST07A_ONE + "7f")));
        Tre tst07a = parseTST07AsingleByte(bufferedStream);

        TreEntry uint = tst07a.getEntries().get(0);
        assertNotNull(uint);
        assertEquals(127, tst07a.getIntValue("UINT"));
    }

    @Test
    public void parseTST07A_0xff() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST07A_ONE + "ff")));
        Tre tst07a = parseTST07AsingleByte(bufferedStream);

        TreEntry uint = tst07a.getEntries().get(0);
        assertNotNull(uint);
        assertEquals(255, tst07a.getIntValue("UINT"));
    }

    @Test
    public void parseTST08A_0xffff() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST08A_TWO + "ffff")));
        Tre tst08a = parseTST_UINT(bufferedStream, 2);

        TreEntry uint = tst08a.getEntries().get(0);
        assertNotNull(uint);
        assertEquals(65535, tst08a.getIntValue("UINT"));
    }

    @Test
    public void parseTST09A_0x01ffff() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST09A_THREE + "01ffff")));
        Tre tst09a = parseTST_UINT(bufferedStream, 3);

        TreEntry uint = tst09a.getEntries().get(0);
        assertNotNull(uint);
        assertEquals(65536 + 65535, tst09a.getIntValue("UINT"));
    }

    @Test
    public void parseTST09A_0xffffff() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST09A_THREE + "ffffff")));
        Tre tst09a = parseTST_UINT(bufferedStream, 3);

        TreEntry uint = tst09a.getEntries().get(0);
        assertNotNull(uint);
        assertEquals(16777215, tst09a.getIntValue("UINT"));
    }

    @Test
    public void parseTST09A_0xf02367() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST09A_THREE + "f02367")));
        Tre tst09a = parseTST_UINT(bufferedStream, 3);

        TreEntry uint = tst09a.getEntries().get(0);
        assertNotNull(uint);
        assertEquals(15737703, tst09a.getIntValue("UINT"));
    }

    @Test
    public void parseTST09A_BigInteger() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST09A_THREE + "f02367")));
        Tre tst09a = parseTST_UINT(bufferedStream, 3);

        TreEntry uint = tst09a.getEntries().get(0);
        assertNotNull(uint);
        assertEquals(BigInteger.valueOf(15737703), tst09a.getBigIntegerValue("UINT"));
    }

    @Test
    public void parseTST09A_BadBigInteger() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST09A_THREE + "f02367")));
        Tre tst09a = parseTST_UINT(bufferedStream, 3);

        TreEntry uint = tst09a.getEntries().get(0);
        assertNotNull(uint);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Failed to look up NOSUCHFIELD as a numerical value");
        tst09a.getBigIntegerValue("NOSUCHFIELD");
    }

    @Test
    public void parseTST09A_BadLong() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST09A_THREE + "f02367")));
        Tre tst09a = parseTST_UINT(bufferedStream, 3);

        TreEntry uint = tst09a.getEntries().get(0);
        assertNotNull(uint);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Failed to look up NOSUCHFIELD as a numerical value");
        tst09a.getLongValue("NOSUCHFIELD");
    }

    @Test
    public void parseTST09A_BadField() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary(TST09A_THREE + "f02367")));
        Tre tst09a = parseTST_UINT(bufferedStream, 3);

        TreEntry uint = tst09a.getEntries().get(0);
        assertNotNull(uint);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Failed to look up NOSUCHFIELD");
        tst09a.getFieldValue("NOSUCHFIELD");
    }

    @Test
    public void parseTST10A() throws NitfFormatException, IOException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary("5453543130413030303330" + "fff02367" + "ff01020304" + "ff0102030405" + "ff010203040506" + "7f01020304050607")));
        Tre tre = parseTST10A(bufferedStream);
        assertEquals(0xfff02367L, tre.getLongValue("UINT4"));
        assertEquals(0xff01020304L, tre.getLongValue("UINT5"));
        assertEquals(0xff0102030405L, tre.getLongValue("UINT6"));
        assertEquals(0xff010203040506L, tre.getLongValue("UINT7"));
        assertEquals(new BigInteger("9151598129769154055"), tre.getBigIntegerValue("UINT8"));
    }

    @Test
    public void parseTST10A_0x00() throws NitfFormatException, IOException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary("5453543130413030303330" + "00000000" + "0000000000" + "000000000000" + "00000000000000" + "0000000000000000")));
        Tre tre = parseTST10A(bufferedStream);
        assertEquals(0L, tre.getIntValue("UINT4"));
        assertEquals(0L, tre.getIntValue("UINT5"));
        assertEquals(0L, tre.getIntValue("UINT6"));
        assertEquals(0L, tre.getIntValue("UINT7"));
        assertEquals(0L, tre.getIntValue("UINT8"));
    }

    @Test
    public void parseTST10A_0xff() throws NitfFormatException, IOException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary("5453543130413030303330" + "ffffffff" + "ffffffffff" + "ffffffffffff" + "ffffffffffffff" + "ffffffffffffffff")));
        Tre tre = parseTST10A(bufferedStream);
        assertEquals(4294967295L, tre.getLongValue("UINT4"));
        assertEquals(1099511627775L, tre.getLongValue("UINT5"));
        assertEquals(281474976710655L, tre.getLongValue("UINT6"));
        assertEquals(72057594037927935L, tre.getLongValue("UINT7"));
        assertEquals(new BigInteger("18446744073709551615"), tre.getBigIntegerValue("UINT8"));
    }

    private Tre parseTST10A(BufferedInputStream bufferedStream) throws NitfFormatException, IOException {
        assertTrue(bufferedStream.markSupported());
        bufferedStream.mark(1024);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        TreCollection parseResult = parser.parse(nitfReader, 11 + 30, TreSource.UserDefinedHeaderData);
        assertNotNull(parseResult);
        assertNotNull(parseResult.getTREs());
        assertEquals(1, parseResult.getTREs().size());
        Tre tre = parseResult.getTREs().get(0);
        assertNotNull(tre);
        assertNotNull(tre.getEntries());
        assertEquals(5, tre.getEntries().size());

        TreParser treParser = new TreParser();
        treParser.registerAdditionalTREdescriptor(new StreamSource(new StringReader(xmlTREs)));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("TST10A".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(String.format("%05d", 30).getBytes(StandardCharsets.ISO_8859_1));
        baos.write(treParser.serializeTRE(tre));
        byte[] actualSerialisedResults = baos.toByteArray();
        bufferedStream.reset();
        int available = bufferedStream.available();
        byte[] referenceBytes = new byte[available];
        bufferedStream.read(referenceBytes);
        assertArrayEquals(referenceBytes, actualSerialisedResults);
        return tre;
    }

    private Tre parseTST07AsingleByte(BufferedInputStream bufferedStream) throws NitfFormatException {
        return parseTST_UINT(bufferedStream, 1);
    }

    private Tre parseTST_UINT(BufferedInputStream bufferedStream, int length) throws NitfFormatException {
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        TreCollection parseResult = parser.parse(nitfReader, 11 + length, TreSource.UserDefinedHeaderData);
        assertNotNull(parseResult);
        assertNotNull(parseResult.getTREs());
        assertEquals(1, parseResult.getTREs().size());
        Tre tre = parseResult.getTREs().get(0);
        assertNotNull(tre);
        assertNotNull(tre.getEntries());
        assertEquals(1, tre.getEntries().size());
        return tre;
    }

    @Test
    public void checkTST09A_BuildPad() throws NitfFormatException {
        Tre tst09a = TreFactory.getDefault("TST09A", TreSource.UserDefinedHeaderData);
        tst09a.add(new TreEntryImpl("UINT", "\u0002", "UINT"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst09a);
        assertNotNull(serialisedTre);
        assertEquals("TST09A", tst09a.getName());
        assertArrayEquals("\u0000\u0000\u0002".getBytes(StandardCharsets.ISO_8859_1), serialisedTre);
    }

    @Test
    public void parseTST11A() throws NitfFormatException, IOException {
        ByteArrayOutputStream bufferedStream = new ByteArrayOutputStream();
        bufferedStream.write("TST11A00008".getBytes(StandardCharsets.ISO_8859_1));
        bufferedStream.write(parseHexBinary("0006"));
        bufferedStream.write("ABCDEF".getBytes(StandardCharsets.ISO_8859_1));
        NitfReader nitfReader = new NitfInputStreamReader(new ByteArrayInputStream(bufferedStream.toByteArray()));
        TreCollectionParser parser = new TreCollectionParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        TreCollection parseResult = parser.parse(nitfReader, 11 + 8, TreSource.ImageExtendedSubheaderData);
        assertNotNull(parseResult);
        assertNotNull(parseResult.getTREs());
        assertEquals(1, parseResult.getTREs().size());
        Tre tre = parseResult.getTREs().get(0);
        assertNotNull(tre);
        assertNotNull(tre.getEntries());
        assertEquals(2, tre.getEntries().size());

        int len = tre.getIntValue("LENGTH");
        assertEquals(6, len);
        assertEquals("ABCDEF", tre.getFieldValue("VALUE"));
        assertEquals("VALUE: ABCDEF", tre.getEntry("VALUE").toString());

        TreParser treParser = new TreParser();
        treParser.registerAdditionalTREdescriptor(new StreamSource(new StringReader(xmlTREs)));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("TST11A".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(String.format("%05d", 8).getBytes(StandardCharsets.ISO_8859_1));
        baos.write(treParser.serializeTRE(tre));
        byte[] actualSerialisedResults = baos.toByteArray();
        assertArrayEquals(bufferedStream.toByteArray(), actualSerialisedResults);
    }

    @Test
    public void checkTST12A() throws NitfFormatException {
        Tre tst12a = TreFactory.getDefault("TST12A", TreSource.ImageExtendedSubheaderData);
        TreEntry real1 = new TreEntryImpl("REAL1", "0.532", "real");
        tst12a.add(real1);
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst12a);
        assertNotNull(serialisedTre);
        assertEquals("TST12A", tst12a.getName());
        assertArrayEquals("5.3200000E-01".getBytes(StandardCharsets.ISO_8859_1), serialisedTre);
    }

    @Test
    public void checkTST12A_BadLocation() throws NitfFormatException {
        Tre tst12a = TreFactory.getDefault("TST12A", TreSource.UserDefinedHeaderData);
        TreEntry real1 = new TreEntryImpl("REAL1", "0.532", "real");
        tst12a.add(real1);
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("TRE is only permitted in an image-related sub-header, or in an overflow DES");
        parser.serializeTRE(tst12a);
    }

    @Test
    public void checkTST12A_NaN() throws NitfFormatException {
        Tre tst12a = TreFactory.getDefault("TST12A", TreSource.UserDefinedImageData);
        TreEntry real1 = new TreEntryImpl("REAL1", "NaN", "real");
        tst12a.add(real1);
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst12a);
        assertNotNull(serialisedTre);
        assertEquals("TST12A", tst12a.getName());
        assertArrayEquals("NaN          ".getBytes(StandardCharsets.ISO_8859_1), serialisedTre);
    }

    @Test
    public void checkTST13A() throws NitfFormatException {
        Tre tst13a = TreFactory.getDefault("TST13A", TreSource.ImageExtendedSubheaderData);
        TreEntry real1 = new TreEntryImpl("VAL1", new String(parseHexBinary("40000000"), StandardCharsets.ISO_8859_1), "IEEE754");
        tst13a.add(real1);
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst13a);
        assertNotNull(serialisedTre);
        assertEquals("TST13A", tst13a.getName());
        assertArrayEquals(parseHexBinary("40000000"), serialisedTre);
    }

    @Test
    public void checkTST14A() throws NitfFormatException {
        Tre tst14a = TreFactory.getDefault("TST14A", TreSource.ImageExtendedSubheaderData);
        TreEntry real1 = new TreEntryImpl("VAL1", new String(parseHexBinary("40055C28F5C28F5C"), StandardCharsets.ISO_8859_1), "IEEE754");
        tst14a.add(real1);
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst14a);
        assertNotNull(serialisedTre);
        assertEquals("TST14A", tst14a.getName());
        assertArrayEquals(parseHexBinary("40055C28F5C28F5C"), serialisedTre);
    }

    @Test
    public void parseTST13A_Valid() throws NitfFormatException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("TST13A00004".getBytes(StandardCharsets.ISO_8859_1));
        baos.write(parseHexBinary("40000000"));
        NitfReader nitfReader = new NitfInputStreamReader(new ByteArrayInputStream(baos.toByteArray()));
        TreCollectionParser parser = new TreCollectionParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        TreCollection parseResult = parser.parse(nitfReader, 15, TreSource.UserDefinedHeaderData);
        assertNotNull(parseResult);
        assertNotNull(parseResult.getTREs());
        assertEquals(1, parseResult.getTREs().size());
        Tre tst13a = parseResult.getTREs().get(0);
        assertNotNull(tst13a);
        assertNotNull(tst13a.getEntries());
        assertEquals(1, tst13a.getEntries().size());
        assertEquals(2.0, tst13a.getDoubleValue("VAL1"), 0.000001);
    }

    // TODO: check data types per MIL-STD-2500C 5.1.7a

}
