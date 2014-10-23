/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import org.codice.imaging.nitf.core.Nitf;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

/**
 *
 * @author bradh
 */
public class NitfFileNode extends DataNode {

    private Nitf nitf;

    public NitfFileNode(NitfDataObject nitfDataObj, Children kids, Lookup lookup) {
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
        // TODO: file date time
        set.put(new StringProperty("fileTitle", "File Title", "The title of the file", nitf.getFileTitle()));
        set.put(new StringProperty("fileSecurityClassification",
                                   "File Security Classification",
                                   "The classification level of the entire file.",
                                   nitf.getFileSecurityMetadata().getSecurityClassification().toString()));
        // TODO: rest of the file security elements from FSCLSY through to FSPYS
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
