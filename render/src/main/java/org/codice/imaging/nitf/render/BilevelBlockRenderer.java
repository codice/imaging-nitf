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
package org.codice.imaging.nitf.render;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageSegment;

class BilevelBlockRenderer implements BlockRenderer {

    private ImageSegment mImageSegment = null;
    private ImageInputStream mImageData = null;
    WritableRaster imgRaster = null;
    boolean lineMode2D = false;

    private static final int EOL = 0b000000000001;
    private static final int EOL_LENGTH_IN_BITS = 12;
    private static final int MAX_VALID_BITLENGTH_WHITE = 12;
    private static final int MAX_VALID_BITLENGTH_BLACK = 13;
    private static final int MAX_VALID_2D_CODEWORD_LENGTH = 7;
    private static final int MAX_TERMINATING_RUN_LENGTH = 63;
    private static final String TWOD_S_ENCODING = "2DS";
    private static final String TWOD_H_ENCODING = "2DH";

    private static final int WHITE = 0x1;
    private static final int BLACK = 0x0;

    private enum TwoDmode {
        Unknown,
        Pass,
        Horizontal,
        Vertical0,
        Vertical1Right,
        Vertical2Right,
        Vertical3Right,
        Vertical1Left,
        Vertical2Left,
        Vertical3Left
    }

    private static class CodebookEntry {
        private CodebookEntry(int codelength, int code, int result) {
            codeWordLength = codelength;
            codeWord = code;
            runLength = result;
        }

        private final int codeWordLength;
        private final int codeWord;
        private final int runLength;
    }


    private static final CodebookEntry [] whiteCodeBook = {
        new CodebookEntry(8, 0b00110101, 0),
        new CodebookEntry(6, 0b000111, 1),
        new CodebookEntry(4, 0b0111, 2),
        new CodebookEntry(4, 0b1000, 3),
        new CodebookEntry(4, 0b1011, 4),
        new CodebookEntry(4, 0b1100, 5),
        new CodebookEntry(4, 0b1110, 6),
        new CodebookEntry(4, 0b1111, 7),
        new CodebookEntry(5, 0b10011, 8),
        new CodebookEntry(5, 0b10100, 9),
        new CodebookEntry(5, 0b00111, 10),
        new CodebookEntry(5, 0b01000, 11),
        new CodebookEntry(6, 0b001000, 12),
        new CodebookEntry(6, 0b000011, 13),
        new CodebookEntry(6, 0b110100, 14),
        new CodebookEntry(6, 0b110101, 15),
        new CodebookEntry(6, 0b101010, 16),
        new CodebookEntry(6, 0b101011, 17),
        new CodebookEntry(7, 0b0100111, 18),
        new CodebookEntry(7, 0b0001100, 19),
        new CodebookEntry(7, 0b0001000, 20),
        new CodebookEntry(7, 0b0010111, 21),
        new CodebookEntry(7, 0b0000011, 22),
        new CodebookEntry(7, 0b0000100, 23),
        new CodebookEntry(7, 0b0101000, 24),
        new CodebookEntry(7, 0b0101011, 25),
        new CodebookEntry(7, 0b0010011, 26),
        new CodebookEntry(7, 0b0100100, 27),
        new CodebookEntry(7, 0b0011000, 28),
        new CodebookEntry(8, 0b00000010, 29),
        new CodebookEntry(8, 0b00000011, 30),
        new CodebookEntry(8, 0b00011010, 31),
        new CodebookEntry(8, 0b00011011, 32),
        new CodebookEntry(8, 0b00010010, 33),
        new CodebookEntry(8, 0b00010011, 34),
        new CodebookEntry(8, 0b00010100, 35),
        new CodebookEntry(8, 0b00010101, 36),
        new CodebookEntry(8, 0b00010110, 37),
        new CodebookEntry(8, 0b00010111, 38),
        new CodebookEntry(8, 0b00101000, 39),
        new CodebookEntry(8, 0b00101001, 40),
        new CodebookEntry(8, 0b00101010, 41),
        new CodebookEntry(8, 0b00101011, 42),
        new CodebookEntry(8, 0b00101100, 43),
        new CodebookEntry(8, 0b00101101, 44),
        new CodebookEntry(8, 0b00000100, 45),
        new CodebookEntry(8, 0b00000101, 46),
        new CodebookEntry(8, 0b00001010, 47),
        new CodebookEntry(8, 0b00001011, 48),
        new CodebookEntry(8, 0b01010010, 49),
        new CodebookEntry(8, 0b01010011, 50),
        new CodebookEntry(8, 0b01010100, 51),
        new CodebookEntry(8, 0b01010101, 52),
        new CodebookEntry(8, 0b00100100, 53),
        new CodebookEntry(8, 0b00100101, 54),
        new CodebookEntry(8, 0b01011000, 55),
        new CodebookEntry(8, 0b01011001, 56),
        new CodebookEntry(8, 0b01011010, 57),
        new CodebookEntry(8, 0b01011011, 58),
        new CodebookEntry(8, 0b01001010, 59),
        new CodebookEntry(8, 0b01001011, 60),
        new CodebookEntry(8, 0b00110010, 61),
        new CodebookEntry(8, 0b00110011, 62),
        new CodebookEntry(8, 0b00110100, 63),
        new CodebookEntry(5, 0b11011, 64),
        new CodebookEntry(5, 0b10010, 128),
        new CodebookEntry(6, 0b010111, 192),
        new CodebookEntry(7, 0b0110111, 256),
        new CodebookEntry(8, 0b00110110, 320),
        new CodebookEntry(8, 0b00110111, 384),
        new CodebookEntry(8, 0b01100100, 448),
        new CodebookEntry(8, 0b01100101, 512),
        new CodebookEntry(8, 0b01101000, 576),
        new CodebookEntry(8, 0b01100111, 640),
        new CodebookEntry(9, 0b011001100, 704),
        new CodebookEntry(9, 0b011001101, 768),
        new CodebookEntry(9, 0b011010010, 832),
        new CodebookEntry(9, 0b011010011, 896),
        new CodebookEntry(9, 0b011010100, 960),
        new CodebookEntry(9, 0b011010101, 1024),
        new CodebookEntry(9, 0b011010110, 1088),
        new CodebookEntry(9, 0b011010111, 1152),
        new CodebookEntry(9, 0b011011000, 1216),
        new CodebookEntry(9, 0b011011001, 1280),
        new CodebookEntry(9, 0b011011010, 1344),
        new CodebookEntry(9, 0b011011011, 1408),
        new CodebookEntry(9, 0b010011000, 1472),
        new CodebookEntry(9, 0b010011001, 1536),
        new CodebookEntry(9, 0b010011010, 1600),
        new CodebookEntry(6, 0b011000, 1664),
        new CodebookEntry(9, 0b010011011, 1728),
        new CodebookEntry(11, 0b00000001000, 1792),
        new CodebookEntry(11, 0b00000001100, 1856),
        new CodebookEntry(11, 0b00000001101, 1920),
        new CodebookEntry(12, 0b000000010010, 1984),
        new CodebookEntry(12, 0b000000010011, 2048),
        new CodebookEntry(12, 0b000000010100, 2112),
        new CodebookEntry(12, 0b000000010101, 2176),
        new CodebookEntry(12, 0b000000010110, 2240),
        new CodebookEntry(12, 0b000000010111, 2304),
        new CodebookEntry(12, 0b000000011100, 2368),
        new CodebookEntry(12, 0b000000011101, 2432),
        new CodebookEntry(12, 0b000000011110, 2496),
        new CodebookEntry(12, 0b000000011111, 2560),
    };

    private static final CodebookEntry [] blackCodeBook = {
        new CodebookEntry(10, 0b0000110111, 0),
        new CodebookEntry(3, 0b10, 1),
        new CodebookEntry(2, 0b11, 2),
        new CodebookEntry(2, 0b10, 3),
        new CodebookEntry(3, 0b011, 4),
        new CodebookEntry(4, 0b0011, 5),
        new CodebookEntry(4, 0b0010, 6),
        new CodebookEntry(5, 0b00011, 7),
        new CodebookEntry(6, 0b000101, 8),
        new CodebookEntry(6, 0b000100, 9),
        new CodebookEntry(7, 0b0000100, 10),
        new CodebookEntry(7, 0b0000101, 11),
        new CodebookEntry(7, 0b0000111, 12),
        new CodebookEntry(8, 0b00000100, 13),
        new CodebookEntry(8, 0b00000111, 14),
        new CodebookEntry(9, 0b000011000, 15),
        new CodebookEntry(10, 0b0000010111, 16),
        new CodebookEntry(10, 0b0000011000, 17),
        new CodebookEntry(10, 0b0000001000, 18),
        new CodebookEntry(11, 0b00001100111, 19),
        new CodebookEntry(11, 0b00001101000, 20),
        new CodebookEntry(11, 0b00001101100, 21),
        new CodebookEntry(11, 0b00000110111, 22),
        new CodebookEntry(11, 0b00000101000, 23),
        new CodebookEntry(11, 0b00000010111, 24),
        new CodebookEntry(11, 0b00000011000, 25),
        new CodebookEntry(12, 0b000011001010, 26),
        new CodebookEntry(12, 0b000011001011, 27),
        new CodebookEntry(12, 0b000011001100, 28),
        new CodebookEntry(12, 0b000011001101, 29),
        new CodebookEntry(12, 0b000001101000, 30),
        new CodebookEntry(12, 0b000001101001, 31),
        new CodebookEntry(12, 0b000001101010, 32),
        new CodebookEntry(12, 0b000001101011, 33),
        new CodebookEntry(12, 0b000011010010, 34),
        new CodebookEntry(12, 0b000011010011, 35),
        new CodebookEntry(12, 0b000011010100, 36),
        new CodebookEntry(12, 0b000011010101, 37),
        new CodebookEntry(12, 0b000011010110, 38),
        new CodebookEntry(12, 0b000011010111, 39),
        new CodebookEntry(12, 0b000001101100, 40),
        new CodebookEntry(12, 0b000001101101, 41),
        new CodebookEntry(12, 0b000011011010, 42),
        new CodebookEntry(12, 0b000011011011, 43),
        new CodebookEntry(12, 0b000001010100, 44),
        new CodebookEntry(12, 0b000001010101, 45),
        new CodebookEntry(12, 0b000001010110, 46),
        new CodebookEntry(12, 0b000001010111, 47),
        new CodebookEntry(12, 0b000001100100, 48),
        new CodebookEntry(12, 0b000001100101, 49),
        new CodebookEntry(12, 0b000001010010, 50),
        new CodebookEntry(12, 0b000001010011, 51),
        new CodebookEntry(12, 0b000000100100, 52),
        new CodebookEntry(12, 0b000000110111, 53),
        new CodebookEntry(12, 0b000000111000, 54),
        new CodebookEntry(12, 0b000000100111, 55),
        new CodebookEntry(12, 0b000000101000, 56),
        new CodebookEntry(12, 0b000001011000, 57),
        new CodebookEntry(12, 0b000001011001, 58),
        new CodebookEntry(12, 0b000000101011, 59),
        new CodebookEntry(12, 0b000000101100, 60),
        new CodebookEntry(12, 0b000001011010, 61),
        new CodebookEntry(12, 0b000001100110, 62),
        new CodebookEntry(12, 0b000001100111, 63),
        new CodebookEntry(10, 0b0000001111, 64),
        new CodebookEntry(12, 0b000011001000, 128),
        new CodebookEntry(12, 0b000011001001, 192),
        new CodebookEntry(12, 0b000001011011, 256),
        new CodebookEntry(12, 0b000000110011, 320),
        new CodebookEntry(12, 0b000000110100, 384),
        new CodebookEntry(12, 0b000000110101, 448),
        new CodebookEntry(13, 0b0000001101100, 512),
        new CodebookEntry(13, 0b0000001101101, 576),
        new CodebookEntry(13, 0b0000001001010, 640),
        new CodebookEntry(13, 0b0000001001011, 704),
        new CodebookEntry(13, 0b0000001001100, 768),
        new CodebookEntry(13, 0b0000001001101, 832),
        new CodebookEntry(13, 0b0000001110010, 896),
        new CodebookEntry(13, 0b0000001110011, 960),
        new CodebookEntry(13, 0b0000001110100, 1024),
        new CodebookEntry(13, 0b0000001110101, 1088),
        new CodebookEntry(13, 0b0000001110110, 1152),
        new CodebookEntry(13, 0b0000001110111, 1216),
        new CodebookEntry(13, 0b0000001010010, 1280),
        new CodebookEntry(13, 0b0000001010011, 1344),
        new CodebookEntry(13, 0b0000001010100, 1408),
        new CodebookEntry(13, 0b0000001010101, 1472),
        new CodebookEntry(13, 0b0000001011010, 1536),
        new CodebookEntry(13, 0b0000001011011, 1600),
        new CodebookEntry(13, 0b0000001100100, 1664),
        new CodebookEntry(13, 0b0000001100101, 1728),
        new CodebookEntry(11, 0b00000001000, 1792),
        new CodebookEntry(11, 0b00000001100, 1856),
        new CodebookEntry(11, 0b00000001101, 1920),
        new CodebookEntry(12, 0b000000010010, 1984),
        new CodebookEntry(12, 0b000000010011, 2048),
        new CodebookEntry(12, 0b000000010100, 2112),
        new CodebookEntry(12, 0b000000010101, 2176),
        new CodebookEntry(12, 0b000000010110, 2240),
        new CodebookEntry(12, 0b000000010111, 2304),
        new CodebookEntry(12, 0b000000011100, 2368),
        new CodebookEntry(12, 0b000000011101, 2432),
        new CodebookEntry(12, 0b000000011110, 2496),
        new CodebookEntry(12, 0b000000011111, 2560),
    };

    @Override
    public final void setImageSegment(ImageSegment imageSegment, ImageInputStream imageInputStream) throws IOException {
        mImageSegment = imageSegment;
        mImageData = imageInputStream;
    }

    @Override
    public final BufferedImage getNextImageBlock() throws IOException {
        if (mImageSegment.getActualBitsPerPixelPerBand() != 1) {
            throw new IOException("Unhandled bilevel image depth:" + mImageSegment.getActualBitsPerPixelPerBand());
        }
        BufferedImage img = new BufferedImage(mImageSegment.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegment.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_BYTE_BINARY);
        imgRaster = img.getRaster();
        for (int blockRow = 0; blockRow < mImageSegment.getNumberOfPixelsPerBlockVertical(); ++blockRow) {
            readScanline(blockRow);
        }
        return img;
    }

    @Override
    public final BufferedImage getImageBlock(final int rowIndex, final int columnIndex) throws IOException {
        // TODO: seek to image block location
        return getNextImageBlock();
    }

    private void readScanline(int blockRow) throws IOException {
        readEOL();
        if (lineMode2D) {
            readScanline2D(blockRow);
        } else {
            readScanline1D(blockRow);
        }
    }

    private void readScanline2D(int blockRow) throws IOException {
        int referenceRow = blockRow - 1;
        int a0colour = WHITE;
        int a0 = -1;
        int a0prime = 0;
        while (a0 < mImageSegment.getNumberOfPixelsPerBlockHorizontal()) {
            TwoDmode mode = getTwoDmode();
            int a1;
            int b1 = getBindex(referenceRow, a0colour, a0);
            switch (mode) {
                case Pass:
                    int b2 = getBindex(referenceRow, flipColour(a0colour), b1);
                    writeRunFromTo(blockRow, a0, b2, a0colour);
                    a0prime = b2;
                    break;
                case Horizontal:
                    if (a0 < 0) {
                        a0 = 0;
                    }
                    if (a0colour == WHITE) {
                        int whiteRun = readWhiteRunLength();
                        writeRun(blockRow, a0, whiteRun, WHITE);
                        int blackRun = readBlackRunLength();
                        writeRun(blockRow, a0 + whiteRun, blackRun, BLACK);
                        a0prime += whiteRun;
                        a0prime += blackRun;
                    } else {
                        int blackRun = readBlackRunLength();
                        writeRun(blockRow, a0, blackRun, BLACK);
                        int whiteRun = readWhiteRunLength();
                        writeRun(blockRow, a0 + blackRun, whiteRun, WHITE);
                        a0prime += blackRun;
                        a0prime += whiteRun;
                    }
                    break;
                case Vertical0:
                    a1 = b1;
                    writeRunFromTo(blockRow, a0, a1, a0colour);
                    a0prime = a1;
                    a0colour = flipColour(a0colour);
                    break;
                case Vertical1Left:
                    a1 = b1 - 1;
                    writeRunFromTo(blockRow, a0, a1, a0colour);
                    a0prime = a1;
                    a0colour = flipColour(a0colour);
                    break;
                case Vertical2Left:
                    a1 = b1 - 2;
                    writeRunFromTo(blockRow, a0, a1, a0colour);
                    a0prime = a1;
                    a0colour = flipColour(a0colour);
                    break;
                case Vertical3Left:
                    a1 = b1 - 3;
                    writeRunFromTo(blockRow, a0, a1, a0colour);
                    a0prime = a1;
                    a0colour = flipColour(a0colour);
                    break;
                case Vertical1Right:
                    a1 = b1 + 1;
                    writeRunFromTo(blockRow, a0, a1, a0colour);
                    a0prime = a1;
                    a0colour = flipColour(a0colour);
                    break;
                case Vertical2Right:
                    a1 = b1 + 2;
                    writeRunFromTo(blockRow, a0, a1, a0colour);
                    a0prime = a1;
                    a0colour = flipColour(a0colour);
                    break;
                case Vertical3Right:
                    a1 = b1 + 3;
                    writeRunFromTo(blockRow, a0, a1, a0colour);
                    a0prime = a1;
                    a0colour = flipColour(a0colour);
                    break;
                default:
                    throw new IOException("Unsupported 2D BilevelBlockRenderer encoding at row" + blockRow);
            }
            a0 = a0prime;
        }
    }

    private void writeRunFromTo(int blockRow, int startColumn, int endColumn, int colour) {
        if (startColumn < 0) {
            startColumn = 0;
        }
        for (int i = startColumn; i < endColumn; ++i) {
            imgRaster.setSample(i, blockRow, 0, colour);
        }
    }

    private int flipColour(int colour) {
        return (colour == WHITE) ? BLACK : WHITE;
    }

    private TwoDmode getTwoDmode() throws IOException {
        TwoDmode mode = TwoDmode.Unknown;
        int numberOfCodewordBits = 1;
        int codeWord = (int)mImageData.readBits(numberOfCodewordBits);
        do {
            mode = lookupMode(codeWord, numberOfCodewordBits);
            if (mode == TwoDmode.Unknown) {
                numberOfCodewordBits++;
                codeWord = (codeWord << 1) + (int)mImageData.readBits(1);
            }
        } while ((mode == TwoDmode.Unknown) && (numberOfCodewordBits <= MAX_VALID_2D_CODEWORD_LENGTH));
        return mode;
    }

    private TwoDmode lookupMode(int codeWord, int numberOfCodewordBits) {
        switch (numberOfCodewordBits) {
            case 1:
                if (codeWord == 1) {
                    return TwoDmode.Vertical0;
                } else {
                    return TwoDmode.Unknown;
                }
            case 3:
                if (codeWord == 0b011) {
                    return TwoDmode.Vertical1Right;
                } else if (codeWord == 0b010) {
                    return TwoDmode.Vertical1Left;
                } else if (codeWord == 0b001) {
                    return TwoDmode.Horizontal;
                } else {
                    return TwoDmode.Unknown;
                }
            case 4:
                if (codeWord == 0b0001) {
                    return TwoDmode.Pass;
                } else {
                    return TwoDmode.Unknown;
                }
            case 6:
                if (codeWord == 0b000011) {
                    return TwoDmode.Vertical2Right;
                } else if (codeWord == 0b000010) {
                    return TwoDmode.Vertical2Left;
                } else {
                    return TwoDmode.Unknown;
                }
            case 7:
                if (codeWord == 0b0000011) {
                    return TwoDmode.Vertical3Right;
                } else if (codeWord == 0b0000010) {
                    return TwoDmode.Vertical3Left;
                } else {
                    return TwoDmode.Unknown;
                }
            default:
                return TwoDmode.Unknown;
        }
    }

    private int getBindex(final int referenceRow, final int refcolour, final int refposition) {
        // First find a changing pixel
        int pixelAboveRefPosition;
        if (refposition == -1) {
            pixelAboveRefPosition = WHITE;
        } else {
            pixelAboveRefPosition = imgRaster.getSample(refposition, referenceRow, 0);
        }
        for (int i = refposition + 1; i < mImageSegment.getNumberOfPixelsPerBlockHorizontal(); ++i) {
            int sampleValue = imgRaster.getSample(i, referenceRow, 0);
            if (sampleValue != pixelAboveRefPosition) {
                // This is a changing pixel
                if (sampleValue != refcolour) {
                    // opposite colour to the reference colour, so this is the one we want
                    return i;
                } else {
                    // Look for another changing pixel
                    pixelAboveRefPosition = sampleValue;
                }
            }
        }
        return mImageSegment.getNumberOfPixelsPerBlockHorizontal();
    }

    private void readScanline1D(int blockRow) throws IOException {
        int blockColumn = 0;
        int colour = WHITE;

        while (blockColumn < mImageSegment.getNumberOfPixelsPerBlockHorizontal()) {
            int runLength = readNextRun(colour);
            writeRun(blockRow, blockColumn, runLength, colour);
            blockColumn += runLength;
            colour = flipColour(colour);
        }
        if (blockColumn != mImageSegment.getNumberOfPixelsPerBlockHorizontal()) {
            throw new IOException("Mismatched number of pixels: " + blockColumn);
        }
    }

    private void writeRun(int blockRow, int blockColumn, int runLength, int colour) {
        for (int i = 0; i < runLength; ++i) {
            imgRaster.setSample(blockColumn + i, blockRow, 0, colour);
        }
    }

    private void readEOL() throws IOException {
        int eol = (int)mImageData.readBits(EOL_LENGTH_IN_BITS);
        // check for fill, and keep reading bits until we get something that isn't all fill bits
        while (eol == 0) {
            eol = (eol << 1) + (int)mImageData.readBits(1);
        }
        if (EOL != eol) {
            throw new IOException(String.format("Expected EOL, but got 0x%04d", eol));
        }
        if (TWOD_S_ENCODING.equals(mImageSegment.getCompressionRate()) || TWOD_H_ENCODING.equals(mImageSegment.getCompressionRate())) {
            lineMode2D = (mImageData.readBits(1) != 0x01);
        }
    }

    private int readNextRun(int colour) throws IOException {
        int cumulativeLengthOfThisRun = 0;
        if (colour == WHITE) {
            int whiteRunLength;
            do {
                whiteRunLength = readWhiteRunLength();
                if (whiteRunLength == -1) {
                    throw new IOException("Bad run length");
                }
                cumulativeLengthOfThisRun += whiteRunLength;
            } while (whiteRunLength > MAX_TERMINATING_RUN_LENGTH);
        } else {
            int blackRunLength;
            do {
                blackRunLength = readBlackRunLength();
                if (blackRunLength == -1) {
                    throw new IOException("Bad run length");
                }
                cumulativeLengthOfThisRun += blackRunLength;
            } while (blackRunLength > MAX_TERMINATING_RUN_LENGTH);
        }
        if (cumulativeLengthOfThisRun > mImageSegment.getNumberOfPixelsPerBlockHorizontal()) {
            throw new IOException("Bad run length: " + cumulativeLengthOfThisRun);
        }
        return cumulativeLengthOfThisRun;
    }

    private int readWhiteRunLength() throws IOException {
        int numberOfCodewordBits = 4;
        int codeWord = (int)mImageData.readBits(numberOfCodewordBits);
        int runLength = 0;
        do {
            runLength = lookupRunLength(whiteCodeBook, codeWord, numberOfCodewordBits);
            if (runLength == -1) {
                codeWord = (codeWord << 1) + (int)mImageData.readBits(1);
                ++numberOfCodewordBits;
            }
        } while ((runLength == -1) && (numberOfCodewordBits <= MAX_VALID_BITLENGTH_WHITE));
        return runLength;
    }

    private int readBlackRunLength() throws IOException {
        int numberOfCodewordBits = 2;
        int codeWord = (int)mImageData.readBits(numberOfCodewordBits);
        int runLength = 0;
        do {
            runLength = lookupRunLength(blackCodeBook, codeWord, numberOfCodewordBits);
            if (runLength == -1) {
                codeWord = (codeWord << 1) + (int)mImageData.readBits(1);
                ++numberOfCodewordBits;
            }
        } while ((runLength == -1) && (numberOfCodewordBits <= MAX_VALID_BITLENGTH_BLACK));
        return runLength;
    }

    private int lookupRunLength(final CodebookEntry[] codeBook, int codeWord, int numberOfCodewordBits) throws IOException {
        for (int i = 0; i < codeBook.length; ++i) {
            CodebookEntry entry = codeBook[i];
            if ((entry.codeWordLength == numberOfCodewordBits) && (entry.codeWord == codeWord)) {
                return entry.runLength;
            }
        }
        return -1;
    }
}
