package org.codice.imaging.nitf.common.tre;

import java.text.ParseException;
import java.util.List;

/**
 * interface to represent a TreGroup.
 */
public interface TreGroup {

    /**
     The entries in the TRE entry group.

     @return the list of entries within the group.
     */
    List<TreEntry> getEntries();

    /**
     Add an entry to the group.

     @param entry the entry to add
     */
    void add(final TreEntry entry);

    /**
     Add multiple entries to the group.

     @param group the group containing the entry or entries to add
     */
    void addAll(final TreGroup group);

    /**
     Set the list of entries.

     @param treEntries the new list of entries.
     */
    void setEntries(final List<TreEntry> treEntries);

    /**
     Get the entry for a specific tag.

     @param tagName the name (tag) of the field to look up.
     @return the entry corresponding to the tag name.
     @throws ParseException when the tag is not found
     */
    TreEntry getEntry(final String tagName) throws ParseException;

    /**
     Get the field value for a specific tag.

     @param tagName the name (tag) of the field to look up.
     @return the field value corresponding to the tag name.
     @throws ParseException when the tag is not found
     */
    String getFieldValue(final String tagName) throws ParseException;

    /**
     Get the field value for a specific tag in integer format.

     @param tagName the name (tag) of the field to look up.
     @return the field value corresponding to the tag name, as an integer.
     @throws ParseException when the tag is not found or the value cannot be converted to integer format.
     */
    int getIntValue(final String tagName) throws ParseException;

    /**
     Debug dump of the entries.
     */
    void dump();
}
