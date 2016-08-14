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
package org.codice.imaging.nitf.deswrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.dataextension.UserDefinedDataExtensionSegment;
import static org.codice.imaging.nitf.deswrap.CSSHPAConstants.CLOUD_SHAPES_USE;

/**
 * Implementation of CSSHPA DES.
 *
 * This is defined in STDI-0002-2 Appendix D, as modified by RFC-080.
 */
class CSSHPAUserDefinedDES implements UserDefinedDataExtensionSegment {

    private final File mSHP;
    private final File mSHX;
    private final File mDBF;
    private final String mShapeUse;
    private final String mCloudCoverSensor;

    private static final int FILE_BUF_SIZE = 8192;

    CSSHPAUserDefinedDES(final File shpFile, final File shxFile, final File dbfFile) {
        mSHP = shpFile;
        mSHX = shxFile;
        mDBF = dbfFile;
        mShapeUse = "USER_DEF_SHAPES";
        mCloudCoverSensor = "";
    }

    CSSHPAUserDefinedDES(final String shapeUse, final File shpFile, final File shxFile, final File dbfFile) {
        mSHP = shpFile;
        mSHX = shxFile;
        mDBF = dbfFile;
        mShapeUse = shapeUse;
        mCloudCoverSensor = "PAN, MS";
    }

    CSSHPAUserDefinedDES(final String shapeUse, final String ccSource, final File shpFile, final File shxFile, final File dbfFile) {
        mSHP = shpFile;
        mSHX = shxFile;
        mDBF = dbfFile;
        mShapeUse = shapeUse;
        mCloudCoverSensor = ccSource;
    }

    @Override
    public String getTypeIdentifier() {
        return "CSSHPA DES";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getUserDefinedSubheader() throws NitfFormatException {
        try {
            StringBuilder subheaderData = new StringBuilder();
            subheaderData.append(padStringToLength(mShapeUse, CSSHPAConstants.SHAPE_USE_LENGTH));
            subheaderData.append(getShapefileClassName(mSHP));
            if (CLOUD_SHAPES_USE.equals(mShapeUse)) {
                subheaderData.append(padStringToLength(mCloudCoverSensor, CSSHPAConstants.CC_SOURCE_LENGTH));
            }
            subheaderData.append("SHP");
            subheaderData.append(padNumberToLength(0, CSSHPAConstants.FILEOFFSET_LENGTH));
            long incrementalLength = mSHP.length();
            subheaderData.append("SHX");
            subheaderData.append(padNumberToLength(incrementalLength, CSSHPAConstants.FILEOFFSET_LENGTH));
            incrementalLength += mSHX.length();
            subheaderData.append("DBF");
            subheaderData.append(padNumberToLength(incrementalLength, CSSHPAConstants.FILEOFFSET_LENGTH));
            return subheaderData.toString();
        } catch (IOException ex) {
            throw new NitfFormatException("Could not generate CSSHPA: " + ex.getMessage());
        }
    }

    @Override
    public ImageInputStream getUserData() throws NitfFormatException {
        try {
            FileImageOutputStream desData = new FileImageOutputStream(Files.createTempFile(null, null).toFile());
            writeOutFile(desData, mSHP);
            writeOutFile(desData, mSHX);
            writeOutFile(desData, mDBF);
            return desData;
        } catch (IOException ex) {
            throw new NitfFormatException("Could not generate CSSHPA: " + ex.getMessage());
        }
    }

    private static String getShapefileClassName(final File shpFile) throws IOException, NitfFormatException {
        RandomAccessFile shp = new RandomAccessFile(shpFile, "r");
        shp.seek(CSSHPAConstants.SHAPE_TYPE_OFFSET);
        int shapefileClass = Integer.reverseBytes(shp.readInt());
        String shapeClassName;
        switch (shapefileClass) {
            case CSSHPAConstants.SHAPE_CLASS_NULL:
                shapeClassName = "NULL SHAPE";
                break;
            case CSSHPAConstants.SHAPE_CLASS_POINT:
                shapeClassName = "POINT";
                break;
            case CSSHPAConstants.SHAPE_CLASS_POLYLINE:
                shapeClassName = "POLYLINE";
                break;
            case CSSHPAConstants.SHAPE_CLASS_POLYGON:
                shapeClassName = "POLYGON";
                break;
            case CSSHPAConstants.SHAPE_CLASS_MULTIPOINT:
                shapeClassName = "MULTIPOINT";
                break;
            case CSSHPAConstants.SHAPE_CLASS_POINTZ:
                shapeClassName = "POINTZ";
                break;
            case CSSHPAConstants.SHAPE_CLASS_POLYLINEZ:
                shapeClassName = "POLYLINEZ";
                break;
            case CSSHPAConstants.SHAPE_CLASS_POLYGONZ:
                shapeClassName = "POLYGONZ";
                break;
            case CSSHPAConstants.SHAPE_CLASS_MULTIPOINTZ:
                // The missing I is intentional
                shapeClassName = "MULTPOINTZ";
                break;
            case CSSHPAConstants.SHAPE_CLASS_POINTM:
                shapeClassName = "POINTM";
                break;
            case CSSHPAConstants.SHAPE_CLASS_POLYLINEM:
                shapeClassName = "POLYLINEM";
                break;
            case CSSHPAConstants.SHAPE_CLASS_POLYGONM:
                shapeClassName = "POLYGONM";
                break;
            case CSSHPAConstants.SHAPE_CLASS_MULTIPOINTM:
                // The missing I is intentional
                shapeClassName = "MULTPOINTM";
                break;
            case CSSHPAConstants.SHAPE_CLASS_MULTIPATCH:
                shapeClassName = "MULTIPATCH";
                break;
            default:
                throw new IOException(String.format("Error reading shapefile, unknown shapefile geometry class: %d", shapefileClass));
        }
        return padStringToLength(shapeClassName, CSSHPAConstants.SHAPE_CLASS_LENGTH);
    }

    private static void writeOutFile(final FileImageOutputStream desData, final File shpFile) throws IOException {
        try (InputStream is = new FileInputStream(shpFile)) {
            byte[] tempBuf = new byte[FILE_BUF_SIZE];
            int numBytesRead;
            while ((numBytesRead = is.read(tempBuf, 0, tempBuf.length)) > 0) {
                desData.write(tempBuf, 0, numBytesRead);
            }
        }
    }

    private static String padNumberToLength(final long number, final int length) {
        return String.format("%0" + length + "d", number);
    }

    private static String padStringToLength(final String s, final int length) {
        return String.format("%1$-" + length + "s", s);
    }

}
