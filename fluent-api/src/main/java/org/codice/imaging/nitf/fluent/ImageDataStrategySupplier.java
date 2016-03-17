package org.codice.imaging.nitf.fluent;

import java.util.function.Supplier;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import org.codice.imaging.nitf.core.ConfigurableHeapStrategy;
import org.codice.imaging.nitf.core.FileBackedHeapStrategy;
import org.codice.imaging.nitf.core.HeapStrategy;
import org.codice.imaging.nitf.core.HeapStrategyConfiguration;
import org.codice.imaging.nitf.core.InMemoryHeapStrategy;

/**
 * Supplies an ImageDataStrategy to the SlottedNitfParsingStrategy.
 * <p/>
 * example:
 * <pre>
 *     {@code
 *         new NitfParserInputFlow()
 *             .file(getFile())
 *             .imageDataStrategy(new ImageDataStrategySupplier().inMemory())
 *             .allData()
 *             .forEachImageSegment(image -> render(image));
 *     }
 * </pre>
 */
public class ImageDataStrategySupplier implements Supplier<HeapStrategy<ImageInputStream>> {
    private HeapStrategy<ImageInputStream> imageDataStrategy;

    /**
     * Creates an instance of ConfigurableImageDataStrategy using the supplied values.
     *
     * @param maximumSize The absolute maximum number of pixels that should be stored.  If the
     *                    input image is larger than this, it will be ignored.
     * @return this ImageDataStrategySupplier.
     */
    public ImageDataStrategySupplier configure(long maximumSize) {
        HeapStrategyConfiguration config = new HeapStrategyConfiguration(maximumSize);
        this.imageDataStrategy = new ConfigurableHeapStrategy<ImageInputStream>(config, null, null);
        return this;
    }

    /**
     * Creates an instance of FileBackedImageDataStrategy.  All images are stored in temporary
     * files until ready to be rendered.  This method decreases heap usage, but increases rendering
     * time.
     *
     * @return this ImageDataStrategySupplier.
     */
    public ImageDataStrategySupplier file() {
        this.imageDataStrategy = new FileBackedHeapStrategy<>(
                file -> new FileImageInputStream(file));
        return this;
    }

    /**
     * Creates an instance of InMemoryImageDataStrategy.  All images are stored in memory.  This
     * strategy generally uses more heap space, but renders in shorter time than file().
     *
     * @return this ImageDataStrategySupplier.
     */
    public ImageDataStrategySupplier inMemory() {
        this.imageDataStrategy = new InMemoryHeapStrategy<>(
                is -> new MemoryCacheImageInputStream(is));
        return this;
    }

    @Override
    public HeapStrategy<ImageInputStream> get() {
        return this.imageDataStrategy;
    }
}
