package org.codice.imaging.nitf.common.segment;

import org.codice.imaging.nitf.common.security.NitfSecurityMetadata;

/**
 Common data elements for NITF segment subheaders.
 */
public interface CommonNitfSegment extends NitfSegment {
    /**
     Set the identifier (IID1/SID/LID/TEXTID) for the segment.
     <p>
     This field shall contain a valid alphanumeric identification code associated with the
     segment. The valid codes are determined by the application.

     @param identifier the identifier for the segment
     */
    void setIdentifier(String identifier);

    /**
     Return the identifier (IID1/SID/LID/TEXTID) for the segment.
     <p>
     This field shall contain a valid alphanumeric identification code associated with the
     segment. The valid codes are determined by the application.

     @return the identifier
     */
    String getIdentifier();

    /**
     Set the security metadata elements for the segment.

     See NitfSecurityMetadataImpl for the various elements, which differ slightly between NITF 2.0 and NITF 2.1/NSIF 1.0.

     @param nitfSecurityMetadata the security metadata values to set.
     */
    void setSecurityMetadata(NitfSecurityMetadata nitfSecurityMetadata);

    /**
     Return the security metadata for the segment.

     @return security metadata
     */
    NitfSecurityMetadata getSecurityMetadata();
}
