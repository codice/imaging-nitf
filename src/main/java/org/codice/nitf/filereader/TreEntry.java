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

import java.util.ArrayList;
import java.util.List;

public class TreEntry {
    private String name = null;
    private String value = null;
    private List<TreGroup> groups = null;
    private TreGroup entryParent = null;

    public TreEntry(final String fieldName, final String fieldValue, final TreGroup parent) {
        name = fieldName;
        value = fieldValue;
        entryParent = parent;
    }

    public TreEntry(final String fieldName, final TreGroup parent) {
        name = fieldName;
        entryParent = parent;
        groups = new ArrayList<TreGroup>();
    }

    public final void setName(final String fieldName) {
        name = fieldName;
    }

    public final String getName() {
        return name;
    }

    public final void setFieldValue(final String fieldValue) {
        value = fieldValue;
    }

    public final String getFieldValue() {
        return value;
    }

    public final void initGroups() {
        if (groups == null) {
            groups = new ArrayList<TreGroup>();
        }
    }

    public final List<TreGroup> getGroups() {
        return groups;
    }

    public final void addGroup(final TreGroup group) {
        groups.add(group);
    }

    public final void dump() {
        System.out.println("\tName:" + name);
        if (value != null) {
            System.out.println("\tValue:" + value);
        } else if (groups != null) {
            for (TreGroup group : groups) {
                System.out.println("\t--New Group--");
                group.dump();
                System.out.println("\t--End Group--");
            }
        }
    }
}
