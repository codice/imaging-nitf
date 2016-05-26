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
import java.util.List;
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
            + "</tres>";

    private final Source sourceTREs = new StreamSource(new StringReader(xmlTREs));

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void checkMissingType() throws NitfFormatException {
        Tre tst01a = TreFactory.getDefault("TST01A", TreSource.UserDefinedHeaderData);
        tst01a.add(new TreEntry("INFO", "Some information"));
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
        tst01a.add(new TreEntry("INFO", "Some information"));
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
        tst01a.add(new TreEntry("INFO", "Some more information"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Incorrect length serialising out: INFO");
        parser.serializeTRE(tst01a);
    }

    @Test
    public void checkTST01A_NullString() throws NitfFormatException {
        Tre tst01a = TreFactory.getDefault("TST01A", TreSource.UserDefinedHeaderData);
        tst01a.add(new TreEntry("INFO", null));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Cannot serialize null entry for: INFO");
        parser.serializeTRE(tst01a);
    }

    @Test
    public void checkTST02A_Integer() throws NitfFormatException {
        Tre tst02a = TreFactory.getDefault("TST02A", TreSource.UserDefinedHeaderData);
        tst02a.add(new TreEntry("NUM_INT", "2"));
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
        tst02a.add(new TreEntry("NUM_INT", "1"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Minimum value for NUM_INT is 2, got 1");
        parser.serializeTRE(tst02a);
    }

    @Test
    public void checkTST02A_MaxValue() throws NitfFormatException {
        Tre tst02a = TreFactory.getDefault("TST02A", TreSource.UserDefinedHeaderData);
        tst02a.add(new TreEntry("NUM_INT", "105"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Maximum value for NUM_INT is 104, got 105");
        parser.serializeTRE(tst02a);
    }

    @Test
    public void checkTST02A_BadFormat() throws NitfFormatException {
        Tre tst02a = TreFactory.getDefault("TST02A", TreSource.UserDefinedHeaderData);
        tst02a.add(new TreEntry("NUM_INT", "z1"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Could not parse NUM_INT value z1 as a number.");
        parser.serializeTRE(tst02a);
    }

    @Test
    public void checkTST03A_Valid() throws NitfFormatException {
        Tre tst03a = TreFactory.getDefault("TST03A", TreSource.UserDefinedHeaderData);
        tst03a.add(new TreEntry("INFO_LEN", String.valueOf("Some more information.".length())));
        tst03a.add(new TreEntry("INFO", "Some more information."));
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
        tst03a.add(new TreEntry("INFO_LEN", "21"));
        tst03a.add(new TreEntry("INFO", "Some more information."));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Actual length for INFO did not match specified length of 21");
        parser.serializeTRE(tst03a);
    }

    @Test
    public void checkTST04A_Valid() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntry("SCALE", "2.3"));
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
        tst04a.add(new TreEntry("SCALE", "2.3012343"));
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
        tst04a.add(new TreEntry("SCALE", "z1"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Could not parse SCALE value z1 as a floating point number.");
        parser.serializeTRE(tst04a);
    }

    @Test
    public void checkTST04A_MinValue() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntry("SCALE", "0.24999"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Minimum value for SCALE is 0.250000, got 0.249990");
        parser.serializeTRE(tst04a);
    }

    @Test
    public void checkTST04A_MaxValue() throws NitfFormatException {
        Tre tst04a = TreFactory.getDefault("TST04A", TreSource.UserDefinedHeaderData);
        tst04a.add(new TreEntry("SCALE", "4.0001"));
        TreParser parser = new TreParser();
        parser.registerAdditionalTREdescriptor(sourceTREs);
        exception.expect(NitfFormatException.class);
        exception.expectMessage("Maximum value for SCALE is 4.000000, got 4.000100");
        parser.serializeTRE(tst04a);
    }

    @Test
    public void checkTST05A_ValidNoFlag() throws NitfFormatException {
        Tre tst05a = TreFactory.getDefault("TST05A", TreSource.UserDefinedHeaderData);
        tst05a.add(new TreEntry("FLAG", "N"));
        tst05a.add(new TreEntry("TEXT", "HELLO WORLD"));
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
        tst05a.add(new TreEntry("FLAG", "Y"));
        tst05a.add(new TreEntry("NUMB", "3"));
        tst05a.add(new TreEntry("TEXT", "HELLO WORLD"));
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
            coefficient.add(new TreEntry("CONCEPT", String.format("Key %d", i + 1)));
            coefficient.add(new TreEntry("VALUE", String.valueOf(i * 7 + 1)));
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

    // TODO: check data types per MIL-STD-2500C 5.1.7a
}
