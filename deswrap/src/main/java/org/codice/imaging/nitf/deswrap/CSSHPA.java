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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import static org.codice.imaging.nitf.core.dataextension.impl.DataExtensionSegmentFactory.getUserDefinedDES;
import org.codice.imaging.nitf.core.dataextension.UserDefinedDataExtensionSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.codice.imaging.nitf.deswrap.CSSHPAConstants.CC_SOURCE_LENGTH;
import static org.codice.imaging.nitf.deswrap.CSSHPAConstants.CLOUD_SHAPES_USE;
import static org.codice.imaging.nitf.deswrap.CSSHPAConstants.FILEOFFSET_LENGTH;
import static org.codice.imaging.nitf.deswrap.CSSHPAConstants.SHAPE_CLASS_LENGTH;
import static org.codice.imaging.nitf.deswrap.CSSHPAConstants.SHAPE_FILENAME_LENGTH;
import static org.codice.imaging.nitf.deswrap.CSSHPAConstants.SHAPE_USE_LENGTH;

/**
 * Wrapper for the shapefile (CSSHPA) DES.
 *
 * This data extension segment is defined in STDI-0002-2 Appendix D, as modified
 * by RFC-080.
 */
public class CSSHPA extends DES {

    private static final int NUM_SHAPE_PARTS = 3;

    private static class ShapePart {

        private String filename;
        private long startOffset;
        private long endOffset;
    }

    private Map<String, ShapePart> mShapeParts = new HashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(CSSHPA.class);

    /**
     * Create a Shapefile data extension segment (CSSHPA DES).
     *
     * See STDI-0002-2 Appendix D and NTB RFC-080.
     *
     * This sets the shape use to be "USER_DEF_SHAPES".
     *
     * @param fileType the type (version) of NITF file this data extension
     * segment is for
     * @param shpFile the path to the SHP geometry
     * @param shxFile the path to the SHX index
     * @param dbfFile the path to the DBF attributes
     * @return shapefile DES, ready to write out.
     * @throws NitfFormatException if the shapefile DES could not be generated
     */
    public static DataExtensionSegment createCSSHPA(final FileType fileType, final File shpFile, final File shxFile, final File dbfFile)
            throws NitfFormatException {
        UserDefinedDataExtensionSegment csshpa = new CSSHPAUserDefinedDES(shpFile, shxFile, dbfFile);
        return getUserDefinedDES(fileType, csshpa);
    }

    /**
     * Create a Shapefile data extension segment (CSSHPA DES).
     *
     * See STDI-0002-2 Appendix D and NTB RFC-080.
     *
     * Note that the shape use is required to be a registered value, and can not
     * be set to arbitrary values.
     *
     * @param fileType the type (version) of NITF file this data extension
     * segment is for
     * @param shapeUse the shape use value (SHAPE_USE) to set.
     * @param shpFile the path to the SHP geometry
     * @param shxFile the path to the SHX index
     * @param dbfFile the path to the DBF attributes
     *
     * If the shape use is "CLOUD_SHAPES", a default sensor value of "PAN, MS"
     * will be used as the (required) CC_SOURCE value. If you want to use
     * something else, see createCloudShapesDES().
     *
     * @return shapefile DES, ready to write out.
     * @throws NitfFormatException if the shapefile DES could not be generated
     */
    public static DataExtensionSegment createCSSHPA(final FileType fileType, final String shapeUse,
            final File shpFile, final File shxFile, final File dbfFile) throws NitfFormatException {
        UserDefinedDataExtensionSegment csshpa = new CSSHPAUserDefinedDES(shapeUse, shpFile, shxFile, dbfFile);
        return getUserDefinedDES(fileType, csshpa);
    }

    /**
     * Create a Shapefile data extension segment (CSSHPA DES).
     *
     * See STDI-0002-2 Appendix D and NTB RFC-080.
     *
     * Note that the shape use is required to be a registered value, and can not
     * be set to arbitrary values.
     *
     * @param fileType the type (version) of NITF file this data extension
     * segment is for
     * @param ccSensor the cloud cover sensor ("PAN", "MS" or "PAN, MS")
     * @param shpFile the path to the SHP geometry
     * @param shxFile the path to the SHX index
     * @param dbfFile the path to the DBF attributes
     * @return shapefile DES, ready to write out.
     * @throws NitfFormatException if the shapefile DES could not be generated
     */
    public static DataExtensionSegment createCloudShapesDES(final FileType fileType, final String ccSensor,
            final File shpFile, final File shxFile, final File dbfFile) throws NitfFormatException {
        UserDefinedDataExtensionSegment csshpa = new CSSHPAUserDefinedDES(CLOUD_SHAPES_USE, ccSensor, shpFile, shxFile, dbfFile);
        return getUserDefinedDES(fileType, csshpa);
    }

    /**
     * Create the shapefile (CSSHPA) DES.
     *
     * @param des base DataExtensionSegment to build this DES from.
     */
    public CSSHPA(final DataExtensionSegment des) {
        mDES = des;
    }

    /**
     * Get the shape use (SHAPE_USE) for the DES.
     *
     * @return shape usage, with any trailing whitespace removed.
     */
    public final String getShapeUse() {
        String subheader = mDES.getUserDefinedSubheaderField();
        String shapeUse = subheader.substring(0, SHAPE_USE_LENGTH);
        return shapeUse.trim();
    }

    /**
     * Get the shape class (SHAPE_CLASS) for the DES.
     *
     * This is the type of geometry in the shapefile.
     *
     * @return the shape class, with any trailing whitespace removed.
     */
    public final String getShapeClass() {
        String subheader = mDES.getUserDefinedSubheaderField();
        String shapeClass = subheader.substring(SHAPE_USE_LENGTH, SHAPE_USE_LENGTH + FILEOFFSET_LENGTH);
        return shapeClass.trim();
    }

    /**
     * Get the cloud cover source sensor (CC_SOURCE) for the DES.
     *
     * This can be PAN for Panchromatic or MS for Multi-Spectral or PAN, MS.
     * Additional values must be registered.
     *
     * @return sensor type (or empty string if the shape class is not "CLOUD_SHAPES")
     */
    public final String getCloudCoverSourceSensor() {
        if (CLOUD_SHAPES_USE.equals(getShapeUse())) {
            String subheader = mDES.getUserDefinedSubheaderField();
            int ccoffset = SHAPE_USE_LENGTH + SHAPE_CLASS_LENGTH;
            String cloudSourceSensor = subheader.substring(ccoffset, ccoffset + CC_SOURCE_LENGTH);
            return cloudSourceSensor.trim();
        } else {
            return "";
        }
    }

    /**
     * Get the shapefile (SHP) content.
     *
     * This is the content that would normally be associated with the .shp file.
     *
     * @return shapefile content as a byte array
     */
    public final byte[] getSHP() {
        return getShapePart("SHP");
    }

    /**
     * Get the shapefile index (SHX) content.
     *
     * This is the content that would normally be associated with the .shx file.
     *
     * @return shapefile index content as a byte array
     */
    public final byte[] getSHX() {
        return getShapePart("SHX");
    }

    /**
     * Get the shapefile attribute (DBF) content.
     *
     * This is the content that would normally be associated with the .dbf file.
     *
     * @return shapefile attribute content as a byte array
     */
    public final byte[] getDBF() {
        return getShapePart("DBF");
    }

    private byte[] getShapePart(final String filename) {
        initShapePartsIfNecessary();
        ShapePart shapePart = mShapeParts.get(filename);
        int length = (int) (shapePart.endOffset - shapePart.startOffset);
        byte[] data = new byte[length];
        mDES.consume(imageInputStream -> {
            try {
                imageInputStream.seek(shapePart.startOffset);
                imageInputStream.readFully(data);
            } catch (IOException ieo) {
                LOG.warn(ieo.getMessage(), ieo);
            }
        });
        return data;
    }

    private void initShapePartsIfNecessary() {
        if (mShapeParts.isEmpty()) {
            initShapeParts();
        }
    }

    private void initShapeParts() {
        String subheader = mDES.getUserDefinedSubheaderField();
        int offset = getShape1FilenameOffset();
        List<Long> offsets = new ArrayList<>();
        for (int i = 0; i < NUM_SHAPE_PARTS; ++i) {
            ShapePart shapePart = new ShapePart();
            shapePart.filename = subheader.substring(offset, offset + SHAPE_FILENAME_LENGTH);
            offset += SHAPE_FILENAME_LENGTH;
            shapePart.startOffset = Integer.parseInt(subheader.substring(offset, offset + FILEOFFSET_LENGTH));
            offset += FILEOFFSET_LENGTH;
            shapePart.endOffset = mDES.getDataLength();
            mShapeParts.put(shapePart.filename, shapePart);
            offsets.add(shapePart.startOffset);
        }
        for (ShapePart shapePart : mShapeParts.values()) {
            for (Long o : offsets) {
                if ((o < shapePart.endOffset) && (o > shapePart.startOffset)) {
                    shapePart.endOffset = o;
                }
            }
        }
    }

    private int getShape1FilenameOffset() {
        int baseOffset = SHAPE_USE_LENGTH + SHAPE_CLASS_LENGTH;
        if (CLOUD_SHAPES_USE.equals(getShapeUse())) {
            baseOffset += CC_SOURCE_LENGTH;
        }
        return baseOffset;
    }

}
