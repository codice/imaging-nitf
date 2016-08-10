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
package org.codice.imaging.nitf.core.tre;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfReader;
import org.codice.imaging.nitf.core.common.TaggedRecordExtensionHandler;
import org.codice.imaging.nitf.core.schema.FieldType;
import org.codice.imaging.nitf.core.schema.IfType;
import org.codice.imaging.nitf.core.schema.LoopType;
import org.codice.imaging.nitf.core.schema.TreType;
import org.codice.imaging.nitf.core.schema.Tres;
import static org.codice.imaging.nitf.core.tre.TreConstants.AND_CONDITION;
import static org.codice.imaging.nitf.core.tre.TreConstants.TAGLEN_LENGTH;
import static org.codice.imaging.nitf.core.tre.TreConstants.TAG_LENGTH;
import static org.codice.imaging.nitf.core.tre.TreConstants.UNSUPPORTED_IFTYPE_FORMAT_MESSAGE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    Parser for Tagged Registered Extension (TRE) data.
*/
public class TreParser {

    private static final Logger LOG = LoggerFactory.getLogger(TreParser.class);

    private static final String TRE_XML_LOAD_ERROR_MESSAGE = "Exception while loading TRE XML";

    private static Tres tresStructure = null;

    /**
        Constructor for TRE parser.
        <p>
        This does reasonably complex initialisation, so try to re-use it if possible.

        @throws NitfFormatException if the initialisation fails.
    */
    public TreParser() throws NitfFormatException {
        try (InputStream is = getClass().getResourceAsStream("/nitf_spec.xml")) {
            unmarshal(is);
        } catch (JAXBException ex) {
            LOG.warn("JAXBException parsing TRE XML specification", ex);
            throw new NitfFormatException(TRE_XML_LOAD_ERROR_MESSAGE + ex.getMessage());
        } catch (IOException ex) {
            LOG.warn("IOException parsing TRE XML specification", ex);
            throw new NitfFormatException(TRE_XML_LOAD_ERROR_MESSAGE + ex.getMessage());
        }
    }

    private void unmarshal(final InputStream inputStream) throws JAXBException {
        tresStructure = (Tres) getUnmarshaller().unmarshal(inputStream);
    }

    private Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Tres.class);
        return jc.createUnmarshaller();
    }

    /**
     * Add one or more TRE descriptor to the existing descriptor set.
     *
     * @param source the Source to read the TRE descriptors from
     * @throws NitfFormatException if parsing fails (typically invalid descriptors)
     */
    public final void registerAdditionalTREdescriptor(final Source source) throws NitfFormatException {
        try {
            Tres extraTres = (Tres) getUnmarshaller().unmarshal(source);
            tresStructure.getTre().addAll(extraTres.getTre());
        } catch (JAXBException ex) {
            LOG.warn("JAXBException parsing additional TRE XML specification", ex);
            throw new NitfFormatException(TRE_XML_LOAD_ERROR_MESSAGE + ex.getMessage());
        }
    }

    final Tre parseOneTre(final NitfReader reader, final String tag, final int fieldLength, final TreSource source) throws NitfFormatException {
        Tre tre = new TreImpl(tag, source);
        TreType treType = getTreTypeForTag(tag);
        if (treType == null) {
            tre.setRawData(reader.readBytesRaw(fieldLength));
        } else {
            TreParams parameters = new TreParams();
            tre.setPrefix(treType.getMdPrefix());
            TreGroupImpl group = parseTreComponents(treType.getFieldOrLoopOrIf(),
                    reader, parameters);
            tre.setEntries(group.getEntries());
        }
        return tre;
    }

    private TreGroupImpl parseTreComponents(final List<Object> fieldOrLoopOrIf,
            final NitfReader reader, final TreParams params) throws NitfFormatException {
        TreGroupImpl group = new TreGroupImpl();
        for (Object fieldLoopIf : fieldOrLoopOrIf) {
            if (fieldLoopIf instanceof FieldType) {
                group.add(parseField((FieldType) fieldLoopIf, reader, params));
            } else if (fieldLoopIf instanceof LoopType) {
                group.add(parseLoop((LoopType) fieldLoopIf, reader, params));
            } else if (fieldLoopIf instanceof IfType) {
                TreGroupImpl ifGroup = parseIf((IfType) fieldLoopIf, reader, params);
                group.addAll(ifGroup);
            } else {
                throw new NitfFormatException("Unhandled fieldLoopIf type parsing problem");
            }
        }
        return group;
    }

    private TreEntry parseField(final FieldType field, final NitfReader reader,
            final TreParams parameters) throws NitfFormatException {
        String fieldKey = field.getName();
        String fieldType = field.getType();
        if (fieldKey == null) {
            reader.skip(getLengthForField(field, parameters));
            return null;
        } else {
            if (fieldKey.isEmpty()) {
                fieldKey = field.getLongname();
            }
            int bytesToRead = getLengthForField(field, parameters);
            String fieldValue = reader.readBytes(bytesToRead);
            if (fieldKey.isEmpty()) {
                return new TreEntry("no name", fieldValue, fieldType);
            } else {
                parameters.addParameter(fieldKey, fieldValue, fieldType);
                return new TreEntry(fieldKey, fieldValue, fieldType);
            }
        }
    }

    private int getLengthForField(final FieldType field, final TreParams parameters) throws UnsupportedOperationException, NumberFormatException {
        int fieldLength = 0;
        BigInteger fieldLengthBigInt = field.getLength();
        if (fieldLengthBigInt != null) {
            fieldLength = fieldLengthBigInt.intValue();
        } else if (field.getLengthVar() != null) {
            fieldLength = parameters.getIntValue(field.getLengthVar());
        } else {
            throw new UnsupportedOperationException("Unhandled field type parsing issue");
        }
        return fieldLength;
    }

    private int computeFormula(final String formula, final TreParams treParas) throws NitfFormatException {
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

    private int computeAverageNPart(final TreParams parameters) throws NitfFormatException {
        int npart = parameters.getIntValue("NPART");
        return (npart + 1) * (npart) / 2;
    }

    private int computeAverageNumOrg(final TreParams parameters) throws NitfFormatException {
        int numopg = parameters.getIntValue("NUMOPG");
        return (numopg + 1) * (numopg) / 2;
    }

    private int computeProductNParNParo(final TreParams parameters) throws NitfFormatException {
        int npar = parameters.getIntValue("NPAR");
        int nparo = parameters.getIntValue("NPARO");
        return npar * nparo;
    }

    private int computeNplnMinus(final TreParams parameters) throws NitfFormatException {
        int npln = parameters.getIntValue("NPLN");
        return npln - 1;
    }

    private int computeProductNxptsNypts(final TreParams parameters) throws NitfFormatException {
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

    private TreEntry parseLoop(final LoopType loopType, final NitfReader reader, final TreParams params) throws NitfFormatException {
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
            TreGroupImpl subGroup = parseTreComponents(loopType.getFieldOrLoopOrIf(),
                    reader, params);
            treEntry.addGroup(subGroup);
        }
        return treEntry;
    }

    private TreGroupImpl parseIf(final IfType ifType, final NitfReader reader, final TreParams params) throws NitfFormatException {
        String condition = ifType.getCond();
        if (evaluateCondition(condition, params)) {
            return parseTreComponents(ifType.getFieldOrLoopOrIf(), reader, params);
        }
        return null;
    }

    private boolean evaluateCondition(final String condition, final TreParams params) {
        if (condition.contains(AND_CONDITION)) {
            return evaluateConditionBooleanAnd(condition, params);
        } else if (condition.endsWith("!=")) {
            return evaluateConditionIsNotEmpty(condition, params);
        } else if (condition.contains("!=")) {
            return evaluateConditionIsNotEqual(condition, params);
        } else if (condition.contains("=")) {
            return evaluateConditionIsEqual(condition, params);
        } else {
            throw new UnsupportedOperationException(UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
        }
    }

    private boolean evaluateConditionBooleanAnd(final String condition, final TreParams parameters) {
        String[] condParts = condition.split(AND_CONDITION);
        if (condParts.length != 2) {
            // This is an error
            throw new UnsupportedOperationException(UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
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
            throw new UnsupportedOperationException(UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
        }
        String actualValue = parameters.getFieldValue(conditionParts[0]);
        return !conditionParts[1].equals(actualValue);
    }

    private boolean evaluateConditionIsEqual(final String condition, final TreParams params) {
        String[] conditionParts = condition.split("=");
        if (conditionParts.length != 2) {
            // This is an error
            throw new UnsupportedOperationException(UNSUPPORTED_IFTYPE_FORMAT_MESSAGE + condition);
        }
        String actualValue = params.getFieldValue(conditionParts[0]);
        return conditionParts[1].equals(actualValue);
    }

    /**
     * Serialise out the TREs for the specified source.
     *
     * @param handler the TRE handler to read TREs from
     * @param source the source (which has to match the header) of the TREs.
     * @return byte array of serialised TREs - may be empty if there are no TREs.
     * @throws NitfFormatException on TRE parsing problem.
     * @throws IOException on reading or writing problems.
     */
    public final byte[] getTREs(final TaggedRecordExtensionHandler handler, final TreSource source) throws NitfFormatException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Tre tre : handler.getTREsRawStructure().getTREsForSource(source)) {
            String name = padStringToLength(tre.getName(), TAG_LENGTH);
            baos.write(name.getBytes(StandardCharsets.UTF_8));
            if (tre.getRawData() != null) {
                String tagLen = padIntegerToLength(tre.getRawData().length, TAGLEN_LENGTH);
                baos.write(tagLen.getBytes(StandardCharsets.UTF_8));
                baos.write(tre.getRawData());
            } else {
                byte[] treData = serializeTRE(tre);
                baos.write(padIntegerToLength(treData.length, TAGLEN_LENGTH).getBytes(StandardCharsets.UTF_8));
                baos.write(treData);
            }
        }
        return baos.toByteArray();
    }

    private String padIntegerToLength(final long number, final int length) {
        return String.format("%0" + length + "d", number);
    }

    private String padStringToLength(final String s, final int length) {
        return String.format("%1$-" + length + "s", s);
    }

    private String padRealToLength(final double number, final int length) {
        return String.format("%" + length + "f", number);
    }

    /**
     * Write out one TRE.
     *
     * @param tre the TRE to write out
     * @return byte array containing serialised TRE.
     * @throws NitfFormatException if TRE serialisation fails.
     */
    public final byte[] serializeTRE(final Tre tre) throws NitfFormatException {
        TreType treType = getTreTypeForTag(tre.getName());
        checkTreLocationMatchesTreSource(treType.getLocation(), tre.getSource());
        TreParams parameters = new TreParams();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        serializeFieldOrLoopOrIf(treType.getFieldOrLoopOrIf(), tre, output, parameters);
        return output.toByteArray();
    }

    private void serializeFieldOrLoopOrIf(final List<Object> fieldOrLoopOrIf,
            final TreGroup treGroup,
            final ByteArrayOutputStream baos,
            final TreParams params) throws NitfFormatException {
        try {
            for (Object fieldLoopIf : fieldOrLoopOrIf) {
                if (fieldLoopIf instanceof FieldType) {
                    byte[] field = getFieldValue((FieldType) fieldLoopIf, treGroup, params);
                    baos.write(field);
                } else if (fieldLoopIf instanceof LoopType) {
                    LoopType loopType = (LoopType) fieldLoopIf;
                    TreEntry loopDataEntry = treGroup.getEntry(loopType.getName());
                    for (TreGroup subGroup : loopDataEntry.getGroups()) {
                        serializeFieldOrLoopOrIf(loopType.getFieldOrLoopOrIf(), subGroup, baos, params);
                    }
                } else if (fieldLoopIf instanceof IfType) {
                    IfType ifType = (IfType) fieldLoopIf;
                    if (evaluateCondition(ifType.getCond(), params)) {
                        serializeFieldOrLoopOrIf(ifType.getFieldOrLoopOrIf(), treGroup, baos, params);
                    }
                } else {
                    throw new NitfFormatException("Unexpected TRE structure type");
                }
            }
        } catch (IOException ex) {
            throw new NitfFormatException("Failed to write TRE:" + ex.getMessage());
        }
    }

    private byte[] getFieldValue(final FieldType fieldType, final TreGroup treGroup, final TreParams params) throws NitfFormatException {
        String fieldTypeName = fieldType.getName();
        if ("".equals(fieldTypeName)) {
            fieldTypeName = fieldType.getLongname();
        }
        if (fieldTypeName != null) {
            TreEntry entry = treGroup.getEntry(fieldTypeName);
            String value = getValidatedValue(fieldType, entry, params);
            params.addParameter(fieldTypeName, value, entry.getDataType());
            return value.getBytes(StandardCharsets.UTF_8);
        } else {
            // This is a pad field
            String value = fieldType.getFixedValue();
            return value.getBytes(StandardCharsets.UTF_8);
        }
    }

    private String getValidatedValue(final FieldType fieldType, final TreEntry entry, final TreParams params) throws NitfFormatException {
        String value = entry.getFieldValue();
        if (value == null) {
            throw new NitfFormatException("Cannot serialize null entry for: " + fieldType.getName());
        }
        if ((fieldType.getLength() != null) && (value.length() != fieldType.getLength().intValue())) {
            // Try to pad out to the required length.
            if (fieldType.getType() == null) {
                String err = "Cannot pad unknown data type for " + fieldType.getName();
                LOG.error(err);
                throw new NitfFormatException(err);
            }
            if (fieldType.getType().equals("integer")) {
                value = getValidatedIntegerValue(value, fieldType);
            }

            if (fieldType.getType().equals("string")) {
                value = padStringToLength(value, fieldType.getLength().intValue());
            }

            if (fieldType.getType().equals("real")) {
                value = getValidatedRealValue(value, fieldType);
            }
            if (value.length() != fieldType.getLength().intValue()) {
                throw new NitfFormatException("Incorrect length serialising out: " + fieldType.getName());
            }
        }
        if (fieldType.getLengthVar() != null) {
            String lengthVar = fieldType.getLengthVar();
            int specifiedLength = params.getIntValue(lengthVar);
            if (specifiedLength != value.length()) {
                String err = String.format("Actual length for %s did not match specified length of %d", fieldType.getName(), specifiedLength);
                LOG.error(err);
                throw new NitfFormatException(err);
            }
        }
        return value;
    }

    private String getValidatedIntegerValue(final String value, final FieldType fieldType) throws NitfFormatException {
        String paddedValue;
        try {
            int intValue = Integer.parseInt(value);
            validateIntegerValueRange(intValue, fieldType);
            paddedValue = padIntegerToLength(intValue, fieldType.getLength().intValue());
        } catch (NumberFormatException ex) {
            String err = "Could not parse " + fieldType.getName() + " value " + value + " as a number.";
            LOG.error(err);
            throw new NitfFormatException(err);
        }
        return paddedValue;
    }

    private void validateIntegerValueRange(final int intValue, final FieldType fieldType) throws NitfFormatException {
        if (fieldType.getMinval() != null) {
            int minValue = Integer.parseInt(fieldType.getMinval());
            if (intValue < minValue) {
                throw new NitfFormatException(String.format("Minimum value for %s is %d, got %d", fieldType.getName(), minValue, intValue));
            }
        }
        if (fieldType.getMaxval() != null) {
            int maxValue = Integer.parseInt(fieldType.getMaxval());
            if (intValue > maxValue) {
                throw new NitfFormatException(String.format("Maximum value for %s is %d, got %d", fieldType.getName(), maxValue, intValue));
            }
        }
    }

    private String getValidatedRealValue(final String value, final FieldType fieldType) throws NitfFormatException {
        String paddedValue;
        try {
            double realValue = Double.parseDouble(value);
            validateRealValueRange(realValue, fieldType);
            paddedValue = padRealToLength(realValue, fieldType.getLength().intValue());
        } catch (NumberFormatException ex) {
            String err = "Could not parse " + fieldType.getName() + " value " + value + " as a floating point number.";
            LOG.error(err);
            throw new NitfFormatException(err);
        }
        return paddedValue;
    }

    private void validateRealValueRange(final double realValue, final FieldType fieldType) throws NitfFormatException {
        if (fieldType.getMinval() != null) {
            double minValue = Double.parseDouble(fieldType.getMinval());
            if (realValue < minValue) {
                throw new NitfFormatException(String.format("Minimum value for %s is %f, got %f", fieldType.getName(), minValue, realValue));
            }
        }
        if (fieldType.getMaxval() != null) {
            double maxValue = Double.parseDouble(fieldType.getMaxval());
            if (realValue > maxValue) {
                throw new NitfFormatException(String.format("Maximum value for %s is %f, got %f", fieldType.getName(), maxValue, realValue));
            }
        }
    }

    private void checkTreLocationMatchesTreSource(final String location, final TreSource source) throws NitfFormatException {
        if (location == null) {
            // We don't have a fixed location for this TRE.
            return;
        }
        if (source.equals(TreSource.TreOverflowDES)) {
            // Anything can go into overflow
            return;
        }
        if (location.equalsIgnoreCase("file")) {
            if ((!source.equals(TreSource.UserDefinedHeaderData)) && (!source.equals(TreSource.ExtendedHeaderData))) {
                throw new NitfFormatException("TRE is only permitted in a file-level header, or in an overflow DES");
            }
        } else if (location.equalsIgnoreCase("image")) {
            if ((!source.equals(TreSource.ImageExtendedSubheaderData)) && (!source.equals(TreSource.UserDefinedImageData))) {
                throw new NitfFormatException("TRE is only permitted in an image-related sub-header, or in an overflow DES");
            }
        }
    }

}
