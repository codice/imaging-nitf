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

/**
 * Results of validity checks on a TRE.
 */
public class ValidityResult {

    /**
     * Set of validity status values.
     */
    public enum ValidityStatus {

        /**
         * The validity check did not identify any issues in the TRE.
         */
        VALID,

        /**
         * At least one issue with the TRE values was detected during validity checks.
         */
        NOT_VALID
    };

    private ValidityStatus mValidityStatus = ValidityStatus.VALID;

    private String mValidityResultDescription = "Valid";

    /**
     * Create a new validity result.
     *
     * The default values are that the result is valid.
     */
    public ValidityResult() {
    }

    /**
     * Get the validity status for this result.
     *
     * @return a ValidityStatus value.
     */
    public final ValidityStatus getValidityStatus() {
        return mValidityStatus;
    }

    /**
     * Set the validity status for this result.
     *
     * @param validityStatus a ValidityStatus value.
     */
    public final void setValidityStatus(final ValidityStatus validityStatus) {
        mValidityStatus = validityStatus;
    }

    /**
     * Get a text description of the validity result.
     *
     * This is intended to be suitable for showing to an informed user.
     *
     * @return text identify any validity issues, or that the TRE is valid.
     */
    public final String getValidityResultDescription() {
        return mValidityResultDescription;
    }

    /**
     * Set a text description of the validity result.
     *
     * This should be suitable for showing to an informed user.
     *
     * @param validityResultDescription the text description.
     */
    public final void setValidityResultDescription(final String validityResultDescription) {
        mValidityResultDescription = validityResultDescription;
    }

    /**
     * Check if this result is valid.
     *
     * @return true if it is valid, otherwise false.
     */
    public final boolean isValid() {
        return (getValidityStatus().equals(ValidityStatus.VALID));
    }
}
