package org.codice.imaging.nitf.common.security;

/**
 File security metadata.
 <p>
 The security metadata at the file level is the same as the subheaders, except for
 two extra fields (copy number, and number of copies).
 */
public interface NitfFileSecurityMetadata extends NitfSecurityMetadata {

    /**
     Return the file copy number.
     <p>
     "This field shall contain the copy number of the file. If this field is all BCS zeros (0x30),
     it shall imply that there is no tracking of numbered file copies."
     <p>
     An empty string is also common, especially in NITF 2.0 files, since the specification marked
     this field as optional.

     @return the copy number
     */
    String getFileCopyNumber();

    /**
     Return the file number of copies.
     <p>
     "This field shall contain the total number of copies of the file. If this field is all BCS
     zeros (0x30), it shall imply that there is no tracking of numbered file copies."
     <p>
     An empty string is also common, especially in NITF 2.0 files, since the specification marked
     this field as optional.

     @return the number of copies.
     */
    String getFileNumberOfCopies();
}
