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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class TreParams {

    private final Map<String, TreParameter> parameters = new HashMap<>();

    private static final int DECIMAL_BASE = 10;

    int getIntValue(final String key) {
        TreParameter parameter = parameters.get(key);
        if ("UINT".equals(parameter.mFieldType)) {
            int res = 0;
            for (int i = 0; i < parameter.mFieldValue.length(); ++i) {
                res = (res << Byte.SIZE) + Byte.toUnsignedInt(parameter.mFieldValue.getBytes(StandardCharsets.US_ASCII)[i]);
            }
            return res;
        } else {
            return Integer.parseInt(parameter.mFieldValue, DECIMAL_BASE);
        }
    }

    String getFieldValue(final String key) {
        return parameters.get(key).mFieldValue;
    }

    void addParameter(final String fieldKey, final String fieldValue, final String fieldType) {
        parameters.put(fieldKey, new TreParameter(fieldValue, fieldType));
    }

    private static final class TreParameter {

        private final String mFieldValue;
        private final String mFieldType;

        private TreParameter(final String fieldValue, final String fieldType) {
            mFieldValue = fieldValue;
            mFieldType = fieldType;
        }
    }
}
