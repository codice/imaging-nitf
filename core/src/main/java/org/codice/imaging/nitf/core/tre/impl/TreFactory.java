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

import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Factory class for creating new Tre (Tagged Registered Extension) instances.
 */
public final class TreFactory {

    private TreFactory() {
    }

    /**
     * Create a new TreBuilder instance.
     *
     * This builder will need to have the required entries added.
     *
     * @param tag the name of the TRE (i.e. the six letter tag)
     * @param source the location of this TRE (intended location in the file)
     * @return TreBuilder with no content.
     */
    public static TreBuilder getDefault(final String tag, final TreSource source) {
        TreBuilder treBuilder = new TreBuilder(tag, source);
        return treBuilder;
    }
}
