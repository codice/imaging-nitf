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
package org.codice.nitf.filereader;

import java.text.ParseException;

public class NitfDataExtensionSegment extends AbstractCommonNitfSegment {

    private String desIdentifier = null;
    private int desVersion = -1;
    private NitfSecurityMetadata securityMetadata = null;
    private String desData = null;

    public NitfDataExtensionSegment() {
    }

    public final void parse(final NitfReader nitfReader, final int desLength) throws ParseException {
        new NitfDataExtensionSegmentParser(nitfReader, desLength, this);
    }

    public final void setDESVersion(final int version) {
        desVersion = version;
    }

    public final int getDESVersion() {
        return desVersion;
    }

    public final void setData(final String data) {
        desData = data;
    }
}
