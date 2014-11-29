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

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
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
    private static final String TRE_XML_LOAD_ERROR_MESSAGE = "Exception while loading TRE XML";

    private static Tres tresStructure = null;

    /**
        Constructor for TRE parser.
        <p>
        This does reasonably complex initialisation, so try to re-use it if possible.

        @throws ParseException if the initialisation fails.
    */
    public TreParser() throws ParseException {
        try (InputStream is = getClass().getResourceAsStream("/nitf_spec.xml")) {
            unmarshal(is);
        } catch (JAXBException ex) {
            LOG.warn("JAXBException parsing TRE XML specification", ex);
            throw new ParseException(TRE_XML_LOAD_ERROR_MESSAGE + ex.getMessage(), 0);
        } catch (IOException ex) {
            LOG.warn("IOException parsing TRE XML specification", ex);
            throw new ParseException(TRE_XML_LOAD_ERROR_MESSAGE + ex.getMessage(), 0);
        }
    }

    private void unmarshal(final InputStream inputStream) throws JAXBException {
        tresStructure = (Tres) getUnmarshaller().unmarshal(inputStream);
    }

    private Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Tres.class);
        return jc.createUnmarshaller();
    }

    void registerAdditionalTREdescriptor(final Source source) throws ParseException {
        try {
            Tres extraTres = (Tres) getUnmarshaller().unmarshal(source);
            tresStructure.getTre().addAll(extraTres.getTre());
        } catch (JAXBException ex) {
            LOG.warn("JAXBException parsing additional TRE XML specification", ex);
            throw new ParseException(TRE_XML_LOAD_ERROR_MESSAGE + ex.getMessage(), 0);
        }
    }

    Tre parseOneTre(final NitfReader reader, final String tag, final int fieldLength) throws ParseException {
        Tre tre = new Tre(tag);
        TreType treType = getTreTypeForTag(tag);
        if (treType == null) {
            tre.setRawData(reader.readBytesRaw(fieldLength));
        } else {
            TreParams parameters = new TreParams();
            tre.setPrefix(treType.getMdPrefix());
            TreGroup group = parseTreComponents(treType.getFieldOrLoopOrIf(), reader, parameters);
            tre.setEntries(group.getEntries());
        }
        return tre;
    }

    private TreGroup parseTreComponents(final List<Object> fieldOrLoopOrIf, final NitfReader reader, final TreParams params) throws ParseException {
        TreGroup group = new TreGroup();
        for (Object fieldLoopIf : fieldOrLoopOrIf) {
            if (fieldLoopIf instanceof FieldType) {
                group.add(parseField((FieldType) fieldLoopIf, reader, params));
            } else if (fieldLoopIf instanceof LoopType) {
                group.add(parseLoop((LoopType) fieldLoopIf, reader, params));
            } else if (fieldLoopIf instanceof IfType) {
                TreGroup ifGroup = parseIf((IfType) fieldLoopIf, reader, params);
                group.addAll(ifGroup);
            } else {
                throw new ParseException("Unhandled fieldLoopIf type parsing problem", 0);
            }
        }
        return group;
    }

    private TreEntry parseField(final FieldType field, final NitfReader reader, final TreParams parameters) throws ParseException {
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
                fieldLength = Integer.parseInt(parameters.getFieldValue(field.getLengthVar()));
            } else {
                throw new ParseException("Unhandled field type parsing issue", 0);
            }
            String fieldValue = reader.readBytes(fieldLength);
            if (fieldKey.isEmpty()) {
                return new TreEntry("no name", fieldValue);
            } else {
                parameters.addParameter(fieldKey, fieldValue);
                return new TreEntry(fieldKey, fieldValue);
            }
        }
    }

    private int computeFormula(final String formula, final TreParams treParas) throws ParseException {
        int result = 0;
        switch (formula) {
            case "(NPART+1)*(NPART)/2":
                result = computeAverageNPart(treParas);
                break;
            case "(NUMOPG+1)*(NUMOPG)/2":
                result = computeAverageNumOrg(treParas);
                break;
            case "NPAR*NPARO":
                result = computeProductNParNParo(treParas);
                break;
            case "NPLN-1":
                result = computeNplnMinus(treParas);
                break;
            case "NXPTS*NYPTS":
                result = computeProductNxptsNypts(treParas);
                break;
            default:
                // There shouldn't be any others, so hitting this probably indicates a parse error
                throw new UnsupportedOperationException("Implement missing formula:" + formula);
        }
        return result;
    }

    private int computeAverageNPart(final TreParams parameters) throws ParseException {
        int npart = parameters.getIntValue("NPART");
        return (npart + 1) * (npart) / 2;
    }

    private int computeAverageNumOrg(final TreParams parameters) throws ParseException {
        int numopg = parameters.getIntValue("NUMOPG");
        return (numopg + 1) * (numopg) / 2;
    }
    private int computeProductNParNParo(final TreParams parameters) throws ParseException {
        int npar = parameters.getIntValue("NPAR");
        int nparo = parameters.getIntValue("NPARO");
        return npar * nparo;
    }

    private int computeNplnMinus(final TreParams parameters) throws ParseException {
        int npln = parameters.getIntValue("NPLN");
        return npln - 1;
    }

    private int computeProductNxptsNypts(final TreParams parameters) throws ParseException {
        int nxpts = parameters.getIntValue("NXPTS");
        int nypts = parameters.getIntValue("NYPTS");
        return nxpts * nypts;
    }

    private TreType getTreTypeForTag(final String tag) {
        for (TreType treType : tresStructure.getTre()) {
            if (treType.getName().equals(tag.trim())) {
                return treType;
            }
        }
        return null;
    }

    private TreEntry parseLoop(final LoopType loopType, final NitfReader reader, final TreParams params) throws ParseException {
        int numRepetitions = 0;
        if (loopType.getIterations() != null) {
            numRepetitions = loopType.getIterations().intValue();
        } else if (loopType.getCounter() != null) {
            String repetitionCounter = loopType.getCounter();
            numRepetitions = params.getIntValue(repetitionCounter);
        } else if (loopType.getFormula() != null) {
            numRepetitions = computeFormula(loopType.getFormula(), params);
        } else {
            throw new UnsupportedOperationException("Need to implement other loop type");
        }
        TreEntry treEntry = new TreEntry(loopType.getName());
        for (int i = 0; i < numRepetitions; ++i) {
            TreGroup subGroup = parseTreComponents(loopType.getFieldOrLoopOrIf(), reader, params);
            treEntry.addGroup(subGroup);
        }
        return treEntry;
    }

    private TreGroup parseIf(final IfType ifType, final NitfReader reader, final TreParams params) throws ParseException {
        String condition = ifType.getCond();
        if (evaluateCondition(condition, params)) {
            return parseTreComponents(ifType.getFieldOrLoopOrIf(), reader, params);
        }
        return null;
    }

    private boolean evaluateCondition(final String condition, final TreParams params) {
        if (condition.contains(NitfConstants.AND_CONDITION)) {
            return evaluateConditionBooleanAnd(condition, params);
        } else if (condition.endsWith("!=")) {
            return evaluateConditionIsNotEmpty(condition, params);
        } else if (condition.contains("!=")) {
            return evaluateConditionIsNotEqual(condition, params);
        } else if (condition.contains("=")) {
            return evaluateConditionIsEqual(condition, params);
        } else {
            throw new UnsupportedOperationException(NitfConstants.UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
        }
    }

    private boolean evaluateConditionBooleanAnd(final String condition, final TreParams parameters) {
        String[] condParts = condition.split(NitfConstants.AND_CONDITION);
        if (condParts.length != 2) {
            // This is an error
            throw new UnsupportedOperationException(NitfConstants.UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
        }
        boolean lhs = evaluateCondition(condParts[0], parameters);
        boolean rhs = evaluateCondition(condParts[1], parameters);
        return lhs && rhs;
    }

    private boolean evaluateConditionIsNotEmpty(final String condition, final TreParams parameters) {
        String conditionPart = condition.substring(0, condition.length() - "!=".length());
        String actualValue = parameters.getFieldValue(conditionPart);
        return !actualValue.trim().isEmpty();
    }

    private boolean evaluateConditionIsNotEqual(final String condition, final TreParams parameters) {
        String[] conditionParts = condition.split("!=");
        if (conditionParts.length != 2) {
            // This is an error
            throw new UnsupportedOperationException(NitfConstants.UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
        }
        String actualValue = parameters.getFieldValue(conditionParts[0]);
        return !conditionParts[1].equals(actualValue);
    }

    private boolean evaluateConditionIsEqual(final String condition, final TreParams params) {
        String[] conditionParts = condition.split("=");
        if (conditionParts.length != 2) {
            // This is an error
            throw new UnsupportedOperationException(NitfConstants.UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
        }
        String actualValue = params.getFieldValue(conditionParts[0]);
        return conditionParts[1].equals(actualValue);
    }

}
