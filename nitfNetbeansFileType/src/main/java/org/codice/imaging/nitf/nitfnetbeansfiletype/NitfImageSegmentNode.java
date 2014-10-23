/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import org.codice.imaging.nitf.core.NitfImageSegment;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

/**
 *
 * @author bradh
 */
class NitfImageSegmentNode extends AbstractNode {

    private NitfImageSegment segment;

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
        set.put(new StringProperty("identifier", "Segment Identifier", "Identifier for the segment.", segment.getIdentifier()));
        // TODO: IDATIM
        set.put(new StringProperty("targetIdentifier",
                                   "Target Identifier",
                                   "Primary target identifier (BE number and O-suffix, followed by country code",
                                   segment.getImageTargetId().toString()));
        set.put(new StringProperty("imageIdentifier2",
                                   "Image Identifier",
                                   "Can contain the identification of additional information about the image",
                                   segment.getImageIdentifier2()));
        set.put(new StringProperty("imageSecurityClassification",
                                   "Image Security Classification",
                                   "The classification level of the image.",
                                   segment.getSecurityMetadata().getSecurityClassification().toString()));
        // TODO: rest of the file security elements from ISCLSY through to ISCTLN
        set.put(new StringProperty("imageSource", "Image Source", "Description of the source of the image.", segment.getImageSource()));
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
                
        return sheet;
    }
}
