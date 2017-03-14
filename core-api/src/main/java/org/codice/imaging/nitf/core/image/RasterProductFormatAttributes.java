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

import java.util.Set;

/**
 Raster Product Format (RPF) attributes.
 */
public interface RasterProductFormatAttributes {
    /**
     Return the currency date for the overall RPF product.
     <p>
     This is the date of the most recent revision to the RPF product, in the form YYYYMMDD.

     @return the currency date, or null if there is no global currency date.
     */
    String getCurrencyDate();

    /**
     Return the available areal references for currency date.
     <p>
     This may include 0, which is the (implicit) global areal reference.

     @return the areal references.
     */
    Set<Integer> getCurrencyDateArealReferences();

    /**
     Return the currency date for a given areal reference.

     @param arealReference the areal reference for the part of the image.
     @return the currency date for the part of the image, or null if there is no corresponding date
     */
    String getCurrencyDate(int arealReference);

    /**
     Return the production date for the overall RPF product.
     <p>
     This is the date that the source product was transformed into RPF, in the form YYYYMMDD.

     @return the production date, or null if there is no global production date.
     */
    String getProductionDate();

    /**
     Return the available areal references for production date.
     <p>
     This will usually only be 0, which is the (implicit) global areal reference.

     @return the areal references.
     */
    Set<Integer> getProductionDateArealReferences();

    /**
     Return the production date for a given areal reference.

     @param arealReference the areal reference for the part of the image.
     @return the production date for the part of the image, or null if there is no corresponding date
     */
    String getProductionDate(int arealReference);

    /**
     Return the significant date for the overall RPF product.
     <p>
     This is the "date that most accurately describes the basic date of the source product, in the form
     YYYYMMDD. It can be the completion date, compilation date, collection date, revision date, or
     other date depending on the product and circumstances."

     @return the significant date, or null if there is no global significant date.
     */
    String getSignificantDate();

    /**
     Return the available areal references for significant date.
     <p>
     This may include 0, which is the (implicit) global areal reference.

     @return the areal references.
     */
    Set<Integer> getSignificantDateArealReferences();

    /**
     Return the significant date for a given areal reference.

     @param arealReference the areal reference for the part of the image.
     @return the significant date for the part of the image, or null if there is no corresponding date
     */
    String getSignificantDate(int arealReference);
}
