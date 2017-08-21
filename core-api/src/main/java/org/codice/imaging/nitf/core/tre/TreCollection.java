package org.codice.imaging.nitf.core.tre;

import java.util.List;

/**
 * Collection of TREs.
 */
public interface TreCollection {
    /**
     * Return the TREs.
     *
     * @return list of TREs
     */
    List<Tre> getTREs();

    /**
     * Get the names of the TREs in the collection.
     * <p>
     * This method returns a unique list of TRE names. That list can be
     * iterated over with getTREsWithName() to get the TREs.
     *
     * @return the TRE names.
     */
    List<String> getUniqueNamesOfTRE();

    /**
     * Get the TREs that have a specific name.
     *
     * @param nameToMatch the name of the TREs to match.
     * @return list of TREs with a specific name.
     */
    List<Tre> getTREsWithName(String nameToMatch);

    /**
     * Check whether this collection has any TREs.
     *
     * @return true if there are any TREs, or false if there no TREs in the collection
     */
    boolean hasTREs();

    @Override
    String toString();

    /**
     * Get the TREs that came from a specific source.
     * <p>
     * In this context, the source is the part of the header / subheader that
     * the TRE was read from (or where it should be put on write).
     *
     * @param source the source to select for.
     * @return List of TREs that match the source.
     */
    List<Tre> getTREsForSource(TreSource source);
}
