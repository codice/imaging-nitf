package org.codice.imaging.nitf.common.tre;

import java.util.List;

/**
 * an interface to represent a TreEntry.
 */
public interface TreEntry {

    /**
     Set the name of the TRE entry.

     @param fieldName the name of the TRE entry.
     */
    void setName(String fieldName);

    /**
     Return the name of the TRE entry.

     @return the name of the TRE entry
     */
    String getName();

    /**
     Set the field value of the TRE entry.

     @param fieldValue the value of the TRE
     */
    void setFieldValue(final String fieldValue);

    /**
     Return the field value of the TRE entry.

     @return the value of the TRE
     */
    String getFieldValue();

    /**
     Return the groups for this TRE entry.

     @return the groups for this TRE entry.
     */
     List<TreGroup> getGroups();

    /**
     Add a group to the groups in this TRE entry.

     @param group the group to add.
     */
    void addGroup(final TreGroup group);

    /**
     Dump the value for debugging.
     */

    void dump();

}
