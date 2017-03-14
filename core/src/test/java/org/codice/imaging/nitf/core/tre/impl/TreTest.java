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
package org.codice.imaging.nitf.core.tre.impl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.junit.Test;

public class TreTest extends SharedTreTest {

    @Test
    public void testTreEntryAccessors1() {
        TreEntryImpl entry = new TreEntryImpl("oldName", null, "string");
        assertNotNull(entry);

        entry.setName("DANAME");
        assertEquals("DANAME", entry.getName());

        entry.setFieldValue("Some value");
        assertEquals("Some value", entry.getFieldValue());
    }

    @Test
    public void testTreEntryAccessors2() {
        TreEntryImpl entry = new TreEntryImpl("ANAME", null, null);
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
        TreGroup group2 = new TreGroupImpl();
        entry.addGroup(group2);
        TreEntryImpl subEntry = new TreEntryImpl("SubEntry");
        group2.add(subEntry);
        subEntry.addGroup(new TreGroupImpl());
        entry.dump();
    }

    // Avoid test coverage problem
    @Test
    public void testFactoryConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<TreFactory> constructor = TreFactory.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    // Avoid test coverage problem
    @Test
    public void testConstantsConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<TreConstants> constructor = TreConstants.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testTreImplPrefixGetter() throws NitfFormatException, IOException {
        Tre tre = parseTRE("USE00A00107                                                                                                           ", 11 + 107, "USE00A");
        assertNull(tre.getRawData());
        assertEquals("NITF_USE00A_", tre.getPrefix());
    }
}
