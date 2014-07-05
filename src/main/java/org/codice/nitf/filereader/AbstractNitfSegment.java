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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AbstractNitfSegment
{
    protected NitfReader reader = null;

    ArrayList<TreListEntry> tres = new ArrayList<TreListEntry>();

    public ArrayList<TreListEntry> getTREsRawStructure() {
        return tres;
    }

    public Map<String, String> getTREsFlat() {
        Map<String, String> tresFlat = new TreeMap<String, String>();
        for (TreListEntry listEntry : tres) {
            List<Tre> treForName = listEntry.getTresWithName();
            if (treForName.size() == 1) {
                Tre onlyTre = treForName.get(0);
                List<TreField> treFields = onlyTre.getFields();
                for (TreField treField : treFields) {
                    // TODO: this should recurse properly, and be shared with below.
                    if ((treField.getName() != null) && (treField.getFieldValue() != null)) {
                        System.out.println(String.format("Putting %s|%s|%s|", onlyTre.getName(), treField.getName(), treField.getFieldValue().trim()));
                        tresFlat.put(String.format("%s_%s", onlyTre.getName(), treField.getName()), treField.getFieldValue().trim());
                    }
                }
            } else {
                for (int i = 0; i < treForName.size(); ++i) {
                    Tre thisTre = treForName.get(i);
                    List<TreField> treFields = thisTre.getFields();
                    for (TreField treField : treFields) {
                        System.out.println(String.format("Putting multi %s|%d|%s|", treField.getName(), i, treField.getFieldValue().trim()));
                        tresFlat.put(String.format("%s_%d_%s", thisTre.getName(), i, treField.getName()), treField.getFieldValue().trim());
                    }
                }
            }
        }
        return tresFlat;
    }

    protected void mergeTREs(List<TreListEntry> tresToAdd) {
        for (TreListEntry treEntry : tresToAdd) {
            boolean matchingExistingEntryWasFound = false;
            for (TreListEntry existingEntry : tres) {
                if (existingEntry.getName().equals(treEntry.getName())) {
                    existingEntry.getTresWithName().addAll(existingEntry.getTresWithName());
                    matchingExistingEntryWasFound = true;
                    break;
                }
            }
            if (matchingExistingEntryWasFound == false) {
                tres.add(treEntry);
            }
        }
    }
}
