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
     * Debug dump of the TRE entry.
     */
    void dump();

    @Override
    String toString();
}
