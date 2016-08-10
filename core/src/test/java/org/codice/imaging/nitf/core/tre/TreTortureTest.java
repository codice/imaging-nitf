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
import java.io.StringReader;
import java.math.BigInteger;
import java.util.List;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
            + "     <tre name=\"TST04A\">"
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
            + "     <tre name=\"TST10A\">"
            + "         <field name=\"UINT4\" type=\"UINT\" length=\"4\"/>"
            + "         <field name=\"UINT5\" type=\"UINT\" length=\"5\"/>"
            + "         <field name=\"UINT6\" type=\"UINT\" length=\"6\"/>"
            + "         <field name=\"UINT7\" type=\"UINT\" length=\"7\"/>"
            + "         <field name=\"UINT8\" type=\"UINT\" length=\"8\"/>"
            + "     </tre>"
            + "</tres>";

    private final Source sourceTREs = new StreamSource(new StringReader(xmlTREs));

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void checkMissingType() throws NitfFormatException {
        Tre tst01a = TreFactory.getDefault("TST01A", TreSource.UserDefinedHeaderData);
        tst01a.add(new TreEntry("INFO", "Some information", "string"));
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
        tst01a.add(new TreEntry("INFO", "Some information", "string"));
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
        tst01a.add(new TreEntry("INFO", "Some more information", "string"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length serialising out: INFO");
        parser.serializeTRE(tst01a);
    }

    @Test
    public void checkTST01A_NullString() throws NitfFormatException {
        Tre tst01a = TreFactory.getDefault("TST01A", TreSource.UserDefinedHeaderData);
        tst01a.add(new TreEntry("INFO", null, "string"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Cannot serialize null entry for: INFO");
        parser.serializeTRE(tst01a);
    }

    @Test
    public void checkTST02A_Integer() throws NitfFormatException {
        Tre tst02a = TreFactory.getDefault("TST02A", TreSource.UserDefinedHeaderData);
        tst02a.add(new TreEntry("NUM_INT", "2", "integer"));
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
        tst02a.add(new TreEntry("NUM_INT", "1", "integer"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Minimum value for NUM_INT is 2, got 1");
        parser.serializeTRE(tst02a);
    }

    @Test
    public void checkTST02A_MaxValue() throws NitfFormatException {
        Tre tst02a = TreFactory.getDefault("TST02A", TreSource.UserDefinedHeaderData);
        tst02a.add(new TreEntry("NUM_INT", "105", "integer"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Maximum value for NUM_INT is 104, got 105");
        parser.serializeTRE(tst02a);
    }

    @Test
    public void checkTST02A_BadFormat() throws NitfFormatException {
        Tre tst02a = TreFactory.getDefault("TST02A", TreSource.UserDefinedHeaderData);
        tst02a.add(new TreEntry("NUM_INT", "z1", "integer"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Could not parse NUM_INT value z1 as a number.");
        parser.serializeTRE(tst02a);
    }

    @Test
    public void checkTST03A_Valid() throws NitfFormatException {
        Tre tst03a = TreFactory.getDefault("TST03A", TreSource.UserDefinedHeaderData);
        tst03a.add(new TreEntry("INFO_LEN", String.valueOf("Some more information.".length()), "integer"));
        tst03a.add(new TreEntry("INFO", "Some more information.", "string"));
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
        tst03a.add(new TreEntry("INFO_LEN", "21", "integer"));
        tst03a.add(new TreEntry("INFO", "Some more information.", "string"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Actual length for INFO did not match specified length of 21");
        parser.serializeTRE(tst03a);
    }

    @Test
    public void checkTST04A_Valid() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntry("SCALE", "2.3", "real"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        byte[] serialisedTre = parser.serializeTRE(tst04a);
        assertNotNull(serialisedTre);
        assertEquals("TST04A", tst04a.getName());
        assertArrayEquals("2.300000".getBytes(), serialisedTre);
    }

    @Test
    public void checkTST04A_TooMuchPrecision() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntry("SCALE", "2.3012343", "real"));
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
        tst04a.add(new TreEntry("SCALE", "z1", "real"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Could not parse SCALE value z1 as a floating point number.");
        parser.serializeTRE(tst04a);
    }

    @Test
    public void checkTST04A_MinValue() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntry("SCALE", "0.24999", "real"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Minimum value for SCALE is 0.250000, got 0.249990");
        parser.serializeTRE(tst04a);
    }

    @Test
    public void checkTST04A_MaxValue() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntry("SCALE", "4.0001", "real"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Maximum value for SCALE is 4.000000, got 4.000100");
        parser.serializeTRE(tst04a);
    }

    @Test
    public void checkTST05A_ValidNoFlag() throws NitfFormatException {
        Tre tst05a = TreFactory.getDefault("TST05A", TreSource.UserDefinedHeaderData);
        tst05a.add(new TreEntry("FLAG", "N", "string"));
        tst05a.add(new TreEntry("TEXT", "HELLO WORLD", "string"));
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
        tst05a.add(new TreEntry("FLAG", "Y", "string"));
        tst05a.add(new TreEntry("NUMB", "3", "integer"));
        tst05a.add(new TreEntry("TEXT", "HELLO WORLD", "string"));
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
        TreEntry coefficients = new TreEntry("COEFFICIENTS");
        tst06a.add(coefficients);
        for (int i = 0; i < 5; i++) {
            TreGroup coefficient = new TreGroupImpl();
            coefficient.add(new TreEntry("CONCEPT", String.format("Key %d", i + 1), "string"));
            coefficient.add(new TreEntry("VALUE", String.valueOf(i * 7 + 1), "integer"));
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
    public void parseTST10A() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary("5453543130413030303333" + "fff02367" + "ff01020304" + "ff0102030405" + "ff010203040506" + "7f01020304050607")));
        Tre tre = parseTST10A(bufferedStream);
        assertEquals(0xfff02367L, tre.getLongValue("UINT4"));
        assertEquals(0xff01020304L, tre.getLongValue("UINT5"));
        assertEquals(0xff0102030405L, tre.getLongValue("UINT6"));
        assertEquals(0xff010203040506L, tre.getLongValue("UINT7"));
        assertEquals(new BigInteger("9151598129769154055"), tre.getBigIntegerValue("UINT8"));
    }

    @Test
    public void parseTST10A_0x00() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary("5453543130413030303333" + "00000000" + "0000000000" + "000000000000" + "00000000000000" + "0000000000000000")));
        Tre tre = parseTST10A(bufferedStream);
        assertEquals(0L, tre.getIntValue("UINT4"));
        assertEquals(0L, tre.getIntValue("UINT5"));
        assertEquals(0L, tre.getIntValue("UINT6"));
        assertEquals(0L, tre.getIntValue("UINT7"));
        assertEquals(0L, tre.getIntValue("UINT8"));
    }

    @Test
    public void parseTST10A_0xff() throws NitfFormatException {
        BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(parseHexBinary("5453543130413030303333" + "ffffffff" + "ffffffffff" + "ffffffffffff" + "ffffffffffffff" + "ffffffffffffffff")));
        Tre tre = parseTST10A(bufferedStream);
        assertEquals(4294967295L, tre.getLongValue("UINT4"));
        assertEquals(1099511627775L, tre.getLongValue("UINT5"));
        assertEquals(281474976710655L, tre.getLongValue("UINT6"));
        assertEquals(72057594037927935L, tre.getLongValue("UINT7"));
        assertEquals(new BigInteger("18446744073709551615"), tre.getBigIntegerValue("UINT8"));
    }

    private Tre parseTST10A(BufferedInputStream bufferedStream) throws NitfFormatException {
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        TreCollection parseResult = parser.parse(nitfReader, 11 + 33, TreSource.UserDefinedHeaderData);
        assertNotNull(parseResult);
        assertNotNull(parseResult.getTREs());
        assertEquals(1, parseResult.getTREs().size());
        Tre tre = parseResult.getTREs().get(0);
        assertNotNull(tre);
        assertNotNull(tre.getEntries());
        assertEquals(5, tre.getEntries().size());
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


    // TODO: check data types per MIL-STD-2500C 5.1.7a

}
