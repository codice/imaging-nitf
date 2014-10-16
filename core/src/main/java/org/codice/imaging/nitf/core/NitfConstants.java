/**
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is
 * distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 *
 */
package org.codice.imaging.nitf.core;

/**
 *
 * @author JohnGameComp
 */
public class NitfConstants {

    // Nitf Segment
    protected static final int ENCRYP_LENGTH = 1;
    protected static final int RGB_COLOUR_LENGTH = 3;

    // Date 
    protected static final int STANDARD_DATE_TIME_LENGTH = 14;
    protected static final String NITF21_DATE_FORMAT = "yyyyMMddHHmmss";

    protected static final String NITF20_DATE_FORMAT = "ddHHmmss'Z'MMMyy";

    // Error Messages
    protected static final String GENERIC_READ_ERROR_MESSAGE = "Error reading from NITF file: ";
    protected static final String FILE_NOT_FOUND_EXCEPTION_MESSAGE = "File Not Found Exception opening file:";
    protected static final String NOT_FOUND_MESSAGE_JOINER = " not found:";
    protected static final String READ_MODE = "r";

    // Coordinate
    protected static final double MINUTES_IN_ONE_DEGREE = 60.0;
    protected static final double SECONDS_IN_ONE_MINUTE = 60.0;
    protected static final int LAT_DEGREES_OFFSET = 0;
    protected static final int LAT_DEGREES_LENGTH = 2;
    protected static final int LAT_MINUTES_OFFSET = LAT_DEGREES_OFFSET + LAT_DEGREES_LENGTH;
    protected static final int MINUTES_LENGTH = 2;
    protected static final int LAT_SECONDS_OFFSET = LAT_MINUTES_OFFSET + MINUTES_LENGTH;
    protected static final int SECONDS_LENGTH = 2;
    protected static final int LAT_HEMISPHERE_MARKER_OFFSET = LAT_SECONDS_OFFSET + SECONDS_LENGTH;
    protected static final int HEMISPHERE_MARKER_LENGTH = 1;
    protected static final int LON_DEGREES_OFFSET = LAT_HEMISPHERE_MARKER_OFFSET + HEMISPHERE_MARKER_LENGTH;
    protected static final int LON_DEGREES_LENGTH = 3;
    protected static final int LON_MINUTES_OFFSET = LON_DEGREES_OFFSET + LON_DEGREES_LENGTH;
    protected static final int LON_SECONDS_OFFSET = LON_MINUTES_OFFSET + SECONDS_LENGTH;
    protected static final int LON_HEMISPHERE_MARKER_OFFSET = LON_SECONDS_OFFSET + SECONDS_LENGTH;
    protected static final String LAT_DECIMAL_DEGREES_FORMAT = "+dd.ddd";
    protected static final int LAT_DECIMAL_DEGREES_FORMAT_LENGTH = LAT_DECIMAL_DEGREES_FORMAT.length();

    // Data Extenstion Ssebment (DES)
    protected static final String DE = "DE";
    protected static final int DESID_LENGTH = 25;
    protected static final int DESVER_LENGTH = 2;
    protected static final int DESOFLW_LENGTH = 6;
    protected static final int DESITEM_LENGTH = 3;
    protected static final int DESSHL_LENGTH = 4;

    protected static final String TRE_OVERFLOW = "TRE_OVERFLOW";
    protected static final String REGISTERED_EXTENSIONS = "Registered Extensions";
    protected static final String CONTROLLED_EXTENSIONS = "Controlled Extensions";

    // NITF file lengths
    // file header
    protected static final int FHDR_LENGTH = 4;
    protected static final int FVER_LENGTH = 5;
    protected static final int CLEVEL_LENGTH = 2;
    protected static final int STYPE_LENGTH = 4;
    protected static final int OSTAID_LENGTH = 10;
    protected static final int FTITLE_LENGTH = 80;
    protected static final int ONAME_LENGTH = 24;
    protected static final int ONAME20_LENGTH = 27;
    protected static final int OPHONE_LENGTH = 18;
    protected static final int FL_LENGTH = 12;
    protected static final int HL_LENGTH = 6;
    protected static final int NUMI_LENGTH = 3;
    protected static final int LISH_LENGTH = 6;
    protected static final int LI_LENGTH = 10;
    protected static final int NUMS_LENGTH = 3;
    protected static final int LSSH_LENGTH = 4;
    protected static final int LS_LENGTH = 6;
    protected static final int NUMX_LENGTH = 3;
    protected static final int NUML20_LENGTH = 3;
    protected static final int LLSH_LENGTH = 4;
    protected static final int LL_LENGTH = 3;
    protected static final int NUMT_LENGTH = 3;
    protected static final int LTSH_LENGTH = 4;
    protected static final int LT_LENGTH = 5;
    protected static final int NUMDES_LENGTH = 3;
    protected static final int LDSH_LENGTH = 4;
    protected static final int LD_LENGTH = 9;
    protected static final int NUMRES_LENGTH = 3;
    protected static final int UDHDL_LENGTH = 5;
    protected static final int UDHOFL_LENGTH = 3;
    protected static final int XHDL_LENGTH = 5;
    protected static final int XHDLOFL_LENGTH = 3;
    protected static final int MIN_COMPLEXITY_LEVEL = 0;
    protected static final int MAX_COMPLEXITY_LEVEL = 99;
    protected static final int SFH_L1_LENGTH = 7;
    protected static final int SFH_DELIM1_LENGTH = 4;
    protected static final byte[] SFH_DELIM1 = new byte[]{0x0a, (byte) 0x6e, 0x1d, (byte) 0x97};
    protected static final int SFH_DELIM2_LENGTH = 4;
    protected static final byte[] SFH_DELIM2 = new byte[]{0x0e, (byte) 0xca, 0x14, (byte) 0xbf};
    protected static final int SFH_L2_LENGTH = 7;
    protected static final long STREAMING_FILE_MODE = 999999999999L;

    // file security
    protected static final int FSCOP_LENGTH = 5;
    protected static final int FSCPYS_LENGTH = 5;

    // graphics segment
    protected static final String SY = "SY";
    protected static final int SID_LENGTH = 10;
    protected static final int SNAME_LENGTH = 20;
    protected static final String SFMT_CGM = "C";
    protected static final String SSTRUCT = "0000000000000";
    protected static final int SDLVL_LENGTH = 3;
    protected static final int SALVL_LENGTH = 3;
    protected static final int SLOC_HALF_LENGTH = 5;
    protected static final int SBND1_HALF_LENGTH = 5;
    protected static final int SCOLOR_LENGTH = 1;
    protected static final int SBND2_HALF_LENGTH = 5;
    protected static final String SRES = "00";
    protected static final int SXSHDL_LENGTH = 5;
    protected static final int SXSOFL_LENGTH = 3;

    // Symbol (Graphic) Segment
    protected static final int SYTYPE_LENGTH = 1;
    protected static final int NLIPS_LENGTH = 4;
    protected static final int NPIXPL_LENGTH = 4;
    protected static final int NWDTH_LENGTH = 4;
    protected static final int SYNBPP_LENGTH = 1;
    protected static final int SNUM_LENGTH = 6;
    protected static final int SROT_LENGTH = 3;
    protected static final int SYNELUT_LENGTH = 3;

    // image band
    protected static final int IREPBAND_LENGTH = 2;
    protected static final int ISUBCAT_LENGTH = 6;
    protected static final int IFC_LENGTH = 1;
    protected static final int IMFLT_LENGTH = 3;
    protected static final int NLUTS_LENGTH = 1;
    protected static final int NELUT_LENGTH = 5;

    // image segment
    protected static final String IM = "IM";
    protected static final int IID1_LENGTH = 10;
    protected static final int TGTID_LENGTH = 17;
    protected static final int IID2_LENGTH = 80;
    protected static final int ISORCE_LENGTH = 42;
    protected static final int NROWS_LENGTH = 8;
    protected static final int NCOLS_LENGTH = 8;
    protected static final int PVTYPE_LENGTH = 3;
    protected static final int IREP_LENGTH = 8;
    protected static final int ICAT_LENGTH = 8;
    protected static final int ABPP_LENGTH = 2;
    protected static final int PJUST_LENGTH = 1;
    protected static final int ICORDS_LENGTH = 1;
    protected static final int IGEOLO_LENGTH = 60;
    protected static final int NICOM_LENGTH = 1;
    protected static final int ICOM_LENGTH = 80;
    protected static final int IC_LENGTH = 2;
    protected static final int COMRAT_LENGTH = 4;
    protected static final int NBANDS_LENGTH = 1;
    protected static final int XBANDS_LENGTH = 5;
    protected static final int ISYNC_LENGTH = 1;
    protected static final int IMODE_LENGTH = 1;
    protected static final int NBPR_LENGTH = 4;
    protected static final int NBPC_LENGTH = 4;
    protected static final int NPPBH_LENGTH = 4;
    protected static final int NPPBV_LENGTH = 4;
    protected static final int NBPP_LENGTH = 2;
    protected static final int IDLVL_LENGTH = 3;
    protected static final int IALVL_LENGTH = 3;
    protected static final int ILOC_HALF_LENGTH = 5;
    protected static final int IMAG_LENGTH = 4;
    protected static final int UDIDL_LENGTH = 5;
    protected static final int UDOFL_LENGTH = 3;
    protected static final int IXSHDL_LENGTH = 5;
    protected static final int IXSOFL_LENGTH = 3;

    // label segment
    protected static final String LA = "LA";
    protected static final int LID_LENGTH = 10;
    protected static final int LFS_LENGTH = 1;
    protected static final int LCW_LENGTH = 2;
    protected static final int LCH_LENGTH = 2;
    protected static final int LDLVL_LENGTH = 3;
    protected static final int LALVL_LENGTH = 3;
    protected static final int LLOC_HALF_LENGTH = 5;
    protected static final int LXSHDL_LENGTH = 5;
    protected static final int LXSOFL_LENGTH = 3;

    // security metadata
    protected static final int XSCLAS_LENGTH = 1;
    protected static final int XSCLSY_LENGTH = 2;
    protected static final int XSCODE_LENGTH = 11;
    protected static final int XSCTLH_LENGTH = 2;
    protected static final int XSREL_LENGTH = 20;
    protected static final int XSDCTP_LENGTH = 2;
    protected static final int XSDCDT_LENGTH = 8;
    protected static final int XSDCXM_LENGTH = 4;
    protected static final int XSDG_LENGTH = 1;
    protected static final int XSDGDT_LENGTH = 8;
    protected static final int XSCLTX_LENGTH = 43;
    protected static final int XSCATP_LENGTH = 1;
    protected static final int XSCAUT_LENGTH = 40;
    protected static final int XSCRSN_LENGTH = 1;
    protected static final int XSSRDT_LENGTH = 8;
    protected static final int XSCTLN_LENGTH = 15;

    // NITF 2.0 field lengths
    protected static final int XSCODE20_LENGTH = 40;
    protected static final int XSCTLH20_LENGTH = 40;
    protected static final int XSREL20_LENGTH = 40;
    protected static final int XSCAUT20_LENGTH = 20;
    protected static final int XSCTLN20_LENGTH = 20;
    protected static final int XSDWNG20_LENGTH = 6;
    protected static final int XSDEVT20_LENGTH = 40;

    protected static final String DOWNGRADE_EVENT_MAGIC = "999998";

    // text segment
    protected static final String TE = "TE";
    protected static final int TEXTID_LENGTH = 7;
    protected static final int TXTALVL_LENGTH = 3;
    protected static final int TEXTID20_LENGTH = 10;
    protected static final int TXTITL_LENGTH = 80;
    protected static final int TXTFMT_LENGTH = 3;
    protected static final int TXSHDL_LENGTH = 5;
    protected static final int TXSOFL_LENGTH = 3;

    // Tagged Record Extension (TRE) segment
    protected static final int TAG_LENGTH = 6;
    protected static final int TAGLEN_LENGTH = 5;
    protected static final String AND_CONDITION = " AND ";
    protected static final String UNSUPPORTED_IFTYPE_FORMAT_MESSAGE = "Unsupported format for iftype:";

}
