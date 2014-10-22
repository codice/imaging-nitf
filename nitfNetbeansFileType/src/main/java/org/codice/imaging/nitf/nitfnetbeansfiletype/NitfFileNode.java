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
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        set.put(new StringProperty("fileType", "File Type", "The file profile and version.", nitf.getFileType().toString()));
        set.put(new IntegerProperty("complexityLevel",
                                    "Complexity Level",
                                    "The complexity level required to interpret fully all components of the file.",
                                    nitf.getComplexityLevel()));
        set.put(new StringProperty("standardType", "Standard Type", "Standard type or capability.", nitf.getStandardType()));
        return sheet;
    }
}
