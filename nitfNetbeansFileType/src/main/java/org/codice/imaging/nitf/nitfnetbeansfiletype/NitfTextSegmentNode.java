/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import org.codice.imaging.nitf.core.NitfTextSegment;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

class NitfTextSegmentNode extends AbstractSegmentNode {

    private final NitfTextSegment segment;

    public NitfTextSegmentNode(final NitfTextSegment nitfTextSegment) {
        super(Children.LEAF);
        segment = nitfTextSegment;
        setDisplayName("Text Segment: " + segment.getIdentifier());
    }

   @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        addSubSegmentProperties(set, segment);
        set.put(new DateProperty("textDateTime",
                "Text Date and Time",
                "Date and time of this origination of the text.",
                segment.getTextDateTime().toDate()));
        set.put(new StringProperty("textTitle",
                "Text Title",
                "The title of the text item",
                segment.getTextTitle()));
        set.put(new StringProperty("textFormat",
                "Text Format",
                "Three-character code indicating the format of type of text data.",
                segment.getTextFormat().toString()));
        return sheet;
    }
}
