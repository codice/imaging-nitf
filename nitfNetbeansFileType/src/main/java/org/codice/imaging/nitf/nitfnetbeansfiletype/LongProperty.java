/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;

class LongProperty extends PropertySupport.ReadOnly<Long> {
    private final Long value;

    public LongProperty(String name, String displayName, String shortDescription, long result) {
        super(name, Long.class, displayName, shortDescription);
        value = result;
    }

    @Override
    public Long getValue() throws IllegalAccessException, InvocationTargetException {
        return value;
    }
}