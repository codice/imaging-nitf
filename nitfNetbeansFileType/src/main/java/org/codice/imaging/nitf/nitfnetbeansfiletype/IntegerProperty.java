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
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import org.openide.nodes.PropertySupport;

class IntegerProperty extends PropertySupport.ReadOnly<Integer> {
    private final Integer value;

    public IntegerProperty(final String name, final String displayName, final String shortDescription, final int result) {
        super(name, Integer.class, displayName, shortDescription);
        value = result;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
