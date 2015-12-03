package org.codice.imaging.nitf.common;

import java.util.List;

import org.codice.imaging.nitf.common.tre.Tre;

/**
 * Represents a collection of Tre objects.
 *
 */
public interface TreCollection {

    /**
     Return the TREs.

     @return list of TREs
     */
    List<Tre> getTREs();

    /**
     Add a TRE to the collection.

     @param tre the TRE to add to the collection.
     */
    void add(Tre tre);

    /**
     Add multiple TREs to the collection.

     @param collectionToAdd the TREs to add.
     */
    void add(TreCollection collectionToAdd);

    /**
     Get the names of the TREs in the collection.
     <p>
     This method returns a unique list of TRE names. That list can be
     iterated over with getTREsWithName() to get the TREs.

     @return the TRE names.
     */
    List<String> getUniqueNamesOfTRE();

    /**
     Get the TREs that have a specific name.

     @param nameToMatch the name of the TREs to match.
     @return list of TREs with a specific name.
     */
    List<Tre> getTREsWithName(String nameToMatch);

    /**
     Check whether this collection has any TREs.

     @return true if there are any TREs, or false if there no TREs in the collection
     */
    boolean hasTREs();
}
