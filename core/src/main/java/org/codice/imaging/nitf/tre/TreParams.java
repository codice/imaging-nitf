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
package org.codice.imaging.nitf.tre;

import java.util.HashMap;
import java.util.Map;

/**
 * a class to store key/value pairs for Tres.
 */
public class TreParams {

    private final Map<String, String> parameters = new HashMap<>();

    /**
     *
     * @param key - the key of the value to retrieve.
     * @return an int representation of the value for the given key.
     */
    public final int getIntValue(final String key) {
        return Integer.parseInt(getFieldValue(key));
    }

    /**
     *
     * @param key - the key of the value to retrieve.
     * @return a String representation of the value for the given key.
     */
    public final String getFieldValue(final String key) {
        return parameters.get(key);
    }

    /**
     * Sets a parameter value.
     *
     * @param fieldKey - the key to store the value under.
     * @param fieldValue - the value to store under 'fieldKey'.
     */
    public final void addParameter(final String fieldKey, final String fieldValue) {
        parameters.put(fieldKey, fieldValue);
    }
}
