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
package org.codice.imaging.nitf.deswrap;

import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.security.SecurityMetadata;

/**
 * Data Extension Segment (DES).
 */
public class DES {

    /**
     * Underlying data.
     */
    protected DataExtensionSegment mDES;

    /**
     * Get the identifier for this DES.
     *
     * @return the identifier for this DES.
     */
    public final String getIdentifier() {
        return mDES.getIdentifier();
    }

    /**
     * Get the version for this DES.
     *
     * @return the version for this DES.
     */
    public final int getVersion() {
        return mDES.getDESVersion();
    }

    /**
     * Get the security metadata for this DES.
     *
     * @return security metadata for the DES.
     */
    public final SecurityMetadata getSecurityMetadata() {
        return mDES.getSecurityMetadata();
    }
}
