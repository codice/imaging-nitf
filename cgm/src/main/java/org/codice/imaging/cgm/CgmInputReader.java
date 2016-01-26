/*
 * Copyright (c) 2014, Codice
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.codice.imaging.cgm;

import java.awt.Color;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper for CGM input data.
 */
class CgmInputReader {

    private static final int LONG_COUNT_FLAG_VALUE = 254;
    private static final int NUM_BYTES_IN_SIGNED_VDC_INTEGER = 2;
    private static final int NUM_BYTES_IN_ENUM_VALUE = 2;

    private static final Logger LOGGER = LoggerFactory.getLogger(CgmInputReader.class);
    private final DataInput dataStream;

    CgmInputReader(final byte[] cgmData) {
        dataStream = new DataInputStream(new ByteArrayInputStream(cgmData));
    }

    CgmInputReader(final InputStream stream) {
        dataStream = new DataInputStream(stream);
    }

    CgmInputReader(final DataInput dataInput) {
        dataStream = dataInput;
    }

    int readUnsignedShort() throws IOException {
        return dataStream.readUnsignedShort();
    }

    void skipBytes(final int i) throws IOException {
        int numBytesStillToSkip = i;
        while (numBytesStillToSkip > 0) {
            numBytesStillToSkip -= dataStream.skipBytes(numBytesStillToSkip);
        }
    }

    private int readShort() throws IOException {
        return dataStream.readShort();
    }

    String getStringFixed() throws IOException {
        int count = dataStream.readUnsignedByte();
        if (count > LONG_COUNT_FLAG_VALUE) {
            LOGGER.info("Need to handle long count");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            builder.append((char) dataStream.readByte());
        }
        return builder.toString();
    }

    int readSignedIntegerAtIntegerPrecision() throws IOException {
        // BIIF profile BPCGM 01.00 doesn't allow changing this from the default.
        // The default is 16 bits
        return dataStream.readShort();
    }

    int readSignedIntegerAtVdcIntegerPrecision() throws IOException {
        // BIIF profile BPCGM 01.00 doesn't allow changing this from the default.
        // The default is 16 bits
        return dataStream.readShort();
    }

    Point readPoint() throws IOException {
        int x = readSignedIntegerAtVdcIntegerPrecision();
        int y = readSignedIntegerAtVdcIntegerPrecision();
        return new Point(x, y);
    }

    int getNumberOfBytesInPoint() {
        return 2 * NUM_BYTES_IN_SIGNED_VDC_INTEGER;
    }

    Color readColour(final int length) throws IOException {
        // BIIF profile BPCGM 01.00 only allows direct colour
        int red = dataStream.readUnsignedByte();
        int green = dataStream.readUnsignedByte();
        int blue = dataStream.readUnsignedByte();
        return new Color(red, green, blue);
    }

    int readSizeSpecification() throws IOException {
        // BIIF profile BPCGM 01.00 only allows integer 16 bit.
        return dataStream.readShort();
    }

    int readSignedIntegerAtIndexPrecision() throws IOException {
        // BIIF profile BPCGM 01.00 only allows integer 16 bit.
        return dataStream.readShort();
    }

    List<Point> readPoints(final int parameterListLength) throws IOException {
        // BIIF profile BPCGM 01.00 only allows integer 16 bit.
        List<Point> points = new ArrayList<>();
        int bytesRead = 0;
        while (bytesRead < parameterListLength) {
            Point point = readPoint();
            points.add(point);
            bytesRead += getNumberOfBytesInPoint();
        }
        return points;
    }

    int readEnumValue() throws IOException {
        return readShort();
    }

    int getNumberOfBytesInEnumValue() {
        return NUM_BYTES_IN_ENUM_VALUE;
    }
}
