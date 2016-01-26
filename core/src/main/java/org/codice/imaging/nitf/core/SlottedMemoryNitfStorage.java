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
package org.codice.imaging.nitf.core;

/**
 * An in-memory slotted store.
 *
 * This has no parsing capability, so you need to create the parts yourself, either from parsing another file, or from
 * build up of the data structures.
 */
public class SlottedMemoryNitfStorage extends SlottedNitfStorage {

    /**
     * Set the main (file level) header.
     *
     * @param header header to set
     */
    public final void setNitfFileLevelHeader(final NitfFileHeader header) {
        nitfFileLevelHeader = header;
    }

}
