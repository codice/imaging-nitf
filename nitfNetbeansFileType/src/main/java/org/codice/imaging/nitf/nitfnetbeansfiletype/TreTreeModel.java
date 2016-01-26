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
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import java.util.ArrayList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreEntryList;
import org.codice.imaging.nitf.core.tre.TreGroup;
import org.codice.imaging.nitf.core.tre.TreCollection;
import org.codice.imaging.nitf.core.tre.TreEntry;

/**
 * TreeModel implementation for the Tagged Registered Extensions.
 */
class TreTreeModel implements TreeModel {

    private final ArrayList<TreeModelListener> treeModelListeners = new ArrayList<>();
    private final TreCollection tres;

    public TreTreeModel(final TreCollection treCollection) {
        tres = treCollection;
    }

    @Override
    public Object getRoot() {
        return tres;
    }

    @Override
    public Object getChild(final Object o, final int i) {
        if (o instanceof TreCollection) {
            TreCollection tc = (TreCollection) o;
            return tc.getTREs().get(i);
        }
        if (o instanceof Tre) {
            Tre tre = (Tre) o;
            return tre.getEntries().get(i);
        }
        if (o instanceof TreEntryList) {
            TreEntryList tel = (TreEntryList) o;
            return tel.getEntries().get(i);
        }
        if (o instanceof TreGroup) {
            TreGroup tg = (TreGroup) o;
            return tg.getEntries().get(i);
        }
        if (o instanceof TreEntry) {
            TreEntry te = (TreEntry) o;
            if (te.getGroups() != null) {
                return te.getGroups().get(i);
            }
        }
        return null;
    }

    @Override
    public int getChildCount(final Object o) {
        if (o instanceof TreCollection) {
            TreCollection tc = (TreCollection) o;
            return tc.getTREs().size();
        }
        if (o instanceof Tre) {
            Tre tre = (Tre) o;
            return tre.getEntries().size();
        }
        if (o instanceof TreEntryList) {
            TreEntryList tel = (TreEntryList) o;
            return tel.getEntries().size();
        }
        if (o instanceof TreGroup) {
            TreGroup tg = (TreGroup) o;
            return tg.getEntries().size();
        }
        if (o instanceof TreEntry) {
            TreEntry te = (TreEntry) o;
            if (te.getGroups() != null) {
                return te.getGroups().size();
            }
        }
        return 0;
    }

    @Override
    public boolean isLeaf(final Object o) {
        return (getChildCount(o) == 0);
    }

    @Override
    public void valueForPathChanged(final TreePath tp, final Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getIndexOfChild(final Object o, final Object o1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTreeModelListener(final TreeModelListener tl) {
        treeModelListeners.add(tl);
    }

    @Override
    public void removeTreeModelListener(final TreeModelListener tl) {
        treeModelListeners.remove(tl);
    }

    protected void fireTreeStructureChanged(final TreCollection oldRoot) {
        TreeModelEvent e = new TreeModelEvent(this, new Object[] {oldRoot});
        for (TreeModelListener tml : treeModelListeners) {
            tml.treeStructureChanged(e);
        }
    }
}
