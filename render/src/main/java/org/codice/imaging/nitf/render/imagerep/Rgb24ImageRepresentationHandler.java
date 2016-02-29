package org.codice.imaging.nitf.render.imagerep;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.util.Map;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.render.ImageMask;

class Rgb24ImageRepresentationHandler implements ImageRepresentationHandler {
    private final Map<Integer, Integer> bandMapping;

    public Rgb24ImageRepresentationHandler(Map<Integer, Integer> bandMapping) {
        this.bandMapping = bandMapping;
    }

    @Override
    public void renderPixelBand(DataBuffer data, int pixelIndex, ImageInputStream imageInputStream, int bandIndex)
            throws IOException {
        data.setElem(pixelIndex,
                0xFF000000 | data.getElem(pixelIndex) | (imageInputStream.read() << bandMapping.get(bandIndex)));
    }

    @Override
    public BufferedImage createBufferedImage(int blockWidth, int blockHeight) {
        return new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void renderPadPixel(ImageMask imageMask, DataBuffer data, int pixelIndex) {
        if (imageMask.isPadPixel(data.getElem(pixelIndex))) {
            data.setElem(pixelIndex, 0x00000000);
        }
    }
}
