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

import java.time.LocalDate;
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.tre.Tre;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Wrapper for the Imagery Access Person Identification Extension (PIAPEB).
 *
 * From STDI-0002 Version 4, Appendix C:
 * "The Person Extension is designed to identify information contained in the
 * Imagery Archive that is directly related to a person(s) contained in a data
 * type (image, symbol, label, and text). This extension shall be present for
 * each person identified in a data type. There may be up to 500 occurrences of
 * this extension for each data type in an NITF file. When present, these
 * extension(s) shall be contained within the appropriate data type (image,
 * symbol, label or text) extended subheader data field of the data type
 * subheader or within an overflow DES if there is insufficient room to place
 * the entire extension(s) within the data type extended subheader data field."
 *
 */
public class PIAPEB extends FlatTreWrapper {

    private static final String TAG_NAME = "PIAPEB";

    /**
     * Create a PIAPEB TRE wrapper from an existing TRE.
     *
     * @param tre the TRE to wrap. Must match the PIAPEB tag.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public PIAPEB(final Tre tre) throws NitfFormatException {
        super(tre, TAG_NAME);
    }

    /**
     * Create a new PIAPEB TRE.
     *
     * @param treSource the source location that this TRE is for.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public PIAPEB(final TreSource treSource) throws NitfFormatException {
        super(TAG_NAME, treSource);
        // set default values
        addOrUpdateEntry("LASTNME", "", "string");
        addOrUpdateEntry("FIRSTNME", "", "string");
        addOrUpdateEntry("MIDNME", "", "string");
        addOrUpdateEntry("DOB", "", "string");
        addOrUpdateEntry("ASSOCTRY", "", "string");
    }

    /**
     * Set / update the last name field value.
     *
     * From STDI-0002 Appendix C: "Identifies the surname of individual captured in an image."
     *
     * @param lastName the new last name.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final void setLastName(final String lastName) throws NitfFormatException {
        addOrUpdateEntry("LASTNME", lastName, "string");
    }

    /**
     * Get the last name field value.
     *
     * From STDI-0002 Appendix C: "Identifies the surname of individual captured in an image."
     *
     * @return the last name, with any trailing spaces removed.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getLastName() throws NitfFormatException {
        return getValueAsTrimmedString("LASTNME");
    }

    /**
     * Set / update the first name field value.
     *
     * From STDI-0002 Appendix C: "Identifies the first name of individual captured in an image."
     *
     * @param firstName the new first name.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final void setFirstName(final String firstName) throws NitfFormatException {
        addOrUpdateEntry("FIRSTNME", firstName, "string");
    }

    /**
     * Get the first name field value.
     *
     * From STDI-0002 Appendix C: "Identifies the first name of individual captured in an image."
     *
     * @return the first name, with any trailing spaces removed.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getFirstName() throws NitfFormatException {
        return getValueAsTrimmedString("FIRSTNME");
    }

    /**
     * Set / update the middle name field value.
     *
     * From STDI-0002 Appendix C: "Identifies the middle name of individual captured in an image."
     *
     * @param middleName the new middle name.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final void setMiddleName(final String middleName) throws NitfFormatException {
        addOrUpdateEntry("MIDNME", middleName, "string");
    }

    /**
     * Get the middle name field value.
     *
     * From STDI-0002 Appendix C: "Identifies the middle name of individual captured in an image."
     *
     * @return the middle name, with any trailing spaces removed.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getMiddleName() throws NitfFormatException {
        return getValueAsTrimmedString("MIDNME");
    }

    /**
     * Set the date of birth field value.
     *
     * From STDI-0002 Appendix C: "Identifies the birth date of the individual captured in the image."
     *
     * @param dob date of birth (local date), or null if not known.
     *
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final void setBirthDate(final LocalDate dob) throws NitfFormatException {
        addOrUpdateEntry("DOB", dob);
    }

    /**
     * Get the date of birth field value.
     *
     * From STDI-0002 Appendix C: "Identifies the birth date of the individual captured in the image."
     *
     * @return the date of birth, or null if the field is not filled in / not valid
     * as a date.
     *
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final LocalDate getBirthDate() throws NitfFormatException {
        String fieldName = "DOB";
        return getValueAsLocalDate(fieldName);
    }

    /**
     * Set / update the associated country field value.
     *
     * From STDI-0002 Appendix C: "Identifies the country the person captured in
     * the image is/are associated with."
     *
     * This is documented as a FIPS 10-4 code in STDI-0002 Version 4.
     *
     * @param associatedCountry the new associated country code.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final void setAssociatedCountry(final String associatedCountry) throws NitfFormatException {
        addOrUpdateEntry("ASSOCTRY", associatedCountry, "string");
    }

    /**
     * Get the associated country field value.
     *
     * From STDI-0002 Appendix C: "Identifies the country the person captured in
     * the image is/are associated with."
     *
     * This is documented as a FIPS 10-4 code in STDI-0002 Version 4.
     *
     * @return the associated country code (FIPS 10-4), or two spaces.
     * @throws NitfFormatException if there is a parsing issue.
     */
    public final String getAssociatedCountryCode() throws NitfFormatException {
        return mTre.getFieldValue("ASSOCTRY");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ValidityResult getValidity() throws NitfFormatException {
        for (String tagName : new String[] {"LASTNME", "FIRSTNME", "MIDNME", "ASSOCTRY"}) {
            if (mTre.getFieldValue(tagName) == null) {
                ValidityResult validity = new ValidityResult();
                validity.setValidityStatus(ValidityResult.ValidityStatus.NOT_VALID);
                validity.setValidityResultDescription(String.format("%s may not be null", tagName));
                return validity;
            }
        }
        return new ValidityResult();
    }
}
