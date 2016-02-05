package org.codice.imaging.nitf.render.imagerep;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;

import javax.imageio.stream.ImageInputStream;

public class Rgb24ImageRepresentationHandler implements ImageRepresentationHandler {
    @Override
    public void renderPixelBand(DataBuffer data, int pixelIndex, ImageInputStream imageInputStream, int bandIndex)
            throws IOException {
        data.setElem(pixelIndex, data.getElem(pixelIndex) | (imageInputStream.read() << (8 * (2 - bandIndex))));
    }

    @Override
    public BufferedImage createBufferedImage(int blockWidth, int blockHeight) {
        return new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_ARGB);
    }

    public void applyPixelMask(DataBuffer data, int pixelIndex) {
        data.setElem(pixelIndex, data.getElem(pixelIndex) | 0xFF000000);
    }

    public void renderPadPixel(DataBuffer data, int pixelIndex) {
        data.setElem(pixelIndex, 0x00000000);
    }
}
