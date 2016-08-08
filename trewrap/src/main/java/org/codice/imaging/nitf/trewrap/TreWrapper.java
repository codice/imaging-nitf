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
package org.codice.imaging.nitf.trewrap;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreFactory;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Parent class for TRE wrappers.
 */
public abstract class TreWrapper {

    /**
     * Internal constant for date formatting / parsing in four digit year, month, day convention.
     */
    protected static final DateTimeFormatter CENTURY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Internal constant for date formatting / parsing in four digit year, month, day, hours, minutes convention.
     */
    protected static final DateTimeFormatter CENTURY_DATE_TIME_MINUTES_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    /**
     * Internal constant for date formatting / parsing in UTC to nanosecond precision format.
     */
    protected static final DateTimeFormatter TIMESTAMP_NANO_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.nnnnnnnnn");

    /**
     * The TRE that is being wrapped.
     *
     * This holds the state / data.
     */
    protected Tre mTre;

    private String mTag;

    /**
     * Construct a new TRE wrapper around an existing TRE.
     * @param tre the existing TRE
     * @param tag the tag that this TRE wrapper is meant to be for.
     * @throws NitfFormatException if there is a parsing issue
     */
    protected TreWrapper(final Tre tre, final String tag) throws NitfFormatException {
        mTre = tre;
        mTag = tag;
        verifyTreName();
    }

    /**
     * Construct a new TRE wrapper of the specified type.
     * @param tag the tag for the TRE wrapper to construct.
     * @param treSource the source location that this TRE is for
     * @throws NitfFormatException if there is a parsing issue.
     */
    protected TreWrapper(final String tag, final TreSource treSource) throws NitfFormatException {
        mTre = TreFactory.getDefault(tag, treSource);
        mTag = tag;
    }

    private void verifyTreName() throws IllegalStateException {
        if (!mTre.getName().equals(mTag)) {
            throw new IllegalStateException(String.format("Incorrect TRE name for %s wrapper", mTag));
        }
    }

    /**
     * Retrieve the specified value without any trailing spaces.
     *
     * @param fieldName the name of the field value to retrieve.
     * @return the value for the TRE entry.
     * @throws NitfFormatException if the field was not found or a parsing issue occurs.
     */
    protected final String getValueAsTrimmedString(final String fieldName) throws NitfFormatException {
        return getFieldValue(fieldName).trim();
    }

    /**
     * Retrieve the specified value as an integer field.
     *
     * @param fieldName the name of the field value to retrieve.
     * @return the value for the TRE entry.
     * @throws NitfFormatException if the field was not found or a parsing issue occurs.
     */
    protected final int getValueAsInteger(final String fieldName) throws NitfFormatException {
        return mTre.getIntValue(fieldName);
    }

    /**
     * Retrieve the specified value as a long integer field.
     *
     * @param fieldName the name of the field value to retrieve.
     * @return the value for the TRE entry.
     * @throws NitfFormatException if the field was not found or a parsing issue occurs.
     */
    protected final long getValueAsLongInteger(final String fieldName) throws NitfFormatException {
        return mTre.getLongValue(fieldName);
    }

    /**
     * Retrieve the specified value as an arbitrary precision (BigInteger) field.
     *
     * @param fieldName the name of the field value to retrieve.
     * @return the value for the TRE entry.
     * @throws NitfFormatException if the field was not found or a parsing issue occurs.
     */
    protected final BigInteger getValueAsBigInteger(final String fieldName) throws NitfFormatException {
        return mTre.getBigIntegerValue(fieldName);
    }

    /**
     * Retrieve the specified value as LocalDate.
     *
     * @param fieldName the name of the field value to retrieve.
     *
     * @return the value for the TRE entry, or null if the field did not represent a valid date.
     * @throws NitfFormatException if he field was not found or a parsing issue occurs.
     */
    protected final LocalDate getValueAsLocalDate(final String fieldName) throws NitfFormatException {
        String dob = mTre.getFieldValue(fieldName);
        try {
            LocalDate dt = LocalDate.parse(dob, CENTURY_DATE_FORMATTER);
            return dt;
        } catch (DateTimeParseException ex) {
            // TODO: log?
            return null;
        }
    }

    /**
     * Retrieve the specified value as ZonedDateTime.
     *
     * @param fieldName the name of the field value to retrieve.
     * @param formatter the formatter to use for the field
     *
     * @return the value for the TRE entry, or null if the field did not represent a valid date.
     * @throws NitfFormatException if he field was not found or a parsing issue occurs.
     */
    protected final ZonedDateTime getValueAsZonedDateTime(final String fieldName, final DateTimeFormatter formatter) throws NitfFormatException {
        String dateTimeString = mTre.getFieldValue(fieldName);
        try {
            DateTimeFormatter dtf = formatter.withZone(ZoneId.of("UTC"));
            ZonedDateTime zdt = ZonedDateTime.parse(dateTimeString, dtf);
            return zdt;
        } catch (DateTimeParseException ex) {
            // TODO: log?
            return null;
        }
    }

    /**
     * Retrieve the specified value.
     *
     * @param fieldName the name of the field value to retrieve.
     * @return the value for the TRE entry.
     * @throws NitfFormatException if the field was not found or a parsing issue occurs.
     */
    public final String getFieldValue(final String fieldName) throws NitfFormatException {
        return mTre.getFieldValue(fieldName);
    }

    /**
     * Serialise out the TRE to a byte array.
     *
     * Note that the tag and length will not be included.
     * @return byte array containing serialised data.
     *
     * @throws NitfFormatException if there is a parsing or serialisation problem.
     */
    public final byte[] serialize() throws NitfFormatException {
        TreParser parser = new TreParser();
        return parser.serializeTRE(mTre);
    }

    /**
     * Check if the TRE structure looks (basically) valid.
     *
     * There are cases where the validity cannot be checked (e.g. valid values
     * are not public), in which case this will return valid unless there is
     * something else wrong.
     *
     * @return ValidityResult representing validity of this TRE
     * @throws NitfFormatException if there is a parsing problem
     */
    public abstract ValidityResult getValidity() throws NitfFormatException;

}
