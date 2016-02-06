package org.codice.imaging.nitf.render.imagerep;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;
import org.codice.imaging.nitf.core.image.ImageSegment;

public class Rgb24ImageRepresentationHandler implements ImageRepresentationHandler {

    @Override
    public void renderPixelBand(DataBuffer data, int pixelIndex, ImageSegment imageSegment, int bandIndex)
            throws IOException {
        int pixelShift = getPixelShiftForBand(imageSegment, bandIndex);
        data.setElem(pixelIndex, data.getElem(pixelIndex) | (imageSegment.getData().read() << pixelShift));
    }

    @Override
    public BufferedImage createBufferedImage(int blockWidth, int blockHeight) {
        return new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void applyPixelMask(DataBuffer data, int pixelIndex) {
        data.setElem(pixelIndex, data.getElem(pixelIndex) | 0xFF000000);
    }

    @Override
    public void renderPadPixel(DataBuffer data, int pixelIndex) {
        data.setElem(pixelIndex, 0x00000000);
    }

    private int getPixelShiftForBand(ImageSegment imageSegment, int bandIndex) {
        int leftShift;
        switch (imageSegment.getImageBandZeroBase(bandIndex).getImageRepresentation()) {
            case "R":
                leftShift = 16;
                break;
            case "G":
                leftShift = 8;
                break;
            case "B":
                leftShift = 0;
                break;
            default:
                leftShift = NOT_VISIBLE_MAPPED;
        }
        return leftShift;
    }
}
