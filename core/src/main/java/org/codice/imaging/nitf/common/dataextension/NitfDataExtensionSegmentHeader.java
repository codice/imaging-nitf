package org.codice.imaging.nitf.common.dataextension;

import org.codice.imaging.nitf.common.FileType;
import org.codice.imaging.nitf.common.TreCollection;
import org.codice.imaging.nitf.common.security.NitfSecurityMetadata;
import org.codice.imaging.nitf.common.segment.CommonNitfSegment;


/**
 Data Extension Segment (DES) subheader and associated data.
 */
public interface NitfDataExtensionSegmentHeader extends CommonNitfSegment {
    /**
     Set the DES version (DESVER).
     <p>
     "This field shall contain
     the alphanumeric version number of the use of the tag.
     The version number is assigned as part of the
     registration process."

     @param version the version (valid range 1 to 99).
     */
    void setDESVersion(int version);

    /**
     Return the DES version (DESVER).
     <p>
     "This field shall contain
     the alphanumeric version number of the use of the tag.
     The version number is assigned as part of the
     registration process."

     @return the DES version.
     */
    int getDESVersion();

    /**
     Set the overflowed header type.
     <p>
     For NITF 2.0: <i>This field shall be present if DESTAG = "Registered Extensions" or "Controlled
     Extensions." Its presence indicates that the DES contains a tagged record extension
     that would not fit in the file header or component header where it would ordinarily
     be located. Its value indicates the data type to which the enclosed tagged record is
     relevant. If the value of DESTAG is "Controlled Extensions," the valid values for
     DESOFLOW are XHD, IXSHD, SXSHD, LSXHD or TXSHD. If the value of
     DESTAG is "Registered Extensions," the valid values for DESOFLW are UDHD
     and UDID.</i> [From MIL-STD-2500A TABLE XVIII. Data extension segment subheader field definitions]
     <p>
     For NITF 2.1 / NSIF 1.0: <i>DES Overflowed Header Type. This field shall be
     present if DESID = TRE_OVERFLOW. Its presence
     indicates that the DES contains a TRE that would not fit
     in the file header or segment subheader where it would
     ordinarily be located. Its value indicates the segment
     type to which the enclosed TRE is relevant.</i> [From MIL-STD-2500C TABLE A-8. NITF Data Extension
     Segment (DES) subheader].
     <p>
     The valid values for NITF 2.1 / NSIF 1.0 are XHD, IXSHD, SXSHD, TXSHD, UDHD and UDID.

     @param headerType the overflowed header type.
     */
    void setOverflowedHeaderType(String headerType);

    /**
     Return the overflowed header type.
     <p>
     For NITF 2.0: <i>This field shall be present if DESTAG = "Registered Extensions" or "Controlled
     Extensions." Its presence indicates that the DES contains a tagged record extension
     that would not fit in the file header or component header where it would ordinarily
     be located. Its value indicates the data type to which the enclosed tagged record is
     relevant. If the value of DESTAG is "Controlled Extensions," the valid values for
     DESOFLOW are XHD, IXSHD, SXSHD, LSXHD or TXSHD. If the value of
     DESTAG is "Registered Extensions," the valid values for DESOFLW are UDHD
     and UDID.</i> [From MIL-STD-2500A TABLE XVIII. Data extension segment subheader field definitions]
     <p>
     Note that DESTAG is the identifier (getIdentifier()) for NITF 2.0.
     <p>
     For NITF 2.1 / NSIF 1.0: <i>This field shall be
     present if DESID = TRE_OVERFLOW. Its presence
     indicates that the DES contains a TRE that would not fit
     in the file header or segment subheader where it would
     ordinarily be located. Its value indicates the segment
     type to which the enclosed TRE is relevant.</i>
     [From MIL-STD-2500C TABLE A-8. NITF Data Extension Segment (DES) subheader].
     <p>
     The valid values for NITF 2.1 / NSIF 1.0 are XHD, IXSHD, SXSHD, TXSHD, UDHD and UDID.

     @return the overflowed header type.
     */
    String getOverflowedHeaderType();

    /**
     Set the item that was overflowed.
     <p>
     For NITF 2.0: <i>This field shall be present if DESOFLW is present. It shall contain the number of
     the data item in the file, of the type indicated in DESOFLW to which the tagged
     record extensions in the segment apply. For example, if DESOFLW = UDID and
     DESITEM = 3, then the tagged record extensions in the segment applies to the third
     image in the file. If the value of DESOFLW = UDHD, the value of DESITEM
     shall be 0."</i> [From MIL-STD-2500A TABLE XVIII. Data extension segment subheader field definitions]
     <p>
     For NITF 2.1 / NSIF 1.0: <i>This field shall be present
     if DESOFLW is present. It shall contain the number of
     the data item in the file, of the type indicated in
     DESOFLW to which the TRE in the segment apply. For
     example, if DESOFLW = UDID and DESITEM = 003,
     then the TRE in the segment apply to the third image in
     the file. If the value of DESOFLW = UDHD, the value
     of DESITEM shall be BCS zeros (0x30).</i>
     [From MIL-STD-2500C TABLE A-8. NITF Data Extension Segment (DES) subheader].

     @param itemOverflowed the overflowed item
     */
    void setItemOverflowed(int itemOverflowed);

    /**
     Set the item that was overflowed.
     <p>
     For NITF 2.0: <i>This field shall be present if DESOFLW is present. It shall contain the number of
     the data item in the file, of the type indicated in DESOFLW to which the tagged
     record extensions in the segment apply. For example, if DESOFLW = UDID and
     DESITEM = 3, then the tagged record extensions in the segment applies to the third
     image in the file. If the value of DESOFLW = UDHD, the value of DESITEM
     shall be 0."</i> [From MIL-STD-2500A TABLE XVIII. Data extension segment subheader field definitions]
     <p>
     For NITF 2.1 / NSIF 1.0: <i>type to which the enclosed TRE is relevant.
     DES Data Item Overflowed. This field shall be present
     if DESOFLW is present. It shall contain the number of
     the data item in the file, of the type indicated in
     DESOFLW to which the TRE in the segment apply. For
     example, if DESOFLW = UDID and DESITEM = 003,
     then the TRE in the segment apply to the third image in
     the file. If the value of DESOFLW = UDHD, the value
     of DESITEM shall be BCS zeros (0x30).</i>
     [From MIL-STD-2500C TABLE A-8. NITF Data Extension Segment (DES) subheader].

     @return the overflowed item
     */
    int getItemOverflowed();

    /**
     Set the user defined subheader data (DESSHF).
     <p>
     This can only be non-null if the identifier is not "Registered Extensions" or "Controlled
     Extensions" (NITF 2.0) or "TRE_OVERFLOW" (NITF 2.1 / NSIF 1.0).

     @param data the user defined subheader data
     */
    void setUserDefinedSubheaderField(String data);

    /**
     Set the user defined subheader data (DESSHF).
     <p>
     This will be null if the identifier is "Registered Extensions" or "Controlled
     Extensions" (NITF 2.0) or "TRE_OVERFLOW" (NITF 2.1 / NSIF 1.0).

     @return the user defined subheader data
     */
    String getUserDefinedSubheaderField();

    /**
     Set the data extension segment data length.
     <p>
     This is the length of the contents of the associated data segment.

     @param length the data extension segment data segment length, in bytes
     */
    void setDataExtensionSegmentDataLength(int length);

    /**
     Return the data extension segment data length.

     @return the data extension segment length, in bytes
     */
    int getDataExtensionSegmentDataLength();

    /**
     * Check if this DES is a NITF 2.1 TRE overflow DES.
     *
     * @return true for NITF 2.1 TRE overflow, otherwise false
     */
    boolean isTreOverflowNitf21();

    /**
     * Check if this DES is a NITF 2.0 TRE overflow DES.
     *
     * @return true for NITF 2.0 TRE overflow, otherwise false
     */
    boolean isTreOverflowNitf20();

    /**
     * Check if this DES is a TRE overflow DES.
     * <p>
     * This handles both NITF 2.0 and NITF 2.1 / NSIF 1.0 files, but you need to specify which file type.
     *
     * @param fileType the file type (NITF version)
     * @return true for TRE overflow, otherwise false
     */
    boolean isTreOverflow(FileType fileType);

    /**
     {@inheritDoc}
     */
    String getIdentifier();

    /**
     {@inheritDoc}
     */
    NitfSecurityMetadata getSecurityMetadata();

    /**
     {@inheritDoc}
     */
    TreCollection getTREsRawStructure();
}
