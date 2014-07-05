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

    private ArrayList<TreListEntry> tresList = new ArrayList<TreListEntry>();

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

    public List<TreListEntry> parse(NitfReader reader, int treLength) throws ParseException {
        int bytesRead = 0;
        while (bytesRead < treLength) {
            String tag = reader.readBytes(TAG_LENGTH);
            bytesRead += TAG_LENGTH;
            int fieldLength = reader.readBytesAsInteger(TAGLEN_LENGTH);
            bytesRead += TAGLEN_LENGTH;
            parseOneTre(reader, tag, fieldLength);
            bytesRead += fieldLength;
        }
        return tresList;
    }

    private void parseOneTre(NitfReader reader, String tag, int fieldLength) throws ParseException {
        TreType treType = getTreTypeForTag(tag);
        if (treType == null) {
            System.out.println(String.format("Unhandled TRE %s, skipping over it", tag));
            reader.skip(fieldLength);
            return;
        }
        Tre tre = new Tre();
        tre.setPrefix(treType.getMdPrefix());
        tre.setName(treType.getName());
        parseTreComponents(treType.getFieldOrLoopOrIf(), reader, tre.getFields(), treType.getName());
        TreListEntry entryForTag = null;
        for (TreListEntry entry : tresList) {
            if (entry.getName().equals(tag)) {
                entryForTag = entry;
                break;
            }
        }
        if (entryForTag == null) {
            entryForTag = new TreListEntry(tag);
            tresList.add(entryForTag);
        }
        entryForTag.add(tre);

    }

    private void parseTreComponents(List<Object> components, NitfReader reader, List<TreField> parent, String fallbackName) throws ParseException {
        for (Object fieldLoopIf: components) {
            if (fieldLoopIf instanceof  IfType) {
                throw new UnsupportedOperationException("Implement IfType support");
            } else if (fieldLoopIf instanceof LoopType) {
                LoopType loop = (LoopType) fieldLoopIf;
                // TODO: check for counter mode vs iteration mode
                TreField treField = new TreField(loop.getName(), null);
                treField.initSubFields();
                if (loop.getIterations() != null) {
                    for (int i = 0; i < loop.getIterations().intValue(); ++i) {
                        // System.out.println(String.format("Looping under %s, iteration %d of %d", loop.getName(), i, loop.getIterations().intValue()));
                        parseTreComponents(loop.getFieldOrLoopOrIf(), reader, treField.getSubFields(), loop.getName());
                    }
                } else {
                    String repetitionCounter = loop.getCounter();
                    for (TreField field : parent) {
                        if (field.getName().equals(repetitionCounter)) {
                            // TODO: factor this out with the loop version above.
                            for (int i = 0; i < Integer.parseInt(field.getFieldValue()); ++i) {
                                // System.out.println(String.format("Looping under %s, count %d of %d", loop.getName(), i, loop.getIterations().intValue()));
                                parseTreComponents(loop.getFieldOrLoopOrIf(), reader, treField.getSubFields(), loop.getName());
                            }
                        }
                    }
                }
                parent.add(treField);
            } else if (fieldLoopIf instanceof FieldType) {
                FieldType field = (FieldType) fieldLoopIf;
                String fieldKey = field.getName();
                if (fieldKey == null) {
                    System.out.println("Null fieldKey, skipping " + field.getLength().intValue());
                    reader.skip(field.getLength().intValue());
                } else {
                    String fieldValue = reader.readBytes(field.getLength().intValue());
                    if (fieldKey.isEmpty()) {
                        System.out.println(String.format("Parsed |%s|%d|%s|", fallbackName, field.getLength().intValue(), fieldValue.trim()));
                        parent.add(new TreField(fallbackName, fieldValue));
                    } else {
                        System.out.println(String.format("Parsed |%s|%d|%s|", fieldKey, field.getLength().intValue(), fieldValue.trim()));
                        parent.add(new TreField(fieldKey, fieldValue));
                    }
                }
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