/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;

class IntegerProperty extends PropertySupport.ReadOnly<Integer> {
    private final Integer value;

    public IntegerProperty(String name, String displayName, String shortDescription, int result) {
        super(name, Integer.class, displayName, shortDescription);
        value = result;
    }

    @Override
    public Integer getValue() throws IllegalAccessException, InvocationTargetException {
        return value;
    }
}
