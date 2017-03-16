package org.codice.imaging.nitf.core.tre;

import java.util.List;

/**
 * Entry within a TRE.
 * <p>
 * This is a name and a value, or a name and a group of entries.
 */
public interface TreEntry {

    /**
     * Return the name of the TRE entry.
     *
     * @return the name of the TRE entry
     */
    String getName();

    /**
     * Return the field value of the TRE entry.
     *
     * @return the value of the TRE
     */
    String getFieldValue();

    /**
     * Return the groups for this TRE entry.
     *
     * @return the groups for this TRE entry.
     */
    List<TreGroup> getGroups();

    /**
     * Check whether the TreEntryImpl is a simple field.
     *
     * @return true if it is a simple field (name / value pair), otherwise false.
     */
    boolean isSimpleField();

    /**
     * Check whether the TreEntryImpl has groups.
     *
     * @return true if it has groups, otherwise false.
     */
    boolean hasGroups();

    /**
     * Return the data type for this TRE entry.
     * <p>
     * This is only meaningful if it is a simple entry (not a nested group).
     *
     * @return the data type as a string, or null for a group.
     */
    String getDataType();

    /**
     * Debug dump of the TRE entry.
     */
    void dump();

    @Override
    String toString();

    /**
     * @param value the value to be assigned to this field
     */
    void setFieldValue(String value);
}
