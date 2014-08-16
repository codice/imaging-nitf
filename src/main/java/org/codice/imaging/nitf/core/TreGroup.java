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
package org.codice.imaging.nitf.core;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
    A group of values within a TreEntry.
*/
public class TreGroup {

    private List<TreEntry> entries = new ArrayList<TreEntry>();

    /**
        The entries in the TRE entry group.

        @return the list of entries within the group.
    */
    public final List<TreEntry> getEntries() {
        return entries;
    }

    /**
        Add an entry to the group.

        @param entry the entry to add
    */
    public final void add(final TreEntry entry) {
        entries.add(entry);
    }

    /**
        Add multiple entries to the group.

        @param oneOrMoreEntries the entry or entries to add
    */
    public final void addAll(final List<TreEntry> oneOrMoreEntries) {
        entries.addAll(oneOrMoreEntries);
    }

    /**
        Set the list of entries.

        @param treEntries the new list of entries.
    */
    public final void setEntries(final List<TreEntry> treEntries) {
        entries = new ArrayList<TreEntry>();
        entries.addAll(treEntries);
    }

    /**
        Get the field value for a specific tag.

        @param tagName the name (tag) of the field to look up.
        @return the field value corresponding to the tag name.
        @throws ParseException when the tag is not found
    */
    public final String getFieldValue(final String tagName) throws ParseException {
        for (TreEntry entry : entries) {
            if (entry.getName().equals(tagName)) {
                return entry.getFieldValue();
            }
        }
        throw new ParseException(String.format("Failed to look up %s", tagName), 0);
    }

    /**
        Get the field value for a specific tag in integer format.

        @param tagName the name (tag) of the field to look up.
        @return the field value corresponding to the tag name, as an integer.
        @throws ParseException when the tag is not found or the value cannot be converted to integer format.
    */
    public final int getIntValue(final String tagName) throws ParseException {
        try {
            return Integer.parseInt(getFieldValue(tagName));
        } catch (ParseException ex) {
            throw new ParseException(String.format("Failed to look up %s as integer value", tagName), 0);
        }
    }

    /**
        Debug dump of the entries.
    */
    public final void dump() {
        for (TreEntry entry : entries) {
            System.out.println("\t----Start Entry---");
            entry.dump();
            System.out.println("\t----End Entry---");
        }
    }
}
