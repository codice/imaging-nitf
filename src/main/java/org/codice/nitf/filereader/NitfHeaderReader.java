/**
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
 **/
package org.codice.nitf.filereader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NitfHeaderReader
{
    private Boolean hasNitfHeader = false;
    private NitfVersion nitfVersion = NitfVersion.UNKNOWN;
    private int nitfComplexityLevel = 0;
    private String nitfStandardType = null;
    private String nitfOriginatingStationId = null;
    private Date nitfFileDateTime = null;
    private String nitfFileTitle = null;
    private NitfSecurityClassification nitfSecurityClassification = NitfSecurityClassification.UNKNOWN;
    private String nitfFileSecurityClassificationSystem = null;
    private String nitfFileCodewords = null;
    private String nitfFileControlAndHandling = null;
    private String nitfFileReleaseInstructions = null;
    // Could be an enumerated type
    private String nitfFileDeclassificationType = null;
    // String instead of Date because its frequently just an empty string
    private String nitfFileDeclassificationDate = null;
    private String nitfFileDeclassificationExemption = null;
    private String nitfFileDowngrade = null;
    // String instead of Date because its frequently just an empty string
    private String nitfFileDowngradeDate = null;
    private String nitfFileClassificationText = null;
    // Could be an enumerated type
    private String nitfFileClassificationAuthorityType = null;
    private String nitfFileClassificationAuthority = null;
    private String nitfFileClassificationReason = null;
    private String nitfFileSecuritySourceDate = null;
    private String nitfFileSecurityControlNumber = null;
    private String nitfFileCopyNumber = null;
    private String nitfFileNumberOfCopies = null;
    private int nitfFileBackgroundColourRed = 0;
    private int nitfFileBackgroundColourGreen = 0;
    private int nitfFileBackgroundColourBlue = 0;
    private String nitfOriginatorsName = null;
    private String nitfOriginatorsPhoneNumber = null;
    private long nitfFileLength = -1;
    private int nitfHeaderLength = -1;
    private int numberImageSegments = 0;
    private ArrayList<Integer> lish = new ArrayList<Integer>();
    private ArrayList<Long> li = new ArrayList<Long>();
    private int numberGraphicsSegments = 0;
    private int numberTextSegments = 0;
    private int numberDataExtensionSegments = 0;
    private int numberReservedExtensionSegments = 0;
    private int userDefinedHeaderDataLength = 0;
    private int extendedHeaderDataLength = 0;

    private ArrayList<NitfImageSegment> imageSegments = new ArrayList<NitfImageSegment>();

    private NitfReader reader;

    private static final int FHDR_LENGTH = 4;
    private static final String NITF_FHDR = "NITF";
    private static final int FVER_LENGTH = 5;
    private static final int CLEVEL_LENGTH = 2;
    private static final int STYPE_LENGTH = 4;
    private static final int OSTAID_LENGTH = 10;
    private static final int FDT_LENGTH = 14;
    private static final int FTITLE_LENGTH = 80;
    private static final int FSCLAS_LENGTH = 1;
    private static final int FSCLSY_LENGTH = 2;
    private static final int FSCODE_LENGTH = 11;
    private static final int FSCTLH_LENGTH = 2;
    private static final int FSREL_LENGTH = 20;
    private static final int FSDCTP_LENGTH = 2;
    private static final int FSDCDT_LENGTH = 8;
    private static final int FSDCXM_LENGTH = 4;
    private static final int FSDG_LENGTH = 1;
    private static final int FSDGDT_LENGTH = 8;
    private static final int FSCLTX_LENGTH = 43;
    private static final int FSCATP_LENGTH = 1;
    private static final int FSCAUT_LENGTH = 40;
    private static final int FSCRSN_LENGTH = 1;
    private static final int FSSRDT_LENGTH = 8;
    private static final int FSCTLN_LENGTH = 15;
    private static final int FSCOP_LENGTH = 5;
    private static final int FSCPYS_LENGTH = 5;
    private static final int ENCRYP_LENGTH = 1;
    private static final int FBKGC_LENGTH = 3;
    private static final int ONAME_LENGTH = 24;
    private static final int OPHONE_LENGTH = 18;
    private static final int FL_LENGTH = 12;
    private static final int HL_LENGTH = 6;
    private static final int NUMI_LENGTH = 3;
    private static final int LISH_LENGTH = 6;
    private static final int LI_LENGTH = 10;
    private static final int NUMS_LENGTH = 3;
    private static final int NUMX_LENGTH = 3;
    private static final int NUMT_LENGTH = 3;
    private static final int NUMDES_LENGTH = 3;
    private static final int NUMRES_LENGTH = 3;
    private static final int UDHDL_LENGTH = 5;
    private static final int XHDL_LENGTH = 5;

    public NitfHeaderReader(InputStream nitfInputStream) throws ParseException {
        reader = new NitfReader(new BufferedReader(new InputStreamReader(nitfInputStream)), 0);
        readFHDR();
        readFVER();
        readCLEVEL();
        readSTYPE();
        readOSTAID();
        readFDT();
        readFTITLE();
        readFSCLAS();
        readFSCLSY();
        readFSCODE();
        readFSCTLH();
        readFSREL();
        readFSDCTP();
        readFSDCDT();
        readFSDCXM();
        readFSDG();
        readFSDGDT();
        readFSCLTX();
        readFSCATP();
        readFSCAUT();
        readFSCRSN();
        readFSSRDT();
        readFSCTLN();
        readFSCOP();
        readFSCPYS();
        readENCRYP();
        readFBKGC();
        readONAME();
        readOPHONE();
        readFL();
        readHL();
        readNUMI();
        for (int i = 0; i < numberImageSegments; ++i) {
            readLISH();
            readLI(i);
        }
        readNUMS();
        for (int i = 0; i < numberGraphicsSegments; ++i) {
            // TODO: find a case that exercises this and implement it
        }
        readNUMX();
        readNUMT();
        for (int i = 0; i < numberTextSegments; ++i) {
            // TODO: find a case that exercises this and implement it
        }
        readNUMDES();
        for (int i = 0; i < numberDataExtensionSegments; ++i) {
            // TODO: find a case that exercises this and implement it
        }
        readNUMRES();
        for (int i = 0; i < numberReservedExtensionSegments; ++i) {
            // TODO: find a case that exercises this and implement it
        }
        readUDHDL();
        if (userDefinedHeaderDataLength > 0) {
            // TODO: find a case that exercises this and implement it
        }
        readXHDL();
        if (extendedHeaderDataLength > 0) {
            // TODO: find a case that exercises this and implement it
        }
        readImageSegments();
    }

    public Boolean isNitf() {
        return hasNitfHeader;
    }

    public NitfVersion getVersion() {
        return nitfVersion;
    }

    public int getComplexityLevel() {
        return nitfComplexityLevel;
    }

    public String getStandardType() {
        return nitfStandardType;
    }

    public String getOriginatingStationId() {
        return nitfOriginatingStationId;
    }

    public Date getFileDateTime() {
        return nitfFileDateTime;
    }

    public String getFileTitle() {
        return nitfFileTitle;
    }

    public NitfSecurityClassification getSecurityClassification() {
        return nitfSecurityClassification;
    }

    public String getFileSecurityClassificationSystem() {
        return nitfFileSecurityClassificationSystem;
    }

    public String getFileCodewords() {
        return nitfFileCodewords;
    }

    public String getFileControlAndHandling() {
        return nitfFileControlAndHandling;
    }

    public String getFileReleaseInstructions() {
        return nitfFileReleaseInstructions;
    }

    public String getFileDeclassificationType() {
        return nitfFileDeclassificationType;
    }

    public String getFileDeclassificationDate() {
        return nitfFileDeclassificationDate;
    }

    public String getFileDeclassificationExemption() {
        return nitfFileDeclassificationExemption;
    }

    public String getFileDowngrade() {
        return nitfFileDowngrade;
    }

    public String getFileDowngradeDate() {
        return nitfFileDowngradeDate;
    }

    public String getFileClassificationText() {
        return nitfFileClassificationText;
    }

    public String getFileClassificationAuthorityType() {
        return nitfFileClassificationAuthorityType;
    }

    public String getFileClassificationAuthority() {
        return nitfFileClassificationAuthority;
    }

    public String getFileClassificationReason() {
        return nitfFileClassificationReason;
    }

    public String getFileSecuritySourceDate() {
        return nitfFileSecuritySourceDate;
    }

    public String getFileSecurityControlNumber() {
        return nitfFileSecurityControlNumber;
    }

    public String getFileCopyNumber() {
        return nitfFileCopyNumber;
    }

    public String getFileNumberOfCopies() {
        return nitfFileNumberOfCopies;
    }

    public int getFileBackgroundColourRed() {
        return nitfFileBackgroundColourRed;
    }

    public int getFileBackgroundColourGreen() {
        return nitfFileBackgroundColourGreen;
    }

    public int getFileBackgroundColourBlue() {
        return nitfFileBackgroundColourBlue;
    }

    public String getOriginatorsName() {
        return nitfOriginatorsName;
    }

    public String getOriginatorsPhoneNumber() {
        return nitfOriginatorsPhoneNumber;
    }

    public long getFileLength() {
        return nitfFileLength;
    }

    public int getHeaderLength() {
        return nitfHeaderLength;
    }

    public int getNumberOfImageSegments() {
        // TODO: this should be based on the number we actually found, not what the header claimed
        return numberImageSegments;
    }

    public int getLengthOfImageSubheader(int i) {
        return lish.get(i);
    }

    public long getLengthOfImage(int i) {
        return li.get(i);
    }

    public int getNumberOfGraphicsSegments() {
        return numberGraphicsSegments;
    }

    public int getNumberOfTextSegments() {
        return numberTextSegments;
    }

    public int getNumberOfDataExtensionSegments() {
        return numberDataExtensionSegments;
    }

    public int getNumberOfReservedExtensionSegments() {
        return numberReservedExtensionSegments;
    }

    public int getUserDefinedHeaderDataLength() {
        return userDefinedHeaderDataLength;
    }

    public int getExtendedHeaderDataLength() {
        return extendedHeaderDataLength;
    }

    public NitfImageSegment getImageSegment(int segmentNumber) {
        return getImageSegmentZeroBase(segmentNumber - 1);
    }

    public NitfImageSegment getImageSegmentZeroBase(int segmentNumberZeroBase) {
        return imageSegments.get(segmentNumberZeroBase);
    }

    private void readFHDR() throws ParseException {
        reader.verifyHeaderMagic(NITF_FHDR);
        hasNitfHeader = true;
    }

    private void readFVER() throws ParseException {
        String fver = reader.readBytes(FVER_LENGTH);
        nitfVersion = NitfVersion.getEnumValue(fver);
    }

    private void readCLEVEL() throws ParseException {
        nitfComplexityLevel = reader.readBytesAsInteger(CLEVEL_LENGTH);
        if ((nitfComplexityLevel < 0) || (nitfComplexityLevel > 99)) {
            new ParseException(String.format("CLEVEL out of range: %i", nitfComplexityLevel), reader.getNumBytesRead());
        }
    }

    private void readSTYPE() throws ParseException {
        nitfStandardType = reader.readBytes(STYPE_LENGTH);
    }

    private void readOSTAID() throws ParseException {
        nitfOriginatingStationId = reader.readTrimmedBytes(OSTAID_LENGTH);
    }

    private void readFDT() throws ParseException {
        nitfFileDateTime = reader.readNitfDateTime();
    }

    private void readFTITLE() throws ParseException {
        nitfFileTitle = reader.readTrimmedBytes(FTITLE_LENGTH);
    }

    private void readFSCLAS() throws ParseException {
        String fsclas = reader.readBytes(FSCLAS_LENGTH);
        nitfSecurityClassification = NitfSecurityClassification.getEnumValue(fsclas);
    }

    private void readFSCLSY() throws ParseException {
        nitfFileSecurityClassificationSystem = reader.readTrimmedBytes(FSCLSY_LENGTH);
    }

    private void readFSCODE() throws ParseException {
        nitfFileCodewords = reader.readTrimmedBytes(FSCODE_LENGTH);
    }

    private void readFSCTLH() throws ParseException {
        nitfFileControlAndHandling = reader.readTrimmedBytes(FSCTLH_LENGTH);
    }

    private void readFSREL() throws ParseException {
        nitfFileReleaseInstructions = reader.readTrimmedBytes(FSREL_LENGTH);
    }

    private void readFSDCTP() throws ParseException {
        nitfFileDeclassificationType = reader.readTrimmedBytes(FSDCTP_LENGTH);
    }

    private void readFSDCDT() throws ParseException {
        nitfFileDeclassificationDate = reader.readTrimmedBytes(FSDCDT_LENGTH);
    }

    private void readFSDCXM() throws ParseException {
        nitfFileDeclassificationExemption = reader.readTrimmedBytes(FSDCXM_LENGTH);
    }

    private void readFSDG() throws ParseException {
        nitfFileDowngrade = reader.readTrimmedBytes(FSDG_LENGTH);
    }

    private void readFSDGDT() throws ParseException {
        nitfFileDowngradeDate = reader.readTrimmedBytes(FSDGDT_LENGTH);
    }

    private void readFSCLTX() throws ParseException {
        nitfFileClassificationText= reader.readTrimmedBytes(FSCLTX_LENGTH);
    }

    private void readFSCATP() throws ParseException {
        nitfFileClassificationAuthorityType = reader.readTrimmedBytes(FSCATP_LENGTH);
    }

    private void readFSCAUT() throws ParseException {
        nitfFileClassificationAuthority = reader.readTrimmedBytes(FSCAUT_LENGTH);
    }

    private void readFSCRSN() throws ParseException {
        nitfFileClassificationReason = reader.readTrimmedBytes(FSCRSN_LENGTH);
    }

    private void readFSSRDT() throws ParseException {
        nitfFileSecuritySourceDate = reader.readTrimmedBytes(FSSRDT_LENGTH);
    }

    private void readFSCTLN() throws ParseException {
        nitfFileSecurityControlNumber = reader.readTrimmedBytes(FSCTLN_LENGTH);
    }

    private void readFSCOP() throws ParseException {
        nitfFileCopyNumber = reader.readTrimmedBytes(FSCOP_LENGTH);
    }

    private void readFSCPYS() throws ParseException {
        nitfFileNumberOfCopies = reader.readTrimmedBytes(FSCPYS_LENGTH);
    }

    private void readENCRYP() throws ParseException {
        if (!reader.readBytes(ENCRYP_LENGTH).equals("0")) {
            new ParseException("Unexpected ENCRYP values", reader.getNumBytesRead());
        }
    }

    private void readFBKGC() throws ParseException {
        String fbkgc = reader.readBytes(FBKGC_LENGTH);
        try {
            nitfFileBackgroundColourRed = Integer.parseInt(fbkgc.substring(0, 1));
            nitfFileBackgroundColourGreen = Integer.parseInt(fbkgc.substring(1, 2));
            nitfFileBackgroundColourBlue = Integer.parseInt(fbkgc.substring(2));
        } catch (NumberFormatException ex) {
            new ParseException("Bad FBKGC", reader.getNumBytesRead());
        }
    }

    private void readONAME() throws ParseException {
        nitfOriginatorsName = reader.readTrimmedBytes(ONAME_LENGTH);
    }

    private void readOPHONE() throws ParseException {
        nitfOriginatorsPhoneNumber = reader.readTrimmedBytes(OPHONE_LENGTH);
    }

    private void readFL() throws ParseException {
        nitfFileLength = reader.readBytesAsLong(FL_LENGTH);
    }

    private void readHL() throws ParseException {
        nitfHeaderLength = reader.readBytesAsInteger(HL_LENGTH);
    }

    private void readNUMI() throws ParseException {
        numberImageSegments = reader.readBytesAsInteger(NUMI_LENGTH);
    }

    private void readLISH() throws ParseException {
        lish.add(reader.readBytesAsInteger(LISH_LENGTH));
    }

    private void readLI(int i) throws ParseException {
        li.add(reader.readBytesAsLong(LI_LENGTH));
    }

    private void readNUMS() throws ParseException {
        numberGraphicsSegments = reader.readBytesAsInteger(NUMS_LENGTH);
    }

    private void readNUMX() throws ParseException {
        // Just throw this away.
        String numx = reader.readBytes(NUMX_LENGTH);
    }

    private void readNUMT() throws ParseException {
        numberTextSegments = reader.readBytesAsInteger(NUMT_LENGTH);
    }

    private void readNUMDES() throws ParseException {
        numberDataExtensionSegments = reader.readBytesAsInteger(NUMDES_LENGTH);
    }

    private void readNUMRES() throws ParseException {
        numberReservedExtensionSegments = reader.readBytesAsInteger(NUMRES_LENGTH);
    }

    private void readUDHDL() throws ParseException {
        userDefinedHeaderDataLength = reader.readBytesAsInteger(UDHDL_LENGTH);
    }

    private void readXHDL() throws ParseException {
        extendedHeaderDataLength = reader.readBytesAsInteger(XHDL_LENGTH);
    }

    private void readImageSegments() throws ParseException {
        for (int i = 0; i < numberImageSegments; ++i) {
            imageSegments.add(new NitfImageSegment(reader.reader, reader.getNumBytesRead()));
        }
    }
}
