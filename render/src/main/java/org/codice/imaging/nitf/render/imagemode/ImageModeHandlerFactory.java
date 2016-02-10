package org.codice.imaging.nitf.render.imagemode;

import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandler;
import org.codice.imaging.nitf.render.imagerep.ImageRepresentationHandlerFactory;

public class ImageModeHandlerFactory {
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
