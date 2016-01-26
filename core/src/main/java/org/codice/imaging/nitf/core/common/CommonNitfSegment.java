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
package org.codice.imaging.nitf.core.common;

import java.util.Map;

import org.codice.imaging.nitf.core.security.SecurityMetadata;
import org.codice.imaging.nitf.core.tre.TreCollection;

/**
 Common data elements for NITF segment subheaders.
 */
public interface CommonNitfSegment {

    /**
     Return the identifier (IID1/SID/LID/TEXTID) for the segment.
     <p>
     This field shall contain a valid alphanumeric identification code associated with the
     segment. The valid codes are determined by the application.

     @return the identifier
     */
    String getIdentifier();

    /**
     Return the security metadata for the segment.

     @return security metadata
     */
    SecurityMetadata getSecurityMetadata();

    /**
     Return the TREs for this segment, in raw form.

     @return TRE collection
     */
    TreCollection getTREsRawStructure();

    /**
     *
     * @return a java.util.Map containing the Tres.
     */
    Map<String, String> getTREsFlat();
}
