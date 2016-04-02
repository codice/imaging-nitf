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
package org.codice.imaging.nitf.core.common;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import static org.codice.imaging.nitf.core.common.CommonConstants.NITF21_DATE_FORMAT;

/**
    Date / time representation.
*/
public class DateTime {

    private String sourceString = null;

    private ZonedDateTime mZonedDateTime = null;

    /**
     * Default constructor.
     *
     * This does not produce a valid NitfDateTime - use set().
     */
    public DateTime() {
    }

    /**
        Set the date / time components.

        @param year the year (including any century part)
        @param month the month (one base, so 1 is January)
        @param day the day of the month
        @param hour the hour of the day
        @param minute the minutes of the hour
        @param second the seconds of the minute
    */
    public final void set(final int year, final int month, final int day, final int hour, final int minute, final int second) {
        if ((month == 0) || (day == 0)) {
            mZonedDateTime = null;
        }
        mZonedDateTime = ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.of("UTC"));
    }

    /**
     * Set the date / time components.
     *
     * This will modify the source string to match, assuming the target is a NITF 2.1 file.
     *
     * @param zdt the ZonedDateTime to set to.
     */
    public final void set(final ZonedDateTime zdt) {
        mZonedDateTime = zdt;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(NITF21_DATE_FORMAT);
        setSourceString(zdt.format(formatter));
    }

    /**
        Set the original source value.
        <p>
        This is the value parsed out of the file, including any whitespace and hyphens.

        @param sourceValue the original source value.
    */
    public final void setSourceString(final String sourceValue) {
        sourceString = sourceValue;
    }

    /**
        Return the original source value.
        <p>
        This is the value parsed out of the file, including any whitespace and hyphens. If the
        value was not parsed from the file, it will be null.

        @return the original source value.
    */
    public final String getSourceString() {
        return sourceString;
    }

    /**
     * Return the value of this object as a ZonedDateTime (in UTC).
     *
     * This is a best effort conversion, based on the information available, which can be incomplete.
     *
     * @return the value of this object as a ZonedDateTime, or null if the date is not valid.
     */
    public final ZonedDateTime getZonedDateTime() {
        return mZonedDateTime;
    }

    /**
     * Create a DateTime instance for the current time.
     *
     * @return initialised NitfDataTime instance.
     */
    public static DateTime getNitfDateTimeForNow() {
        DateTime ndt = new DateTime();
        ndt.set(ZonedDateTime.now(ZoneId.of("UTC")));
        return ndt;
    }

}
