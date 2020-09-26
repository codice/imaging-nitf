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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.junit.Test;

/**
 * Tests for parsing MTIMSA.
 *
 * This is defined in STDI-0002 Appendix AK.
 */
public class MATESA_Test extends SharedTreTest {

    public MATESA_Test() {
    }

    @Test
    public void SimpleParse() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("MATESA00897EO-1_HYPERION                             FTITLE          006307APR2005_Hyperion_331406N0442000E_SWIR172_001_L1R-01B-BIB-GLAS0005RADIOMTRC_CALIB         0001EO-1_HYPERION                             FILENAME        0020HypGain_revC.dat.svfPARENT                  0001EO-1_HYPERION                             FILENAME        0032EO12005097_020D020C_r1_WPS_01.L0PRE_DARKCOLLECT         0001EO-1_HYPERION                             FILENAME        0032EO12005097_020A0209_r1_WPS_01.L0POST_DARKCOLLECT        0001EO-1_HYPERION                             FILENAME        0032EO12005097_020F020E_r1_WPS_01.L0PARENT                  0003EO-1_HYPERION                             FILENAME        0026EO1H1680372005097110PZ.L1REO-1_HYPERION                             FILENAME        0026EO1H1680372005097110PZ.AUXEO-1_HYPERION                             FILENAME        0026EO1H1680372005097110PZ.MET".getBytes(StandardCharsets.ISO_8859_1));
        Tre mtimsa = doParseAndCheckResults(baos);
    }

    private Tre doParseAndCheckResults(ByteArrayOutputStream baos) throws NitfFormatException, IOException {
        Tre matesa = parseTRE(baos.toByteArray(), baos.toByteArray().length, "MATESA");
        assertEquals(matesa.getFieldValue("CUR_SOURCE"), "EO-1_HYPERION                             ");
        assertEquals(matesa.getFieldValue("CUR_MATE_TYPE"), "FTITLE          ");
        assertEquals(matesa.getIntValue("CUR_FILE_ID_LEN"), 63);
        assertEquals(matesa.getFieldValue("CUR_FILE_ID"), "07APR2005_Hyperion_331406N0442000E_SWIR172_001_L1R-01B-BIB-GLAS");
        assertEquals(matesa.getIntValue("NUM_GROUPS"), 5);
        assertEquals(matesa.getEntry("GROUPS").getGroups().size(), 5);
        TreGroup group1 = matesa.getEntry("GROUPS").getGroups().get(0);
        assertEquals(group1.getFieldValue("RELATIONSHIP"), "RADIOMTRC_CALIB         ");
        assertEquals(group1.getIntValue("NUM_MATES"), 1);
        assertEquals(group1.getEntry("MATES").getGroups().size(), 1);
        TreGroup mate_1_1 = group1.getEntry("MATES").getGroups().get(0);
        assertEquals(mate_1_1.getFieldValue("SOURCE"), "EO-1_HYPERION                             ");
        assertEquals(mate_1_1.getFieldValue("MATE_TYPE"), "FILENAME        ");
        assertEquals(mate_1_1.getIntValue("MATE_ID_LEN"), 20);
        assertEquals(mate_1_1.getFieldValue("MATE_ID"), "HypGain_revC.dat.svf");
        TreGroup group2 = matesa.getEntry("GROUPS").getGroups().get(1);
        assertEquals(group2.getFieldValue("RELATIONSHIP"), "PARENT                  ");
        assertEquals(group2.getIntValue("NUM_MATES"), 1);
        assertEquals(group2.getEntry("MATES").getGroups().size(), 1);
        TreGroup mate_2_1 = group2.getEntry("MATES").getGroups().get(0);
        assertEquals(mate_2_1.getFieldValue("SOURCE"), "EO-1_HYPERION                             ");
        assertEquals(mate_2_1.getFieldValue("MATE_TYPE"), "FILENAME        ");
        assertEquals(mate_2_1.getIntValue("MATE_ID_LEN"), 32);
        assertEquals(mate_2_1.getFieldValue("MATE_ID"), "EO12005097_020D020C_r1_WPS_01.L0");
        TreGroup group3 = matesa.getEntry("GROUPS").getGroups().get(2);
        assertEquals(group3.getFieldValue("RELATIONSHIP"), "PRE_DARKCOLLECT         ");
        assertEquals(group3.getIntValue("NUM_MATES"), 1);
        assertEquals(group3.getEntry("MATES").getGroups().size(), 1);
        TreGroup mate_3_1 = group3.getEntry("MATES").getGroups().get(0);
        assertEquals(mate_3_1.getFieldValue("SOURCE"), "EO-1_HYPERION                             ");
        assertEquals(mate_3_1.getFieldValue("MATE_TYPE"), "FILENAME        ");
        assertEquals(mate_3_1.getIntValue("MATE_ID_LEN"), 32);
        assertEquals(mate_3_1.getFieldValue("MATE_ID"), "EO12005097_020A0209_r1_WPS_01.L0");
        TreGroup group4 = matesa.getEntry("GROUPS").getGroups().get(3);
        assertEquals(group4.getFieldValue("RELATIONSHIP"), "POST_DARKCOLLECT        ");
        assertEquals(group4.getIntValue("NUM_MATES"), 1);
        assertEquals(group4.getEntry("MATES").getGroups().size(), 1);
        TreGroup mate_4_1 = group4.getEntry("MATES").getGroups().get(0);
        assertEquals(mate_4_1.getFieldValue("SOURCE"), "EO-1_HYPERION                             ");
        assertEquals(mate_4_1.getFieldValue("MATE_TYPE"), "FILENAME        ");
        assertEquals(mate_4_1.getIntValue("MATE_ID_LEN"), 32);
        assertEquals(mate_4_1.getFieldValue("MATE_ID"), "EO12005097_020F020E_r1_WPS_01.L0");
        TreGroup group5 = matesa.getEntry("GROUPS").getGroups().get(4);
        assertEquals(group5.getFieldValue("RELATIONSHIP"), "PARENT                  ");
        assertEquals(group5.getIntValue("NUM_MATES"), 3);
        assertEquals(group5.getEntry("MATES").getGroups().size(), 3);
        TreGroup mate_5_1 = group5.getEntry("MATES").getGroups().get(0);
        assertEquals(mate_5_1.getFieldValue("SOURCE"), "EO-1_HYPERION                             ");
        assertEquals(mate_5_1.getFieldValue("MATE_TYPE"), "FILENAME        ");
        assertEquals(mate_5_1.getIntValue("MATE_ID_LEN"), 26);
        assertEquals(mate_5_1.getFieldValue("MATE_ID"), "EO1H1680372005097110PZ.L1R");
        TreGroup mate_5_2 = group5.getEntry("MATES").getGroups().get(1);
        assertEquals(mate_5_2.getFieldValue("SOURCE"), "EO-1_HYPERION                             ");
        assertEquals(mate_5_2.getFieldValue("MATE_TYPE"), "FILENAME        ");
        assertEquals(mate_5_2.getIntValue("MATE_ID_LEN"), 26);
        assertEquals(mate_5_2.getFieldValue("MATE_ID"), "EO1H1680372005097110PZ.AUX");
        TreGroup mate_5_3 = group5.getEntry("MATES").getGroups().get(2);
        assertEquals(mate_5_3.getFieldValue("SOURCE"), "EO-1_HYPERION                             ");
        assertEquals(mate_5_3.getFieldValue("MATE_TYPE"), "FILENAME        ");
        assertEquals(mate_5_3.getIntValue("MATE_ID_LEN"), 26);
        assertEquals(mate_5_3.getFieldValue("MATE_ID"), "EO1H1680372005097110PZ.MET");
        return matesa;
    }
}
