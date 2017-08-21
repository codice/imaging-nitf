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
 *
 */
package org.codice.imaging.nitf.core.tre;

/**
 * Tagged registered extension (TRE).
 */
public interface Tre extends TreGroup {

    /**
     * Set the metadata prefix format string.
     *
     * @param mdPrefix the metadata prefix.
     */
    void setPrefix(String mdPrefix);

    /**
     * Return the metadata prefix format string.
     *
     * @return the metadata prefix.
     */
    String getPrefix();

    /**
     * Set the raw data for this TRE.
     * <p>
     * This is only used for TREs that we couldn't parse.
     *
     * @param treDataRaw the raw bytes for the TRE.
     */
    void setRawData(byte[] treDataRaw);

    /**
     * Get the raw data for this TRE.
     * <p>
     * This is only used for TREs that we couldn't parse.
     *
     * @return the raw bytes for the TRE.
     */
    byte[] getRawData();

    /**
     * The source for this TRE.
     *
     * In this context, the source is the segment header / subheader that the
     * TRE was read from, or where it should be written to.
     *
     * @return source for the TRE.
     */
    TreSource getSource();

    /**
     * Return the name of the entry list key.
     *
     * @return the entry list key name
     */
    String getName();
}
