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

import java.awt.Color;

import javax.swing.Action;

import org.codice.imaging.nitf.core.common.CommonSegment;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;

abstract class AbstractCommonSegmentNode extends AbstractNode {

    public AbstractCommonSegmentNode(final Children children) {
        super(children);
    }


    protected void addCommonSegmentProperties(final Sheet.Set set, final CommonSegment segment) {
        set.put(new StringProperty("identifier", "Segment Identifier", "Identifier for the segment.", segment.getIdentifier()));
        if (segment.getSecurityMetadata().getSecurityClassification() != null) {
            set.put(new StringProperty("securityClassification",
                    "Security Classification",
                    "The classification level of the segment.",
                    segment.getSecurityMetadata().getSecurityClassification().toString()));
        }
        if (segment.getSecurityMetadata().getSecurityClassificationSystem() != null) {
            set.put(new StringProperty("securityClassificationSystem",
                    "Security Classification System",
                    "The national or multinational security system used to classify the segment content. "
                            + "'XN' indicates NATO security system marking guidance.",
                    segment.getSecurityMetadata().getSecurityClassificationSystem()));
        }
        if (segment.getSecurityMetadata().getCodewords() != null) {
            set.put(new StringProperty("codewords",
                    "Security Codewords",
                    "Indicator of the security compartments associated with the segment.",
                    segment.getSecurityMetadata().getCodewords()));
        }
        if (segment.getSecurityMetadata().getControlAndHandling() != null) {
            set.put(new StringProperty("controlAndHandling",
                    "Control and Handling",
                    "Additional security control and handling instructions (caveats) associated with the segment.",
                    segment.getSecurityMetadata().getControlAndHandling()));
        }
        if (segment.getSecurityMetadata().getReleaseInstructions() != null) {
            set.put(new StringProperty("releaseInstructions",
                    "Releasing Instructions",
                    "List of country and/or multilateral entity codes to which the segment content is authorised for release.",
                    segment.getSecurityMetadata().getReleaseInstructions()));
        }
        if (segment.getSecurityMetadata().getDeclassificationType() != null) {
            set.put(new StringProperty("declassificationType",
                    "Declassification Type",
                    "Type of security declassification or downgrading instructions which apply to the segment.",
                    segment.getSecurityMetadata().getDeclassificationType()));
        }
        if (segment.getSecurityMetadata().getDeclassificationDate() != null) {
            set.put(new StringProperty("declassificationDate",
                    "Declassification Date",
                    "Date on which the segment is to be declassified (if any).",
                    segment.getSecurityMetadata().getDeclassificationDate()));
        }
        if (segment.getSecurityMetadata().getDeclassificationExemption() != null) {
            set.put(new StringProperty("declassificationExemption",
                    "Declassification Exemption",
                    "The reason why the segment is exempt from automatic declassification.",
                    segment.getSecurityMetadata().getDeclassificationExemption()));
        }
        if (segment.getSecurityMetadata().getDowngrade() != null) {
            set.put(new StringProperty("downgrade",
                    "Downgrade",
                    "Classification level to which the segment is to be downgraded.",
                    segment.getSecurityMetadata().getDowngrade()));
        }
        if (segment.getSecurityMetadata().getDowngradeDate() != null) {
            set.put(new StringProperty("downgradeDate",
                    "Downgrade Date",
                    "Date on which the segment is to be downgraded.",
                    segment.getSecurityMetadata().getDowngradeDate()));
        }
        if (segment.getSecurityMetadata().getClassificationText() != null) {
            set.put(new StringProperty("classificationText",
                    "Classification Text",
                    "Additional information about the segment classification to include identification of a declassification or downgrade event.",
                    segment.getSecurityMetadata().getClassificationText()));
        }
        if (segment.getSecurityMetadata().getClassificationAuthorityType() != null) {
            set.put(new StringProperty("classificationAuthorityType",
                    "Classification Authority Type",
                    "The type of authority used to classify the segment.",
                    segment.getSecurityMetadata().getClassificationAuthorityType()));
        }
        if (segment.getSecurityMetadata().getClassificationAuthority() != null) {
            set.put(new StringProperty("classificationAuthority",
                    "Classification Authority",
                    "The classification authority for the segment.",
                    segment.getSecurityMetadata().getClassificationAuthority()));
        }
        if (segment.getSecurityMetadata().getClassificationReason() != null) {
            set.put(new StringProperty("classificationReason",
                    "Classification Reason",
                    "The reason for classifying the segment.",
                    segment.getSecurityMetadata().getClassificationReason()));
        }
        if (segment.getSecurityMetadata().getSecuritySourceDate() != null) {
        set.put(new StringProperty("securitySourceDate",
                "Security Source Date",
                "The date of the source used to derive classification of the segment.",
                segment.getSecurityMetadata().getSecuritySourceDate()));
        }
        if (segment.getSecurityMetadata().getSecurityControlNumber() != null) {
            set.put(new StringProperty("securityControlNumber",
                    "Security Control Number",
                    "The security control number associated with the segment.",
                    segment.getSecurityMetadata().getSecurityControlNumber()));
        }
        if (segment.getSecurityMetadata().getDowngradeDateOrSpecialCase() != null) {
            set.put(new StringProperty("fileDowngradeDateOrSpecialCase",
                    "File Downgrade Date or Special Case",
                    "The downgrade date or special case for the entire file. The valid values are:\n"
                        + " (1) the calendar date in the format YYMMDD\n"
                        + " (2) the code \"999999\" when the originating agency's determination is required (OADR)\n"
                        + " (3) the code \"999998\" when a specific event determines at what point declassification "
                        + "or downgrading is to take place.",
                    segment.getSecurityMetadata().getDowngradeDateOrSpecialCase()));
            if (segment.getSecurityMetadata().hasDowngradeMagicValue()) {
                set.put(new StringProperty("fileDowngradeEvent",
                        "File Downgrade Event",
                        "The event for downgrade. Only present if the File Downgrade Date or Special Case is 999998",
                        segment.getSecurityMetadata().getDowngradeEvent()));
            }
        }
    }

    /**
     * Prepend an action to an existing array of actions.
     *
     * @param action the action to prepend
     * @param actions the existing actions
     * @return combined array of actions
     */
    protected Action[] combineActions(final Action action, final Action[] actions) {
        Action[] combinedActions = new Action[actions.length + 1];
        combinedActions[0] = action;
        for (int i = 1; i < combinedActions.length; ++i) {
            combinedActions[i] = actions[i - 1];
        }
        return combinedActions;
    }

    Color getBackgroundColour() {
        NitfFileNode fileNode = getFileNode();
        return fileNode.getBackgroundColour();
    }

    protected NitfFileNode getFileNode() {
        Node node = getParentNode();
        while (!(node instanceof NitfFileNode)) {
            node = node.getParentNode();
        }
        return (NitfFileNode) node;
    }

}
