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
package org.codice.imaging.nitf.core.tre;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class TreTest {

    @Test
    public void testTreEntryAccessors1() {
        TreEntry entry = new TreEntry("oldName", null, "string");
        assertNotNull(entry);

        entry.setName("DANAME");
        assertEquals("DANAME", entry.getName());

        entry.setFieldValue("Some value");
        assertEquals("Some value", entry.getFieldValue());
    }

    @Test
    public void testTreEntryAccessors2() {
        TreEntry entry = new TreEntry("ANAME", null, null);
        assertNotNull(entry);

        entry.initGroups();
        assertEquals(0, entry.getGroups().size());
        entry.addGroup(new TreGroupImpl());
        assertEquals(1, entry.getGroups().size());
        // check it doesn't kill what we already have
        entry.initGroups();
        assertEquals(1, entry.getGroups().size());
    }

    @Test
    public void testTreEntry() {
        TreEntry entry = new TreEntry("ANAME");
        assertNotNull(entry);

        entry.initGroups();
        assertEquals(0, entry.getGroups().size());
        TreGroup group1 = new TreGroupImpl();
        entry.addGroup(group1);
        TreGroup group2 = new TreGroupImpl();
        entry.addGroup(group2);
        TreEntry subEntry = new TreEntry("SubEntry");
        group2.add(subEntry);
        subEntry.addGroup(new TreGroupImpl());
        entry.dump();
    }
}
