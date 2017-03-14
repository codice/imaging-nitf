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

package org.codice.imaging.nitf.core.image;

import org.codice.imaging.nitf.core.common.NitfFormatException;

/**
 A Target ID (TGTID) representation.
 <p>
 <i>Target Identifier. This field shall contain the
 identification of the primary target in the format,
 BBBBBBBBBBOOOOOCC, consisting of ten
 characters of Basic Encyclopedia (BE) identifier,
 followed by five characters of facility OSUFFIX,
 followed by the two character country code as
 specified in FIPS PUB 10-4.</i>
 <p>
 Note that FIPS PUB 10-4 has been withdrawn. The NGA
 has a replacement in the GEC (Geopolitical Entities and
 Codes), but the codes do not always represent the same
 countries (e.g. Australia was AS in FIPS 10-4, and is AU
 in the GEC).
 */
public interface TargetId {
    /**
     Return the Basic Encyclopedia (BE) number.

     @return the BE number for this target identifier.
     */
    String getBasicEncyclopediaNumber();

    /**
     Return the O-suffix.

     @return the O-suffix for this target identifier.
     */
    String getOSuffix();

    /**
     Return the Country Code.

     @return country code for this target identifier.
     */
    String getCountryCode();

    /**
     * Get the text equivalent of this target identifier.
     *
     * This will produce the text form of the target identifier, in BBBBBBBBBBOOOOOCC form. The component elements
     * cannot be null, although they can be space filled (or empty, in which case they will be space filled as
     * required). Note that there are likely MIDB rules on valid ranges for BE numbers and O-suffixes that are not
     * enforced here.
     *
     * @return a space-padded string containing the target identifier.
     * @throws NitfFormatException if any of the fields are null, or are too long to fit.
     */
    String textValue() throws NitfFormatException;
}
