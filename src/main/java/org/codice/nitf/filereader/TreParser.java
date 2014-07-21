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
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;
import javax.xml.bind.JAXBContext;
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
    private static final String AND_CONDITION = " AND ";
    private static final String UNSUPPORTED_IFTYPE_FORMAT_MESSAGE = "Unsupported format for iftype:";

    private TreCollection treCollection = new TreCollection();

    public TreParser() throws ParseException {
        InputStream is = getClass().getResourceAsStream("/nitf_spec.xml");
        try {
            unmarshal(is);
        } catch (JAXBException e) {
            throw new ParseException("Exception while loading TRE XML" + e.getMessage(), 0);
        }
    }

    private void unmarshal(final InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Tres.class);
        Unmarshaller u = jc.createUnmarshaller();
        tresStructure = (Tres) u.unmarshal(inputStream);
    }

    public final TreCollection parse(final NitfReader reader, final int treLength) throws ParseException {
        int bytesRead = 0;
        while (bytesRead < treLength) {
            String tag = reader.readBytes(TAG_LENGTH);
            // System.out.println("reading tag:" + tag + "|");
            bytesRead += TAG_LENGTH;
            int fieldLength = reader.readBytesAsInteger(TAGLEN_LENGTH);
            bytesRead += TAGLEN_LENGTH;
            parseOneTre(reader, tag, fieldLength);
            bytesRead += fieldLength;
        }
        return treCollection;
    }

    private void parseOneTre(final NitfReader reader, final String tag, final int fieldLength) throws ParseException {
        TreType treType = getTreTypeForTag(tag);
        if (treType == null) {
            if ((!tag.startsWith("PIX")) && (!"JITCID".equals(tag))) {
                // We only care about something that we could handle and aren't.
                System.out.println(String.format("Unhandled TRE %s, skipping over it", tag));
            }
            reader.skip(fieldLength);
            return;
        }
        Tre tre = new Tre(tag);
        tre.setPrefix(treType.getMdPrefix());
        TreGroup group = parseTreComponents(treType.getFieldOrLoopOrIf(), reader, tre);
        tre.setEntries(group.getEntries());
        treCollection.add(tre);
    }

    private TreGroup parseTreComponents(final List<Object> components, final NitfReader reader, final TreEntryList parent) throws ParseException {
        TreGroup treGroup = new TreGroup();
        for (Object fieldLoopIf: components) {
            if (fieldLoopIf instanceof  IfType) {
                IfType ifType = (IfType) fieldLoopIf;
                evaluateIfType(ifType, treGroup, reader, parent);
            } else if (fieldLoopIf instanceof LoopType) {
                LoopType loop = (LoopType) fieldLoopIf;
                int numRepetitions = 0;
                if (loop.getIterations() != null) {
                    numRepetitions = loop.getIterations().intValue();
                } else if (loop.getCounter() != null) {
                    String repetitionCounter = loop.getCounter();
                    numRepetitions = treGroup.getIntValue(repetitionCounter);
                } else if (loop.getFormula() != null) {
                    numRepetitions = computeFormula(loop.getFormula(), treGroup);
                } else {
                    throw new UnsupportedOperationException("Need to implement other loop type");
                }
                TreEntry treEntry = new TreEntry(loop.getName(), parent);
                for (int i = 0; i < numRepetitions; ++i) {
                    // System.out.println(String.format("Looping under %s, count %d of %d", treEntry.getName(), i, numRepetitions));
                    TreGroup subGroup = parseTreComponents(loop.getFieldOrLoopOrIf(), reader, parent);
                    treEntry.addGroup(subGroup);
                }
                treGroup.add(treEntry);
            } else if (fieldLoopIf instanceof FieldType) {
                FieldType field = (FieldType) fieldLoopIf;
                TreEntry treEntry = parseOneField(reader, field, parent, treGroup);
                if (treEntry != null) {
                    treGroup.add(treEntry);
                }
            }
        }
        return treGroup;
    }

    private void evaluateIfType(final IfType ifType,
                                final TreGroup treGroup,
                                final NitfReader reader,
                                final TreEntryList parent) throws ParseException {
        String condition = ifType.getCond();
        // System.out.println("condition: " + condition);
        if (evaluateCondition(condition, treGroup)) {
            // System.out.println(String.format("[%s] evaluated to true", condition));
            TreGroup ifGroup = parseTreComponents(ifType.getFieldOrLoopOrIf(), reader, parent);
            treGroup.addAll(ifGroup.getEntries());
        }
    }

    private int computeFormula(final String formula, final TreGroup treGroup) throws ParseException {
        if ("(NPART+1)*(NPART)/2".equals(formula)) {
            int npart = treGroup.getIntValue("NPART");
            return (npart + 1) * (npart) / 2;
        } else if ("(NUMOPG+1)*(NUMOPG)/2".equals(formula)) {
            int numopg = treGroup.getIntValue("NUMOPG");
            return (numopg + 1) * (numopg) / 2;
        } else if ("NPAR*NPARO".equals(formula)) {
            int npar = treGroup.getIntValue("NPAR");
            int nparo = treGroup.getIntValue("NPARO");
            return npar * nparo;
        } else if ("NPLN-1".equals(formula)) {
            int npln = treGroup.getIntValue("NPLN");
            return npln - 1;
        } else if ("NXPTS*NYPTS".equals(formula)) {
            int nxpts = treGroup.getIntValue("NXPTS");
            int nypts = treGroup.getIntValue("NYPTS");
            return nxpts * nypts;
        } else {
            // There shouldn't be any others, so hitting this probably indicates a parse error
            throw new UnsupportedOperationException("Implement missing formula:" + formula);
        }
    }

    private boolean evaluateCondition(final String condition, final TreGroup treGroup) throws ParseException {
        if (condition.contains(AND_CONDITION)) {
            return evaluateConditionBooleanAnd(condition, treGroup);
        } else if (condition.endsWith("!=")) {
            return evaluateConditionIsNotEmpty(condition, treGroup);
        } else if (condition.contains("!=")) {
            return evaluateConditionIsNotEqual(condition, treGroup);
        } else if (condition.contains("=")) {
            return evaluateConditionIsEqual(condition, treGroup);
        } else {
            throw new UnsupportedOperationException(UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
        }
    }

    private boolean evaluateConditionBooleanAnd(final String condition, final TreGroup treGroup) throws ParseException {
        String[] condParts = condition.split(AND_CONDITION);
        if (condParts.length != 2) {
            // This is an error
            throw new UnsupportedOperationException(UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
        }
        boolean lhs = evaluateCondition(condParts[0], treGroup);
        boolean rhs = evaluateCondition(condParts[1], treGroup);
        return lhs && rhs;
    }

    private boolean evaluateConditionIsNotEmpty(final String condition, final TreGroup treGroup) throws ParseException {
        String conditionPart = condition.substring(0, condition.length() - "!=".length());
        String actualValue = treGroup.getFieldValue(conditionPart);
        return !actualValue.trim().isEmpty();
    }

    private boolean evaluateConditionIsEqual(final String condition, final TreGroup treGroup) throws ParseException {
        String[] conditionParts = condition.split("=");
        if (conditionParts.length != 2) {
            // This is an error
            throw new UnsupportedOperationException("Unsupported format for iftype:" + condition);
        }
        // System.out.println(String.format("Condition |%s|%s|", conditionParts[0], conditionParts[1]));
        String actualValue = treGroup.getFieldValue(conditionParts[0]);
        // System.out.println("Comparing: actual=[" + actualValue + "] with conditionCriteria=[" + conditionParts[1] + "]");
        // System.out.println("equality comparison:" + (conditionParts[1].equals(actualValue)));
        return conditionParts[1].equals(actualValue);
    }

    private boolean evaluateConditionIsNotEqual(final String condition, final TreGroup treGroup) throws ParseException {
        String[] conditionParts = condition.split("!=");
        if (conditionParts.length != 2) {
            // This is an error
            throw new UnsupportedOperationException("Unsupported format for iftype:" + condition);
        }
        String actualValue = treGroup.getFieldValue(conditionParts[0]);
        return !conditionParts[1].equals(actualValue);
    }

    private TreEntry parseOneField(final NitfReader reader,
                                   final FieldType field,
                                   final TreEntryList parent,
                                   final TreGroup treGroup) throws ParseException {
        String fieldKey = field.getName();
        if (fieldKey == null) {
            // System.out.println("Null fieldKey, skipping " + field.getLength().intValue());
            reader.skip(field.getLength().intValue());
            return null;
        } else {
            if (fieldKey.isEmpty()) {
                fieldKey = field.getLongname();
            }
            int fieldLength = 0;
            BigInteger fieldLengthBigInt = field.getLength();
            if (fieldLengthBigInt != null) {
                fieldLength = fieldLengthBigInt.intValue();
            } else if (field.getLengthVar() != null) {
                fieldLength = Integer.parseInt(treGroup.getFieldValue(field.getLengthVar()));
            } else {
                throw new ParseException("Unhandled field type parsing issue", 0);
            }
            String fieldValue = reader.readBytes(fieldLength);
            if (fieldKey.isEmpty()) {
                // System.out.println(String.format("parent |%s|%d|%s|", parent.getName(), fieldLength, fieldValue.trim()));
                return new TreEntry(parent.getName(), fieldValue, parent);
            } else {
                // System.out.println(String.format("Parsed |%s|%d|%s|", fieldKey, fieldLength, fieldValue.trim()));
                return new TreEntry(fieldKey, fieldValue, parent);
            }
        }
    }

    private TreType getTreTypeForTag(final String tag) {
        for (TreType treType : tresStructure.getTre()) {
            if (treType.getName().equals(tag.trim())) {
                return treType;
            }
        }
        return null;
    }
}
