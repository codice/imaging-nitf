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

import java.time.LocalDate;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreSource;
import org.codice.imaging.nitf.core.tre.impl.TreSimpleEntryImpl;

/**
 * Shared superclass for wrappers for TREs that are "flat".
 *
 * In this context, "flat" means a TRE that only has fixed length fields,
 * without looping or conditional structures.
 */
public abstract class FlatTreWrapper extends TreWrapper {

    /**
     * Construct a new TRE wrapper around an existing TRE.
     *
     * @param tre the existing TRE
     * @param tag the tag that this TRE wrapper is meant to be for.
     * @throws NitfFormatException if there is a parsing issue
     */
    protected FlatTreWrapper(final Tre tre, final String tag) throws NitfFormatException {
        super(tre, tag);
    }

    /**
     * Construct a new TRE wrapper of the specified type.
     *
     * @param tag the tag for the TRE wrapper to construct.
     * @param treSource the source location that this TRE is for
     * @throws NitfFormatException if there is a parsing issue.
     */
    protected FlatTreWrapper(final String tag, final TreSource treSource) throws NitfFormatException {
        super(tag, treSource);
    }

    /**
     * Add or update a TRE entry value.
     *
     * If the entry already exists, it will be overwritten (updated). If the entry
     * does not already exist, it will be added.
     *
     * @param fieldName the field name for the TRE.
     * @param value the value to set.
     * @param fieldType the data type of the value to be written
     * @throws NitfFormatException if there is a parsing error.
     */
    protected final void addOrUpdateEntry(final String fieldName, final String value, final String fieldType) throws NitfFormatException {
        for (TreEntry entry : mTreBuilder.getEntries()) {
            if (entry.getName().equals(fieldName) && (entry instanceof TreSimpleEntryImpl)) {
                TreSimpleEntryImpl simpleEntry = (TreSimpleEntryImpl) entry;
                simpleEntry.setFieldValue(value);
                return;
            }
        }
        // Didn't find it, just add.
        mTreBuilder.add(new TreSimpleEntryImpl(fieldName, value, fieldType));
    }

    /**
     * Add or update a TRE entry value in date format (YYYYMMDD).
     *
     * If the entry already exists, it will be overwritten (updated). If the entry
     * does not already exist, it will be added. If the date is null, then the
     * entry will be written as spaces.
     *
     * @param fieldName the field name for the TRE.
     * @param date the date to set.
     * @throws NitfFormatException if there is a parsing error.
     */
    protected final void addOrUpdateEntry(final String fieldName, final LocalDate date) throws NitfFormatException {
        String value = "        ";
        if (date != null) {
            value = date.format(CENTURY_DATE_FORMATTER);
        }
        addOrUpdateEntry(fieldName, value, "string");
    }
}
