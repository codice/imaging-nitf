/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codice.imaging.nitf.nitfnetbeansfiletype;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import org.openide.nodes.PropertySupport;

class DateProperty extends PropertySupport.ReadOnly<Date> {
    private final Date value;

    public DateProperty(String name, String displayName, String shortDescription, Date result) {
        super(name, Date.class, displayName, shortDescription);
        value = result;
    }

    @Override
    public Date getValue() throws IllegalAccessException, InvocationTargetException {
        return value;
    }
}
