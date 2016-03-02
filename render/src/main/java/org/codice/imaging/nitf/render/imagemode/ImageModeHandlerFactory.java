package org.codice.imaging.nitf.render.imagemode;

import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandler;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandlerFactory;

/**
 * Factory class for creating image mode handlers.
 */
public class ImageModeHandlerFactory {

    private ImageModeHandlerFactory() {
    }

    /**
     * Get an appropriate image mode handler for the specified image segment.
     *
     * @param imageSegment the image segment specifying the image
     * characteristics.
     * @return a handler for the image mode, or null if an appropriate reader
     * could not be found.
     */
    public static ImageModeHandler forImageSegment(ImageSegment imageSegment) {
        ImageRepresentationHandler imageRepresentationHandler =
                ImageRepresentationHandlerFactory.forImageSegment(imageSegment);

        if (imageRepresentationHandler == null) {
            return null;
        }

        switch (imageSegment.getImageMode()) {
        case BANDSEQUENTIAL: {
            return new BandSequentialImageModeHandler(imageRepresentationHandler);
        }

        case PIXELINTERLEVE: {
            return new PixelInterleveImageModeHandler(imageRepresentationHandler);
        }

        case ROWINTERLEVE: {
            return new RowInterleveImageModeHandler(imageRepresentationHandler);
        }

        case BLOCKINTERLEVE: {
            return new BlockInterleveImageModeHandler(imageRepresentationHandler);
        }

        default: {
            throw new UnsupportedOperationException("Unsupported Image Mode: " +
                    imageSegment.getImageMode().name());
        }
        }
    }
}
