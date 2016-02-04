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

/**
    Tagged registered extension (TRE).
*/
class TreImpl extends TreEntryListImpl implements Tre {
    private String prefix = null;
    private byte[] rawData = null;
    private final TreSource mSource;

    /**
     * Construct TRE with specific tag name.
     *
     * @param tag the name for the TRE.
     * @param source the TreSource associated with this TRE
     */
    TreImpl(final String tag, final TreSource source) {
        super(tag);
        mSource = source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setPrefix(final String mdPrefix) {
        prefix = mdPrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getPrefix() {
        return prefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setRawData(final byte[] treDataRaw) {
        rawData = treDataRaw;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final byte[] getRawData() {
        return rawData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TreSource getSource() {
        return mSource;
    }

}
