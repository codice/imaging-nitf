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
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import org.codice.imaging.nitf.core.NitfImageBand;
import org.codice.imaging.nitf.core.NitfImageSegment;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

class NitfImageSegmentNode extends AbstractSegmentNode {

    private final NitfImageSegment segment;

    public NitfImageSegmentNode(final NitfImageSegment nitfImageSegment) {
        super(Children.LEAF);
        segment = nitfImageSegment;
        setDisplayName("Image Segment: " + segment.getIdentifier());
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        addSubSegmentProperties(set, segment);
        set.put(new DateProperty("imageDateTime",
                "Image Date and Time",
                "Date and time of this image's acquisition.",
                segment.getImageDateTime().toDate()));
        set.put(new StringProperty("targetIdentifier",
                "Target Identifier",
                "Primary target identifier (BE number and O-suffix, followed by country code",
                segment.getImageTargetId().toString()));
        set.put(new StringProperty("imageIdentifier2",
                "Image Identifier",
                "Can contain the identification of additional information about the image",
                segment.getImageIdentifier2()));
        set.put(new StringProperty("imageSource",
                "Image Source",
                "Description of the source of the image.",
                segment.getImageSource()));
        set.put(new LongProperty("numRows",
                "Number of Significant Rows",
                "The total number of significant rows in the image.",
                segment.getNumberOfRows()));
        set.put(new LongProperty("numColumns",
                "Number of Significant Columns",
                "The total number of significant columns in the image.",
                segment.getNumberOfColumns()));
        set.put(new StringProperty("pixelValueType",
                "Pixel Value Type",
                "Computer representation used for the value of each pixel in each band in the image",
                segment.getPixelValueType().toString()));
        set.put(new StringProperty("imageRepresentation",
                "Image Representation",
                "Indicator of the processing required in order to display an image",
                 segment.getImageRepresentation().toString()));
        set.put(new StringProperty("imageCategory",
                "Image Category",
                "Indicator of the specific category of image, raster or grid data.",
                segment.getImageCategory().toString()));
        set.put(new IntegerProperty("actualBitsPerPixelPerBand",
                "Actual Bits-Per-Pixel Per Band",
                "The number of 'significant bits' for the value in each band of each pixel without compression",
                segment.getActualBitsPerPixelPerBand()));
        set.put(new StringProperty("pixelJustification",
                "Pixel Justification",
                "When ABPP is not equal to NBPP, this field indicates whether the significant bits are left justified (L) or right justified(R)",
                segment.getPixelJustification().toString()));
        set.put(new StringProperty("icords",
                "Image Coordinate Representation",
                "A code indicating the type of coordinate representation used for providing an approximate location of the image.",
                segment.getImageCoordinatesRepresentation().toString()));
        if (segment.getImageCoordinates() != null) {
            set.put(new StringProperty("igeolo",
                    "Image Geographic Location",
                    "The approximate geographic location for the image. It is not intended for analytical purposes.",
                    String.format("[%s], [%s], [%s], [%s]",
                            segment.getImageCoordinates().getCoordinate00().getSourceFormat(),
                            segment.getImageCoordinates().getCoordinate0MaxCol().getSourceFormat(),
                            segment.getImageCoordinates().getCoordinateMaxRowMaxCol().getSourceFormat(),
                            segment.getImageCoordinates().getCoordinateMaxRow0().getSourceFormat())));
        }
        for (int i = 1; i <= segment.getImageComments().size(); ++i) {
            set.put(new StringProperty("icom" + i,
                    "Image Comment " + i,
                    "nth Single comment block.",
                    segment.getImageComments().get(i - 1)));
        }
        set.put(new StringProperty("imageCompression",
                "Image Compression",
                "The form of compression used in representing the image data.",
                segment.getImageCompression().toString()));
        set.put(new StringProperty("compressionRate",
                "Compression Rate",
                "The compression rate for the image.",
                segment.getCompressionRate()));
        for (int i = 0; i < segment.getNumBands(); ++i) {
            addImageBandSetToSheet(i, sheet);
        }
        set.put(new StringProperty("imageMode",
                "Image Mode",
                "Indicator for how pixels are stored in the image.",
                segment.getImageMode().toString()));
        set.put(new IntegerProperty("nbpr",
                "Number of Blocks Per Row",
                "The number of image blocks in a row of blocks in the horizontal direction.",
                segment.getNumberOfBlocksPerRow()));
        set.put(new IntegerProperty("nbpc",
                "Number of Blocks Per Column",
                "The number of image blocks in a column of blocks in the vertical direction.",
                segment.getNumberOfBlocksPerColumn()));
        set.put(new IntegerProperty("nppbh",
                "Number of Pixels Per Block Horizontal",
                "The number of pixels horizontally in each block of the image.",
                segment.getNumberOfPixelsPerBlockHorizontal()));
        set.put(new IntegerProperty("nppbv",
                "Number of Pixels Per Block Vertical",
                "The number of pixels vertically in each block of the image.",
                segment.getNumberOfPixelsPerBlockVertical()));
        set.put(new IntegerProperty("nbpp",
                "Number of Bits Per Pixel Per Band",
                "The number of storage bits used for the value from each component of a pixel vector.",
                segment.getNumberOfBitsPerPixelPerBand()));
        set.put(new IntegerProperty("imageDisplayLevel",
                "Image Display Level",
                "The display level of the image relative to other displayed file components in a composite display.",
                segment.getImageDisplayLevel()));
        set.put(new StringProperty("imageLocation",
                "Image Location",
                "The location of the first pixel in the first line in the image, relative to the segment the image is attached to.",
                String.format("[%d, %d]", segment.getImageLocationColumn(), segment.getImageLocationRow())));
        set.put(new StringProperty("imageMagnification",
                "Image Magnification",
                "The magnification or reduction factor of the image relative to the original source image.",
                segment.getImageMagnification()));
        return sheet;
    }

    private void addImageBandSetToSheet(final int bandNumber, final Sheet sheet) {
        NitfImageBand band = segment.getImageBandZeroBase(bandNumber);
        Sheet.Set bandProperties = Sheet.createPropertiesSet();
        bandProperties.setName("imageBand" + bandNumber);
        bandProperties.setDisplayName(String.format("Image Band %d", bandNumber));
        bandProperties.put(new StringProperty("irepband",
                "Image Band Representation",
                "Indicator of the processing required to display this image band.",
                band.getImageRepresentation()));
        bandProperties.put(new StringProperty("isubcat",
                "Image Band Subcategory",
                "The significance of the band with regard to the specific category of the image.",
                band.getSubCategory()));
        bandProperties.put(new IntegerProperty("numluts",
                "Number of Lookup Tables for Image Band",
                "The number of Lookup Tables (LUTs) for this band of the image. LUTs are only alloowed if pixel value type is INT or B.",
                band.getNumLUTs()));
        bandProperties.put(new IntegerProperty("numeluts",
                "Number of Entries in Lookup Tables",
                "The number of entries in Lookup Tables (LUTs) for this band of the image.",
                band.getNumLUTEntries()));
        sheet.put(bandProperties);
    }

}
