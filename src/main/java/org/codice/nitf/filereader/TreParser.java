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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private Tres tres = null;

    private static final int TAG_LENGTH = 6;
    private static final int TAGLEN_LENGTH = 5;

    public TreParser() throws ParseException {
        InputStream is = getClass().getResourceAsStream("/nitf_spec.xml");
        try {
            tres = unmarshal(is);
        } catch (JAXBException e) {
            throw new ParseException("Exception while loading TRE XML" + e.getMessage(), 0);
        }
    }

    private Tres unmarshal(InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Tres.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (Tres)u.unmarshal( inputStream );
    }

    public Map<String, List<Tre>> parse(NitfReader reader, int treLength) throws ParseException {
        Map<String, List<Tre>> tres = new HashMap<String, List<Tre>>();
        int bytesRead = 0;
        while (bytesRead < treLength) {
            String tag = reader.readBytes(TAG_LENGTH);
            bytesRead += TAG_LENGTH;
            int fieldLength = reader.readBytesAsInteger(TAGLEN_LENGTH);
            bytesRead += TAGLEN_LENGTH;
            Tre tre = parseOneTre(reader, tag, fieldLength);
            if (!tres.containsKey(tag)) {
                tres.put(tag, new ArrayList<Tre>());
            }
            tres.get(tag).add(tre);
            bytesRead += fieldLength;
        }
        return tres;
    }

    private Tre parseOneTre(NitfReader reader, String tag, int fieldLength) throws ParseException {
        TreType treType = getTreTypeForTag(tag);
        if (treType == null) {
            System.out.println(String.format("Unhandled TRE %s, skipping over it", tag));
            return null;
        }
        Tre tre = new Tre();
        tre.setPrefix(treType.getMdPrefix());
        tre.setName(treType.getName());
        for (Object fieldLoopIf: treType.getFieldOrLoopOrIf()) {
            if (fieldLoopIf instanceof  IfType) {
                throw new UnsupportedOperationException("Implement IfType support");
            } else if (fieldLoopIf instanceof LoopType) {
                throw new UnsupportedOperationException("Implement LoopType support");
            } else if (fieldLoopIf instanceof FieldType) {
                FieldType field = (FieldType) fieldLoopIf;
                String fieldKey = field.getName();
                if (fieldKey == null) {
                    reader.skip(field.getLength().intValue());
                } else {
                    String fieldValue = reader.readBytes(field.getLength().intValue());
                    tre.add(fieldKey, fieldValue);
                }
            }
        }
        return tre;
    }

    private TreType getTreTypeForTag(String tag) {
        for (TreType treType : tres.getTre()) {
            if (treType.getName().equals(tag)) {
                return treType;
            }
        }
        return null;
    }
}