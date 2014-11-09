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

import org.codice.imaging.nitf.core.FileType;
import org.codice.imaging.nitf.core.Nitf;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

class NitfFileNode extends DataNode {

    private final Nitf nitf;

    public NitfFileNode(final NitfDataObject nitfDataObj, final Children kids, final Lookup lookup) {
        super(nitfDataObj, kids, lookup);
        nitf = nitfDataObj.getNitf();
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        set.put(new StringProperty("fileType", "File Type", "The file profile and version.", nitf.getFileType().toString()));
        set.put(new IntegerProperty("complexityLevel",
                "Complexity Level",
                "The complexity level required to interpret fully all components of the file.",
                nitf.getComplexityLevel()));
        set.put(new StringProperty("standardType", "Standard Type", "Standard type or capability.", nitf.getStandardType()));
        set.put(new StringProperty("originatingStationId",
                "Originating Station ID",
                "The identification code or name of the originating organisation, system, station or product.",
                nitf.getOriginatingStationId()));
        set.put(new DateProperty("fileDateTime",
                "File Date and Time",
                "Date and time of this file's orgination.",
                nitf.getFileDateTime().toDate()));
        set.put(new StringProperty("fileTitle",
                "File Title",
                "The title of the file",
                nitf.getFileTitle()));
        if (nitf.getFileType() != FileType.NITF_TWO_ZERO) {
            set.put(new StringProperty("fileSecurityClassification",
                    "File Security Classification",
                    "The classification level of the entire file.",
                    nitf.getFileSecurityMetadata().getSecurityClassification().toString()));
            set.put(new StringProperty("securityClassificationSystem",
                    "File Security Classification System",
                    "The national or multinational security system used to classify the file content. "
                    + "'XN' indicates NATO security system marking guidance.",
                    nitf.getFileSecurityMetadata().getSecurityClassificationSystem()));
            set.put(new StringProperty("codewords",
                    "File Security Codewords",
                    "Indicator of the security compartments associated with the file.",
                    nitf.getFileSecurityMetadata().getCodewords()));
            set.put(new StringProperty("controlAndHandling",
                    "File Control and Handling",
                    "Additional security control and handling instructions (caveats) associated with the file.",
                    nitf.getFileSecurityMetadata().getControlAndHandling()));
            set.put(new StringProperty("releaseInstructions",
                    "File Releasing Instructions",
                    "List of country and/or multilateral entity codes to which the file content is authorised for release.",
                    nitf.getFileSecurityMetadata().getReleaseInstructions()));
            set.put(new StringProperty("declassificationType",
                    "File Declassification Type",
                    "Type of security declassification or downgrading instructions which apply to the file.",
                    nitf.getFileSecurityMetadata().getDeclassificationType()));
            set.put(new StringProperty("declassificationDate",
                    "File Declassification Date",
                    "Date on which the file is to be declassified (if any).",
                    nitf.getFileSecurityMetadata().getDeclassificationDate()));
            set.put(new StringProperty("declassificationExemption",
                    "File Declassification Exemption",
                    "The reason why the file is exempt from automatic declassification.",
                    nitf.getFileSecurityMetadata().getDeclassificationExemption()));
            set.put(new StringProperty("downgrade",
                    "File Downgrade",
                    "Classification level to which the file is to be downgraded.",
                    nitf.getFileSecurityMetadata().getDowngrade()));
            set.put(new StringProperty("downgradeDate",
                    "File Downgrade Date",
                    "Date on which the file is to be downgraded.",
                    nitf.getFileSecurityMetadata().getDowngradeDate()));
            set.put(new StringProperty("classificationText",
                    "File Classification Text",
                    "Additional information about the file classification to include identification of a declassification or downgrade event.",
                    nitf.getFileSecurityMetadata().getClassificationText()));
            set.put(new StringProperty("classificationAuthorityType",
                    "File Classification Authority Type",
                    "The type of authority used to classify the file.",
                    nitf.getFileSecurityMetadata().getClassificationAuthorityType()));
            set.put(new StringProperty("classificationAuthority",
                    "File Classification Authority",
                    "The classification authority for the file.",
                    nitf.getFileSecurityMetadata().getClassificationAuthority()));
            set.put(new StringProperty("classificationReason",
                    "File Classification Reason",
                    "The reason for classifying the file.",
                    nitf.getFileSecurityMetadata().getClassificationReason()));
            set.put(new StringProperty("securitySourceDate",
                    "File Security Source Date",
                    "The date of the source used to derive classification of the file.",
                    nitf.getFileSecurityMetadata().getSecuritySourceDate()));
            set.put(new StringProperty("securityControlNumber",
                    "File Security Control Number",
                    "The security control number associated with the file.",
                    nitf.getFileSecurityMetadata().getSecurityControlNumber()));
            set.put(new StringProperty("fileCopyNumber",
                    "File Copy Number",
                    "The copy number of the file. If this is all zeros, there is no tracking of numbered file copies.",
                    nitf.getFileSecurityMetadata().getFileCopyNumber()));
            set.put(new StringProperty("fileNumberOfCopies",
                    "File Number of Copies",
                    "The total number of copies of the file. If this is all zeros, there is no tracking of numbered file copies.",
                    nitf.getFileSecurityMetadata().getFileNumberOfCopies()));
        }
        if (nitf.getFileBackgroundColour() != null) {
            set.put(new StringProperty("fileBackgroundColour",
                    "File Background Colour",
                    "The three colour components of the file background.",
                    nitf.getFileBackgroundColour().toString()));
        }
        set.put(new StringProperty("originatorsName",
                "Originator's Name",
                "The name of the operator who originated the file.",
                nitf.getOriginatorsName()));
        set.put(new StringProperty("originatorsPhone",
                "Originator's Phone Number",
                "The phone number of the operator who originated the file.",
                nitf.getOriginatorsPhoneNumber()));
        return sheet;
    }
}
