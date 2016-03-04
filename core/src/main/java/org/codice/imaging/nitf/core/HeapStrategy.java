package org.codice.imaging.nitf.core;

import java.text.ParseException;

import org.codice.imaging.nitf.core.common.NitfReader;

/**
 * HeapStrategy abstracts the process of extracting and storing NITF image data from the
 * parsing of other NITF elements.  Implementations of this interface can store data in JVM
 * heap, on disk, in a cache implementation like EhCache or Infinispan, etc.
 *
 * @param <R> - The type to be returned by this heap strategy.
 */
public interface HeapStrategy<R> {

    /**
     *
     * @param reader the NitfReader which contains the image data.
     * @param length the length of the image data segment.
     * @return an InputStream to read the segment data from.
     * @throws ParseException - when reading 'length' bytes from 'reader' causes one.
     */
    R handleSegment(final NitfReader reader, long length) throws ParseException;
}
