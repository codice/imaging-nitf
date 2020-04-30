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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.junit.Test;

/**
 * Tests for SECURA TRE parsing.
 *
 * This TRE is described in STDI-0002-1 Appendix AI: SECURA 1.0
 *
 */
public class SECURA_Test {

    public SECURA_Test() {
    }

    @Test
    public void SimpleSECURA() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("SECURA0228720200423091801NITF02.10                                                                                                                                                                                                               ARH XML         02036<?xml version=\"1.0\" encoding=\"UTF-8\"?> <arh:Security   xmlns:arh=\"urn:us:gov:ic:arh\"   xmlns:ism=\"urn:us:gov:ic:ism\"   xmlns:ntk=\"urn:us:gov:is:ntk\"   ism:compliesWith=\"USGov USIC\"   ism:DESVersion=\"201609.201707\"   ism:ISMCATCESVersion=\"201707\"   ism:resourceElement=\"true\"   arh:DESVersion=\"3\"   ntk:DESVersion=\"201508\"   ism:createDate=\"2006-05-04\"   ism:classification=\"U\"   ism:ownerProducer=\"USA\">   <ntk:Access ism:classification=\"U\" ism:ownerProducer=\"USA\">     <ntk:RequiresAnyOf>       <ntk:AccessProfileList xmlns=\"urn:us:gov:ic:ntk\">         <ntk:AccessProfile ism:classification=\"U\" ism:ownerProducer=\"USA\">           <ntk:AccessPolicy>urn:us:gov:ic:aces:ntk:permissive</ntk:AccessPolicy>           <ntk:ProfileDes>urn:us:gov:ic:ntk:profile:grp-ind</ntk:ProfileDes>           <ntk:VocabularyType ntk:name=\"individual:unclasssourceforge\"             ntk:source=\"UnclassSourceForge\"/>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">johndoe</ntk:AccessProfileValue>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">ssun</ntk:AccessProfileValue>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">cjhodges</ntk:AccessProfileValue>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">cgilsenan</ntk:AccessProfileValue>         </ntk:AccessProfile>       </ntk:AccessProfileList>     </ntk:RequiresAnyOf>   </ntk:Access>   <ism:NoticeList ism:classification=\"U\" ism:ownerProducer=\"USA\">     <ism:Notice ism:classification=\"U\" ism:ownerProducer=\"USA\" ism:unregisteredNoticeType=\"Holiday\">       <ism:NoticeText ism:classification=\"U\" ism:ownerProducer=\"USA\">Memorial day is on May 28th 2012</ism:NoticeText>     </ism:Notice>     <ism:Notice ism:classification=\"U\" ism:ownerProducer=\"USA\" ism:unregisteredNoticeType=\"Holiday\">       <ism:NoticeText ism:classification=\"U\" ism:ownerProducer=\"USA\">The next Holiday will be July 4th 2012</ism:NoticeText>     </ism:Notice>   </ism:NoticeList> </arh:Security>".getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 39, TreSource.UserDefinedHeaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre secura = parseResult.getTREsWithName("SECURA").get(0);
        assertNotNull(secura);
        assertNull(secura.getRawData());
        assertEquals(7, secura.getEntries().size());
        assertEquals("20200423091801", secura.getFieldValue("FDATTIM"));
        assertEquals("NITF02.10", secura.getFieldValue("NITFVER"));
        assertEquals("                                                                                                                                                                                                               ", secura.getFieldValue("NFSECFLDS"));
        assertEquals("ARH XML ", secura.getFieldValue("SECSTD"));
        assertEquals("        ", secura.getFieldValue("SECCOMP"));
        assertEquals("02036", secura.getFieldValue("SECLEN"));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <arh:Security   xmlns:arh=\"urn:us:gov:ic:arh\"   xmlns:ism=\"urn:us:gov:ic:ism\"   xmlns:ntk=\"urn:us:gov:is:ntk\"   ism:compliesWith=\"USGov USIC\"   ism:DESVersion=\"201609.201707\"   ism:ISMCATCESVersion=\"201707\"   ism:resourceElement=\"true\"   arh:DESVersion=\"3\"   ntk:DESVersion=\"201508\"   ism:createDate=\"2006-05-04\"   ism:classification=\"U\"   ism:ownerProducer=\"USA\">   <ntk:Access ism:classification=\"U\" ism:ownerProducer=\"USA\">     <ntk:RequiresAnyOf>       <ntk:AccessProfileList xmlns=\"urn:us:gov:ic:ntk\">         <ntk:AccessProfile ism:classification=\"U\" ism:ownerProducer=\"USA\">           <ntk:AccessPolicy>urn:us:gov:ic:aces:ntk:permissive</ntk:AccessPolicy>           <ntk:ProfileDes>urn:us:gov:ic:ntk:profile:grp-ind</ntk:ProfileDes>           <ntk:VocabularyType ntk:name=\"individual:unclasssourceforge\"             ntk:source=\"UnclassSourceForge\"/>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">johndoe</ntk:AccessProfileValue>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">ssun</ntk:AccessProfileValue>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">cjhodges</ntk:AccessProfileValue>           <ntk:AccessProfileValue ntk:vocabulary=\"individual:unclasssourceforge\">cgilsenan</ntk:AccessProfileValue>         </ntk:AccessProfile>       </ntk:AccessProfileList>     </ntk:RequiresAnyOf>   </ntk:Access>   <ism:NoticeList ism:classification=\"U\" ism:ownerProducer=\"USA\">     <ism:Notice ism:classification=\"U\" ism:ownerProducer=\"USA\" ism:unregisteredNoticeType=\"Holiday\">       <ism:NoticeText ism:classification=\"U\" ism:ownerProducer=\"USA\">Memorial day is on May 28th 2012</ism:NoticeText>     </ism:Notice>     <ism:Notice ism:classification=\"U\" ism:ownerProducer=\"USA\" ism:unregisteredNoticeType=\"Holiday\">       <ism:NoticeText ism:classification=\"U\" ism:ownerProducer=\"USA\">The next Holiday will be July 4th 2012</ism:NoticeText>     </ism:Notice>   </ism:NoticeList> </arh:Security>", secura.getFieldValue("SECURITY"));
    }
}
