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
import java.time.format.DateTimeFormatter;
import javax.swing.Action;
import javax.swing.tree.TreeModel;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

class NitfFileNode extends DataNode {

    private final NitfHeader nitfHeader;

    public NitfFileNode(final NitfDataObject nitfDataObj, final Children kids, final Lookup lookup) {
        super(nitfDataObj, kids, lookup);
        nitfHeader = nitfDataObj.getNitfHeader();
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        set.put(new StringProperty("fileType", "File Type", "The file profile and version.", nitfHeader.getFileType().toString()));
        set.put(new IntegerProperty("complexityLevel",
                "Complexity Level",
                "The complexity level required to interpret fully all components of the file.",
                nitfHeader.getComplexityLevel()));
        set.put(new StringProperty("standardType", "Standard Type", "Standard type or capability.", nitfHeader.getStandardType()));
        set.put(new StringProperty("originatingStationId",
                "Originating Station ID",
                "The identification code or name of the originating organisation, system, station or product.",
                nitfHeader.getOriginatingStationId()));
        set.put(new StringProperty("fileDateTime",
                "File Date and Time",
                "Date and time of this file's orgination.",
                nitfHeader.getFileDateTime().getZonedDateTime().format(DateTimeFormatter.ISO_DATE_TIME)));
        set.put(new StringProperty("fileTitle",
                "File Title",
                "The title of the file",
                nitfHeader.getFileTitle()));
        set.put(new StringProperty("fileSecurityClassification",
                "File Security Classification",
                "The classification level of the entire file.",
                nitfHeader.getFileSecurityMetadata().getSecurityClassification().toString()));
        set.put(new StringProperty("codewords",
                "File Security Codewords",
                "Indicator of the security compartments associated with the file.",
                nitfHeader.getFileSecurityMetadata().getCodewords()));
        set.put(new StringProperty("controlAndHandling",
                "File Control and Handling",
                "Additional security control and handling instructions (caveats) associated with the file.",
                nitfHeader.getFileSecurityMetadata().getControlAndHandling()));
        set.put(new StringProperty("releaseInstructions",
                "File Releasing Instructions",
                "List of country and/or multilateral entity codes to which the file content is authorised for release.",
                nitfHeader.getFileSecurityMetadata().getReleaseInstructions()));
        set.put(new StringProperty("classificationAuthority",
                    "File Classification Authority",
                    "The classification authority for the file.",
                nitfHeader.getFileSecurityMetadata().getClassificationAuthority()));
        set.put(new StringProperty("securityControlNumber",
                    "File Security Control Number",
                    "The security control number associated with the file.",
                nitfHeader.getFileSecurityMetadata().getSecurityControlNumber()));
        if (nitfHeader.getFileType() == FileType.NITF_TWO_ZERO) {
            set.put(new StringProperty("fileDowngradeDateOrSpecialCase",
                    "File Downgrade Date or Special Case",
                    "The downgrade date or special case for the entire file. The valid values are:\n"
                        + " (1) the calendar date in the format YYMMDD\n"
                        + " (2) the code \"999999\" when the originating agency's determination is required (OADR)\n"
                        + " (3) the code \"999998\" when a specific event determines at what point declassification "
                        + "or downgrading is to take place.",
                    nitfHeader.getFileSecurityMetadata().getDowngradeDateOrSpecialCase()));
            if (nitfHeader.getFileSecurityMetadata().hasDowngradeMagicValue()) {
                set.put(new StringProperty("fileDowngradeEvent",
                        "File Downgrade Event",
                        "The event for downgrade. Only present if the File Downgrade Date or Special Case is 999998",
                        nitfHeader.getFileSecurityMetadata().getDowngradeEvent()));
            }
        } else {
            set.put(new StringProperty("fileSecurityClassification",
                    "File Security Classification",
                    "The classification level of the entire file.",
                    nitfHeader.getFileSecurityMetadata().getSecurityClassification().toString()));
            set.put(new StringProperty("securityClassificationSystem",
                    "File Security Classification System",
                    "The national or multinational security system used to classify the file content. "
                    + "'XN' indicates NATO security system marking guidance.",
                    nitfHeader.getFileSecurityMetadata().getSecurityClassificationSystem()));
            set.put(new StringProperty("declassificationType",
                    "File Declassification Type",
                    "Type of security declassification or downgrading instructions which apply to the file.",
                    nitfHeader.getFileSecurityMetadata().getDeclassificationType()));
            set.put(new StringProperty("declassificationDate",
                    "File Declassification Date",
                    "Date on which the file is to be declassified (if any).",
                    nitfHeader.getFileSecurityMetadata().getDeclassificationDate()));
            set.put(new StringProperty("declassificationExemption",
                    "File Declassification Exemption",
                    "The reason why the file is exempt from automatic declassification.",
                    nitfHeader.getFileSecurityMetadata().getDeclassificationExemption()));
            set.put(new StringProperty("downgrade",
                    "File Downgrade",
                    "Classification level to which the file is to be downgraded.",
                    nitfHeader.getFileSecurityMetadata().getDowngrade()));
            set.put(new StringProperty("downgradeDate",
                    "File Downgrade Date",
                    "Date on which the file is to be downgraded.",
                    nitfHeader.getFileSecurityMetadata().getDowngradeDate()));
            set.put(new StringProperty("classificationText",
                    "File Classification Text",
                    "Additional information about the file classification to include identification of a declassification or downgrade event.",
                    nitfHeader.getFileSecurityMetadata().getClassificationText()));
            set.put(new StringProperty("classificationAuthorityType",
                    "File Classification Authority Type",
                    "The type of authority used to classify the file.",
                    nitfHeader.getFileSecurityMetadata().getClassificationAuthorityType()));
            set.put(new StringProperty("classificationReason",
                    "File Classification Reason",
                    "The reason for classifying the file.",
                    nitfHeader.getFileSecurityMetadata().getClassificationReason()));
            set.put(new StringProperty("securitySourceDate",
                    "File Security Source Date",
                    "The date of the source used to derive classification of the file.",
                    nitfHeader.getFileSecurityMetadata().getSecuritySourceDate()));
        }
        set.put(new StringProperty("fileCopyNumber",
                "File Copy Number",
                "The copy number of the file. If this is all zeros, there is no tracking of numbered file copies.",
                nitfHeader.getFileSecurityMetadata().getFileCopyNumber()));
        set.put(new StringProperty("fileNumberOfCopies",
                "File Number of Copies",
                "The total number of copies of the file. If this is all zeros, there is no tracking of numbered file copies.",
                nitfHeader.getFileSecurityMetadata().getFileNumberOfCopies()));
        if (nitfHeader.getFileBackgroundColour() != null) {
            set.put(new StringProperty("fileBackgroundColour",
                    "File Background Colour",
                    "The three colour components of the file background.",
                    nitfHeader.getFileBackgroundColour().toString()));
        }
        set.put(new StringProperty("originatorsName",
                "Originator's Name",
                "The name of the operator who originated the file.",
                nitfHeader.getOriginatorsName()));
        set.put(new StringProperty("originatorsPhone",
                "Originator's Phone Number",
                "The phone number of the operator who originated the file.",
                nitfHeader.getOriginatorsPhoneNumber()));
        return sheet;
    }

    @Override
    public Action[] getActions(final boolean popup) {
        return combineActions(new HeaderShowTreAction(this), super.getActions(popup));
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

    TreeModel getTreTreeModel() {
        return new TreTreeModel(nitfHeader.getTREsRawStructure());
    }

    Color getBackgroundColour() {
        if (nitfHeader.getFileBackgroundColour() != null) {
            return nitfHeader.getFileBackgroundColour().toColour();
        } else {
            return Color.LIGHT_GRAY;
        }
    }

}
