package org.codice.imaging.nitf.core.tre;

/**
 * A simple entry within a TRE.
 * <p>
 * This is a name and a value, along with the type of the value.
 */
public interface TreSimpleEntry extends TreEntry {

    /**
     * Return the field value of the TRE entry.
     *
     * @return the value of the TRE
     */
    String getFieldValue();

    /**
     * Return the data type for this TRE entry.
     * <p>
     * This is only meaningful if it is a simple entry (not a nested group).
     *
     * @return the data type as a string, or null for a group.
     */
    String getDataType();
}
