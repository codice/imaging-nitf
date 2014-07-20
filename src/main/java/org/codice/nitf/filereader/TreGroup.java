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
import java.util.ArrayList;
import java.util.List;

public class TreGroup {

    private ArrayList<TreEntry> entries = new ArrayList<TreEntry>();

    public final List<TreEntry> getEntries() {
        return entries;
    }

    public final void add(final TreEntry entry) {
        entries.add(entry);
    }

    public final void addAll(final List<TreEntry> oneOrMoreEntries) {
        entries.addAll(oneOrMoreEntries);
    }

    public final void setEntries(final List<TreEntry> treEntries) {
        entries = new ArrayList<TreEntry>();
        entries.addAll(treEntries);
    }

    public final String getFieldValue(final String tagName) throws ParseException {
        for (TreEntry entry : entries) {
            if (entry.getName().equals(tagName)) {
                return entry.getFieldValue();
            }
        }
        throw new ParseException(String.format("Failed to look up %s", tagName), 0);
    }

    public final int getIntValue(final String tagName) throws ParseException {
        try {
            return Integer.parseInt(getFieldValue(tagName));
        } catch (ParseException ex) {
            throw new ParseException(String.format("Failed to look up %s as integer value", tagName), 0);
        }
    }

    public final void dump() {
        for (TreEntry entry : entries) {
            System.out.println("\t----Start Entry---");
            entry.dump();
            System.out.println("\t----End Entry---");
        }
    }
}
