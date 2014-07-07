/**
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
 **/
package org.codice.nitf.filereader;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.codice.nitf.filereader.schema.FieldType;
import org.codice.nitf.filereader.schema.IfType;
import org.codice.nitf.filereader.schema.LoopType;
import org.codice.nitf.filereader.schema.Tres;
import org.codice.nitf.filereader.schema.TreType;

public class TreParser {

    private Tres tresStructure = null;

    private static final int TAG_LENGTH = 6;
    private static final int TAGLEN_LENGTH = 5;

    private TreCollection treCollection = new TreCollection();

    public TreParser() throws ParseException {
        InputStream is = getClass().getResourceAsStream("/nitf_spec.xml");
        try {
            unmarshal(is);
        } catch (JAXBException e) {
            throw new ParseException("Exception while loading TRE XML" + e.getMessage(), 0);
        }
    }

    private void unmarshal(InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Tres.class);
        Unmarshaller u = jc.createUnmarshaller();
        tresStructure = (Tres)u.unmarshal( inputStream );
    }

    public TreCollection parse(NitfReader reader, int treLength) throws ParseException {
        int bytesRead = 0;
        while (bytesRead < treLength) {
            String tag = reader.readBytes(TAG_LENGTH);
            bytesRead += TAG_LENGTH;
            int fieldLength = reader.readBytesAsInteger(TAGLEN_LENGTH);
            bytesRead += TAGLEN_LENGTH;
            parseOneTre(reader, tag, fieldLength);
            bytesRead += fieldLength;
        }
        return treCollection;
    }

    private void parseOneTre(NitfReader reader, String tag, int fieldLength) throws ParseException {
        TreType treType = getTreTypeForTag(tag);
        if (treType == null) {
            System.out.println(String.format("Unhandled TRE %s, skipping over it", tag));
            reader.skip(fieldLength);
            return;
        }
        Tre tre = new Tre(tag);
        tre.setPrefix(treType.getMdPrefix());
        TreGroup group = parseTreComponents(treType.getFieldOrLoopOrIf(), reader, tre);
        tre.setEntries(group.getEntries());
        treCollection.add(tre);
    }

    private TreGroup parseTreComponents(List<Object> components, NitfReader reader, TreEntryList parent) throws ParseException {
        TreGroup treGroup = new TreGroup();
        for (Object fieldLoopIf: components) {
            if (fieldLoopIf instanceof  IfType) {
                throw new UnsupportedOperationException("Implement IfType support");
            } else if (fieldLoopIf instanceof LoopType) {
                // throw new UnsupportedOperationException("Implement LoopType support");
                LoopType loop = (LoopType) fieldLoopIf;
                int numRepetitions = 0;
                if (loop.getIterations() != null) {
                    numRepetitions = loop.getIterations().intValue();
                } else {
                    String repetitionCounter = loop.getCounter();
                    numRepetitions = treGroup.getIntValue(repetitionCounter);
                }
                TreEntry treEntry = new TreEntry(loop.getName());
                for (int i = 0; i < numRepetitions; ++i) {
                    // System.out.println(String.format("Looping under %s, count %d of %d", treEntry.getName(), i, numRepetitions));
                    TreGroup subGroup = parseTreComponents(loop.getFieldOrLoopOrIf(), reader, parent);
                    treEntry.addGroup(subGroup);
                }
                treGroup.add(treEntry);
            } else if (fieldLoopIf instanceof FieldType) {
                FieldType field = (FieldType) fieldLoopIf;
                TreEntry treEntry = parseOneField(reader, field, parent.getName());
                if (treEntry != null) {
                    treGroup.add(treEntry);
                }
            }
        }
        return treGroup;
    }

    private TreEntry parseOneField(NitfReader reader, FieldType field, String parentName) throws ParseException {
        String fieldKey = field.getName();
        if (fieldKey.isEmpty()) {
            fieldKey = field.getLongname();
        }
        if (fieldKey == null) {
            System.out.println("Null fieldKey, skipping " + field.getLength().intValue());
            reader.skip(field.getLength().intValue());
            return null;
        } else {
            String fieldValue = reader.readBytes(field.getLength().intValue());
            if (fieldKey.isEmpty()) {
                // System.out.println(String.format("parent |%s|%d|%s|", parentName, field.getLength().intValue(), fieldValue.trim()));
                return new TreEntry(parentName, fieldValue);
            } else {
                // System.out.println(String.format("Parsed |%s|%d|%s|", fieldKey, field.getLength().intValue(), fieldValue.trim()));
                return new TreEntry(fieldKey, fieldValue);
            }
        }
    }

    private TreType getTreTypeForTag(String tag) {
        for (TreType treType : tresStructure.getTre()) {
            if (treType.getName().equals(tag)) {
                return treType;
            }
        }
        return null;
    }
}