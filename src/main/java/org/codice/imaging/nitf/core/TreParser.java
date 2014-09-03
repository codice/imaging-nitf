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
package org.codice.imaging.nitf.core;

import java.io.InputStream;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.codice.imaging.nitf.core.schema.FieldType;
import org.codice.imaging.nitf.core.schema.IfType;
import org.codice.imaging.nitf.core.schema.LoopType;
import org.codice.imaging.nitf.core.schema.Tres;
import org.codice.imaging.nitf.core.schema.TreType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    Parser for Tagged Registered Extension (TRE) data.
*/
class TreParser {

    private static final Logger LOG = LoggerFactory.getLogger(TreParser.class);

    private Tres tresStructure = null;

    private static final int TAG_LENGTH = 6;
    private static final int TAGLEN_LENGTH = 5;
    private static final String AND_CONDITION = " AND ";
    private static final String UNSUPPORTED_IFTYPE_FORMAT_MESSAGE = "Unsupported format for iftype:";

    private TreCollection treCollection = new TreCollection();

    /**
        Constructor for TRE parser.
        <p>
        This does reasonably complex initialisation, so try to re-use it if possible.

        @throws ParseException if the initialisation fails.
    */
    public TreParser() throws ParseException {
        InputStream is = getClass().getResourceAsStream("/nitf_spec.xml");
        try {
            unmarshal(is);
        } catch (JAXBException ex) {
            LOG.warn("JAXBException parsing TRE XML specification", ex);
            throw new ParseException("Exception while loading TRE XML" + ex.getMessage(), 0);
        }
    }

    private void unmarshal(final InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Tres.class);
        Unmarshaller u = jc.createUnmarshaller();
        tresStructure = (Tres) u.unmarshal(inputStream);
    }

    /**
        Parse the TRE from the current reader.

        @param reader the reader to use.
        @param treLength the length of the TRE.
        @return TRE collection.
        @throws ParseException if the TRE parsing fails (e.g. end of file or TRE that is clearly incorrect.
    */
    public final TreCollection parse(final NitfReader reader, final int treLength) throws ParseException {
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

    private void parseOneTre(final NitfReader reader, final String tag, final int fieldLength) throws ParseException {
        Tre tre = new Tre(tag);

        TreType treType = getTreTypeForTag(tag);
        if (treType == null) {
            tre.setRawData(reader.readBytesRaw(fieldLength));
        } else {
            tre.setPrefix(treType.getMdPrefix());
            TreGroup group = parseTreComponents(treType.getFieldOrLoopOrIf(), reader, tre);
            tre.setEntries(group.getEntries());
        }

        treCollection.add(tre);
    }

    private TreGroup parseTreComponents(final List<Object> components, final NitfReader reader, final TreEntryList parent) throws ParseException {
        TreGroup treGroup = new TreGroup();
        for (Object fieldLoopIf: components) {
            if (fieldLoopIf instanceof  IfType) {
                IfType ifType = (IfType) fieldLoopIf;
                evaluateIfType(ifType, treGroup, reader, parent);
            } else if (fieldLoopIf instanceof LoopType) {
                LoopType loopType = (LoopType) fieldLoopIf;
                evaluateLoopType(loopType, treGroup, parent, reader);
            } else if (fieldLoopIf instanceof FieldType) {
                FieldType field = (FieldType) fieldLoopIf;
                evaluateFieldType(reader, field, parent, treGroup);
            }
        }
        return treGroup;
    }

    private void evaluateFieldType(final NitfReader reader, final FieldType field, final TreEntryList parent, final TreGroup treGroup)
            throws ParseException {
        TreEntry treEntry = parseOneField(reader, field, parent, treGroup);
        if (treEntry != null) {
            treGroup.add(treEntry);
        }
    }

    private void evaluateLoopType(final LoopType loopType, final TreGroup treGroup, final TreEntryList parent, final NitfReader reader)
            throws ParseException {
        int numRepetitions = 0;
        if (loopType.getIterations() != null) {
            numRepetitions = loopType.getIterations().intValue();
        } else if (loopType.getCounter() != null) {
            String repetitionCounter = loopType.getCounter();
            numRepetitions = treGroup.getIntValue(repetitionCounter);
        } else if (loopType.getFormula() != null) {
            numRepetitions = computeFormula(loopType.getFormula(), treGroup);
        } else {
            throw new UnsupportedOperationException("Need to implement other loop type");
        }
        TreEntry treEntry = new TreEntry(loopType.getName(), parent);
        for (int i = 0; i < numRepetitions; ++i) {
            TreGroup subGroup = parseTreComponents(loopType.getFieldOrLoopOrIf(), reader, parent);
            treEntry.addGroup(subGroup);
        }
        treGroup.add(treEntry);
    }

    private void evaluateIfType(final IfType ifType,
                                final TreGroup treGroup,
                                final NitfReader reader,
                                final TreEntryList parent) throws ParseException {
        String condition = ifType.getCond();
        if (evaluateCondition(condition, treGroup)) {
            TreGroup ifGroup = parseTreComponents(ifType.getFieldOrLoopOrIf(), reader, parent);
            treGroup.addAll(ifGroup.getEntries());
        }
    }

    private int computeFormula(final String formula, final TreGroup treGroup) throws ParseException {
        if ("(NPART+1)*(NPART)/2".equals(formula)) {
            return computeAverageNPart(treGroup);
        } else if ("(NUMOPG+1)*(NUMOPG)/2".equals(formula)) {
            return computeAverageNumOrg(treGroup);
        } else if ("NPAR*NPARO".equals(formula)) {
            return computeProductNParNParo(treGroup);
        } else if ("NPLN-1".equals(formula)) {
            return computeNplnMinus(treGroup);
        } else if ("NXPTS*NYPTS".equals(formula)) {
            return computeProductNxptsNypts(treGroup);
        } else {
            // There shouldn't be any others, so hitting this probably indicates a parse error
            throw new UnsupportedOperationException("Implement missing formula:" + formula);
        }
    }

    private int computeAverageNPart(final TreGroup treGroup) throws ParseException {
        int npart = treGroup.getIntValue("NPART");
        return (npart + 1) * (npart) / 2;
    }

    private int computeAverageNumOrg(final TreGroup treGroup) throws ParseException {
        int numopg = treGroup.getIntValue("NUMOPG");
        return (numopg + 1) * (numopg) / 2;
    }
    private int computeProductNParNParo(final TreGroup treGroup) throws ParseException {
        int npar = treGroup.getIntValue("NPAR");
        int nparo = treGroup.getIntValue("NPARO");
        return npar * nparo;
    }

    private int computeNplnMinus(final TreGroup treGroup) throws ParseException {
        int npln = treGroup.getIntValue("NPLN");
        return npln - 1;
    }

    private int computeProductNxptsNypts(final TreGroup treGroup) throws ParseException {
        int nxpts = treGroup.getIntValue("NXPTS");
        int nypts = treGroup.getIntValue("NYPTS");
        return nxpts * nypts;
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
            throw new UnsupportedOperationException(UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
        }
        String actualValue = treGroup.getFieldValue(conditionParts[0]);
        return conditionParts[1].equals(actualValue);
    }

    private boolean evaluateConditionIsNotEqual(final String condition, final TreGroup treGroup) throws ParseException {
        String[] conditionParts = condition.split("!=");
        if (conditionParts.length != 2) {
            // This is an error
            throw new UnsupportedOperationException(UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
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
                return new TreEntry(parent.getName(), fieldValue, parent);
            } else {
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
