package org.codice.imaging.nitf.core.tre;

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
     * Check whether the TreEntry is a simple field.
     *
     * @return true if it is a simple field (name / value pair), otherwise false.
     */
    boolean isSimpleField();

    /**
     * Check whether the TreEntry has groups.
     *
     * @return true if it has groups, otherwise false.
     */
    boolean hasGroups();

    /**
     * Debug dump of the TRE entry.
     */
    void dump();

    @Override
    String toString();
}
