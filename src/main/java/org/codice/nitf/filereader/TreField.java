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

// import java.text.Collator;
import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
// import java.util.Map;
// import java.util.TreeMap;

public class TreField {
    private String name = null;
    private String value = null;
    private ArrayList<TreField> subfields = null;

    public TreField(String fieldName, String fieldValue) {
        name = fieldName;
        value = fieldValue;
    }

    public void setName(String fieldName) {
        name = fieldName;
    }

    public String getName() {
        return name;
    }

    public void setFieldValue(String fieldValue) {
        value = fieldValue;
    }

    public String getFieldValue() {
        return value;
    }

    public void initSubFields() {
        if (subfields == null) {
            subfields = new ArrayList<TreField>();
        }
    }
    
    public ArrayList<TreField> getSubFields() {
        return subfields;
    }
}