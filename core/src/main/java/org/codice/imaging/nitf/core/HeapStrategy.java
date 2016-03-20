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
package org.codice.imaging.nitf.core;

import java.text.ParseException;
import org.codice.imaging.nitf.core.common.NitfReader;

/**
 * HeapStrategy abstracts the process of extracting and storing NITF image data from the
 * parsing of other NITF elements.  Implementations of this interface can store data in JVM
 * heap, on disk, in a cache implementation like EhCache or Infinispan, etc.
 *
 * @param <R> The type to be returned by this heap strategy.
 */
public interface HeapStrategy<R> {

    /**
     * Handle one segment.
     *
     * @param reader the NitfReader which contains the segment data.
     * @param length the length of the segment data.
     * @return an InputStream to read the segment data from.
     * @throws ParseException when reading 'length' bytes from 'reader' causes one.
     */
    R handleSegment(final NitfReader reader, long length) throws ParseException;
}
