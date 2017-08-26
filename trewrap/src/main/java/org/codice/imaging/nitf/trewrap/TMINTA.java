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

import java.time.ZonedDateTime;
import java.util.List;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreGroup;

/**
 * Wrapper for the Time Interval Definition TRE (TMINTA) TRE.
 */
public class TMINTA extends TreWrapper {

    private static final String TAG_NAME = "TMINTA";

    /**
     * Create a TMINTA TRE wrapper from an existing TRE.
     *
     * @param tre the TRE to wrap. Must match the TMINTA tag.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public TMINTA(final Tre tre) throws NitfFormatException {
        super(tre, TAG_NAME);
    }

    @Override
    public final ValidityResult getValidity() throws NitfFormatException {
        ValidityResult validityResult = new ValidityResult();
        return validityResult;
    }

    /**
     * The number of time intervals (NUM_TIME_INT) specified in this TRE.
     *
     * Note that this TRE can occur multiple times. It is possible that some of
     * the time intervals should be ignored.
     *
     * @return the number of time intervals in this TRE.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getNumberOfTimeIntervals() throws NitfFormatException {
        return getValueAsInteger("NUM_TIME_INT");
    }

    /**
     * Get the time interval index (TIME_INTERVAL_INDEXn).
     *
     * From MIE4NITF Version 1.1: TIME_INTERVAL_INDEXn – The index of this time
     * interval. If the value of this field is zero, the data consumer should
     * ignore the data for this time interval in this copy of the TRE. Setting
     * the index to zero may be necessary in cases where the collection system
     * is writing predicted information into NITF files but must end the
     * collection (e.g., a camera is disabled) prematurely. Eliminating the
     * entry for a time interval from this TRE modifies the size of the TRE,
     * which may require rewriting of the entire NITF file.
     *
     * @param i zero-based index (within this TRE) of the time interval
     * information to retrieve
     * @return the time interval index (within the collection) for the specified
     * interval, or 0 if the interval should be ignored.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final int getTimeIntervalIndex(final int i) throws NitfFormatException {
        List<TreGroup> intervals = mTre.getGroupListEntry("TIME INTERVALS").getGroups();
        TreGroup interval = intervals.get(i);
        return interval.getIntValue("TIME_INTERVAL_INDEX");
    }

    /**
     * Get the start timestamp (START_TIMESTAMPn).
     *
     * From MIE4NITF Version 1.1: START_TIMESTAMPn The start time of this time
     * interval. Spaces filled (24 BCS-A spaces) if the time interval index is
     * set to 0.
     *
     * @param i zero-based index (within this TRE) of the time interval
     * information to retrieve
     * @return start timestamp for the specified interval, or null if the
     * timestamp value did not represent a valid timestamp (e.g. was space
     * filled).
     * @throws NitfFormatException if there was a problem retrieving the field
     * value.
     */
    public final ZonedDateTime getStartTimeStamp(final int i) throws NitfFormatException {
        return getTimestamp(i, "START_TIMESTAMP");
    }

    /**
     * Get the end timestamp (END_TIMESTAMPn).
     *
     * From MIE4NITF Version 1.1: END_TIMESTAMPn – The end time of this time
     * interval. Spaces filled (24 BCS-A spaces) if the time interval index is
     * set to 0. The end timestamp is an exclusive limit to the range; for any
     * time interval n, the end timestamp must be greater than the start
     * timestamp of the last frame for any camera in the collection and not
     * greater than the start time of the first frame for any camera in the
     * collection in time interval n+1.
     *
     * @param i zero-based index (within this TRE) of the time interval
     * information to retrieve
     * @return end timestamp for the specified interval, or null if the
     * timestamp value did not represent a valid timestamp (e.g. was space
     * filled).
     * @throws NitfFormatException if there was a problem retrieving the field
     * value.
     */
    public final ZonedDateTime getEndTimeStamp(final int i) throws NitfFormatException {
        return getTimestamp(i, "END_TIMESTAMP");
    }

    private ZonedDateTime getTimestamp(final int i, final String fieldLabel) throws NitfFormatException {
        List<TreGroup> intervals = mTre.getGroupListEntry("TIME INTERVALS").getGroups();
        TreGroup interval = intervals.get(i);
        String startTimeStamp = interval.getFieldValue(fieldLabel);
        return parseAsZonedDateTime(TIMESTAMP_NANO_FORMATTER, startTimeStamp);
    }
}
