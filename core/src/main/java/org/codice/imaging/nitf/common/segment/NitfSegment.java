package org.codice.imaging.nitf.common.segment;

import java.util.Map;

import org.codice.imaging.nitf.common.TreCollection;
import org.codice.imaging.nitf.common.tre.TreEntry;

/**
 Common data elements for NITF segments.
 */
public interface    NitfSegment {
    /**
     Return the TREs for this segment, in raw form.

     @return TRE collection
     */
    TreCollection getTREsRawStructure();

    /**
     Return the TREs for this segment, flattened into a key-value pair map.
     <p>
     This will build appropriate namespaces for each entry.

     @return TRE map.
     */
    Map<String, String> getTREsFlat();

    /**
     Flatten out a TreEntryImpl.

     @param tresFlat the map to flatten into.
     @param treEntry the TreEntryImpl to flatten.
     @param parentName the name of the parent, required for namespacing.
     */
    void flattenOneTreEntry(final Map<String, String> tresFlat, final TreEntry treEntry, final String parentName);

    /**
     Merge a TreCollectionImpl into the TREs already associated with this segment.

     @param tresToAdd the TRE collection to add.
     */
    void mergeTREs(final TreCollection tresToAdd);
}
