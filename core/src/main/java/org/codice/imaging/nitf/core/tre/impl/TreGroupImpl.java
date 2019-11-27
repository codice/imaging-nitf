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
package org.codice.imaging.nitf.core.tre.impl;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    A group of values within a TreEntryImpl.
*/
class TreGroupImpl implements TreGroup {

    private static final Logger LOG = LoggerFactory.getLogger(TreGroupImpl.class);
    private static final int DECIMAL_BASE = 10;

    private List<TreEntry> entries = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<TreEntry> getEntries() {
        return entries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void add(final TreEntry entry) {
        if (entry != null) {
            entries.add(entry);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void addAll(final TreGroup group) {
        if (group != null) {
            entries.addAll(group.getEntries());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setEntries(final List<TreEntry> treEntries) {
        entries = new ArrayList<>();
        entries.addAll(treEntries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final TreEntry getEntry(final String tagName) throws NitfFormatException {
        for (TreEntry entry : entries) {
            if (entry.getName().equals(tagName)) {
                return entry;
            }
        }
        throw new NitfFormatException(String.format("Failed to look up %s", tagName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getFieldValue(final String tagName) throws NitfFormatException {
        TreEntry entry = getEntry(tagName);
        return entry.getFieldValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getIntValue(final String tagName) throws NitfFormatException {
        return getBigIntegerValue(tagName).intValueExact();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final long getLongValue(final String tagName) throws NitfFormatException {
        return getBigIntegerValue(tagName).longValueExact();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final BigInteger getBigIntegerValue(final String tagName) throws NitfFormatException {
        try {
            TreEntry entry = getEntry(tagName);
            if ("UINT".equals(entry.getDataType())) {
                return new BigInteger(1, entry.getFieldValue().getBytes(StandardCharsets.ISO_8859_1));
            } else {
                return new BigInteger(entry.getFieldValue(), DECIMAL_BASE);
            }
        } catch (NitfFormatException ex) {
            throw new NitfFormatException(String.format("Failed to look up %s as a numerical value", tagName));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final double getDoubleValue(final String tagName) throws NitfFormatException {
        try {
            TreEntry entry = getEntry(tagName);
            if (entry.getDataType().equals("IEEE754")) {
                byte[] floatBytes = entry.getFieldValue().getBytes(StandardCharsets.ISO_8859_1);
                if (floatBytes.length == Float.BYTES) {
                    return ByteBuffer.wrap(floatBytes).order(ByteOrder.BIG_ENDIAN).getFloat();
                } else if (floatBytes.length == Double.BYTES) {
                    return ByteBuffer.wrap(floatBytes).order(ByteOrder.BIG_ENDIAN).getDouble();
                }
                throw new NitfFormatException(String.format("Unexpected length for %s IEEE754 value: %d", tagName, floatBytes.length));
            }
            return Double.parseDouble(entry.getFieldValue());
        } catch (NitfFormatException ex) {
            throw new NitfFormatException(String.format("Failed to look up %s as double value", tagName));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void dump() {
        for (TreEntry entry : entries) {
            LOG.debug("\t----Start Entry---");
            entry.dump();
            LOG.debug("\t----End Entry---");
        }
    }

    /**
     * {@inheritDoc}
     */
    // CSOFF: DesignForExtension
    @Override
    public String toString() {
        return "(Group)";
    }
    // CSON: DesignForExtension
}
