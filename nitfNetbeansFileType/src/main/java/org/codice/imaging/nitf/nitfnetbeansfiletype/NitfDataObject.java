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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import org.codice.imaging.nitf.core.common.FileReader;
import org.codice.imaging.nitf.core.header.NitfFileParser;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Messages({
    "LBL_Nitf_LOADER=Files of NitfFileHeader"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_Nitf_LOADER",
        mimeType = "image/nitf",
        extension = {"ntf", "NTF", "nsf", "NSF", "hr1", "hr2", "hr3", "hr4", "hr5", "hr6", "hr7", "hr8" }
)
// CSOFF: MagicNumber
@DataObject.Registration(
        mimeType = "image/nitf",
        iconBase = "org/codice/imaging/nitf/nitfnetbeansfiletype/world-16.png",
        displayName = "#LBL_Nitf_LOADER",
        position = 300
)
@ActionReferences({
    @ActionReference(
            path = "Loaders/image/nitf/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200
    ),
    @ActionReference(
            path = "Loaders/image/nitf/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300
    ),
    @ActionReference(
            path = "Loaders/image/nitf/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500
    ),
    @ActionReference(
            path = "Loaders/image/nitf/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600
    ),
    @ActionReference(
            path = "Loaders/image/nitf/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800
    ),
    @ActionReference(
            path = "Loaders/image/nitf/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000
    ),
    @ActionReference(
            path = "Loaders/image/nitf/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200
    ),
    @ActionReference(
            path = "Loaders/image/nitf/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300
    ),
    @ActionReference(
            path = "Loaders/image/nitf/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400
    )
})
// CSON: MagicNumber
class NitfDataObject extends MultiDataObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiDataObject.class);
    private DeferredSegmentParseStrategy parseStrategy;

    public NitfDataObject(final FileObject pf, final MultiFileLoader loader) throws IOException {
        super(pf, loader);
        registerEditor("image/nitf", false);
        FileObject fObj = getPrimaryFile();

        try {
            NitfReader reader = new FileReader(new File(fObj.getPath()));
            parseStrategy = new DeferredSegmentParseStrategy();
            NitfFileParser.parse(reader, parseStrategy);
        } catch (ParseException e) {
            LOGGER.error("NitfDataObject Exception:", e);
            throw new IOException(e);
        }
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @Override
    protected Node createNodeDelegate() {
        return new NitfFileNode(this, Children.create(new NitfChildFactory(parseStrategy), true), getLookup());
    }

    NitfHeader getNitfHeader() {
        return parseStrategy.getNitfHeader();
    }

}
