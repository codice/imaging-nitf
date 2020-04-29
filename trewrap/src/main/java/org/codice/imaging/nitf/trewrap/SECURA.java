/*
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 */
package org.codice.imaging.nitf.trewrap;

import static org.codice.imaging.nitf.core.common.FileType.NITF_TWO_ONE;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import org.codice.imaging.nitf.core.common.DateTime;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.impl.DateTimeParser;
import org.codice.imaging.nitf.core.common.impl.NitfInputStreamReader;
import org.codice.imaging.nitf.core.security.SecurityMetadata;
import org.codice.imaging.nitf.core.security.impl.SecurityMetadataParser;
import org.codice.imaging.nitf.core.tre.Tre;

/**
 * Wrapper for the Extended Security Marking Metadata (SECURA) TRE.
 *
 * From STDI-0002-1 Appendix AI: SECURA 1.0
 * The Extended Security Marking Metadata (SECURA) Tagged Record Extension (TRE) was developed to
 * provide additional security marking metadata for NITF 2.0 and NITF 2.1 files. NITF was developed
 * in the 1990s and the security data available in the file header and subheaders was based on the
 * requirements of that time. This TRE is designed to supplement the original data in a general
 * manner to be fully flexible moving forward and allow ingest by any systems into the National
 * System for Geospatial Intelligence (NSG). Using SECURA, security marking metadata can be provided
 * using ODNI Intelligence Community Technical Specification, XML Data Encoding Specification for
 * Access Rights and Handling (ARH.XML)
 *
 */
public class SECURA extends TreWrapper {

    private static final String TAG_NAME = "SECURA";

    /**
     * Invalid version message.
     */
    protected static final String INVALID_VERSION = "NITF version %s is not NITF 2.0 nor NITF 2.1.";

    /**
     *  Invalid security standard message.
     */
    protected static final String INVALID_SECURITY_STANDARD = "Security Standard %s is not registered with the NITF Technical Board.";

    /**
     * Invalid compression message.
     */
    protected static final String INVALID_COMPRESSION = "Compression is not using GZIP.";


    /**
     * Create a SECURA TRE wrapper from an existing TRE.
     *
     * @param tre the TRE to wrap. Must match the ACFTB tag.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public SECURA(final Tre tre) throws NitfFormatException {
        super(tre, TAG_NAME);
    }

    /**
     * Get the NITF Date Time Field.
     *
     * Byte copy of the associated FDT field in the NITF file.  CCYYMMDDhhmmss( NITF 2.1) DDHHMMSSZMONYY (NITF 2.0)
     *
     * @return the Nitf Date Time Field as a string.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final DateTime getNitfDateTimeField() throws NitfFormatException {
        InputStream stream = new ByteArrayInputStream(getValueAsTrimmedString("FDATTIM").getBytes(StandardCharsets.ISO_8859_1));
        NitfInputStreamReader nitfReader = new NitfInputStreamReader(stream);
        nitfReader.setFileType(getNitfVersion());
        DateTimeParser dateTimeParser = new DateTimeParser();
        return dateTimeParser.readNitfDateTime(nitfReader);
    }

    /**
     * Get the NITF version field value.
     *
     * The version of the NITF file.
     *
     * @return the FileType enum.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final FileType getNitfVersion() throws NitfFormatException {
        return FileType.getEnumValue(getValueAsTrimmedString("NITFVER"));
    }

    /**
     * Get the NITF Security Fields.
     *
     * Associated segment's security fields.
     *
     * @return a SecurityMetadata object.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final SecurityMetadata getNitfSecurityFields() throws NitfFormatException {
        final SecurityMetadata securityMetadata;
        if (getNitfVersion() == NITF_TWO_ONE || getNitfVersion() == FileType.NITF_TWO_ZERO) {
            InputStream stream = new ByteArrayInputStream(getFieldValue("NFSECFLDS").getBytes(StandardCharsets.ISO_8859_1));
            SecurityMetadataParser parser = new SecurityMetadataParser();
            NitfInputStreamReader nitfReader = new NitfInputStreamReader(stream);
            nitfReader.setFileType(getNitfVersion());
            securityMetadata = parser.parseSecurityMetadata(nitfReader);
        } else {
            securityMetadata = null;
                throw new NitfFormatException("Could not parse Security Fields");
        }
        return securityMetadata;
    }

    /**
     * Get Security Standard field value.
     *
     * The security standard used to populate the SECURITY field.
     *
     * @return the security standard string
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getSecurityStandard() throws NitfFormatException {
        return getValueAsTrimmedString("SECSTD");
    }

    /**
     * Get the SECURITY field compression.
     *
     * This field identifies the compression used, if any, of the SECURITY field.
     *
     * @return the SECURITY field compression string
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getSecurityFieldCompression() throws NitfFormatException {
        return getValueAsTrimmedString("SECCOMP");
    }

    /**
     * Get the length of the SECURITY field.
     *
     * This field holds the length of the SECURITY field in bytes.
     *
     * @return SECURITY field length
     * @throws NitfFormatException if there is a parsing issue on the NITF side
     */
    public final int getSecurityLength() throws NitfFormatException {
        return getValueAsInteger("SECLEN");
    }

    /**
     * Get the Security field value.
     *
     * The actual security data, as encoded using the security standard specified in the
     * SECSTD field and compressed as specified by the SECCOMP field.
     *
     * @return the Security data
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final byte[] getSecurity() throws NitfFormatException {
        return getFieldValue("SECURITY").getBytes(StandardCharsets.ISO_8859_1);
    }

    /**
     * Get the Security field value.
     *
     * The actual security data, as encoded using the security standard specified in the
     * SECSTD field and compressed as specified by the SECCOMP field.
     *
     * @return the uncompressed Security data
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getSecurityUncompressed() throws NitfFormatException {
        if (getSecurityFieldCompression().equals("GZIP")) {
            byte[] security = getSecurity();

            StringBuilder sb = new StringBuilder();
            try (ByteArrayInputStream bis = new ByteArrayInputStream(security);
            GZIPInputStream gis = new GZIPInputStream(bis);
            BufferedReader br = new BufferedReader(new InputStreamReader(gis, StandardCharsets.ISO_8859_1))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            } catch (IOException e) {
                throw new NitfFormatException("Could not uncompress SECURITY field." + e.getLocalizedMessage());
            }
            return sb.toString();
        }
        return getFieldValue("SECURITY");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ValidityResult getValidity() throws NitfFormatException {
        if (getNitfVersion() != FileType.NITF_TWO_ZERO  && getNitfVersion() != NITF_TWO_ONE) {
            return new ValidityResult(ValidityResult.ValidityStatus.NOT_VALID,
                    String.format(INVALID_VERSION, getNitfVersion().getTextEquivalent()));
        }
        if (!getSecurityStandard().equals("ARH.XML")) {
            return new ValidityResult(ValidityResult.ValidityStatus.NOT_VALID,
                    String.format(INVALID_SECURITY_STANDARD, getSecurityStandard()));
        }
        if (!getSecurityFieldCompression().isEmpty() && !getSecurityFieldCompression().equals("GZIP")) {
            return new ValidityResult(ValidityResult.ValidityStatus.NOT_VALID, INVALID_COMPRESSION);
        }
        return new ValidityResult();
    }
}
