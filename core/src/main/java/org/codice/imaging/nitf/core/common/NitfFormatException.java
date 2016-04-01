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
package org.codice.imaging.nitf.core.common;

/**
 * Exception indicating a problem in the NITF file or stream format.
 */
public class NitfFormatException extends Exception {

    private long locationOffset = 0;

    /**
     * Construct empty NITF format exception.
     *
     * In general, you should try to pass something more specific.
     */
    public NitfFormatException() {
    }

    /**
     * Construct NITF format exception.
     *
     * If available, consider passing the location of the problem.
     *
     * @param message a text description of the format issue.
     */
    public NitfFormatException(final String message) {
        super(message);
    }

    /**
     * Construct NITF format exception specifying location.
     *
     * @param message a text description of the format issue.
     * @param offset the location (in bytes as a file offset) of the format issue.
     */
    public NitfFormatException(final String message, final long offset) {
        super(message);
        locationOffset = offset;
    }

    /**
     * Construct a NitfFormatException from a Throwable.
     *
     * @param cause the throwable issue.
     */
    public NitfFormatException(final Throwable cause) {
        super(cause);
    }

    /**
     * Construct a NitfFormatException from a Throwable.
     *
     * @param message additional information on the exception.
     * @param cause the throwable issue.
     */
    public NitfFormatException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Get the offset location for the format issue.
     *
     * @return location offset in bytes, or 0 if not available / specified.
     */
    public final long getOffset() {
        return locationOffset;
    }
}
