/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import org.codice.imaging.nitf.core.AbstractNitfSubSegment;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;

abstract class AbstractSegmentNode extends AbstractNode {

    public AbstractSegmentNode(final Children children) {
        super(children);
    }

    protected void addSubSegmentProperties(final Sheet.Set set, final AbstractNitfSubSegment segment) {
        set.put(new StringProperty("identifier", "Segment Identifier", "Identifier for the segment.", segment.getIdentifier()));
        set.put(new StringProperty("securityClassification",
                "Security Classification",
                "The classification level of the segment.",
                segment.getSecurityMetadata().getSecurityClassification().toString()));
        set.put(new StringProperty("securityClassificationSystem",
                "Security Classification System",
                "The national or multinational security system used to classify the segment content. "
                + "'XN' indicates NATO security system marking guidance.",
                segment.getSecurityMetadata().getSecurityClassificationSystem()));
        set.put(new StringProperty("codewords",
                "Security Codewords",
                "Indicator of the security compartments associated with the segment.",
                segment.getSecurityMetadata().getCodewords()));
        set.put(new StringProperty("controlAndHandling",
                "Control and Handling",
                "Additional security control and handling instructions (caveats) associated with the segment.",
                segment.getSecurityMetadata().getControlAndHandling()));
        set.put(new StringProperty("releaseInstructions",
                "Releasing Instructions",
                "List of country and/or multilateral entity codes to which the segment content is authorised for release.",
                segment.getSecurityMetadata().getReleaseInstructions()));
        set.put(new StringProperty("declassificationType",
                "Declassification Type",
                "Type of security declassification or downgrading instructions which apply to the segment.",
                segment.getSecurityMetadata().getDeclassificationType()));
        set.put(new StringProperty("declassificationDate",
                "Declassification Date",
                "Date on which the segment is to be declassified (if any).",
                segment.getSecurityMetadata().getDeclassificationDate()));
        set.put(new StringProperty("declassificationExemption",
                "Declassification Exemption",
                "The reason why the segment is exempt from automatic declassification.",
                segment.getSecurityMetadata().getDeclassificationExemption()));
        set.put(new StringProperty("downgrade",
                "Downgrade",
                "Classification level to which the segment is to be downgraded.",
                segment.getSecurityMetadata().getDowngrade()));
        set.put(new StringProperty("downgradeDate",
                "Downgrade Date",
                "Date on which the segment is to be downgraded.",
                segment.getSecurityMetadata().getDowngradeDate()));
        set.put(new StringProperty("classificationText",
                "Classification Text",
                "Additional information about the segment classification to include identification of a declassification or downgrade event.",
                segment.getSecurityMetadata().getClassificationText()));
        set.put(new StringProperty("classificationAuthorityType",
                "Classification Authority Type",
                "The type of authority used to classify the segment.",
                segment.getSecurityMetadata().getClassificationAuthorityType()));
        set.put(new StringProperty("classificationAuthority",
                "Classification Authority",
                "The classification authority for the segment.",
                segment.getSecurityMetadata().getClassificationAuthority()));
        set.put(new StringProperty("classificationReason",
                "Classification Reason",
                "The reason for classifying the segment.",
                segment.getSecurityMetadata().getClassificationReason()));
        set.put(new StringProperty("securitySourceDate",
                "Security Source Date",
                "The date of the source used to derive classification of the segment.",
                segment.getSecurityMetadata().getSecuritySourceDate()));
        set.put(new StringProperty("securityControlNumber",
                "Security Control Number",
                "The security control number associated with the segment.",
                segment.getSecurityMetadata().getSecurityControlNumber()));
        set.put(new IntegerProperty("attachmentLevel",
                "Attachment Level",
                "The attachment level for this segment.",
                segment.getAttachmentLevel()));
    }

}
