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
package org.codice.imaging.nitf.core.dataextension;

import java.util.function.Consumer;

import javax.imageio.stream.ImageInputStream;

import org.codice.imaging.nitf.core.common.NitfFormatException;

/**
 * Implementation for user-defined elements of a DES.
 *
 * This is intended to be implemented for a DES data type (not TRE overflow).
 */
public interface UserDefinedDataExtensionSegment {

    /**
     * Get the identifier string (DESID) for this DES.
     *
     * From MIL-STD-2500C: "Unique DES Type Identifier. This field shall contain
     * a valid alphanumeric identifier properly registered with the ISMC."
     *
     * This is a 25 character field BCS-A field. If it is less than 25
     * characters, it will be padded out.
     *
     * @return DES identifier.
     */
    String getTypeIdentifier();

    /**
     * Get the version of the DES definition.
     *
     * Valid value range is 1 to 99.
     *
     * From MIL-STD-2500C: "Version of the Data Definition. This field shall
     * contain the alphanumeric version number of the use of the tag. The
     * version number is assigned as part of the registration process."
     *
     * The version is almost always 1.
     *
     * @return the DES version as a number.
     */
    int getVersion();

    /**
     * Get the user defined subheader.
     *
     * From MIL-STD-2500C: "DES User-defined Subheader Fields. This field shall
     * contain user-defined fields. Data in this field shall be alphanumeric,
     * formatted according to user specification."
     *
     * This is variable length, from 0 to 9999 bytes long. Only BCS-A values are
     * allowed in this subheader.
     *
     * @return the user defined subeader.
     * @throws NitfFormatException if there was a problem generating the subheader.
     */
    String getUserDefinedSubheader() throws NitfFormatException;

    /**
     * Returns a consumer that gets user defined DES data. The consumer also
     * contains a call back consumer to be called at the end of the operation.
     *
     * From MIL-STD-2500C: "DES User-Defined Data. This field shall contain data
     * of either binary or character types defined by and formatted according to
     * the user’s specification. [...] The length of this field shall not cause
     * another NITF field length limits to be exceeded, but is otherwise fully
     * user-defined."
     *
     * @return Consumer to get the user-defined DES data.
     */
    Consumer<Consumer<ImageInputStream>> getUserDataConsumer();

    /**
     * Get the combined length of the user-defined DES files.
     *
     * @return the combined length of the user-defined DES files.
     */
    long getLength();

}
