package org.codice.imaging.nitf.core.common;

import java.time.ZonedDateTime;

/**
 Date / time representation.
 */
public interface DateTime {
    /**
     Return the original source value.
     <p>
     This is the value parsed out of the file, including any whitespace and hyphens. If the
     value was not parsed from the file, it will be null.

     @return the original source value.
     */
    String getSourceString();

    /**
     * Return the value of this object as a ZonedDateTime (in UTC).
     *
     * This is a best effort conversion, based on the information available, which can be incomplete.
     *
     * @return the value of this object as a ZonedDateTime, or null if the date is not valid.
     */
    ZonedDateTime getZonedDateTime();
}
