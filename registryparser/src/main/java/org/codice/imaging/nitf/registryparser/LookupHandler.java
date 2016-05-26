/*
 * The MIT License
 *
 * Copyright Codice Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codice.imaging.nitf.registryparser;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;

/**
 * Shared code for lookup handlers.
 */
public abstract class LookupHandler {

    protected final String mOutputFileName;
    protected final String mTreName;
    protected final String mFieldName;

    public LookupHandler(String outputFileName, String treName, String fieldName) {
        mOutputFileName = outputFileName;
        mTreName = treName;
        mFieldName = fieldName;
    }

    public String getTreName() {
        return mTreName;
    }

    public String getFieldName() {
        return mFieldName;
    }

    public abstract void serialise() throws IOException, XMLStreamException;

}
