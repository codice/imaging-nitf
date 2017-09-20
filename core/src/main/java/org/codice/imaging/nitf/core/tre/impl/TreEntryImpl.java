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

import org.codice.imaging.nitf.core.tre.TreEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    Entry within a TRE.
    <p>
    This is a name and a value, or a name and a group of entries.
*/
public abstract class TreEntryImpl implements TreEntry {

    private static final Logger LOG = LoggerFactory.getLogger(TreEntryImpl.class);

    private String name = null;

    /**
        Set the name of the TRE entry.

        @param fieldName the name of the TRE entry.
    */
    public final void setName(final String fieldName) {
        name = fieldName;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public final String getName() {
        return name;
    }

}
