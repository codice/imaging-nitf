package org.codice.imaging.nitf.core.tre.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.codice.imaging.nitf.core.HeapStrategy;
import org.codice.imaging.nitf.core.NitfWriter;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.junit.Assert.*;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import org.codice.imaging.nitf.core.header.impl.NitfParser;
import org.codice.imaging.nitf.core.impl.ConfigurableHeapStrategy;
import org.codice.imaging.nitf.core.impl.HeapStrategyConfiguration;
import org.codice.imaging.nitf.core.impl.NitfFileWriter;
import org.codice.imaging.nitf.core.impl.SlottedParseStrategy;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.junit.Test;

public class CMETAA_Test {

    @Test
    public void testCmetaaGeod() throws NitfFormatException {
        Tre cmetaa = readTre("/CMETAA/CMETAA_GEOD.txt");
        assertNotNull(cmetaa);
        assertEquals(187,  cmetaa.getEntries().size());
        evaluateCommonFields(cmetaa);
        assertEquals("GEOD", cmetaa.getFieldValue("CG_MAP_TYPE"));
        assertEquals("+16.3000000", cmetaa.getFieldValue("CG_PATCH_LATCEN"));
        assertEquals("-164.0000000", cmetaa.getFieldValue("CG_PATCH_LNGCEN"));
        assertEquals("+16.5000000", cmetaa.getFieldValue("CG_PATCH_LTCORUL"));
        assertEquals("-166.0000000", cmetaa.getFieldValue("CG_PATCH_LGCORUL"));
        assertEquals("+16.7000000", cmetaa.getFieldValue("CG_PATCH_LTCORUR"));
        assertEquals("-168.0000000", cmetaa.getFieldValue("CG_PATCH_LGCORUR"));
        assertEquals("+16.9000000", cmetaa.getFieldValue("CG_PATCH_LTCORLR"));
        assertEquals("-170.0000000", cmetaa.getFieldValue("CG_PATCH_LGCORLR"));
        assertEquals("+17.1000000", cmetaa.getFieldValue("CG_PATCH_LTCORLL"));
        assertEquals("-172.0000000", cmetaa.getFieldValue("CG_PATCH_LGCORLL"));
        assertEquals("1.7300000", cmetaa.getFieldValue("CG_PATCH_LAT_CONFIDENCE"));
        assertEquals("1.7400000", cmetaa.getFieldValue("CG_PATCH_LONG_CONFIDENCE"));
    }

    @Test
    public void testCmetaaMgrs() throws NitfFormatException {
        Tre cmetaa = readTre("/CMETAA/CMETAA_MGRS.txt");

        assertNotNull(cmetaa);
        assertEquals(182,  cmetaa.getEntries().size());
        evaluateCommonFields(cmetaa);
        assertEquals("MGRS", cmetaa.getFieldValue("CG_MAP_TYPE"));
        assertEquals("zzBJKeeeeeeeee17500nnnn", cmetaa.getFieldValue("CG_MGRS_CENT"));
        assertEquals("zzBJKeeeeeeeee17600nnnn", cmetaa.getFieldValue("CG_MGRSCORUL"));
        assertEquals("zzBJKeeeeeeeee17700nnnn", cmetaa.getFieldValue("CG_MGRSCORUR"));
        assertEquals("zzBJKeeeeeeeee17800nnnn", cmetaa.getFieldValue("CG_MGRSCORLR"));
        assertEquals("zzBJKeeeeeeeee17900nnnn", cmetaa.getFieldValue("CG_MGRSCORLL"));
        assertEquals("1800.00", cmetaa.getFieldValue("CG_MGRS_CONFIDENCE"));
        assertEquals("           ", cmetaa.getFieldValue("CG_MGRS_PAD"));
    }

    @Test
    public void testCmetaaNa() throws NitfFormatException {
        Tre cmetaa = readTre("/CMETAA/CMETAA_NA.txt");
        assertNotNull(cmetaa);
        assertEquals(176, cmetaa.getEntries().size());
        evaluateCommonFields(cmetaa);
        assertEquals("NA  ", cmetaa.getFieldValue("CG_MAP_TYPE"));
        assertEquals("                                                                                                                                     ", cmetaa.getFieldValue("CG_MAP_TYPE_BLANK"));
    }

    @Test
    public void testCmetaaNitf() throws FileNotFoundException, NitfFormatException {
        createNitfWithCmetaa("/CMETAA/CMETAA_GEOD.txt", "/JitcNitf21Samples/i_3128b.ntf");
    }

    private void createNitfWithCmetaa(String cmetaaFilename, String nitfFilename)
            throws NitfFormatException, FileNotFoundException {
        String output = FilenameUtils.getName(nitfFilename) + ".modified";
        InputStream stream = getClass().getResourceAsStream(nitfFilename);
        NitfReader reader = new NitfInputStreamReader(stream);
        SlottedParseStrategy parseStrategy = new SlottedParseStrategy(SlottedParseStrategy.ALL_SEGMENT_DATA);
        HeapStrategyConfiguration heapStrategyConfiguration = new HeapStrategyConfiguration(length -> length > 100000);
        HeapStrategy<ImageInputStream> imageDataStrategy = new ConfigurableHeapStrategy<>(heapStrategyConfiguration,
                file -> new FileImageInputStream(file), is -> new MemoryCacheImageInputStream(is));
        parseStrategy.setImageHeapStrategy(imageDataStrategy);

        NitfParser.parse(reader, parseStrategy);
        Tre cmetaa = readTre(cmetaaFilename);
        parseStrategy.getDataSource().getImageSegments().get(0).getTREsRawStructure().add(cmetaa);

        NitfWriter writer = new NitfFileWriter(parseStrategy.getDataSource(), output);
        writer.write();
    }

    private Tre readTre(String filename) throws NitfFormatException {
        InputStream testDataStream = getClass().getResourceAsStream(filename);
        BufferedInputStream bufferedStream = new BufferedInputStream(testDataStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 1583, TreSource.ImageExtendedSubheaderData);
        return parseResult.getTREsWithName("CMETAA").get(0);
    }

    private void evaluateCommonFields(Tre cmetaa) throws NitfFormatException {
        assertEquals("01", cmetaa.getFieldValue("RELATED_TRES"));
        assertEquals("ASTORA                                                                                                                  ", cmetaa.getFieldValue("ADDITIONAL_TRES"));
        assertEquals("Imaging-0.9 ", cmetaa.getFieldValue("RD_PRC_NO"));
        assertEquals("PF  ", cmetaa.getFieldValue("IF_PROCESS"));
        assertEquals("L   ", cmetaa.getFieldValue("RD_CEN_FREQ"));
        assertEquals("22FR ", cmetaa.getFieldValue("RD_MODE"));
        assertEquals("0023", cmetaa.getFieldValue("RD_PATCH_NO"));
        assertEquals("I1Q2 ", cmetaa.getFieldValue("CMPLX_DOMAIN"));
        assertEquals("LLM ", cmetaa.getFieldValue("CMPLX_MAG_SCALE_TYPE"));
        assertEquals("1.00000", cmetaa.getFieldValue("CMPLX_LIN_SCALE"));
        assertEquals("12345.6", cmetaa.getFieldValue("CMPLX_AVG_POWER"));
        assertEquals("01234", cmetaa.getFieldValue("CMPLX_LINLOG_TP"));
        assertEquals("UQ2", cmetaa.getFieldValue("CMPLX_PHASE_SCALING_TYPE"));
        assertEquals("19", cmetaa.getFieldValue("CMPLX_PHASE_SCALE"));
        assertEquals("32", cmetaa.getFieldValue("CMPLX_SIZE_1"));
        assertEquals("C5", cmetaa.getFieldValue("CMPLX_IC_1"));
        assertEquals("32", cmetaa.getFieldValue("CMPLX_SIZE_2"));
        assertEquals("C5", cmetaa.getFieldValue("CMPLX_IC_2"));
        assertEquals("5.000", cmetaa.getFieldValue("CMPLX_IC_BPP"));
        assertEquals("TAY", cmetaa.getFieldValue("CMPLX_WEIGHT"));
        assertEquals("45", cmetaa.getFieldValue("CMPLX_AZ_SLL"));
        assertEquals("67", cmetaa.getFieldValue("CMPLX_RNG_SLL"));
        assertEquals("12", cmetaa.getFieldValue("CMPLX_AZ_TAY_NBAR"));
        assertEquals("34", cmetaa.getFieldValue("CMPLX_RNG_TAY_NBAR"));
        assertEquals("AVG", cmetaa.getFieldValue("CMPLX_WEIGHT_NORM"));
        assertEquals("G", cmetaa.getFieldValue("CMPLX_SIGNAL_PLANE"));
        assertEquals("654321", cmetaa.getFieldValue("IF_DC_SF_ROW"));
        assertEquals("543210", cmetaa.getFieldValue("IF_DC_SF_COL"));
        assertEquals("123456", cmetaa.getFieldValue("IF_PATCH_1_ROW"));
        assertEquals("654321", cmetaa.getFieldValue("IF_PATCH_1_COL"));
        assertEquals("098765", cmetaa.getFieldValue("IF_PATCH_2_ROW"));
        assertEquals("654342", cmetaa.getFieldValue("IF_PATCH_2_COL"));
        assertEquals("-99999", cmetaa.getFieldValue("IF_PATCH_3_ROW"));
        assertEquals("-99999", cmetaa.getFieldValue("IF_PATCH_3_COL"));
        assertEquals("-99999", cmetaa.getFieldValue("IF_PATCH_4_ROW"));
        assertEquals("-99999", cmetaa.getFieldValue("IF_PATCH_4_COL"));
        assertEquals("01234567", cmetaa.getFieldValue("IF_DC_IS_ROW"));
        assertEquals("23456789", cmetaa.getFieldValue("IF_DC_IS_COL"));
        assertEquals("78901234", cmetaa.getFieldValue("IF_IMG_ROW_DC"));
        assertEquals("56789012", cmetaa.getFieldValue("IF_IMG_COL_DC"));
        assertEquals("123456", cmetaa.getFieldValue("IF_TILE_1_ROW"));
        assertEquals("654321", cmetaa.getFieldValue("IF_TILE_1_COL"));
        assertEquals("234567", cmetaa.getFieldValue("IF_TILE_2_ROW"));
        assertEquals("765432", cmetaa.getFieldValue("IF_TILE_2_COL"));
        assertEquals("345678", cmetaa.getFieldValue("IF_TILE_3_ROW"));
        assertEquals("876543", cmetaa.getFieldValue("IF_TILE_3_COL"));
        assertEquals("456789", cmetaa.getFieldValue("IF_TILE_4_ROW"));
        assertEquals("987654", cmetaa.getFieldValue("IF_TILE_4_COL"));
        assertEquals("Y", cmetaa.getFieldValue("IF_RD"));
        assertEquals("N", cmetaa.getFieldValue("IF_RGWLK"));
        assertEquals("O", cmetaa.getFieldValue("IF_KEYSTN"));
        assertEquals("N", cmetaa.getFieldValue("IF_LINSFT"));
        assertEquals("O", cmetaa.getFieldValue("IF_SUBPATCH"));
        assertEquals("N", cmetaa.getFieldValue("IF_GEODIST"));
        assertEquals("Y", cmetaa.getFieldValue("IF_RGFO"));
        assertEquals("Y", cmetaa.getFieldValue("IF_BEAM_COMP"));
        assertEquals("1234.567", cmetaa.getFieldValue("IF_RGRES"));
        assertEquals("8901.234", cmetaa.getFieldValue("IF_AZRES"));
        assertEquals("01.23456", cmetaa.getFieldValue("IF_RSS"));
        assertEquals("78.90123", cmetaa.getFieldValue("IF_AZSS"));
        assertEquals("45.67890", cmetaa.getFieldValue("IF_RSR"));
        assertEquals("12.34567", cmetaa.getFieldValue("IF_AZSR"));
        assertEquals("0006300", cmetaa.getFieldValue("IF_RFFT_SAMP"));
        assertEquals("0006400", cmetaa.getFieldValue("IF_AZFFT_SAMP"));
        assertEquals("0006500", cmetaa.getFieldValue("IF_RFFT_TOT"));
        assertEquals("0006600", cmetaa.getFieldValue("IF_AZFFT_TOT"));
        assertEquals("006700", cmetaa.getFieldValue("IF_SUBP_ROW"));
        assertEquals("006800", cmetaa.getFieldValue("IF_SUBP_COL"));
        assertEquals("0123", cmetaa.getFieldValue("IF_SUB_RG"));
        assertEquals("0456", cmetaa.getFieldValue("IF_SUB_AZ"));
        assertEquals("+", cmetaa.getFieldValue("IF_RFFTS"));
        assertEquals("-", cmetaa.getFieldValue("IF_AFFTS"));
        assertEquals("ROW_INC", cmetaa.getFieldValue("IF_RANGE_DATA"));
        assertEquals("+", cmetaa.getFieldValue("IF_INCPH"));
        assertEquals("S-SVA   ", cmetaa.getFieldValue("IF_SR_NAME1"));
        assertEquals("01.07600", cmetaa.getFieldValue("IF_SR_AMOUNT1"));
        assertEquals("NLS     ", cmetaa.getFieldValue("IF_SR_NAME2"));
        assertEquals("02.07800", cmetaa.getFieldValue("IF_SR_AMOUNT2"));
        assertEquals("HDSAR   ", cmetaa.getFieldValue("IF_SR_NAME3"));
        assertEquals("03.08000", cmetaa.getFieldValue("IF_SR_AMOUNT"));
        assertEquals("PGA  ", cmetaa.getFieldValue("AF_TYPE1"));
        assertEquals("HOAF ", cmetaa.getFieldValue("AF_TYPE2"));
        assertEquals("PHDIF", cmetaa.getFieldValue("AF_TYPE3"));
        assertEquals("H", cmetaa.getFieldValue("POL_TR"));
        assertEquals("V", cmetaa.getFieldValue("POL_RE"));
        assertEquals("XYZ4567890123456789012345678901234567890", cmetaa.getFieldValue("POL_REFERENCE"));
        assertEquals("D", cmetaa.getFieldValue("POL"));
        assertEquals("Y", cmetaa.getFieldValue("POL_REG"));
        assertEquals("89.00", cmetaa.getFieldValue("POL_ISO_1"));
        assertEquals("C", cmetaa.getFieldValue("POL_BAL"));
        assertEquals("0.009100", cmetaa.getFieldValue("POL_BAL_MAG"));
        assertEquals("0.009200", cmetaa.getFieldValue("POL_BAL_PHS"));
        assertEquals("B", cmetaa.getFieldValue("POL_HCOMP"));
        assertEquals("LEGENDRE  ", cmetaa.getFieldValue("POL_HCOMP_BASIS"));
        assertEquals("000009500", cmetaa.getFieldValue("POL_HCOMP_COEF_1"));
        assertEquals("000009600", cmetaa.getFieldValue("POL_HCOMP_COEF_2"));
        assertEquals("000009700", cmetaa.getFieldValue("POL_HCOMP_COEF_3"));
        assertEquals("M", cmetaa.getFieldValue("POL_AFCOMP"));
        assertEquals("               ", cmetaa.getFieldValue("POL_SPARE_A"));
        assertEquals("000000000", cmetaa.getFieldValue("POL_SPARE_N"));
        assertEquals("2018JAN31", cmetaa.getFieldValue("T_UTC_YYYYMMMDD"));
        assertEquals("123049", cmetaa.getFieldValue("T_HHMMSSUTC"));
        assertEquals("153049", cmetaa.getFieldValue("T_HHMMSSLOCAL"));
        assertEquals("00010400.00", cmetaa.getFieldValue("CG_SRAC"));
        assertEquals("0105.00", cmetaa.getFieldValue("CG_SLANT_CONFIDENCE"));
        assertEquals("00010600.00", cmetaa.getFieldValue("CG_CROSS"));
        assertEquals("0107.00", cmetaa.getFieldValue("CG_CROSS_CONFIDENCE"));
        assertEquals("+108.0000", cmetaa.getFieldValue("CG_CAAC"));
        assertEquals("0.1090", cmetaa.getFieldValue("CG_CONE_CONFIDENCE"));
        assertEquals("-11.0000", cmetaa.getFieldValue("CG_GPSAC"));
        assertEquals("0.1110", cmetaa.getFieldValue("CG_SQUINT_CONFIDENCE"));
        assertEquals("+11.2000", cmetaa.getFieldValue("CG_SQUINT"));
        assertEquals("11.3000", cmetaa.getFieldValue("CG_GAAC"));
        assertEquals("0.1140", cmetaa.getFieldValue("CG_GRAZE_CONFIDENCE"));
        assertEquals("11.5000", cmetaa.getFieldValue("CG_INCIDENT"));
        assertEquals("-11.600", cmetaa.getFieldValue("CG_SLOPE"));
        assertEquals("+11.7000", cmetaa.getFieldValue("CG_TILT"));
        assertEquals("R", cmetaa.getFieldValue("CG_LD"));
        assertEquals("119.0000", cmetaa.getFieldValue("CG_NORTH"));
        assertEquals("1.2000", cmetaa.getFieldValue("CG_NORTH_CONFIDENCE"));
        assertEquals("121.0000", cmetaa.getFieldValue("CG_EAST"));
        assertEquals("122.0000", cmetaa.getFieldValue("CG_RLOS"));
        assertEquals("1.2300", cmetaa.getFieldValue("CG_LOS_CONFIDENCE"));
        assertEquals("124.0000", cmetaa.getFieldValue("CG_LAYOVER"));
        assertEquals("125.0000", cmetaa.getFieldValue("CG_SHADOW"));
        assertEquals("126.000", cmetaa.getFieldValue("CG_OPM"));
        assertEquals("WGS84", cmetaa.getFieldValue("CG_MODEL"));
        assertEquals("+12800000.000", cmetaa.getFieldValue("CG_AMPT_X"));
        assertEquals("-12900000.000", cmetaa.getFieldValue("CG_AMPT_Y"));
        assertEquals("+13000000.000", cmetaa.getFieldValue("CG_AMPT_Z"));
        assertEquals("131.00", cmetaa.getFieldValue("CG_AP_CONF_XY"));
        assertEquals("132.00", cmetaa.getFieldValue("CG_AP_CONF_Z"));
        assertEquals("+13300000.000", cmetaa.getFieldValue("CG_APCEN_X"));
        assertEquals("-13400000.000", cmetaa.getFieldValue("CG_APCEN_Y"));
        assertEquals("+13500000.000", cmetaa.getFieldValue("CG_APCEN_Z"));
        assertEquals("136.00", cmetaa.getFieldValue("CG_APER_CONF_XY"));
        assertEquals("137.00", cmetaa.getFieldValue("CG_APER_CONF_Z"));
        assertEquals("0.1380000", cmetaa.getFieldValue("CG_FPNUV_X"));
        assertEquals("-0.139000", cmetaa.getFieldValue("CG_FPNUV_Y"));
        assertEquals("0.1400000", cmetaa.getFieldValue("CG_FPNUV_Z"));
        assertEquals("0.1410000", cmetaa.getFieldValue("CG_IDPNUVX"));
        assertEquals("-0.142000", cmetaa.getFieldValue("CG_IDPNUVY"));
        assertEquals("0.1430000", cmetaa.getFieldValue("CG_IDPNUVZ"));
        assertEquals("+14400000.000", cmetaa.getFieldValue("CG_SCECN_X"));
        assertEquals("-14500000.000", cmetaa.getFieldValue("CG_SCECN_Y"));
        assertEquals("+14600000.000", cmetaa.getFieldValue("CG_SCECN_Z"));
        assertEquals("147.00", cmetaa.getFieldValue("CG_SC_CONF_XY"));
        assertEquals("148.00", cmetaa.getFieldValue("CG_SC_CONF_Z"));
        assertEquals("14900.00", cmetaa.getFieldValue("CG_SWWD"));
        assertEquals("+15000.000", cmetaa.getFieldValue("CG_SNVEL_X"));
        assertEquals("-15100.000", cmetaa.getFieldValue("CG_SNVEL_Y"));
        assertEquals("+15200.000", cmetaa.getFieldValue("CG_SNVEL_Z"));
        assertEquals("+15.300000", cmetaa.getFieldValue("CG_SNACC_X"));
        assertEquals("-15.400000", cmetaa.getFieldValue("CG_SNACC_Y"));
        assertEquals("+15.500000", cmetaa.getFieldValue("CG_SNACC_Z"));
        assertEquals("+156.000", cmetaa.getFieldValue("CG_SNATT_ROLL"));
        assertEquals("-157.000", cmetaa.getFieldValue("CG_SNATT_PITCH"));
        assertEquals("+158.000", cmetaa.getFieldValue("CG_SNATT_YAW"));
        assertEquals("0.1590000", cmetaa.getFieldValue("CG_GTP_X"));
        assertEquals("-0.160000", cmetaa.getFieldValue("CG_GTP_Y"));
        assertEquals("0.1610000", cmetaa.getFieldValue("CG_GTP_Z"));
        assertEquals("                                                                                                                                                ", cmetaa.getFieldValue("CG_SPARE_A"));
        assertEquals("183.000", cmetaa.getFieldValue("CA_CALPA"));
        assertEquals("184000000000.0", cmetaa.getFieldValue("WF_SRTFR"));
        assertEquals("185000000000.0", cmetaa.getFieldValue("WF_ENDFR"));
        assertEquals("+18.600000", cmetaa.getFieldValue("WF_CHRPRT"));
        assertEquals("0.1870000", cmetaa.getFieldValue("WF_WIDTH"));
        assertEquals("18800000000.0", cmetaa.getFieldValue("WF_CENFRQ"));
        assertEquals("18900000000.0", cmetaa.getFieldValue("WF_BW"));
        assertEquals("19000.0", cmetaa.getFieldValue("WF_PRF"));
        assertEquals("0.1910000", cmetaa.getFieldValue("WF_PRI"));
        assertEquals("019.200", cmetaa.getFieldValue("WF_CDP"));
        assertEquals("193000000", cmetaa.getFieldValue("WF_NUMBER_OF_PULSES"));
        assertEquals("N", cmetaa.getFieldValue("VPH_COND"));
    }
}
