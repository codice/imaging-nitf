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
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for TreCollection class.
 */
public class TreCollectionTest {

    public TreCollectionTest() {
    }

    @Test
    public void checkToString() {
        TreCollection collection = new TreCollection();
        assertEquals("TRE Collection", collection.toString());
    }

    @Test
    public void addRemove() {
        TreCollection collection = new TreCollection();
        Tre tre1 = TreFactory.getDefault("One", TreSource.TreOverflowDES);
        collection.add(tre1);
        assertEquals(1, collection.getTREs().size());
        assertEquals(1, collection.getTREsWithName("One").size());
        assertEquals(tre1, collection.getTREs().get(0));
        Tre tre2 = TreFactory.getDefault("Two", TreSource.TreOverflowDES);
        collection.add(tre2);
        assertEquals(2, collection.getTREs().size());
        assertEquals(1, collection.getTREsWithName("One").size());
        assertEquals(1, collection.getTREsWithName("Two").size());
        assertTrue(collection.remove(tre1));
        assertEquals(1, collection.getTREs().size());
        assertEquals(0, collection.getTREsWithName("One").size());
        assertEquals(1, collection.getTREsWithName("Two").size());
        assertEquals(tre2, collection.getTREs().get(0));
    }
}
