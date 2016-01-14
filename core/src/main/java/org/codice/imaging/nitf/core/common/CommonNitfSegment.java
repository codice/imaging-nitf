package org.codice.imaging.nitf.core.common;

import java.util.Map;

import org.codice.imaging.nitf.core.SecurityMetadata;
import org.codice.imaging.nitf.core.tre.TreCollection;

/**
 Common data elements for NITF segment subheaders.
 */
public interface CommonNitfSegment {

    /**
     Return the identifier (IID1/SID/LID/TEXTID) for the segment.
     <p>
     This field shall contain a valid alphanumeric identification code associated with the
     segment. The valid codes are determined by the application.

     @return the identifier
     */
    String getIdentifier();

    /**
     Return the security metadata for the segment.

     @return security metadata
     */
    SecurityMetadata getSecurityMetadata();

    /**
     Return the TREs for this segment, in raw form.

     @return TRE collection
     */
    TreCollection getTREsRawStructure();

    /**
     *
     * @return a java.util.Map containing the Tres.
     */
    Map<String, String> getTREsFlat();
}
