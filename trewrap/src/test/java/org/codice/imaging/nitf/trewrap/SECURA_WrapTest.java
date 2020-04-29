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
package org.codice.imaging.nitf.trewrap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.security.SecurityClassification;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import org.codice.imaging.nitf.core.tre.Tre;
import org.junit.Assert;
import org.junit.Test;

import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Tests for the SECURA wrapper.
 */
public class SECURA_WrapTest extends SharedTreTestSupport {

    TestLogger LOGGER = TestLoggerFactory.getTestLogger(TreWrapper.class);
    private final String securityUncompressedLength = "02036";
    private final String securityUncompressed = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <arh:Security   xmlns:arh=\"urn:us:gov:ic:arh\"   xmlns:ism=\"urn:us:gov:ic:ism\"   xmlns:ntk=\"urn:us:gov:is:ntk\"   ism:compliesWith=\"USGov USIC\"   ism:DESVersion=\"201609.201707\"   ism:ISMCATCESVersion=\"201707\"   ism:resourceElement=\"true\"   arh:DESVersion=\"3\"   ntk:DESVersion=\"201508\"   ism:createDate=\"2006-05-04\"   ism:classification=\"U\"   ism:ownerProducer=\"USA\">   <ntk:Access ism:classification=\"U\" ism:ownerProducer=\"USA\">     <ntk:RequiresAnyOf>       <ntk:AccessProfileList xmlns=\"urn:us:gov:ic:ntk\">         <ntk:AccessProfile ism:classification=\"U\" ism:ownerProducer=\"USA\">           <ntk:AccessPolicy>urn:us:gov:ic:aces:ntk:permissive</ntk:AccessPolicy>           <ntk:ProfileDes>urn:us:gov:ic:ntk:profile:grp-ind</ntk:ProfileDes>           <ntk:VocabularyType ntk:name=\"individual:unclasssourceforge\"             ntk:source=\"UnclassSourceForge\"/>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">johndoe</ntk:AccessProfileValue>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">ssun</ntk:AccessProfileValue>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">cjhodges</ntk:AccessProfileValue>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">cgilsenan</ntk:AccessProfileValue>         </ntk:AccessProfile>       </ntk:AccessProfileList>     </ntk:RequiresAnyOf>   </ntk:Access>   <ism:NoticeList ism:classification=\"U\" ism:ownerProducer=\"USA\">     <ism:Notice ism:classification=\"U\" ism:ownerProducer=\"USA\" ism:unregisteredNoticeType=\"Holiday\">       <ism:NoticeText ism:classification=\"U\" ism:ownerProducer=\"USA\">Memorial day is on May 28th 2012</ism:NoticeText>     </ism:Notice>     <ism:Notice ism:classification=\"U\" ism:ownerProducer=\"USA\" ism:unregisteredNoticeType=\"Holiday\">       <ism:NoticeText ism:classification=\"U\" ism:ownerProducer=\"USA\">The next Holiday will be July 4th 2012</ism:NoticeText>     </ism:Notice>   </ism:NoticeList> </arh:Security>";
    private final String securityCompressedLength = "00576";
    private final byte[] secuirtyGzipped = compress(securityUncompressed);
    private final String nitf20 = "NITF02.00";
    private final String nitf21 = "NITF02.10";
    private final String invalidVersion = "NITF02.22";
    private final String nitfSecurityFields = "                                                                                                                                                                                                               ";
    private final String securityStandard = "ARH.XML ";
    private final String invalidsecurityStandard = "XXX.XML ";
    private final String seccompUncompressed = "        ";
    private final String seccompCompressed = "GZIP    ";
    private final String seccompInvalid = "ZIP     ";

    private final String mTestData20 = "SECURA0228723091801ZAPR20" + nitf20 + nitfSecurityFields +
            securityStandard + seccompUncompressed + securityUncompressedLength + securityUncompressed;

    private final String mTestData21 = "SECURA0228720200423091801" + nitf21 + nitfSecurityFields +
            securityStandard + seccompUncompressed + securityUncompressedLength + securityUncompressed;

    private final String mTestDataGzip = "SECURA0082720200423091801" + nitf21 + nitfSecurityFields +
            securityStandard + seccompCompressed + securityCompressedLength;

    private final String mTestDataBadCompression = "SECURA0228720200423091801NITF02.10" + nitf21 + nitfSecurityFields +
            securityStandard + seccompCompressed + securityUncompressedLength + securityUncompressed;

    private final String mTestInvalidVersion = "SECURA0228720200423091801" + invalidVersion + nitfSecurityFields +
            securityStandard + seccompUncompressed + securityUncompressedLength + securityUncompressed;

    private final String mTestInvlaidSecurityStandard = "SECURA0228720200423091801" + nitf21 + nitfSecurityFields +
            invalidsecurityStandard + seccompUncompressed + securityUncompressedLength + securityUncompressed;

    private final String mTestInvalidCompression = "SECURA0228720200423091801" + nitf21 + nitfSecurityFields +
            securityStandard + seccompInvalid + securityUncompressedLength + securityUncompressed;


    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SECURA_WrapTest() {
    }

    @Test
    public void testNitf21Gzipped() throws NitfFormatException {
        Tre tre = parseTRE(buildTREBytes(), "SECURA");
        SECURA secura = new SECURA(tre);
        assertTrue(secura.getValidity().isValid());
        assertEquals("2020-04-23 09:18:01", formatter.format(secura.getNitfDateTimeField().getZonedDateTime()));
        assertEquals(FileType.NITF_TWO_ONE, secura.getNitfVersion());
        checkNitf21SecurityMetadataUnclasAndEmpty(secura.getNitfSecurityFields());
        assertEquals("ARH.XML", secura.getSecurityStandard());
        assertEquals("GZIP", secura.getSecurityFieldCompression());
        assertEquals(576, secura.getSecurityLength());
        String uncompressed = uncompress(secuirtyGzipped);
        assertEquals(uncompressed, securityUncompressed);
        assertArrayEquals(secuirtyGzipped, secura.getSecurity());
        assertEquals(securityUncompressed, secura.getSecurityUncompressed());
    }

    @Test
    public void testGettersNitf20() throws NitfFormatException {
        Tre tre = parseTRE(mTestData20, "SECURA");
        SECURA secura = new SECURA(tre);
        assertTrue(secura.getValidity().isValid());
        assertEquals("2020-04-23 09:18:01", formatter.format(secura.getNitfDateTimeField().getZonedDateTime()));
        assertEquals(FileType.NITF_TWO_ZERO, secura.getNitfVersion());
        checkNitf20SecurityMetadataUnclasAndEmpty(secura.getNitfSecurityFields());
        assertEquals("ARH.XML", secura.getSecurityStandard());
        assertEquals("", secura.getSecurityFieldCompression());
        assertEquals(2036, secura.getSecurityLength());
        assertEquals(securityUncompressed, new String(secura.getSecurity(), StandardCharsets.ISO_8859_1));
        assertEquals(securityUncompressed, secura.getSecurityUncompressed());
    }

    @Test
    public void testGettersNitf21() throws NitfFormatException {
        Tre tre = parseTRE(mTestData21, "SECURA");
        SECURA secura = new SECURA(tre);
        assertTrue(secura.getValidity().isValid());
        assertEquals("2020-04-23 09:18:01", formatter.format(secura.getNitfDateTimeField().getZonedDateTime()));
        assertEquals(FileType.NITF_TWO_ONE, secura.getNitfVersion());
        checkNitf21SecurityMetadataUnclasAndEmpty(secura.getNitfSecurityFields());
        assertEquals("ARH.XML", secura.getSecurityStandard());
        assertEquals("", secura.getSecurityFieldCompression());
        assertEquals(2036, secura.getSecurityLength());
        assertEquals(securityUncompressed, new String(secura.getSecurity(), StandardCharsets.ISO_8859_1));
        assertEquals(securityUncompressed, secura.getSecurityUncompressed());
    }

    @Test(expected = NitfFormatException.class)
    public void testBadCompression() throws NitfFormatException {
        Tre tre = parseTRE(mTestDataBadCompression, "SECURA");
        SECURA secura = new SECURA(tre);
        String test = secura.getSecurityUncompressed();
    }

    @Test
    public void testInvalidNitfVersion() throws NitfFormatException {
        Tre tre = parseTRE(mTestInvalidVersion, "SECURA");
        SECURA secura = new SECURA(tre);
        String error = secura.getValidity().getValidityResultDescription();
        assertEquals(error, SECURA.INVALID_VERSION);
    }

    @Test
    public void testInvalidSecurityStandard() throws NitfFormatException {
        Tre tre = parseTRE(mTestInvlaidSecurityStandard, "SECURA");
        SECURA secura = new SECURA(tre);
        String error = secura.getValidity().getValidityResultDescription();
        assertEquals(error, SECURA.INVALID_SECURITY_STANDARD);
    }

    @Test
    public void mTestInvalidCompression() throws NitfFormatException {
        Tre tre = parseTRE(mTestInvalidCompression, "SECURA");
        SECURA secura = new SECURA(tre);
        String error = secura.getValidity().getValidityResultDescription();
        assertEquals(error, SECURA.INVALID_COMPRESSION);
    }


    public static void checkNitf20SecurityMetadataUnclasAndEmpty(SecurityMetadata securityMetadata) {
        assertNotNull(securityMetadata);
        assertEquals(SecurityClassification.UNKNOWN, securityMetadata.getSecurityClassification());
        assertNull(securityMetadata.getSecurityClassificationSystem());
        assertEquals("", securityMetadata.getCodewords());
        assertEquals("", securityMetadata.getControlAndHandling());
        assertEquals("", securityMetadata.getReleaseInstructions());
        assertNull(securityMetadata.getDeclassificationType());
        assertNull(securityMetadata.getDeclassificationDate());
        assertNull(securityMetadata.getDeclassificationExemption());
        assertNull(securityMetadata.getDowngrade());
        assertNull(securityMetadata.getDowngradeDate());
        assertNull(securityMetadata.getClassificationText());
        assertNull(securityMetadata.getClassificationAuthorityType());
        assertEquals("", securityMetadata.getClassificationAuthority());
        assertNull(securityMetadata.getClassificationReason());
        assertEquals("", securityMetadata.getSecurityControlNumber());
    }

    public static void checkNitf21SecurityMetadataUnclasAndEmpty(SecurityMetadata securityMetadata) {
        Assert.assertEquals(SecurityClassification.UNKNOWN, securityMetadata.getSecurityClassification());
        assertEquals("", securityMetadata.getSecurityClassificationSystem());
        assertEquals("", securityMetadata.getCodewords());
        assertEquals("", securityMetadata.getControlAndHandling());
        assertEquals("", securityMetadata.getReleaseInstructions());
        assertEquals("", securityMetadata.getDeclassificationType());
        assertEquals("", securityMetadata.getDeclassificationDate());
        assertEquals("", securityMetadata.getDeclassificationExemption());
        assertEquals("", securityMetadata.getDowngrade());
        assertEquals("", securityMetadata.getDowngradeDate());
        assertEquals("", securityMetadata.getClassificationText());
        assertEquals("", securityMetadata.getClassificationAuthorityType());
        assertEquals("", securityMetadata.getClassificationAuthority());
        assertEquals("", securityMetadata.getClassificationReason());
        assertEquals("", securityMetadata.getSecurityControlNumber());
    }

    private static byte[] compress(String data) {
        byte[] compressed = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(bos);) {
            gzip.write(data.getBytes(StandardCharsets.ISO_8859_1));
            // in order to get the full set of bytes you must close the output stream.
            gzip.close();
            compressed = bos.toByteArray();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return compressed;
    }

    private final String uncompress(byte[] compressed) throws NitfFormatException {
            StringBuilder sb = new StringBuilder();
            try (ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
                    GZIPInputStream gis = new GZIPInputStream(bis);
                    BufferedReader br = new BufferedReader(new InputStreamReader(gis));) {
                String line;
                while((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                throw new NitfFormatException("Could not uncompress SECURITY field.");
            }
            return sb.toString();
    }

    private final byte[] buildTREBytes() {
        String result;
        String defaltEncoding = System.getProperty("file.encoding");
        Charset defaultCharset = Charset.forName(defaltEncoding);
        Charset iso88591charset = Charset.forName("ISO-8859-1");


        ByteBuffer inputBuffer = ByteBuffer.wrap(mTestDataGzip.getBytes());

        // decode UTF-8
        CharBuffer data = defaultCharset.decode(inputBuffer);

        // encode ISO-8559-1
        ByteBuffer outputBuffer = iso88591charset.encode(data);
        byte[] outputData = outputBuffer.array();
        byte[] gzipped = new byte[outputData.length + secuirtyGzipped.length];

        System.arraycopy(outputData, 0, gzipped, 0, outputData.length);
        System.arraycopy(secuirtyGzipped, 0, gzipped, outputData.length, secuirtyGzipped.length);

        return gzipped;
    }
}
