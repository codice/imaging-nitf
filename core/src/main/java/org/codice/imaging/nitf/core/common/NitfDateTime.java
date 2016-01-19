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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
    Date / time representation.
*/
public class NitfDateTime {

    private String sourceString = null;

    private int mYear = 0;
    private int mMonth = 0;
    private int mDay = 0;
    private int mHour = 0;
    private int mMinute = 0;
    private int mSecond = 0;

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
        mYear = year;
        mMonth = month;
        mDay = day;
        mHour = hour;
        mMinute = minute;
        mSecond = second;
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
        Return the value of this object as a Date.
        <p>
        This is a best-effort, based on the information available, which can be incomplete. If
        no other information is available, a default Date will be returned.

        @return the value of this object as a Date.
    */
    public final Date toDate() {
        // TODO: fix implementation, check for validity
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(mYear, mMonth - 1, mDay, mHour, mMinute, mSecond);
        return calendar.getTime();
    }

}
