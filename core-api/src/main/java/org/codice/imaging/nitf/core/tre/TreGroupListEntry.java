package org.codice.imaging.nitf.core.tre;

import java.util.List;

/**
 * Groups entry within a TRE.
 * <p>
 * This is a name and a group of entries.
 */
public interface TreGroupListEntry extends TreEntry {

    /**
     * Return the groups for this TRE entry.
     *
     * @return the groups for this TRE entry.
     */
    List<TreGroup> getGroups();

}
