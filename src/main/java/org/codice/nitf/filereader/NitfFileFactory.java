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
package org.codice.nitf.filereader;

import java.io.InputStream;
import java.text.ParseException;
import java.util.EnumSet;
import java.util.Set;

public final class NitfFileFactory {

    private NitfFileFactory() {
    }

    public static NitfFile parse(final InputStream nitfInputStream) throws ParseException {
        NitfFile file = new NitfFile();
        NitfFileParser parser = new NitfFileParser(nitfInputStream, EnumSet.noneOf(ParseOption.class), file);
        return file;
    }

    public static NitfFile parse(final InputStream nitfInputStream, final Set<ParseOption> parseOptions) throws ParseException {
        NitfFile file = new NitfFile();
        NitfFileParser parser = new NitfFileParser(nitfInputStream, parseOptions, file);
        return file;
    }
}
