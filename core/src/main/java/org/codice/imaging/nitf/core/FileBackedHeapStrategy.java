package org.codice.imaging.nitf.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.function.Function;

import org.codice.imaging.nitf.core.common.NitfReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of HeapStrategy that stores the image data in a temporary file and
 * returns an FileImageImputStream pointing to that.
 *
 * @param <R> - the return type for this heap strategy.
 */
public class FileBackedHeapStrategy<R> implements HeapStrategy<R> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileBackedHeapStrategy.class);

    private Function<RandomAccessFile, R> resultConversionFunction;

    /**
     * @param resultConverter - a function that converts a RandomAccessFile to <T>
     */
    public FileBackedHeapStrategy(final Function<RandomAccessFile, R> resultConverter) {
        this.resultConversionFunction = resultConverter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final R handleSegment(final NitfReader reader,
            final long dataLength) throws ParseException {
        LOGGER.info(String.format("Storing %s bytes in temporary file.", dataLength));
        byte[] bytes = reader.readBytesRaw((int) dataLength);

        File dataFile = null;

        try {
            dataFile = File.createTempFile("nitf", (String) null);
            dataFile.deleteOnExit();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            fos.write(bytes);
            RandomAccessFile randomAccessFile = new RandomAccessFile(dataFile, "rwd");
            R result = resultConversionFunction.apply(randomAccessFile);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
