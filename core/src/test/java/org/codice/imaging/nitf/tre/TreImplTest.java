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
package org.codice.imaging.nitf.tre;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.codice.imaging.nitf.common.tre.TreEntry;
import org.codice.imaging.nitf.common.tre.TreGroup;
import org.junit.Test;

public class TreImplTest {

    @Test
    public void testTreEntryAccessors1() {
        TreEntryImpl entry = new TreEntryImpl("oldName", null);
        assertNotNull(entry);

        entry.setName("DANAME");
        assertEquals("DANAME", entry.getName());

        entry.setFieldValue("Some value");
        assertEquals("Some value", entry.getFieldValue());
    }

    @Test
    public void testTreEntryAccessors2() {
        TreEntryImpl entry = new TreEntryImpl("ANAME", null);
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
        TreEntryImpl entry = new TreEntryImpl("ANAME");
        assertNotNull(entry);

        entry.initGroups();
        assertEquals(0, entry.getGroups().size());
        TreGroup group1 = new TreGroupImpl();
        entry.addGroup(group1);
        TreGroupImpl group2 = new TreGroupImpl();
        entry.addGroup(group2);
        TreEntry subEntry = new TreEntryImpl("SubEntry");
        group2.add(subEntry);
        subEntry.addGroup(new TreGroupImpl());
        entry.dump();
    }
}
