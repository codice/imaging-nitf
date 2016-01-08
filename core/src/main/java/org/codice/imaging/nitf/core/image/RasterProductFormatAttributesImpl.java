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
package org.codice.imaging.nitf.core.image;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
   Raster Product Format (RPF) attributes.
*/
class RasterProductFormatAttributesImpl
        implements RasterProductFormatAttributes {

    private static final int GLOBAL_AREAL_INDEX = 0;

    private Map<Integer, String> currencyDates = new HashMap<Integer, String>();
    private Map<Integer, String> productionDates = new HashMap<Integer, String>();
    private Map<Integer, String> significantDates = new HashMap<Integer, String>();

    /**
        Add a currency date.
        <p>
        This is the date of the most recent revision to the RPF product, in the form YYYYMMDD.

        @param arealIndex the areal index for the currency date (0 for whole file)
        @param date the currency date.
    */
    public final void addCurrencyDate(final int arealIndex, final String date) {
        currencyDates.put(arealIndex, date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getCurrencyDate() {
        return currencyDates.get(GLOBAL_AREAL_INDEX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Set<Integer> getCurrencyDateArealReferences() {
        return currencyDates.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getCurrencyDate(final int arealReference) {
        return currencyDates.get(arealReference);
    }

    /**
        Add a production date.
        <p>
        This is the date that the source product was transformed into RPF, in the form YYYYMMDD.

        @param arealIndex the areal index for the production date (0 for whole file)
        @param date the production date.
    */
    public final void addProductionDate(final int arealIndex, final String date) {
        productionDates.put(arealIndex, date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getProductionDate() {
        return productionDates.get(GLOBAL_AREAL_INDEX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Set<Integer> getProductionDateArealReferences() {
        return productionDates.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getProductionDate(final int arealReference) {
        return productionDates.get(arealReference);
    }

    /**
        Add a significant date.
        <p>
        This is the "date that most accurately describes the basic date of the source product, in the form
        YYYYMMDD. It can be the completion date, compilation date, collection date, revision date, or
        other date depending on the product and circumstances."

        @param arealIndex the areal index for the significant date (0 for whole file)
        @param date the significant date.
    */
    public final void addSignificantDate(final int arealIndex, final String date) {
        significantDates.put(arealIndex, date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSignificantDate() {
        return significantDates.get(GLOBAL_AREAL_INDEX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Set<Integer> getSignificantDateArealReferences() {
        return significantDates.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSignificantDate(final int arealReference) {
        return significantDates.get(arealReference);
    }
}
