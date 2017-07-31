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
package org.codice.imaging.nitf.core.tre.impl;

import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreEntry;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Factory class for creating new Tre (Tagged Registered Extension) instances.
 */
public final class TreFactory {

    private TreFactory() {
    }

    /**
     * Create a new Tre instance.
     *
     * This instance will need to have the required entries added.
     *
     * @param tag the name of the TRE (i.e. the six letter tag)
     * @param source the location of this TRE (intended location in the file)
     * @return TRE with no content.
     */
    public static Tre getDefault(final String tag, final TreSource source) {
        Tre tre = new TreImpl(tag, source);
        return tre;
    }

    /**
     * Construct a TRE entry with a specific field name, field value and value type.
     * <p>
     * This is the simple (not-repeating) TRE entry form.
     *
     * @param fieldName the field name of the new TRE entry.
     * @param fieldValue the field value for the new TRE entry.
     * @param fieldType the data type ("string", "real", "UINT", "integer") for the data
     * @return TreEntry with the specified name, value and data type.
     */
    public static TreEntry getEntry(final String fieldName, final String fieldValue, final String fieldType) {
        return new TreEntryImpl(fieldName, fieldValue, fieldType);
    }

    /**
     * Build a TRE entry with a specific field name.
     * <p>
     * This is the repeating TRE entry form of an entry, initialised with an empty group list.
     *
     * @param fieldName the field name of the new TRE entry.
     * @return TreEntry with the specified name.
     */
    public static TreEntry getEntry(final String fieldName) {
        return new TreEntryImpl(fieldName);
    }

    /**
     * Create a new TreGroup instance.
     *
     * This instance will need to have the required entries added.
     *
     * @return TreGroup with no content.
     */
    public static TreGroup getGroup() {
        return new TreGroupImpl();
    }

}
