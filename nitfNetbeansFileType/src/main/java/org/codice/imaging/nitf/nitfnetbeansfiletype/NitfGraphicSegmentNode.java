/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import org.codice.imaging.nitf.core.NitfGraphicSegment;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;


class NitfGraphicSegmentNode extends AbstractSegmentNode {

    private NitfGraphicSegment segment;

    public NitfGraphicSegmentNode(final NitfGraphicSegment nitfGraphicSegment) {
        super(Children.LEAF);
        segment = nitfGraphicSegment;
        setDisplayName("Graphic Segment: " + segment.getIdentifier());
    }

   @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        addSubSegmentProperties(set, segment);
        set.put(new StringProperty("graphicName", "Graphic Name", "The alphanumeric name for the graphic", segment.getGraphicName()));
        set.put(new IntegerProperty("graphicDisplayLevel",
                                    "Graphic Display Level",
                                    "The display level of the graphic relative to other displayed file components.",
                                    segment.getGraphicDisplayLevel()));
        set.put(new StringProperty("graphicLocation",
                                   "Graphic Location",
                                   "The location of the graphic's origin point relative to the CCS, image or graphic to which it is attached.",
                                   String.format("[%d, %d]", segment.getGraphicLocationColumn(), segment.getGraphicLocationRow())));
        set.put(new StringProperty("firstGraphicBoundLocation",
                                   "First Graphic Bound Location",
                                   "The upper left corner of the bounding box for the CGM graphic, relative to the CCS, image or graphic to which the graphic is attached.",
                                   String.format("[%d, %d]", segment.getBoundingBox1Column(), segment.getBoundingBox1Row())));
        set.put(new StringProperty("secondGraphicBoundLocation",
                                   "Second Graphic Bound Location",
                                   "The lower right corner of the bounding box for the CGM graphic, relative to the CCS, image or graphic to which the graphic is attached.",
                                   String.format("[%d, %d]", segment.getBoundingBox2Column(), segment.getBoundingBox2Row())));
        set.put(new StringProperty("graphicColour",
                                   "Graphic Colour",
                                   "Flag for whether the CGM graphic has any colour ('C') or is monochrome ('M').",
                                   segment.getGraphicColour().toString()));
        return sheet;
    }
}
