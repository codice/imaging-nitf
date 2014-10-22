/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;

public class StringProperty extends PropertySupport.ReadOnly<String> {
    private final String value;

    public StringProperty(String name, String displayName, String shortDescription, String result) {
        super(name, String.class, displayName, shortDescription);
        value = result;
    }

    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        return value;
    }
}
